package com.github.waifu.gui.actions;

import com.github.waifu.entities.*;
import com.github.waifu.gui.GUI;
import com.github.waifu.gui.Main;
import com.github.waifu.gui.tables.SetTable;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;

import static com.github.waifu.gui.Main.packetProcessor;

/**
 *
 */
public class ParseWebAppSetsAction implements ActionListener {

    private final JProgressBar progressBar;
    private final JButton stopButton;

    /**
     *
     * @param progressBar
     * @param stopButton
     */
    public ParseWebAppSetsAction(JProgressBar progressBar, JButton stopButton) {
        this.progressBar = progressBar;
        this.stopButton = stopButton;
    }

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    if (GUI.checkProcessRunning()) {
                        return null;
                    } else if (RealmeyeRequestHandler.checkDirectConnect()) {
                        packetProcessor.stopSniffer();
                        stopButton.setText("Stop Process");
                        GUI.setWorker(this);
                        progressBar.setValue(0);
                        GUI.setProcessRunning(true);
                        List<Raider> sets = Main.accountsToRaiders();
                        //List<Raider> sets = getSets(GUI.raid, progressBar);
                        if (sets != null) {
                            new SetTable(sets);
                        }
                        GUI.setProcessRunning(false);
                        stopButton.setText("Finished");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    GUI.setProcessRunning(false);
                }
                return null;
            }
        }.execute();
    }

    public enum Rune {
        SHIELD_RUNE(20), SWORD_RUNE(21), HELMET_RUNE(22);

        int id;

        Rune(int id) {
            this.id = id;
        }
    }

    /**
     * getSets method.
     * <p>
     * Constructs a map containing accounts and their inventories.
     *
     *
     * @param bar  progress bar to be updated over time.
     */
    private List<Raider> getSets(Raid raid, JProgressBar bar) throws InterruptedException, IOException {
        if (raid.getRaiders().isEmpty()) {
            return null;
        } else {
            List<Raider> raiders = new ArrayList<>();

            for (Raider r : raid.getRaiders()) {
                List<Object> reactIds = r.getReacts().toList();
                if (reactIds.isEmpty()) {
                    raiders.add(r);
                } else {
                    if (!reactIds.contains(Rune.SHIELD_RUNE.id) && !reactIds.contains(Rune.SWORD_RUNE.id) && !reactIds.contains(Rune.HELMET_RUNE.id)) {
                        raiders.add(r);
                    }
                }
            }
            bar.setMaximum(raiders.size());
            for (int i = 0; i < raiders.size(); i++) {
                for (int j = 0; j < raiders.get(i).getAccounts().size(); j++) {
                    String username = raiders.get(i).getAccounts().get(j).getName();
                    Account account = RealmeyeRequestHandler.parseHTML(Objects.requireNonNull(RealmeyeRequestHandler.getRealmeyeData(username)), username);
                    if (account.getCharacters() != null) {
                        account.getCharacters().get(0).getInventory().parseInventory();
                    }
                    raiders.get(i).getAccounts().set(j, account);
                }
                bar.setValue(i + 1);
            }
            return raiders;
        }
    }
}

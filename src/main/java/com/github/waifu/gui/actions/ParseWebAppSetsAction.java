package com.github.waifu.gui.actions;

import com.github.waifu.entities.*;
import com.github.waifu.gui.GUI;
import com.github.waifu.gui.tables.SetTable;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.util.Utilities;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;

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
                        stopButton.setText("Stop Process");
                        GUI.setWorker(this);
                        progressBar.setValue(0);
                        GUI.setProcessRunning(true);
                        Map<Raider, Inventory> sets = getSets(GUI.getJson(), progressBar);
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

    /**
     * getSets method.
     * <p>
     * Constructs a map containing accounts and their inventories.
     *
     * @param json JSONObject returned from the WebApp
     * @param bar  progress bar to be updated over time.
     */
    private Map<Raider, Inventory> getSets(JSONObject json, JProgressBar bar) throws InterruptedException, IOException {
        if (json == null || !json.has("raid")) {
            return null;
        } else {
            List<String> usernames = Utilities.parseRaiderSets(json).stream().toList();
            if (usernames.isEmpty()) {
                return null;
            }
            bar.setMaximum(usernames.size());
            Map<Raider, Inventory> map = new HashMap<>();
            for (int i = 0; i < usernames.size(); i++) {
                String username = usernames.get(i);
                Account account = RealmeyeRequestHandler.parseHTML(Objects.requireNonNull(RealmeyeRequestHandler.getRealmeyeData(username)), username);
                Raider raider = Utilities.constructRaider(username, account);
                if (account.getCharacters() != null) {
                    Inventory inventory = account.getCharacters().get(0).getInventory();
                    map.put(raider, inventory.parseInventory());
                } else {
                    map.put(raider, null);
                }
                bar.setValue(i + 1);
            }
            return map;
        }
    }
}

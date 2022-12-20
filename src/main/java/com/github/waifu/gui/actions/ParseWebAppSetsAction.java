package com.github.waifu.gui.actions;

import com.github.waifu.entities.*;
import com.github.waifu.entities.Character;
import com.github.waifu.gui.GUI;
import com.github.waifu.gui.tables.SetTable;
import com.github.waifu.handlers.PacketHandler;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.packets.PacketType;
import com.github.waifu.packets.packetcapture.PacketProcessor;
import com.github.waifu.packets.packetcapture.register.Register;
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
    private final JButton parseSetsButton;
    private PacketProcessor packetProcessor;

    /**
     *
     * @param progressBar
     * @param stopButton
     * @param parseSetsButton
     */
    public ParseWebAppSetsAction(JProgressBar progressBar, JButton stopButton, JButton parseSetsButton) {
        this.progressBar = progressBar;
        this.stopButton = stopButton;
        this.parseSetsButton = parseSetsButton;
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
                    if (parseSetsButton.getText().equals("Stop Sniffer")) {
                        if (GUI.raid.isWebAppRaid() && !GUI.raid.getStatus().equals("RUNNING")) return null;
                        packetProcessor.closeSniffer();
                        packetProcessor = null;
                        Register.INSTANCE = new Register();
                        List<Raider> sets = getSnifferSets(GUI.raid, progressBar);
                        if (sets != null) {
                            new SetTable(sets);
                        }
                        GUI.raid = null;
                        parseSetsButton.setText("Parse Sets");
                        GUI.setProcessRunning(false);
                    } else if (GUI.checkProcessRunning()) {
                        return null;
                    } else if (GUI.raid == null) {
                        parseSetsButton.setEnabled(false);
                        if (PacketHandler.isXMLNull()) {
                            if (!PacketHandler.loadXML()) {
                                parseSetsButton.setEnabled(true);
                                return null;
                            }
                        }
                        parseSetsButton.setEnabled(true);
                        parseSetsButton.setText("Stop Sniffer");
                        GUI.setWorker(this);
                        progressBar.setValue(0);
                        GUI.raid = new Raid();
                        Register.INSTANCE.register(PacketType.UPDATE, PacketHandler::handlePacket);
                        packetProcessor = new PacketProcessor();
                        packetProcessor.start();
                    } else if (GUI.raid.isWebAppRaid()) { // webapp
                        GUI.setProcessRunning(true);
                        if (RealmeyeRequestHandler.checkDirectConnect()) {
                            List<Raider> sets = getRealmeyeSets(GUI.raid, progressBar);
                            if (sets != null) {
                                new SetTable(sets);
                            }
                            GUI.setProcessRunning(false);
                            stopButton.setText("Finished");
                        } else {
                            GUI.setProcessRunning(false);
                            stopButton.setText("Finished");
                            return null;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    GUI.setProcessRunning(false);
                }
                return null;
            }
        }.execute();
    }

    private List<Raider> getSnifferSets(Raid raid, JProgressBar bar) {
        if (raid.getRaiders().isEmpty()) {
            return null;
        } else {
            if (!raid.isWebAppRaid()) {
                return parseRaiderInventories(raid.getRaiders(), bar);
            } else {
                List<Raider> raiders = removeRunes(raid.getRaiders());
                return parseRaiderInventories(raiders, bar);
            }
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
    private List<Raider> getRealmeyeSets(Raid raid, JProgressBar bar) throws InterruptedException, IOException {
        if (raid.getRaiders().isEmpty()) {
            return null;
        } else {
            List<Raider> raiders = removeRunes(raid.getRaiders());
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

    private List<Raider> parseRaiderInventories(List<Raider> raiders, JProgressBar bar) {
        bar.setMaximum(raiders.size());
        for (int i = 0; i < raiders.size(); i++) {
            for (int j = 0; j < raiders.get(i).getAccounts().size(); j++) {
                if (raiders.get(i).getAccounts().get(j).getCharacters() != null) {
                    raiders.get(i).getAccounts().get(j).getRecentCharacter().getInventory().parseInventory();
                } else {
                    List<Character> characters = new ArrayList<>();
                    Character character = new Character();
                    character.getInventory().parseInventory();
                    characters.add(character);
                    raiders.get(i).getAccounts().set(j, new Account(raiders.get(i).getAccounts().get(j).getName(), characters));
                }
            }
            bar.setValue(i + 1);
        }
        return raiders;
    }

    public enum Rune {
        SHIELD_RUNE(20), SWORD_RUNE(21), HELMET_RUNE(22);

        final int id;

        Rune(int id) {
            this.id = id;
        }
    }

    private List<Raider> removeRunes(List<Raider> raiders) {
        List<Raider> newRaiders = new ArrayList<>();
        for (Raider r : raiders) {
            if (r.getRoles() != null) {
                List<Object> reactIds = r.getReacts().toList();
                if (reactIds.isEmpty()) {
                    newRaiders.add(r);
                } else {
                    if (!reactIds.contains(Rune.SHIELD_RUNE.id) && !reactIds.contains(Rune.SWORD_RUNE.id) && !reactIds.contains(Rune.HELMET_RUNE.id)) {
                        newRaiders.add(r);
                    }
                }
            }
        }
        return newRaiders;
    }

}

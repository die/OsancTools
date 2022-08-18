package com.github.waifu.gui.actions;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.React;
import com.github.waifu.gui.GUI;
import com.github.waifu.gui.ReactTable;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.util.Utilities;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParseWebAppReactsAction implements ActionListener {

    private JProgressBar progressBar;
    private JButton stopButton;

    public ParseWebAppReactsAction(JProgressBar progressBar, JButton stopButton) {
        this.progressBar = progressBar;
        this.stopButton = stopButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    if (GUI.checkProcessRunning()) {
                       return null;
                    } else if (RealmeyeRequestHandler.checkDirectConnect()){
                        stopButton.setText("Stop Process");
                        GUI.setWorker(this);
                        progressBar.setValue(0);
                        GUI.setProcessRunning(true);
                        List<React> reacts = getReacts(GUI.getJson(), progressBar);
                        if (reacts != null) {
                            new ReactTable(reacts);
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
     * getReacts method.
     * <p>
     * Constructs a list of React objects that contain:
     * React metadata, including icon and name.
     * All raiders who reacted and their Accounts.
     *
     * @param json JSONObject returned from the WebApp.
     * @param bar  progress bar to be updated over time.
     */
    private List<React> getReacts(JSONObject json, JProgressBar bar) throws InterruptedException, IOException {
        if (json == null || !json.has("raid")) {
            return null;
        } else {
            Map<String, React> map = Utilities.parseRaiderReacts(json);
            if (map.isEmpty()) {
                return null;
            } else {
                JSONArray items = (JSONArray) Utilities.json.get("reactItem");
                JSONArray classes = (JSONArray) Utilities.json.get("reactClass");
                JSONArray reactDPS = (JSONArray) Utilities.json.get("reactDps");
                bar.setMaximum(map.size());
                int count = 1;
                List<React> reacts = new ArrayList<>();
                for (Map.Entry<String, React> m : map.entrySet()) {
                    List<Account> raiders = new ArrayList<>();
                    for (int i = 0; i < m.getValue().getRaiders().size(); i++) {
                        String username = m.getValue().getRaiders().get(i).getName();
                        Account account = RealmeyeRequestHandler.parseHTML(RealmeyeRequestHandler.getRealmeyeData(username), username);
                        raiders.add(account);
                    }
                    m.getValue().setRaiders(raiders);
                    String reactName = m.getValue().getName();
                    if (items.toList().contains(reactName)) {
                        m.getValue().setType("item");
                    } else if (classes.toList().contains(reactName)) {
                        m.getValue().setType("class");
                    } else if (reactDPS.toList().contains(reactName)) {
                        m.getValue().setType("dps");
                    } else if (reactName.contains("Effusion")) {
                        m.getValue().setType("lock");
                    }
                    m.getValue().parseReact(m.getValue().getType());
                    reacts.add(m.getValue());
                    bar.setValue(count);
                    count++;
                }
                return reacts;
            }
        }
    }
}

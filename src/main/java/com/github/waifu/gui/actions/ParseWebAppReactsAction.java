package com.github.waifu.gui.actions;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Raid;
import com.github.waifu.entities.Raider;
import com.github.waifu.entities.React;
import com.github.waifu.gui.GUI;
import com.github.waifu.gui.tables.ReactTable;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.util.Utilities;
import org.json.JSONArray;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ParseWebAppReactsAction implements ActionListener {

    private final JProgressBar progressBar;
    private final JButton stopButton;
    private final JButton parseReactsButton;


    /**
     *
     * @param progressBar
     * @param stopButton
     */
    public ParseWebAppReactsAction(JProgressBar progressBar, JButton stopButton, JButton parseReactsButton) {
        this.progressBar = progressBar;
        this.stopButton = stopButton;
        this.parseReactsButton = parseReactsButton;
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
                    } else if (GUI.raid != null && GUI.raid.isWebAppRaid()) { // webapp
                         if (RealmeyeRequestHandler.checkDirectConnect()){
                            stopButton.setText("Stop Process");
                            GUI.setWorker(this);
                            progressBar.setValue(0);
                            GUI.setProcessRunning(true);
                            List<React> reacts = getRealmeyeReacts(GUI.raid, progressBar);
                            if (reacts != null) {
                                new ReactTable(reacts);
                            }
                            GUI.setProcessRunning(false);
                            stopButton.setText("Finished");
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

    /**
     * getReacts method.
     * <p>
     * Constructs a list of React objects that contain:
     * React metadata, including icon and name.
     * All raiders who reacted and their Accounts.
     *
     * @param raid JSONObject returned from the WebApp.
     * @param bar  progress bar to be updated over time.
     */
    private List<React> getRealmeyeReacts(Raid raid, JProgressBar bar) throws InterruptedException, IOException {
        if (raid.getRaiders().isEmpty()) {
            return null;
        } else {
            JSONArray items = (JSONArray) Utilities.json.get("reactItem");
            JSONArray classes = (JSONArray) Utilities.json.get("reactClass");
            JSONArray reactDPS = (JSONArray) Utilities.json.get("reactDps");
            List<React> reactList = createReactObjects();
            bar.setMaximum(reactList.size());
            int count = 1;
            for (React react : reactList) {
                for (Raider raider : raid.getRaiders()) {
                    if (raider.getReacts().toList().contains(Integer.parseInt(react.getId()))) {
                        /* Create deep copy */
                        Raider temp = new Raider();
                        for (int i = 0; i < raider.getAccounts().size(); i++) {
                            String username = raider.getAccounts().get(i).getName();
                            Account account = RealmeyeRequestHandler.parseHTML(RealmeyeRequestHandler.getRealmeyeData(username), username);
                            raider.getAccounts().set(i, account);
                            temp.getAccounts().add(account);
                        }
                        react.getRaiders().add(temp);
                    }
                }

                String reactName = react.getName();
                if (items.toList().contains(reactName)) {
                    react.setType("item");
                } else if (classes.toList().contains(reactName)) {
                    react.setType("class");
                } else if (reactDPS.toList().contains(reactName)) {
                    react.setType("dps");
                } else if (reactName.contains("Effusion")) {
                    react.setType("lock");
                }
                react.parseReact(react.getType());
                bar.setValue(count);
                count++;
            }
            return reactList;
        }
    }

    private List<React> createReactObjects() throws MalformedURLException {
        JSONArray reacts = GUI.getJson().getJSONObject("raid").getJSONArray("reacts");
        List<React> reactList = new ArrayList<>();
        for (int i = 0; i < reacts.length(); i++) {
            String id = String.valueOf(reacts.getJSONObject(i).getInt("id"));
            String name = reacts.getJSONObject(i).getString("name");
            String icon = reacts.getJSONObject(i).getString("icon");
            String requirement = String.valueOf(reacts.getJSONObject(i).get("reqs"));
            React react = new React(id, name, requirement, icon, new ArrayList<>());
            reactList.add(react);
        }
        return reactList;
    }
}

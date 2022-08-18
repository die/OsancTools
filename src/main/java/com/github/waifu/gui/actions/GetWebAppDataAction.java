package com.github.waifu.gui.actions;

import com.github.waifu.gui.GUI;
import com.github.waifu.handlers.WebAppHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class GetWebAppDataAction implements ActionListener {

    private final JPanel main;
    private final TitledBorder border;
    private final JPanel connected;
    private final JLabel raid;
    private final JPanel raidPanel;
    private final JLabel description;
    private final JLabel metadata;
    private final GUI gui;

    public GetWebAppDataAction(JPanel main, TitledBorder border, JPanel connected, JLabel raid, JPanel raidPanel, JLabel description, JLabel metadata, GUI gui) {
        this.main = main;
        this.border = border;
        this.connected = connected;
        this.raid = raid;
        this.raidPanel = raidPanel;
        this.description = description;
        this.metadata = metadata;
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (GUI.checkProcessRunning()) {
            return;
        } else {
            switch (GUI.getMode()) {
                case GUI.NORMAL_MODE, GUI.DEBUG_MODE -> getWebAppDataFromId();
                case GUI.LAN_MODE -> getWebAppDataFromFile();
            }
            this.gui.updateGUI();
        }
    }

    private void getWebAppDataFromId() {
        try {
            String s = (String) JOptionPane.showInputDialog(
                    main,
                    "Please paste in the raid id from osanc.net",
                    "Input",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
            GUI.setJson(WebAppHandler.getRaid(s));

            JSONObject json = GUI.getJson();
            String id = String.valueOf(json.getJSONObject("raid").getInt("id"));
            File file = new File("src/test/resources/raids");
            if (!file.exists()){
                file.mkdirs();
            }
            File idpath = new File("src/test/resources/raids" + "/" + id + "/");
            if (idpath.exists()) {
                System.out.println("Raid data already exists.");
                return;
            }

            File raid = new File(file.getPath() + "/" + id);
            if (!raid.exists()) {
                raid.mkdirs();
            }
            FileWriter raidJSON = new FileWriter(raid.getPath() + "/" + id + ".json");
            raidJSON.write(json.toString(3));
            raidJSON.close();
            File reacts = new File(raid.getPath() + "/reacts");
            if (!reacts.exists()) {
                reacts.mkdirs();
            }
            JSONArray reactArray = json.getJSONObject("raid").getJSONArray("reacts");
            for (int r = 0; r < reactArray.length(); r++) {

                String reactId = String.valueOf(reactArray.getJSONObject(r).getInt("id"));
                String reactUrl = reactArray.getJSONObject(r).getString("icon");

                try {
                    URL rURL = new URL(reactUrl);
                    String reactExtension = reactUrl.split(".com")[1].split("\\.")[1].substring(0, 3);
                    try (InputStream in = rURL.openStream()) {
                        Files.copy(in, new File("src/test/resources/raids" + "/" + id + "/reacts/" + reactId + "." + reactExtension).toPath());
                    }
                } catch (Exception e) {
                    System.out.println("Error with react: " + reactId);
                }
            }

            JSONArray array = json.getJSONObject("raid").getJSONArray("members");
            for (int i = 0; i < array.length(); i++) {
                List<String> raiderUsernames = new ArrayList<>();
                String[] username = array.getJSONObject(i).getString("server_nickname").split(" ");
                for (String alts : username) {
                    String replace = alts.replaceAll("[^\\p{L}]", "");
                    if (!replace.equals(""))
                    {
                        raiderUsernames.add(replace);
                    }
                }

                for (String r : raiderUsernames) {
                    try {
                        File raider = new File("src/test/resources/raids/" + id + "/players/" + r + "/");
                        if (!raider.exists()) {
                            raider.mkdirs();
                        }

                        if (raider.exists()) {
                            String url = array.getJSONObject(i).getString("avatar");
                            String extension = url.split(".com")[1].split("\\.")[1].substring(0, 3);
                            URL url1 = new URL(url);
                            try (InputStream in = url1.openStream()) {
                                Files.copy(in, new File("src/test/resources/raids/" + id + "/players/" + r + "/" + "avatar." + extension).toPath());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error with:" + r);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getWebAppDataFromFile() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(GUI.TEST_RESOURCE_PATH));
        Component rootPane = GUI.getFrames()[0].getComponents()[0];
        int returnVal = fc.showOpenDialog(rootPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                GUI.setJson(new JSONObject(new JSONTokener(new InputStreamReader(new FileInputStream(fc.getSelectedFile()), StandardCharsets.UTF_8))));
            } catch (FileNotFoundException exception) {
                System.out.println("Could not find file: " + fc.getSelectedFile().getName());
            } catch (Exception exception) {
                System.out.println("Could not parse file: " + fc.getSelectedFile().getName());
            }
        }
    }
}

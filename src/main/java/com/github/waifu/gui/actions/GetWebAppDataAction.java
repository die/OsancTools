package com.github.waifu.gui.actions;

import com.github.waifu.gui.GUI;
import com.github.waifu.handlers.WebAppHandler;
import org.json.JSONObject;
import org.json.JSONTokener;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class GetWebAppDataAction implements ActionListener {

    private final JPanel main;
    private final TitledBorder border;
    private final JPanel connected;
    private final JLabel raid;
    private final JPanel raidPanel;
    private final JLabel description;
    private final JLabel metadata;
    private final GUI gui;

    /**
     *
     * @param main
     * @param border
     * @param connected
     * @param raid
     * @param raidPanel
     * @param description
     * @param metadata
     * @param gui
     */
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

    /**
     *
     * @param e
     */
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

    /**
     *
     */
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
            JSONObject jsonObject = WebAppHandler.getRaid(s);
            if (jsonObject != null) {
                GUI.setJson(jsonObject);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     */
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

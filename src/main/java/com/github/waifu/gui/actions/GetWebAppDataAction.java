package com.github.waifu.gui.actions;

import com.github.waifu.entities.Raid;
import com.github.waifu.gui.GUI;
import com.github.waifu.handlers.WebAppHandler;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Action to grab data from the WebApp.
 */
public class GetWebAppDataAction implements ActionListener {

  /**
   * Main GUI panel that shows raid metadata.
   */
  private final JPanel main;
  /**
   * GUI instance.
   */
  private final GUI gui;

  /**
   * Constructs the WebAppDataAction.
   *
   * @param newMain main panel as a JPanel.
   * @param newGui gui instance as a GUI.
   */
  public GetWebAppDataAction(final JPanel newMain, final GUI newGui) {
    this.main = newMain;
    this.gui = newGui;
  }

  /**
   * Function fires when the button is pressed.
   *
   * @param e ActionEvent object.
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    if (!GUI.checkProcessRunning()) {
      switch (GUI.getMode()) {
        case GUI.NORMAL_MODE, GUI.DEBUG_MODE -> getWebAppDataFromId();
        case GUI.LAN_MODE -> getWebAppDataFromFile();
        default -> {
          return;
        }
      }
      this.gui.updateGUI();
    }
  }

  /**
   * Gets the WebApp data from a raid id.
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
        if (GUI.raid != null) {
          GUI.raid.deepCopy(jsonObject.getJSONObject("raid"));
        } else {
          GUI.raid = new Raid(jsonObject.getJSONObject("raid"));
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Gets WebApp data by loading a local json.
   */
  private void getWebAppDataFromFile() {
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(new File(GUI.TEST_RESOURCE_PATH));
    Component rootPane = GUI.getFrames()[0].getComponents()[0];
    int returnVal = fc.showOpenDialog(rootPane);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      try {
        Charset charSet = StandardCharsets.UTF_8;
        FileInputStream fileIs = new FileInputStream(fc.getSelectedFile());
        InputStreamReader isr = new InputStreamReader(fileIs, charSet);
        JSONTokener tokener = new JSONTokener(isr);
        JSONObject jsonObject = new JSONObject(tokener);
        GUI.setJson(jsonObject);
        GUI.raid = new Raid(jsonObject.getJSONObject("raid"));
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
  }
}

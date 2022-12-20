package com.github.waifu.gui.actions;

import com.github.waifu.entities.Raid;
import com.github.waifu.gui.GUI;
import com.github.waifu.handlers.WebAppHandler;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 */
public class GetWebAppDataAction implements ActionListener {

  private final JPanel main;
  private final GUI gui;

  /**
   * @param main
   * @param gui
   */
  public GetWebAppDataAction(JPanel main, GUI gui) {
    this.main = main;
    this.gui = gui;
  }

  /**
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
   *
   */
  private void getWebAppDataFromFile() {
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(new File(GUI.TEST_RESOURCE_PATH));
    Component rootPane = GUI.getFrames()[0].getComponents()[0];
    int returnVal = fc.showOpenDialog(rootPane);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      try {
        JSONObject jsonObject = new JSONObject(new JSONTokener(new InputStreamReader(new FileInputStream(fc.getSelectedFile()), StandardCharsets.UTF_8)));
        GUI.setJson(jsonObject);
        GUI.raid = new Raid(jsonObject.getJSONObject("raid"));
      } catch (FileNotFoundException exception) {
        System.out.println("Could not find file: " + fc.getSelectedFile().getName());
      } catch (Exception exception) {
        System.out.println("Could not parse file: " + fc.getSelectedFile().getName());
      }
    }
  }
}

package com.github.waifu.gui.actions;

import com.github.waifu.entities.Raid;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.panels.HomePanel;
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
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Action to grab data from the WebApp.
 */
public class GetWebAppDataAction implements ActionListener {

  /**
   * Main Gui panel that shows raid metadata.
   */
  private final HomePanel homePanel;

  /**
   * Constructs the WebAppDataAction.
   *
   * @param homePanel main panel as a JPanel.
   */
  public GetWebAppDataAction(final HomePanel homePanel) {
    this.homePanel = homePanel;
  }

  /**
   * Function fires when the button is pressed.
   *
   * @param e ActionEvent object.
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    if (!Gui.checkProcessRunning()) {
      getWebAppDataFromId();
      homePanel.updateHomePanel();
    }
  }

  /**
   * Gets the WebApp data from a raid id.
   */
  private void getWebAppDataFromId() {
    try {
      final String s = (String) JOptionPane.showInputDialog(
              homePanel,
              "Please paste in the raid id from osanc.net",
              "Input",
              JOptionPane.PLAIN_MESSAGE,
              null,
              null,
              "");
      final JSONObject jsonObject = WebAppHandler.getRaid(s);
      if (jsonObject != null) {
        Gui.setJson(jsonObject);
        if (Gui.getRaid() != null) {
          // todo: efficient copy
          // Gui.getRaid().deepCopy(jsonObject.getJSONObject("raid"));
          Gui.setRaid(new Raid(jsonObject.getJSONObject("raid")));
        } else {
          Gui.setRaid(new Raid(jsonObject.getJSONObject("raid")));
        }
      }
    } catch (final Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Gets WebApp data by loading a local json.
   */
  private void getWebAppDataFromFile() {
    final JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(new File(Gui.TEST_RESOURCE_PATH));
    final Component rootPane = Gui.getFrames()[0].getComponents()[0];
    final int returnVal = fc.showOpenDialog(rootPane);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      try {
        final Charset charSet = StandardCharsets.UTF_8;
        final FileInputStream fileIs = new FileInputStream(fc.getSelectedFile());
        final InputStreamReader isr = new InputStreamReader(fileIs, charSet);
        final JSONTokener tokener = new JSONTokener(isr);
        final JSONObject jsonObject = new JSONObject(tokener);
        Gui.setJson(jsonObject);
        Gui.setRaid(new Raid(jsonObject.getJSONObject("raid")));
      } catch (final Exception exception) {
        exception.printStackTrace();
      }
    }
  }
}

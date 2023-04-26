package com.github.waifu.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.github.waifu.entities.Raid;
import com.github.waifu.gui.panels.CreditsPanel;
import com.github.waifu.gui.panels.ExaltsPanel;
import com.github.waifu.gui.panels.HomePanel;
import com.github.waifu.gui.panels.OptionsPanel;
import com.github.waifu.gui.panels.RequirementSheetPanel;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Component;
import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Gui class to construct the Gui.
 */
public class Gui extends JFrame {

  /**
   * Path for getting test resources.
   */
  public static final String TEST_RESOURCE_PATH = "src/test/resources/";
  /**
   * Raid object.
   */
  private static Raid raid;
  /**
   * WebApp JSON.
   */
  private static JSONObject json;
  /**
   * Check if a process is running.
   */
  private static boolean processRunning;
  /**
   * Worker for handling background processes.
   */
  private static SwingWorker<Void, Void> worker;
  /**
   * To be documented.
   */
  private JPanel main;
  /**
   * To be documented.
   */
  private JTabbedPane tabbedPane;

  /**
   * Gui method.
   *
   * <p>Constructs a JFrame to display the Gui.
   */
  public Gui() {
    try {
      final String theme = Main.getSettings().getTheme();
      final LookAndFeel lookAndFeel = theme.equals("dark") ? new FlatDarkLaf() : new FlatLightLaf();
      UIManager.setLookAndFeel(lookAndFeel);
      FlatLaf.updateUI();
    } catch (final Exception e) {
      e.printStackTrace();
    }
    createUIComponents();
    SwingUtilities.updateComponentTreeUI(getRootPane());
    add(main);
    setAlwaysOnTop(true);
    setResizable(false);
    setTitle("OsancTools");
    setIconImage(new ImageIcon(Utilities.getClassResource("images/gui/Gravestone.png")).getImage());
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pack();
    setVisible(true);
    setFocusable(false);
    checkRealmeye();
    checkNewVersion();
  }

  /**
   * Set the Swing worker.
   *
   * @param newWorker Swing Worker that runs background tasks.
   */
  public static void setWorker(final SwingWorker newWorker) {
    Gui.worker = newWorker;
  }

  /**
   * Gets the WebApp JSON.
   *
   * @return WebApp data as JSON
   */
  public static JSONObject getJson() {
    return Gui.json;
  }

  /**
   * Sets the WebApp JSON.
   *
   * @param newJson WebApp data as JSON
   */
  public static void setJson(final JSONObject newJson) {
    Gui.json = newJson;
  }

  /**
   * Set the boolean for tracking processes.
   *
   * @param newProcessRunning boolean if the process is running.
   */
  public static void setProcessRunning(final boolean newProcessRunning) {
    Gui.processRunning = newProcessRunning;
  }

  /**
   * Check if there is a process running.
   *
   * @return state of the worker as boolean.
   */
  public static boolean checkProcessRunning() {
    if (processRunning) {
      final Component rootPane = Gui.getFrames()[0].getComponents()[0];
      JOptionPane.showMessageDialog(rootPane,
              "Please wait until the current process is finished.",
              "Wait!",
              JOptionPane.ERROR_MESSAGE);
    }
    return processRunning;
  }

  /**
   * Gets the raid.
   *
   * @return raid as a Raid object.
   */
  public static Raid getRaid() {
    return Gui.raid;
  }

  /**
   * Sets the raid.
   *
   * @param newRaid as a Raid object.
   */
  public static void setRaid(final Raid newRaid) {
    Gui.raid = newRaid;
  }

  /**
   * Check if Realm-eye is down.
   */
  private void checkRealmeye() {
    try {
      if (Main.getSettings().showAlert()) {
        final JLabel status = RealmeyeRequestHandler.checkRealmeyeStatus();
        if (status != null) {
          final JCheckBox checkbox = new JCheckBox("Don't alert in the future");
          final Object[] params = {status, checkbox};
          JOptionPane.showMessageDialog(this,
                  params, "Realmeye server error", JOptionPane.WARNING_MESSAGE);
          Main.getSettings().setShowAlert(!checkbox.isSelected());
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Check if there is a new version of the program.
   */
  private void checkNewVersion() {
    final int remaining = getGithubRateLimit();

    if (remaining <= 30) {
      return;
    }

    try {
      final URL url = new URL("https://api.github.com/repos/waifu/osanctools/releases");
      final JSONTokener tokener = new JSONTokener(url.openStream());
      final JSONArray jsonArray = new JSONArray(tokener);
      for (int i = 0; i < jsonArray.length(); i++) {
        if (!jsonArray.getJSONObject(i).getBoolean("prerelease") && !jsonArray.getJSONObject(i).getBoolean("draft")) {
          final JSONObject release = jsonArray.getJSONObject(i);

          final String recentVersion = release.getString("tag_name");
          final int parseVersion = parseVersion(recentVersion);
          final String version = "v1.0.0.8";
          final int currentVersion = parseVersion(version);

          if (currentVersion < parseVersion) {
            final JTextArea textarea = new JTextArea("> https://github.com/Waifu/OsancTools/releases/tag/" + recentVersion);
            textarea.setEditable(false);
            final String text = "<html>Note: previous versions are <b>deprecated</b>. Please update immediately.";
            final JLabel label = new JLabel(text);
            final Object[] params = {"A new version was found. Please download it here:", textarea, label};

            JOptionPane.showMessageDialog(this,
                    params, "New release version found", JOptionPane.WARNING_MESSAGE);
          }
          break;
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the current rate limit.
   *
   * @return remaining requests as int.
   */
  private int getGithubRateLimit() {
    final JSONObject jsonObject;
    try {
      final URL url = new URL("https://api.github.com/rate_limit");
      final JSONTokener tokener = new JSONTokener(url.openStream());
      jsonObject = new JSONObject(tokener);
      return jsonObject.getJSONObject("resources").getJSONObject("core").getInt("remaining");
    } catch (final Exception e) {
      return 0;
    }
  }

  /**
   * Get the current version of the program.
   *
   * @param version version as a String.
   * @return parsed version as an int.
   */
  private int parseVersion(final String version) {
    return Integer.parseInt(version.replace('.', ' ').replace("v", "").replace(" ", ""));
  }

  /**
   * To be documented.
   *
   * @return worker.
   */
  public static SwingWorker getWorker() {
    return worker;
  }

  /**
   * createUIComponents method.
   *
   * <p>Modifies custom components on launch.
   */
  private void createUIComponents() {
    main = new JPanel();
    main.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane = new JTabbedPane();
    main.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));

    tabbedPane.addTab("Home", new HomePanel(this));
    tabbedPane.addTab("Sheets", new RequirementSheetPanel());
    tabbedPane.addTab("Exalts", new ExaltsPanel());
    tabbedPane.addTab("Options", new OptionsPanel());
    tabbedPane.addTab("Credits", new CreditsPanel());
  }

  /**
   * Displays a modal message panel.
   *
   * @param message the message to tell the user.
   * @param type the type of message value [INFORMATION, WARNING, ERROR]
   */
  public static void displayMessage(final String message, final int type) {
    JOptionPane.showMessageDialog(Gui.getFrames()[0], message, null, type);
  }
}

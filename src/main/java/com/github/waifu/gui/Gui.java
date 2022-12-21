package com.github.waifu.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.github.waifu.debug.KeyListener;
import com.github.waifu.entities.Raid;
import com.github.waifu.enums.Stat;
import com.github.waifu.gui.actions.CalculatePlayerExaltationsAction;
import com.github.waifu.gui.actions.GetWebAppDataAction;
import com.github.waifu.gui.actions.ParseWebAppReactsAction;
import com.github.waifu.gui.actions.ParseWebAppSetsAction;
import com.github.waifu.gui.actions.StoreStatIndexAction;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Gui class to construct the Gui.
 */
public class Gui extends JFrame {

  /**
   * To be documented.
   */
  public static final int NORMAL_MODE = 0;
  /**
   * To be documented.
   */
  public static final int DEBUG_MODE = 1;
  /**
   * To be documented.
   */
  public static final int LAN_MODE = 2;
  /**
   * Path for getting test resources.
   */
  public static final String TEST_RESOURCE_PATH = "src/test/resources/";
  /**
   * Raid object.
   */
  private static Raid raid;
  /**
   * Mode of the program.
   */
  private static int mode;
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
   * Button to get WebApp data.
   */
  private JButton inputRaidButton;
  /**
   * Button to parse vc.
   */
  private JButton vcParseButton;
  /**
   * Button to parse reacts.
   */
  private JButton parseReactsButton;
  /**
   * Button to parse sets.
   */
  private JButton parseSetsButton;
  /**
   * To be documented.
   */
  private JProgressBar progressBar;
  /**
   * To be documented.
   */
  private JPanel main;
  /**
   * To be documented.
   */
  private JTabbedPane tabbedPane;
  /**
   * To be documented.
   */
  private JPanel home;
  /**
   * To be documented.
   */
  private JLabel raidLabel;
  /**
   * To be documented.
   */
  private JPanel raidPanel;
  /**
   * To be documented.
   */
  private JPanel descriptionPanel;
  /**
   * To be documented.
   */
  private JLabel description;
  /**
   * To be documented.
   */
  private JButton stopButton;
  /**
   * To be documented.
   */
  private JPanel connected;
  /**
   * To be documented.
   */
  private JLabel metadata;
  /**
   * To be documented.
   */
  private JPanel creditsPanel;
  /**
   * To be documented.
   */
  private JPanel options;
  /**
   * To be documented.
   */
  private JPasswordField tokenField;
  /**
   * To be documented.
   */
  private JButton setTokenButton;
  /**
   * To be documented.
   */
  private JRadioButton lightRadioButton;
  /**
   * To be documented.
   */
  private JRadioButton darkRadioButton;
  /**
   * To be documented.
   */
  private JPasswordField betaTokenField;
  /**
   * To be documented.
   */
  private JButton setBetaTokenButton;
  /**
   * To be documented.
   */
  private JLabel webAppTokenField;
  /**
   * To be documented.
   */
  private JButton clearSettingsButton;
  /**
   * To be documented.
   */
  private JPanel metadataPanel;
  /**
   * To be documented.
   */
  private JPanel buttonsPanel;
  /**
   * To be documented.
   */
  private JPanel progressBarPanel;
  /**
   * To be documented.
   */
  private JLabel creditsImage;
  /**
   * To be documented.
   */
  private JRadioButton manaRadioButton;
  /**
   * To be documented.
   */
  private JRadioButton defenseRadioButton;
  /**
   * To be documented.
   */
  private JRadioButton dexterityRadioButton;
  /**
   * To be documented.
   */
  private JRadioButton wisdomRadioButton;
  /**
   * To be documented.
   */
  private JRadioButton lifeRadioButton;
  /**
   * To be documented.
   */
  private JRadioButton attackRadioButton;
  /**
   * To be documented.
   */
  private JRadioButton speedRadioButton;
  /**
   * To be documented.
   */
  private JRadioButton vitalityRadioButton;
  /**
   * To be documented.
   */
  private JTextField exaltsInput;
  /**
   * To be documented.
   */
  private JButton exaltsButton;
  /**
   * To be documented.
   */
  private JLabel exaltsResult;
  /**
   * To be documented.
   */
  private JLabel lifePotionImage;
  /**
   * To be documented.
   */
  private JLabel manaPotionImage;
  /**
   * To be documented.
   */
  private JLabel attackPotionImage;
  /**
   * To be documented.
   */
  private JLabel speedPotionImage;
  /**
   * To be documented.
   */
  private JLabel vitalityPotionImage;
  /**
   * To be documented.
   */
  private JLabel defensePotionImage;
  /**
   * To be documented.
   */
  private JLabel dexterityPotionImage;
  /**
   * To be documented.
   */
  private JLabel wisdomPotionImage;
  /**
   * To be documented.
   */
  private JTextField requirementInput;
  /**
   * To be documented.
   */
  private JButton requirementButton;
  /**
   * To be documented.
   */
  private JPanel exaltsSelection;
  /**
   * To be documented.
   */
  private JComboBox requirementSheetComboBox;
  /**
   * To be documented.
   */
  private JCheckBox showRealmeyeAlertCheckBox;
  /**
   * To be documented.
   */
  private JButton setResourceDirButton;
  /**
   * To be documented.
   */
  private TitledBorder border;

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
    $$$setupUI$$$();
    createUIComponents();
    SwingUtilities.updateComponentTreeUI(getRootPane());
    addActionListeners();
    add(main);
    setAlwaysOnTop(true);
    setResizable(false);
    setTitle("OsancTools");
    setIconImage(new ImageIcon(Utilities.getImageResource("images/gui/Gravestone.png")).getImage());
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
   * Get the mode of the GUI.
   *
   * @return mode as int.
   */
  public static int getMode() {
    return mode;
  }

  /**
   * Set the mode of the GUI.
   *
   * @param newMode int representing the mode.
   */
  public void setMode(final int newMode) {
    Gui.mode = newMode;
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
      final String recentVersion = jsonArray.getJSONObject(0).getString("tag_name");
      final int parseVersion = parseVersion(recentVersion);
      final String version = "v1.0.0.4";
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
   * addActionListeners method.
   *
   * <p>Constructs all listeners for the JFrame.
   */
  private void addActionListeners() {
    inputRaidButton.addActionListener(new GetWebAppDataAction(main, this));

    vcParseButton.addActionListener(e -> {
      final AcceptFilePanel acceptFilePanel = new AcceptFilePanel();
      acceptFilePanel.setLocationRelativeTo(this);
    });

    parseReactsButton.addActionListener(new ParseWebAppReactsAction(progressBar, stopButton, parseReactsButton));

    parseSetsButton.addActionListener(new ParseWebAppSetsAction(progressBar, stopButton, parseSetsButton));

    stopButton.addActionListener(e -> {
      if (worker != null && !worker.isDone() && !worker.isCancelled()) {
        worker.cancel(true);
        stopButton.setText("Stopped");
      }
    });

    requirementButton.addActionListener(e -> Main.getSettings().setRequirement(Integer.parseInt(requirementInput.getText())));

    exaltsButton.addActionListener(new CalculatePlayerExaltationsAction(exaltsInput, exaltsResult));

    lifeRadioButton.addActionListener(new StoreStatIndexAction(Stat.LIFE));

    manaRadioButton.addActionListener(new StoreStatIndexAction(Stat.MANA));

    attackRadioButton.addActionListener(new StoreStatIndexAction(Stat.ATTACK));

    defenseRadioButton.addActionListener(new StoreStatIndexAction(Stat.DEFENSE));

    speedRadioButton.addActionListener(new StoreStatIndexAction(Stat.SPEED));

    dexterityRadioButton.addActionListener(new StoreStatIndexAction(Stat.DEXTERITY));

    vitalityRadioButton.addActionListener(new StoreStatIndexAction(Stat.VITALITY));

    wisdomRadioButton.addActionListener(new StoreStatIndexAction(Stat.WISDOM));

    setResourceDirButton.addActionListener(e -> {
      final JFileChooser fc = new JFileChooser();
      final FileFilter filter = new FileNameExtensionFilter("Assets (*.assets)", "assets");

      fc.setFileFilter(filter);
      final int returnVal = fc.showOpenDialog(main);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        Main.getSettings().setResourceDir(fc.getSelectedFile().getAbsolutePath());
        System.out.println(fc.getSelectedFile().getAbsolutePath());
      }
    });

    showRealmeyeAlertCheckBox.addActionListener(e -> {
      Main.getSettings().setShowAlert(showRealmeyeAlertCheckBox.isSelected());
    });

    requirementSheetComboBox.addActionListener(e -> {
      final JComboBox comboBox = (JComboBox) e.getSource();
      Main.getSettings().setRequirementSheetName(String.valueOf(comboBox.getSelectedItem()));
      try {
        Main.loadRequirementSheet();
        TimeUnit.SECONDS.sleep(1);
      } catch (final Exception ex) {
        ex.printStackTrace();
      }
    });

    clearSettingsButton.addActionListener(e -> {
      final int confirm = JOptionPane.showConfirmDialog(main,
              "Are you sure you want to clear your settings?",
              "Confirmation",
              JOptionPane.YES_NO_OPTION);
      if (confirm == 0) {
        Main.getSettings().clearSettings();
      }
    });

    setTokenButton.addActionListener(e -> {
      final String token = String.valueOf(tokenField.getPassword());
      final int confirm = JOptionPane.showConfirmDialog(main,
              "The current token is: " + Main.getSettings().getToken() + "\n Would you like to change it to: " + token,
              "Confirmation",
              JOptionPane.YES_NO_OPTION);
      if (confirm == 0) {
        Main.getSettings().setToken(token);
      }
    });

    setBetaTokenButton.addActionListener(e -> {
      final String betaToken = String.valueOf(betaTokenField.getPassword());
      final int confirm = JOptionPane.showConfirmDialog(main,
              "The current token is: " + Main.getSettings().getBetaToken() + "\n Would you like to change it to: " + betaToken,
              "Confirmation",
              JOptionPane.YES_NO_OPTION);
      if (confirm == 0) {
        Main.getSettings().setBetaToken(betaToken);
      }
    });

    tokenField.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(final FocusEvent e) {
        tokenField.setEchoChar((char) 0);
      }

      @Override
      public void focusLost(final FocusEvent e) {
        tokenField.setEchoChar('•');
      }
    });

    betaTokenField.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(final FocusEvent e) {
        betaTokenField.setEchoChar((char) 0);
      }

      @Override
      public void focusLost(final FocusEvent e) {
        betaTokenField.setEchoChar('•');
      }
    });

    lightRadioButton.addActionListener(e -> Main.getSettings().setTheme("light"));

    darkRadioButton.addActionListener(e -> Main.getSettings().setTheme("dark"));
  }

  /**
   * Method generated by IntelliJ IDEA Gui Designer.
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    main = new JPanel();
    main.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane = new JTabbedPane();
    main.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    home = new JPanel();
    home.setLayout(new GridLayoutManager(2, 2, new Insets(5, 5, 5, 5), -1, -1));
    tabbedPane.addTab("Home", home);
    connected = new JPanel();
    connected.setLayout(new GridLayoutManager(3, 1, new Insets(0, 5, 0, 0), -1, -1));
    home.add(connected, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(300, 150), null, 0, false));
    connected.setBorder(BorderFactory.createTitledBorder(null, "Not Connected", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    raidPanel = new JPanel();
    raidPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    connected.add(raidPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
    raidLabel = new JLabel();
    raidLabel.setHorizontalAlignment(0);
    raidLabel.setHorizontalTextPosition(2);
    raidLabel.setText(" ");
    raidPanel.add(raidLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
    descriptionPanel = new JPanel();
    descriptionPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    connected.add(descriptionPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
    description = new JLabel();
    description.setHorizontalAlignment(0);
    description.setText(" ");
    descriptionPanel.add(description, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, 1, null, null, null, 0, false));
    metadataPanel = new JPanel();
    metadataPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    connected.add(metadataPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
    metadata = new JLabel();
    metadata.setHorizontalAlignment(0);
    metadata.setText(" ");
    metadataPanel.add(metadata, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, 1, null, null, null, 0, false));
    buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 5), -1, -1));
    home.add(buttonsPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    inputRaidButton = new JButton();
    inputRaidButton.setText("Input Raid ID");
    buttonsPanel.add(inputRaidButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    vcParseButton = new JButton();
    vcParseButton.setActionCommand("Parse VC");
    vcParseButton.setLabel("Parse VC");
    vcParseButton.setText("Parse VC");
    buttonsPanel.add(vcParseButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    parseReactsButton = new JButton();
    parseReactsButton.setText("Parse Reacts");
    buttonsPanel.add(parseReactsButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    parseSetsButton = new JButton();
    parseSetsButton.setText("Parse Sets");
    buttonsPanel.add(parseSetsButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    stopButton = new JButton();
    stopButton.setHorizontalTextPosition(0);
    stopButton.setText("Stop Process");
    buttonsPanel.add(stopButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    progressBarPanel = new JPanel();
    progressBarPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    home.add(progressBarPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    progressBar = new JProgressBar();
    progressBarPanel.add(progressBar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(8, 10, new Insets(5, 5, 5, 5), -1, -1));
    tabbedPane.addTab("Exalts", panel1);
    final JLabel label1 = new JLabel();
    label1.setText("Enter username:");
    panel1.add(label1, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    exaltsInput = new JTextField();
    exaltsInput.setMargin(new Insets(2, 6, 2, 6));
    panel1.add(exaltsInput, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
    exaltsButton = new JButton();
    exaltsButton.setText("Enter");
    panel1.add(exaltsButton, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    requirementInput = new JTextField();
    requirementInput.setMargin(new Insets(2, 6, 2, 6));
    panel1.add(requirementInput, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
    final JLabel label2 = new JLabel();
    label2.setText("Set requirement:");
    panel1.add(label2, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    requirementButton = new JButton();
    requirementButton.setText("Set");
    panel1.add(requirementButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    exaltsSelection = new JPanel();
    exaltsSelection.setLayout(new GridLayoutManager(4, 4, new Insets(0, 5, 0, 0), -1, -1));
    panel1.add(exaltsSelection, new GridConstraints(0, 2, 8, 8, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    exaltsSelection.setBorder(BorderFactory.createTitledBorder(null, "Stats", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    manaRadioButton = new JRadioButton();
    manaRadioButton.setHorizontalTextPosition(4);
    manaRadioButton.setText("");
    exaltsSelection.add(manaRadioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    manaPotionImage = new JLabel();
    manaPotionImage.setHorizontalTextPosition(2);
    manaPotionImage.setText("");
    exaltsSelection.add(manaPotionImage, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    defensePotionImage = new JLabel();
    defensePotionImage.setHorizontalTextPosition(2);
    defensePotionImage.setText("");
    exaltsSelection.add(defensePotionImage, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    defenseRadioButton = new JRadioButton();
    defenseRadioButton.setHorizontalTextPosition(4);
    defenseRadioButton.setText("");
    exaltsSelection.add(defenseRadioButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    lifeRadioButton = new JRadioButton();
    lifeRadioButton.setHorizontalTextPosition(4);
    lifeRadioButton.setText("");
    exaltsSelection.add(lifeRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    dexterityRadioButton = new JRadioButton();
    dexterityRadioButton.setHorizontalTextPosition(4);
    dexterityRadioButton.setText("");
    exaltsSelection.add(dexterityRadioButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    dexterityPotionImage = new JLabel();
    dexterityPotionImage.setHorizontalTextPosition(2);
    dexterityPotionImage.setText("");
    exaltsSelection.add(dexterityPotionImage, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    wisdomPotionImage = new JLabel();
    wisdomPotionImage.setHorizontalTextPosition(2);
    wisdomPotionImage.setText("");
    exaltsSelection.add(wisdomPotionImage, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    wisdomRadioButton = new JRadioButton();
    wisdomRadioButton.setHorizontalTextPosition(4);
    wisdomRadioButton.setText("");
    exaltsSelection.add(wisdomRadioButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    attackRadioButton = new JRadioButton();
    attackRadioButton.setHorizontalTextPosition(4);
    attackRadioButton.setText("");
    exaltsSelection.add(attackRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    speedRadioButton = new JRadioButton();
    speedRadioButton.setHorizontalTextPosition(4);
    speedRadioButton.setText("");
    exaltsSelection.add(speedRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    vitalityRadioButton = new JRadioButton();
    vitalityRadioButton.setText("");
    exaltsSelection.add(vitalityRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    lifePotionImage = new JLabel();
    lifePotionImage.setText("");
    exaltsSelection.add(lifePotionImage, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    attackPotionImage = new JLabel();
    attackPotionImage.setHorizontalTextPosition(2);
    attackPotionImage.setText("");
    exaltsSelection.add(attackPotionImage, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    speedPotionImage = new JLabel();
    speedPotionImage.setText("");
    exaltsSelection.add(speedPotionImage, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    vitalityPotionImage = new JLabel();
    vitalityPotionImage.setText("");
    exaltsSelection.add(vitalityPotionImage, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    exaltsResult = new JLabel();
    exaltsResult.setHorizontalAlignment(11);
    exaltsResult.setHorizontalTextPosition(10);
    exaltsResult.setText("Calculate completions for each exalt.");
    panel1.add(exaltsResult, new GridConstraints(1, 0, 3, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JLabel label3 = new JLabel();
    label3.setText("Result:");
    label3.setVerticalAlignment(0);
    panel1.add(label3, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    options = new JPanel();
    options.setLayout(new GridLayoutManager(7, 5, new Insets(5, 5, 5, 5), -1, -1));
    tabbedPane.addTab("Options", options);
    tokenField = new JPasswordField();
    tokenField.putClientProperty("JPasswordField.cutCopyAllowed", Boolean.TRUE);
    options.add(tokenField, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
    setTokenButton = new JButton();
    setTokenButton.setText("Set Token");
    options.add(setTokenButton, new GridConstraints(3, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    webAppTokenField = new JLabel();
    webAppTokenField.setText("WebApp Token");
    options.add(webAppTokenField, new GridConstraints(2, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    lightRadioButton = new JRadioButton();
    lightRadioButton.setText("Light");
    options.add(lightRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    darkRadioButton = new JRadioButton();
    darkRadioButton.setText("Dark");
    options.add(darkRadioButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JLabel label4 = new JLabel();
    label4.setText("WebApp Beta Token");
    options.add(label4, new GridConstraints(4, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    betaTokenField = new JPasswordField();
    betaTokenField.putClientProperty("JPasswordField.cutCopyAllowed", Boolean.TRUE);
    options.add(betaTokenField, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
    setBetaTokenButton = new JButton();
    setBetaTokenButton.setText("Set Beta Token");
    options.add(setBetaTokenButton, new GridConstraints(5, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    clearSettingsButton = new JButton();
    clearSettingsButton.setText("Clear Settings");
    options.add(clearSettingsButton, new GridConstraints(1, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JLabel label5 = new JLabel();
    label5.setText("Theme (Requires Restart)");
    options.add(label5, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    requirementSheetComboBox = new JComboBox();
    options.add(requirementSheetComboBox, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    showRealmeyeAlertCheckBox = new JCheckBox();
    showRealmeyeAlertCheckBox.setText("Show Realmeye Alert");
    options.add(showRealmeyeAlertCheckBox, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    setResourceDirButton = new JButton();
    setResourceDirButton.setText("Set Resource Dir");
    options.add(setResourceDirButton, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    creditsPanel = new JPanel();
    creditsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane.addTab("Credits", creditsPanel);
    creditsImage = new JLabel();
    creditsImage.setHorizontalAlignment(0);
    creditsImage.setHorizontalTextPosition(0);
    creditsImage.setText("<html>Made with ♡ by Su<br><center>su#4008</html>");
    creditsImage.setToolTipText("https://discord.gg/oryx");
    creditsImage.setVerticalAlignment(0);
    creditsImage.setVerticalTextPosition(3);
    creditsPanel.add(creditsImage, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    ButtonGroup buttonGroup;
    buttonGroup = new ButtonGroup();
    buttonGroup.add(lightRadioButton);
    buttonGroup.add(darkRadioButton);
    buttonGroup = new ButtonGroup();
    buttonGroup.add(lifeRadioButton);
    buttonGroup.add(manaRadioButton);
    buttonGroup.add(defenseRadioButton);
    buttonGroup.add(dexterityRadioButton);
    buttonGroup.add(wisdomRadioButton);
    buttonGroup.add(vitalityRadioButton);
    buttonGroup.add(attackRadioButton);
    buttonGroup.add(speedRadioButton);
  }

  /**
   * To be documented.
   *
   * @noinspection ALL
   * @return To be documented.
   */
  public JComponent $$$getRootComponent$$$() {
    return main;
  }

  /**
   * Update the GUI panel with WebApp metadata.
   */
  public void updateGui() {
    try {
      if (raid != null) {
        border.setTitle("Connected");
        border.setTitleColor(Color.green);
        connected.setBorder(border);
        SwingUtilities.updateComponentTreeUI(connected);
        raidPanel.setOpaque(true);
        final Color color = new Color(raid.getJson().getInt("bg_color"));
        final int max = 255;
        final int invR = max - color.getRed();
        final int invG = max - color.getGreen();
        final int invB = max - color.getBlue();
        final Color invertedColor = new Color(invR, invG, invB);
        raidPanel.setBackground(color);
        raidLabel.setForeground(invertedColor);
        raidLabel.setIcon(raid.getRaidLeader().getResizedAvatar(25, 25));
        raidLabel.setText(raid.getName() + " led by " + raid.getRaidLeader().getServerNickname());

        final String textId = "ID: " + raid.getId();
        final String textStatus = " Status: " + raid.getStatus();
        final String textLocation = " Location: " + raid.getLocation();
        final String raidMetadata = textId + textStatus + textLocation;
        metadata.setText(raidMetadata);

        String raidDescription = raid.getDescription();
        if (raidDescription.length() > raidMetadata.length()) {
          raidDescription = raidDescription.substring(0, raidMetadata.length()) + "...";
        }
        description.setText(raidDescription);
        pack();
      }
    } catch (final JSONException exception) {
      System.out.println("Cannot find assets from JSON, did you select the right file?");
    }
  }

  /**
   * createUIComponents method.
   *
   * <p>Modifies custom components on launch.
   */
  private void createUIComponents() {
    if ("dark".equals(Main.getSettings().getTheme())) {
      darkRadioButton.setSelected(true);
    } else {
      lightRadioButton.setSelected(true);
    }
    creditsImage.setIcon(new ImageIcon(Utilities.getImageResource("images/gui/Gravestone.png")));
    lifePotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/life.png")));
    manaPotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/mana.png")));
    attackPotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/attack.png")));
    defensePotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/defense.png")));
    speedPotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/speed.png")));
    dexterityPotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/dexterity.png")));
    vitalityPotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/vitality.png")));
    wisdomPotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/wisdom.png")));
    switch (Main.getSettings().getStat()) {
      case 1 -> manaRadioButton.setSelected(true);
      case 2 -> attackRadioButton.setSelected(true);
      case 3 -> defenseRadioButton.setSelected(true);
      case 4 -> speedRadioButton.setSelected(true);
      case 5 -> dexterityRadioButton.setSelected(true);
      case 6 -> vitalityRadioButton.setSelected(true);
      case 7 -> wisdomRadioButton.setSelected(true);
      default -> lifeRadioButton.setSelected(true);
    }
    requirementInput.setText(String.valueOf(Main.getSettings().getRequirement()));
    border = BorderFactory.createTitledBorder("None");
    border.setTitle("Not Connected");
    border.setTitleColor(Color.red);
    connected.setBorder(border);
    raidPanel.setOpaque(false);
    showRealmeyeAlertCheckBox.setSelected(Main.getSettings().showAlert());
    tokenField.setText(Main.getSettings().getToken());
    betaTokenField.setText(Main.getSettings().getBetaToken());
    creditsPanel.addKeyListener(new KeyListener());

    final List<String> requirementSheets = Main.getSettings().getRequirementSheets();
    final String selectedSheet = Main.getSettings().getRequirementSheetName();
    for (final String s : requirementSheets) {
      requirementSheetComboBox.addItem(s);
      if (selectedSheet.equals(s)) {
        requirementSheetComboBox.setSelectedIndex(requirementSheets.indexOf(s));
      }
    }
  }
}

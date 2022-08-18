package com.github.waifu.gui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.github.waifu.entities.Account;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.React;
import com.github.waifu.enums.Stat;
import com.github.waifu.gui.actions.*;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.handlers.WebAppHandler;
import com.github.waifu.util.ExaltCalculator;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import net.sourceforge.tess4j.TesseractException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

/**
 * GUI class to construct the GUI.
 */
public class GUI extends JFrame {

    public static final int NORMAL_MODE = 0;
    public static final int DEBUG_MODE = 1;
    public static final int LAN_MODE = 2;


    public static String RESOURCE_PATH = "src/main/resources/";
    public static String TEST_RESOURCE_PATH = "src/test/resources/";
    private static int mode;
    private JButton inputRaidButton;
    private JButton vcParseButton;
    private JButton parseReactsButton;
    private JButton parseSetsButton;
    private JProgressBar progressBar;
    private JPanel main;
    private JTabbedPane tabbedPane;
    private JPanel home;
    private JLabel raid;
    private JPanel raidPanel;
    private JPanel descriptionPanel;
    private JLabel description;
    private JButton stopButton;
    private JPanel connected;
    private JLabel metadata;
    private JPanel creditsPanel;
    private JPanel options;
    private JPasswordField tokenField;
    private JButton setTokenButton;
    private JRadioButton lightRadioButton;
    private JRadioButton darkRadioButton;
    private JPasswordField betaTokenField;
    private JButton setBetaTokenButton;
    private JLabel WebAppTokenField;
    private JButton clearSettingsButton;
    private JPanel metadataPanel;
    private JPanel buttonsPanel;
    private JPanel progressBarPanel;
    private JLabel creditsImage;
    private JRadioButton manaRadioButton = new JRadioButton();
    private JRadioButton defenseRadioButton = new JRadioButton();
    private JRadioButton dexterityRadioButton = new JRadioButton();
    private JRadioButton wisdomRadioButton = new JRadioButton();
    private JRadioButton lifeRadioButton = new JRadioButton();
    private JRadioButton attackRadioButton = new JRadioButton();
    private JRadioButton speedRadioButton = new JRadioButton();
    private JRadioButton vitalityRadioButton = new JRadioButton();
    private JTextField exaltsInput;
    private JButton exaltsButton = new JButton();
    private JLabel exaltsResult;
    private JLabel lifePotionImage;
    private JLabel manaPotionImage;
    private JLabel attackPotionImage;
    private JLabel speedPotionImage;
    private JLabel vitalityPotionImage;
    private JLabel defensePotionImage;
    private JLabel dexterityPotionImage;
    private JLabel wisdomPotionImage;
    private JTextField requirementInput = new JTextField();
    private JButton requirementButton = new JButton();
    private JPanel exaltsSelection;
    private TitledBorder border;
    private static JSONObject json;
    private static boolean processRunning;
    private static SwingWorker<Void, Void> worker;

    /**
     * GUI method.
     * <p>
     * Constructs a JFrame to display the GUI.
     */
    public GUI() {
        try {
            UIManager.setLookAndFeel(Preferences.userRoot().get("theme", "light").equals("dark") ? new FlatDarkLaf() : new FlatLightLaf());
            FlatLaf.updateUI();
        } catch (Exception ignored) {
        }
        $$$setupUI$$$();
        createUIComponents();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SwingUtilities.updateComponentTreeUI(getRootPane());
        addActionListeners();
        add(main);
        setAlwaysOnTop(true);
        setResizable(false);
        setTitle("OsancTools");
        setBounds(0, 0, screenSize.width / 4, screenSize.height / 4);
        setIconImage(new ImageIcon(Utilities.getImageResource("Gravestone.png")).getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setMinimumSize(new Dimension(screenSize.width / 4, screenSize.height / 4));
        pack();
        setVisible(true);
    }

    /**
     * addActionListeners method.
     * <p>
     * Constructs all listeners for the JFrame.
     */
    private void addActionListeners() {
        inputRaidButton.addActionListener(new GetWebAppDataAction(main, border, connected, raid, raidPanel, description, metadata, this));

        vcParseButton.addActionListener(new ParseVoiceChannelAction());

        parseReactsButton.addActionListener(new ParseWebAppReactsAction(progressBar, stopButton));

        parseSetsButton.addActionListener(new ParseWebAppSetsAction(progressBar, stopButton));

        stopButton.addActionListener(e -> {
            if (worker != null && !worker.isDone() && !worker.isCancelled()) {
                worker.cancel(true);
                stopButton.setText("Stopped");
            }
        });

        requirementButton.addActionListener(e -> {
            Preferences userPrefs = Preferences.userRoot();
            userPrefs.putInt("requirement", Integer.parseInt(requirementInput.getText()));
        });

        exaltsButton.addActionListener(new CalculatePlayerExaltationsAction(exaltsInput, exaltsResult));

        lifeRadioButton.addActionListener(new StoreStatIndexAction(Stat.LIFE));

        manaRadioButton.addActionListener(new StoreStatIndexAction(Stat.MANA));

        attackRadioButton.addActionListener(new StoreStatIndexAction(Stat.ATTACK));

        defenseRadioButton.addActionListener(new StoreStatIndexAction(Stat.DEFENSE));

        speedRadioButton.addActionListener(new StoreStatIndexAction(Stat.SPEED));

        dexterityRadioButton.addActionListener(new StoreStatIndexAction(Stat.DEXTERITY));

        vitalityRadioButton.addActionListener(new StoreStatIndexAction(Stat.VITALITY));

        wisdomRadioButton.addActionListener(new StoreStatIndexAction(Stat.WISDOM));

        clearSettingsButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(main,
                    "Are you sure you want to clear your settings?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                Preferences userPrefs = Preferences.userRoot();
                userPrefs.remove("token");
                userPrefs.remove("betaToken");
                userPrefs.remove("theme");
                userPrefs.remove("stat");
                userPrefs.remove("requirement");
            }
        });

        setTokenButton.addActionListener(e -> {
            Preferences userPrefs = Preferences.userRoot();
            String newToken = String.valueOf(tokenField.getPassword());
            int confirm = JOptionPane.showConfirmDialog(main,
                    "The current token is: " + userPrefs.get("token", "") + "\n Would you like to change it to: " + newToken,
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                userPrefs.put("token", newToken);
            }
        });

        setBetaTokenButton.addActionListener(e -> {
            Preferences userPrefs = Preferences.userRoot();
            String newToken = String.valueOf(betaTokenField.getPassword());
            int confirm = JOptionPane.showConfirmDialog(main,
                    "The current token is: " + userPrefs.get("betaToken", "") + "\n Would you like to change it to: " + newToken,
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                userPrefs.put("betaToken", newToken);
            }
        });

        tokenField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                tokenField.setEchoChar((char) 0);
            }

            @Override
            public void focusLost(FocusEvent e) {
                tokenField.setEchoChar('•');
            }
        });

        betaTokenField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                betaTokenField.setEchoChar((char) 0);
            }

            @Override
            public void focusLost(FocusEvent e) {
                betaTokenField.setEchoChar('•');
            }
        });

        lightRadioButton.addActionListener(e -> Preferences.userRoot().put("theme", "light"));

        darkRadioButton.addActionListener(e -> Preferences.userRoot().put("theme", "dark"));

       // this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(78, 0, true), "n");
        //this.getRootPane().getActionMap().put("n", act);
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
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
        raid = new JLabel();
        raid.setHorizontalAlignment(0);
        raid.setHorizontalTextPosition(2);
        raid.setText(" ");
        raidPanel.add(raid, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
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
        manaPotionImage.setIcon(new ImageIcon(getClass().getResource("/images/potions/mana.png")));
        manaPotionImage.setText("");
        exaltsSelection.add(manaPotionImage, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        defensePotionImage = new JLabel();
        defensePotionImage.setHorizontalTextPosition(2);
        defensePotionImage.setIcon(new ImageIcon(getClass().getResource("/images/potions/defense.png")));
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
        dexterityPotionImage.setIcon(new ImageIcon(getClass().getResource("/images/potions/dexterity.png")));
        dexterityPotionImage.setText("");
        exaltsSelection.add(dexterityPotionImage, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        wisdomPotionImage = new JLabel();
        wisdomPotionImage.setHorizontalTextPosition(2);
        wisdomPotionImage.setIcon(new ImageIcon(getClass().getResource("/images/potions/wisdom.png")));
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
        lifePotionImage.setIcon(new ImageIcon(getClass().getResource("/images/potions/life.png")));
        lifePotionImage.setText("");
        exaltsSelection.add(lifePotionImage, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        attackPotionImage = new JLabel();
        attackPotionImage.setHorizontalTextPosition(2);
        attackPotionImage.setIcon(new ImageIcon(getClass().getResource("/images/potions/attack.png")));
        attackPotionImage.setText("");
        exaltsSelection.add(attackPotionImage, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        speedPotionImage = new JLabel();
        speedPotionImage.setIcon(new ImageIcon(getClass().getResource("/images/potions/speed.png")));
        speedPotionImage.setText("");
        exaltsSelection.add(speedPotionImage, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        vitalityPotionImage = new JLabel();
        vitalityPotionImage.setIcon(new ImageIcon(getClass().getResource("/images/potions/vitality.png")));
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
        options.setLayout(new GridLayoutManager(6, 4, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane.addTab("Options", options);
        tokenField = new JPasswordField();
        tokenField.putClientProperty("JPasswordField.cutCopyAllowed", Boolean.TRUE);
        options.add(tokenField, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        setTokenButton = new JButton();
        setTokenButton.setText("Set Token");
        options.add(setTokenButton, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        WebAppTokenField = new JLabel();
        WebAppTokenField.setText("WebApp Token");
        options.add(WebAppTokenField, new GridConstraints(2, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lightRadioButton = new JRadioButton();
        lightRadioButton.setText("Light");
        options.add(lightRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        darkRadioButton = new JRadioButton();
        darkRadioButton.setText("Dark");
        options.add(darkRadioButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Theme (Requires Restart)");
        options.add(label4, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("WebApp Beta Token");
        options.add(label5, new GridConstraints(4, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        betaTokenField = new JPasswordField();
        betaTokenField.putClientProperty("JPasswordField.cutCopyAllowed", Boolean.TRUE);
        options.add(betaTokenField, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        setBetaTokenButton = new JButton();
        setBetaTokenButton.setText("Set Beta Token");
        options.add(setBetaTokenButton, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        clearSettingsButton = new JButton();
        clearSettingsButton.setText("Clear Settings");
        options.add(clearSettingsButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        creditsPanel = new JPanel();
        creditsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Credits", creditsPanel);
        creditsImage = new JLabel();
        creditsImage.setHorizontalAlignment(0);
        creditsImage.setHorizontalTextPosition(0);
        creditsImage.setIcon(new ImageIcon(getClass().getResource("/Gravestone.png")));
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
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return main;
    }

    public void setMode(int mode) {
        GUI.mode = mode;
    }

    public void updateGUI() {
        try {
            if (json != null && json.get("status").equals(1)) {
                border.setTitle("Connected");
                border.setTitleColor(Color.green);
                connected.setBorder(border);
                SwingUtilities.updateComponentTreeUI(connected);
                raidPanel.setOpaque(true);
                Color color = new Color(json.getJSONObject("raid").getInt("bg_color"));
                Color invertedColor = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
                raidPanel.setBackground(color);
                raid.setForeground(invertedColor);
                try {
                    String avatar = json.getJSONObject("raid").getJSONObject("creator").getString("avatar");
                    Image i;
                    if (avatar.contains(".gif")) {
                        i = new ImageIcon(new URL(json.getJSONObject("raid").getJSONObject("creator").getString("avatar"))).getImage().getScaledInstance(25, 25, Image.SCALE_REPLICATE);
                    } else {
                        i = new ImageIcon(new URL(json.getJSONObject("raid").getJSONObject("creator").getString("avatar"))).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                    }
                    raid.setIcon(new ImageIcon(i));
                    raid.setText(json.getJSONObject("raid").getString(("name")) + " led by " + json.getJSONObject("raid").getJSONObject("creator").getString("server_nickname"));
                    metadata.setText("ID: " + json.getJSONObject("raid").getInt(("id")) + " Status: " + json.getJSONObject("raid").getString("status") + " Location: " + json.getJSONObject("raid").get("location"));
                    description.setText(String.valueOf(json.getJSONObject("raid").get("description")));
                    pack();
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (JSONException exception) {
            System.out.println("Cannot find assets from JSON, did you select the right file?");
        }
    }

    /**
     * createUIComponents method.
     * <p>
     * Modifies custom components on launch.
     */
    private void createUIComponents() {
        // TODO: place custom component creation code here
        switch (Preferences.userRoot().get("theme", "light")) {
            case "light" -> lightRadioButton.setSelected(true);
            case "dark" -> darkRadioButton.setSelected(true);
        }

        switch (Preferences.userRoot().getInt("stat", 0)) {
            case 0 -> lifeRadioButton.setSelected(true);
            case 1 -> manaRadioButton.setSelected(true);
            case 2 -> attackRadioButton.setSelected(true);
            case 3 -> defenseRadioButton.setSelected(true);
            case 4 -> speedRadioButton.setSelected(true);
            case 5 -> dexterityRadioButton.setSelected(true);
            case 6 -> vitalityRadioButton.setSelected(true);
            case 7 -> wisdomRadioButton.setSelected(true);
        }
        requirementInput.setText(String.valueOf(Preferences.userRoot().getInt("requirement", 100)));
        border = BorderFactory.createTitledBorder("None");
        border.setTitle("Not Connected");
        border.setTitleColor(Color.red);
        connected.setBorder(border);
        raidPanel.setOpaque(false);
        tokenField.setText(Preferences.userRoot().get("token", ""));
        betaTokenField.setText(Preferences.userRoot().get("betaToken", ""));
    }


    public static SwingWorker getWorker() {
        return worker;
    }

    public static void setWorker(SwingWorker worker) {
        GUI.worker = worker;
    }

    public static JSONObject getJson() {
        return GUI.json;
    }

    public static void setJson(JSONObject json) {
        GUI.json = json;
    }

    public static int getMode() {
        return mode;
    }

    public static void setProcessRunning(boolean processRunning) {
        GUI.processRunning = processRunning;
    }

    public static boolean checkProcessRunning() {
        if (processRunning) {
            Component rootPane = GUI.getFrames()[0].getComponents()[0];
            JOptionPane.showMessageDialog(rootPane,
                    "Please wait until the current process is finished.",
                    "Wait!",
                    JOptionPane.ERROR_MESSAGE);
        }
        return processRunning;
    }
}

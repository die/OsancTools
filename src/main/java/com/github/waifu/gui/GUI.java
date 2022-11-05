package com.github.waifu.gui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.github.waifu.entities.Raid;
import com.github.waifu.enums.Stat;
import com.github.waifu.gui.actions.*;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GUI class to construct the GUI.
 */
public class GUI extends JFrame {

    public static final int NORMAL_MODE = 0;
    public static final int DEBUG_MODE = 1;
    public static final int LAN_MODE = 2;
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
    private JLabel raidLabel;
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
    private JRadioButton manaRadioButton;
    private JRadioButton defenseRadioButton;
    private JRadioButton dexterityRadioButton;
    private JRadioButton wisdomRadioButton;
    private JRadioButton lifeRadioButton;
    private JRadioButton attackRadioButton;
    private JRadioButton speedRadioButton;
    private JRadioButton vitalityRadioButton;
    private JTextField exaltsInput;
    private JButton exaltsButton;
    private JLabel exaltsResult;
    private JLabel lifePotionImage;
    private JLabel manaPotionImage;
    private JLabel attackPotionImage;
    private JLabel speedPotionImage;
    private JLabel vitalityPotionImage;
    private JLabel defensePotionImage;
    private JLabel dexterityPotionImage;
    private JLabel wisdomPotionImage;
    private JTextField requirementInput;
    private JButton requirementButton;
    private JPanel exaltsSelection;
    private TitledBorder border;
    private static JSONObject json;
    public static Raid raid;
    private static boolean processRunning;
    private static SwingWorker<Void, Void> worker;

    /**
     * GUI method.
     * <p>
     * Constructs a JFrame to display the GUI.
     */
    public GUI() {
        try {
            UIManager.setLookAndFeel(Main.settings.getTheme().equals("dark") ? new FlatDarkLaf() : new FlatLightLaf());
            FlatLaf.updateUI();
        } catch (Exception ignored) {
        }
        $$$setupUI$$$();
        createUIComponents();
        SwingUtilities.updateComponentTreeUI(getRootPane());
        addActionListeners();
        add(main);
        setAlwaysOnTop(true);
        setResizable(false);
        setTitle("OsancTools");
        setIconImage(new ImageIcon(Utilities.getImageResource("Gravestone.png")).getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        // checkRealmeye();
    }

    private void checkRealmeye() {
        try {

            if (Main.settings.showAlert()) {
                JLabel status = RealmeyeRequestHandler.checkRealmeyeStatus();
                if (status != null) {
                    JCheckBox checkbox = new JCheckBox("Don't alert in the future");
                    Object[] params = {status, checkbox};
                    JOptionPane.showMessageDialog(this,
                            params, "Realmeye server error", JOptionPane.WARNING_MESSAGE);
                    if (checkbox.isSelected()) {
                        Main.settings.setShowAlert(false);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * addActionListeners method.
     * <p>
     * Constructs all listeners for the JFrame.
     */
    private void addActionListeners() {
        inputRaidButton.addActionListener(new GetWebAppDataAction(main, this));

        vcParseButton.addActionListener(e -> {
            //new ParseVoiceChannelAction()
            AcceptFilePanel acceptFilePanel = new AcceptFilePanel();
            acceptFilePanel.setLocationRelativeTo(this);
        });

        parseReactsButton.addActionListener(new ParseWebAppReactsAction(progressBar, stopButton));

        parseSetsButton.addActionListener(new ParseWebAppSetsAction(progressBar, stopButton));

        stopButton.addActionListener(e -> {
            if (worker != null && !worker.isDone() && !worker.isCancelled()) {
                worker.cancel(true);
                stopButton.setText("Stopped");
            }
        });

        requirementButton.addActionListener(e -> {
            Main.settings.setRequirement(Integer.parseInt(requirementInput.getText()));
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
                Main.settings.clearSettings();
            }
        });

        setTokenButton.addActionListener(e -> {
            String token = String.valueOf(tokenField.getPassword());
            int confirm = JOptionPane.showConfirmDialog(main,
                    "The current token is: " + Main.settings.getToken() + "\n Would you like to change it to: " + token,
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                Main.settings.setToken(token);
            }
        });

        setBetaTokenButton.addActionListener(e -> {
            String betaToken = String.valueOf(betaTokenField.getPassword());
            int confirm = JOptionPane.showConfirmDialog(main,
                    "The current token is: " + Main.settings.getBetaToken() + "\n Would you like to change it to: " + betaToken,
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                Main.settings.setBetaToken(betaToken);
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

        lightRadioButton.addActionListener(e -> Main.settings.setTheme("light"));

        darkRadioButton.addActionListener(e -> Main.settings.setTheme("dark"));

        //getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK, true), "activateDebugMode");
       // getRootPane().getActionMap().put("activateDebugMode", new DebugModeAction());
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
        options.setLayout(new GridLayoutManager(6, 5, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane.addTab("Options", options);
        tokenField = new JPasswordField();
        tokenField.putClientProperty("JPasswordField.cutCopyAllowed", Boolean.TRUE);
        options.add(tokenField, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
        setTokenButton = new JButton();
        setTokenButton.setText("Set Token");
        options.add(setTokenButton, new GridConstraints(3, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        WebAppTokenField = new JLabel();
        WebAppTokenField.setText("WebApp Token");
        options.add(WebAppTokenField, new GridConstraints(2, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return main;
    }

    /**
     * @param mode
     */
    public void setMode(int mode) {
        GUI.mode = mode;
    }

    /**
     *
     */
    public void updateGUI() {
        try {
            if (raid != null) {
                border.setTitle("Connected");
                border.setTitleColor(Color.green);
                connected.setBorder(border);
                SwingUtilities.updateComponentTreeUI(connected);
                raidPanel.setOpaque(true);
                Color color = new Color(raid.getJson().getInt("bg_color"));
                Color invertedColor = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
                raidPanel.setBackground(color);
                raidLabel.setForeground(invertedColor);
                raidLabel.setIcon(raid.getRaidLeader().getResizedAvatar(25, 25));
                raidLabel.setText(raid.getName() + " led by " + raid.getRaidLeader().getServerNickname());
                metadata.setText("ID: " + raid.getId() + " Status: " + raid.getStatus() + " Location: " + raid.getLocation());
                description.setText(String.valueOf(raid.getDescription()));
                pack();
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
        switch (Main.settings.getTheme()) {
            case "light" -> lightRadioButton.setSelected(true);
            case "dark" -> darkRadioButton.setSelected(true);
        }
        creditsImage.setIcon(new ImageIcon(Utilities.getImageResource("Gravestone.png")));
        lifePotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/life.png")));
        manaPotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/mana.png")));
        attackPotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/attack.png")));
        defensePotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/defense.png")));
        speedPotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/speed.png")));
        dexterityPotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/dexterity.png")));
        vitalityPotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/vitality.png")));
        wisdomPotionImage.setIcon(new ImageIcon(Utilities.getImageResource("images/potions/wisdom.png")));
        switch (Main.settings.getStat()) {
            case 0 -> lifeRadioButton.setSelected(true);
            case 1 -> manaRadioButton.setSelected(true);
            case 2 -> attackRadioButton.setSelected(true);
            case 3 -> defenseRadioButton.setSelected(true);
            case 4 -> speedRadioButton.setSelected(true);
            case 5 -> dexterityRadioButton.setSelected(true);
            case 6 -> vitalityRadioButton.setSelected(true);
            case 7 -> wisdomRadioButton.setSelected(true);
        }
        requirementInput.setText(String.valueOf(Main.settings.getRequirement()));
        border = BorderFactory.createTitledBorder("None");
        border.setTitle("Not Connected");
        border.setTitleColor(Color.red);
        connected.setBorder(border);
        raidPanel.setOpaque(false);
        tokenField.setText(Main.settings.getToken());
        betaTokenField.setText(Main.settings.getBetaToken());
    }

    /**
     * @param worker
     */
    public static void setWorker(SwingWorker worker) {
        GUI.worker = worker;
    }

    /**
     * @return
     */
    public static JSONObject getJson() {
        return GUI.json;
    }

    /**
     * @param json
     */
    public static void setJson(JSONObject json) {
        GUI.json = json;
    }

    /**
     * @return
     */
    public static int getMode() {
        return mode;
    }

    /**
     * @param processRunning
     */
    public static void setProcessRunning(boolean processRunning) {
        GUI.processRunning = processRunning;
    }

    /**
     * @return
     */
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

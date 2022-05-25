package com.github.waifu.gui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.github.waifu.entities.Account;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.React;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.handlers.WebAppHandler;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.jsoup.Jsoup;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

/**
 * GUI class to construct the GUI.
 */
public class GUI extends JFrame {

    private JButton inputRaidButton;
    private JButton calcExaltsButton;
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
    private TitledBorder border;
    private JSONObject json;
    private boolean processRunning;
    private SwingWorker<Void, Void> worker;

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
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/Gravestone.png"))).getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(screenSize.width / 4, screenSize.height / 4));
        pack();
        setVisible(true);
    }

    /**
     * addActionListeners method.
     * <p>
     * Constructs all listeners for the JFrame.
     */
    private void addActionListeners() {
        inputRaidButton.addActionListener(e -> {
            try {
                if (processRunning) {
                    JOptionPane.showMessageDialog(main,
                            "Please wait until the current process is finished.",
                            "Wait!",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String s = (String) JOptionPane.showInputDialog(
                        main,
                        "Please paste in the raid id from osanc.net",
                        "Input",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "");
                JSONObject request = WebAppHandler.getRaid(s);
                if (request != null && request.get("status").equals(1)) {
                    json = request;
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
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(main,
                            "Error: failed to get json.",
                            "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(main,
                        ex.getStackTrace(),
                        ex.getMessage(),
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        calcExaltsButton.addActionListener(e -> {
            String username = (String) JOptionPane.showInputDialog(
                    main,
                    "Please provide a username",
                    "Calculate the number of life exalts.",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
            if (username != null && !username.equals("") && username.chars().allMatch((Character::isAlphabetic)) && username.length() <= 10) {
                try {
                    List<String[]> collection = RealmeyeRequestHandler.GETExalts(username);
                    int o3Completes = 0;
                    if (collection != null) {
                        for (String[] strings : collection) {
                            int exalt = Integer.parseInt(strings[2].replace("+", ""));
                            int m = 1;
                            while (m <= (exalt / 5)) {
                                o3Completes += 5 * m;
                                m++;
                            }
                        }
                    }
                    if (o3Completes >= 100) {
                        JOptionPane.showMessageDialog(main,
                                username + " can be vet verified with " + o3Completes + " completes!",
                                "Success",
                                JOptionPane.QUESTION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(main,
                                username + " cannot be vet verified with " + o3Completes + " completes!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(main,
                        "Please input a valid username\nYou provided [" + username + "]",
                        "Encountered an error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        parseReactsButton.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    if (processRunning) {
                        JOptionPane.showMessageDialog(main,
                                "Please wait until the current process is finished.",
                                "Wait!",
                                JOptionPane.ERROR_MESSAGE);
                        return null;
                    } else if (testProxy()) {
                        stopButton.setText("Stop Process");
                        worker = this;
                        progressBar.setValue(0);
                        processRunning = true;
                        List<React> reacts = getReacts(json, progressBar);
                        new ReactTable(reacts);
                        processRunning = false;
                        stopButton.setText("Finished");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    processRunning = false;
                }
                return null;
            }
        }.execute());

        parseSetsButton.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    if (processRunning) {
                        JOptionPane.showMessageDialog(main,
                                "Please wait until the current process is finished.",
                                "Wait!",
                                JOptionPane.ERROR_MESSAGE);
                        return null;
                    } else if (testProxy()) {
                        stopButton.setText("Stop Process");
                        worker = this;
                        progressBar.setValue(0);
                        processRunning = true;
                        Map<Account, Inventory> sets = getSets(json, progressBar);
                        new SetTable(sets);
                        processRunning = false;
                        stopButton.setText("Finished");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    processRunning = false;
                }
                return null;
            }
        }.execute());

        stopButton.addActionListener(e -> {
            if (worker != null && !worker.isDone() && !worker.isCancelled()) {
                worker.cancel(true);
                stopButton.setText("Stopped");
            }
        });

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
        calcExaltsButton = new JButton();
        calcExaltsButton.setText("Calculate Exalts");
        buttonsPanel.add(calcExaltsButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        options = new JPanel();
        options.setLayout(new GridLayoutManager(6, 4, new Insets(0, 5, 5, 5), -1, -1));
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
        final JLabel label1 = new JLabel();
        label1.setText("Theme (Requires Restart)");
        options.add(label1, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("WebApp Beta Token");
        options.add(label2, new GridConstraints(4, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
        creditsImage.setIcon(new ImageIcon(getClass().getResource("/resources/Gravestone.png")));
        creditsImage.setText("<html>Made with ♡ by Su<br><center>su#4008</html>");
        creditsImage.setToolTipText("https://discord.gg/oryx");
        creditsImage.setVerticalAlignment(0);
        creditsImage.setVerticalTextPosition(3);
        creditsPanel.add(creditsImage, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(lightRadioButton);
        buttonGroup.add(darkRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return main;
    }

    private boolean testProxy() {
        try {
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9150));
            Jsoup.connect("http://example.com/").proxy(proxy).get();
            return true;
        } catch (Exception e) {
            int confirm = JOptionPane.showConfirmDialog(main,
                    "Warning: you have chosen to use Direct Connect. Are you sure?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            return confirm == 0;
        }
    }

    /**
     * getReacts method.
     * <p>
     * Constructs a list of React objects that contain:
     * React metadata, including icon and name.
     * All raiders who reacted and their Accounts.
     *
     * @param json JSONObject returned from the WebApp.
     * @param bar  progress bar to be updated over time.
     */
    private List<React> getReacts(JSONObject json, JProgressBar bar) throws InterruptedException, IOException {
        if (json == null || !json.has("raid")) {
            return null;
        } else {
            Map<String, List<String>> map = Utilities.parseRaiderReacts(json);
            if (map.isEmpty()) {
                return null;
            } else {
                JSONArray items = (JSONArray) Utilities.json.get("reactItem");
                JSONArray classes = (JSONArray) Utilities.json.get("reactClass");
                JSONArray reactDPS = (JSONArray) Utilities.json.get("reactDps");
                List<React> reacts = new ArrayList<>();
                bar.setMaximum(map.size());
                int count = 1;
                for (Map.Entry<String, List<String>> m : map.entrySet()) {
                    List<Account> raiders = new ArrayList<>();
                    for (int i = 2; i < m.getValue().size(); i++) {
                        Account account = RealmeyeRequestHandler.GET(m.getValue().get(i));
                        raiders.add(account);
                        TimeUnit.SECONDS.sleep(1);
                    }
                    String reactName = m.getValue().get(1);
                    if (items.contains(reactName)) {
                        reacts.add(new React(m.getKey(), reactName, "item", m.getValue().get(0), raiders));
                    } else if (classes.contains(reactName)) {
                        reacts.add(new React(m.getKey(), reactName, "class", m.getValue().get(0), raiders));
                    } else if (reactDPS.contains(reactName)) {
                        reacts.add(new React(m.getKey(), reactName, "dps", m.getValue().get(0), raiders));
                    } else if (reactName.contains("Effusion")) {
                        reacts.add(new React(m.getKey(), reactName, "lock", m.getValue().get(0), raiders));
                    } else {
                        reacts.add(new React(m.getKey(), reactName, m.getValue().get(0), raiders));
                    }
                    bar.setValue(count);
                    count++;
                }
                return reacts;
            }
        }
    }

    /**
     * getSets method.
     * <p>
     * Constructs a map containing accounts and their inventories.
     *
     * @param json JSONObject returned from the WebApp
     * @param bar  progress bar to be updated over time.
     */
    private Map<Account, Inventory> getSets(JSONObject json, JProgressBar bar) throws InterruptedException, IOException {
        if (json == null || !json.has("raid")) {
            return null;
        } else {
            List<String> usernames = Utilities.parseRaiders(json).stream().toList();
            if (usernames.isEmpty()) {
                return null;
            }
            bar.setMaximum(usernames.size());
            Map<Account, Inventory> map = new HashMap<>();
            for (int i = 0; i < usernames.size(); i++) {
                Account account = RealmeyeRequestHandler.GET(usernames.get(i));
                if (account.getCharacters() != null) {
                    Inventory inventory = account.getCharacters().get(0).getInventory();
                    map.put(account, inventory.parseInventory());
                } else {
                    map.put(account, null);
                }
                TimeUnit.SECONDS.sleep(1);
                bar.setValue(i + 1);
            }
            return map;
        }
    }

    /**
     * createUIComponents method.
     * <p>
     * Modifies custom components on launch.
     */
    private void createUIComponents() {
        // TODO: place custom component creation code here
        if (Preferences.userRoot().get("theme", "light").equals("light")) {
            lightRadioButton.setSelected(true);
        } else {
            darkRadioButton.setSelected(true);
        }
        border = BorderFactory.createTitledBorder("None");
        border.setTitle("Not Connected");
        border.setTitleColor(Color.red);
        connected.setBorder(border);
        raidPanel.setOpaque(false);
        tokenField.setText(Preferences.userRoot().get("token", ""));
        betaTokenField.setText(Preferences.userRoot().get("betaToken", ""));
    }
}

package com.github.waifu.gui.panels;

import com.github.waifu.gui.Main;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * To be documented.
 */
public class OptionsPanel extends JPanel {

  /**
   * To be documented.
   */
  private final ButtonGroup buttonGroup;
  /**
   * To be documented.
   */
  private final JRadioButton lightThemeRadioButton;
  /**
   * To be documented.
   */
  private final JRadioButton darkThemeRadioButton;
  /**
   * To be documented.
   */
  private final JButton clearSettingsButton;
  /**
   * To be documented.
   */
  private final JPasswordField tokenPasswordField;
  /**
   * To be documented.
   */
  private final JButton setTokenButton;
  /**
   * To be documented.
   */
  private final JCheckBox showRealmeyeAlertCheckBox;
  /**
   * To be documented.
   */
  private final JButton setResourceDirButton;

  /**
   * To be documented.
   */
  public OptionsPanel() {
    setLayout(new GridLayoutManager(7, 5, new Insets(5, 5, 5, 5), -1, -1));
    tokenPasswordField = new JPasswordField();
    tokenPasswordField.putClientProperty("JPasswordField.cutCopyAllowed", Boolean.TRUE);
    tokenPasswordField.setText(Main.getSettings().getToken());
    add(tokenPasswordField, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
    setTokenButton = new JButton();
    setTokenButton.setText("Set Token");
    add(setTokenButton, new GridConstraints(3, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JLabel token = new JLabel();
    token.setText("Token (Requires Restart)");
    add(token, new GridConstraints(2, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    lightThemeRadioButton = new JRadioButton();
    lightThemeRadioButton.setText("Light");
    add(lightThemeRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    darkThemeRadioButton = new JRadioButton();
    darkThemeRadioButton.setText("Dark");
    add(darkThemeRadioButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    buttonGroup = new ButtonGroup();
    buttonGroup.add(lightThemeRadioButton);
    buttonGroup.add(darkThemeRadioButton);
    clearSettingsButton = new JButton();
    clearSettingsButton.setText("Clear Settings");
    add(clearSettingsButton, new GridConstraints(1, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JLabel label5 = new JLabel();
    label5.setText("Theme (Requires Restart)");
    add(label5, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    showRealmeyeAlertCheckBox = new JCheckBox();
    showRealmeyeAlertCheckBox.setText("Show Realmeye Alert");
    showRealmeyeAlertCheckBox.setSelected(Main.getSettings().showAlert());
    add(showRealmeyeAlertCheckBox, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    setResourceDirButton = new JButton();
    setResourceDirButton.setText("Set Resource Dir");
    add(setResourceDirButton, new GridConstraints(6, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    if ("dark".equals(Main.getSettings().getTheme())) {
      darkThemeRadioButton.setSelected(true);
    } else {
      lightThemeRadioButton.setSelected(true);
    }
    addActionListeners();
  }

  private void addActionListeners() {
    /*
      To be documented.
     */
    lightThemeRadioButton.addActionListener(e -> Main.getSettings().setTheme("light"));
    /*
      To be documented.
     */
    darkThemeRadioButton.addActionListener(e -> Main.getSettings().setTheme("dark"));
    /*
      To be documented.
     */
    clearSettingsButton.addActionListener(e -> {
      final int confirm = JOptionPane.showConfirmDialog(this,
              "Are you sure you want to clear your settings?",
              "Confirmation",
              JOptionPane.YES_NO_OPTION);
      if (confirm == 0) {
        Main.getSettings().clearSettings();
      }
    });
    /*
      To be documented.
     */
    setTokenButton.addActionListener(e -> {
      final String token = String.valueOf(tokenPasswordField.getPassword());
      final int confirm = JOptionPane.showConfirmDialog(this,
              "The current token is: " + Main.getSettings().getToken() + "\n Would you like to change it to: " + token,
              "Confirmation",
              JOptionPane.YES_NO_OPTION);
      if (confirm == 0) {
        Main.getSettings().setToken(token);
      }
    });

    /*
      To be documented.
     */
    tokenPasswordField.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(final FocusEvent e) {
        tokenPasswordField.setEchoChar((char) 0);
      }

      @Override
      public void focusLost(final FocusEvent e) {
        tokenPasswordField.setEchoChar('•');
      }
    });

    /*
      To be documented.
     */
    showRealmeyeAlertCheckBox.addActionListener(e -> {
      Main.getSettings().setShowAlert(showRealmeyeAlertCheckBox.isSelected());
    });
    /*
      To be documented.
     */
    setResourceDirButton.addActionListener(e -> {
      final JFileChooser fc = new JFileChooser();
      final FileFilter filter = new FileNameExtensionFilter("Assets (*.assets)", "assets");

      fc.setFileFilter(filter);
      final int returnVal = fc.showOpenDialog(this);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        Main.getSettings().setResourceDir(fc.getSelectedFile().getAbsolutePath());
      }
    });
  }
}

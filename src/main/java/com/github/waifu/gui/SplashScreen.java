package com.github.waifu.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * To be documented.
 */
public class SplashScreen extends JFrame {

  /**
   * To be documented.
   */
  public static int steps = 196;
  /**
   * To be documented.
   */
  private JProgressBar progressBar1;
  /**
   * To be documented.
   */
  private JPanel main;
  /**
   * To be documented.
   */
  private JLabel label;
  private JLabel label1;

  /**
   * To be documented.
   */
  public SplashScreen() {
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
    progressBar1.setMaximum(steps);
    add(main);
    setUndecorated(true);
    setAlwaysOnTop(true);
    setResizable(false);
    setTitle("OsancTools");
    setIconImage(new ImageIcon(Utilities.getClassResource("images/gui/Gravestone.png")).getImage());
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
    setFocusable(false);
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
    main.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
    main.setPreferredSize(new Dimension(480, 270));
    label1 = new JLabel();
    label1.setHorizontalAlignment(0);
    label1.setHorizontalTextPosition(0);
    label1.setIcon(new ImageIcon(getClass().getResource("/images/gui/Gravestone.png")));
    label1.setText("");
    label1.setVerticalTextPosition(3);
    main.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    progressBar1 = new JProgressBar();
    progressBar1.setIndeterminate(false);
    progressBar1.setOrientation(0);
    progressBar1.setStringPainted(true);
    progressBar1.setValue(0);
    main.add(progressBar1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, 25), null, 0, false));
    label = new JLabel();
    label.setHorizontalAlignment(0);
    label.setHorizontalTextPosition(0);
    label.setText("");
    main.add(label, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return main;
  }

  private void createUIComponents() {
    // TODO: place custom component creation code here
    label1.setText("Version " + Utilities.getApplicationVersion());
  }

  /**
   * To be documented.
   */
  public void incrementProgressBar() {
    final int increment = progressBar1.getValue() + 1;
    if (increment <= progressBar1.getMaximum()) {
      progressBar1.setValue(increment);
    }
  }

  /**
   * To be documented.
   */
  public JProgressBar getProgressBar1() {
    return progressBar1;
  }

  /**
   * To be documented.
   */
  public JLabel getLabel() {
    return label;
  }

  /**
   * To be documented.
   */
  public static void chooseResourcesFile() {
    JOptionPane.showMessageDialog(getFrames()[0], "Error loading resources.assets. Please select the correct file.");
    final JFileChooser fc = new JFileChooser();
    fc.setDialogTitle("Please select your resources.assets file.");
    final FileFilter filter = new FileNameExtensionFilter("Assets (*.assets)", "assets");

    fc.setFileFilter(filter);
    final int returnVal = fc.showOpenDialog(getFrames()[0]);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      Main.getSettings().setResourceDir(fc.getSelectedFile().getAbsolutePath());
    } else {
      System.exit(0);
    }
  }
}

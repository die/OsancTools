package com.github.waifu.gui;

import com.github.waifu.entities.Raider;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * To be documented.
 */
public class AccountView extends JFrame {

  /**
   * To be documented.
   */
  private final Raider raider;
  /**
   * To be documented.
   */
  private JPanel main;
  /**
   * To be documented.
   */
  private JLabel realmeye;
  /**
   * To be documented.
   */
  private JLabel discordInfo;
  /**
   * To be documented.
   */
  private JTabbedPane discordTab;

  /**
   * To be documented.
   *
   * @param raider To be documented.
   */
  public AccountView(final Raider raider) {
    this.raider = raider;
    $$$setupUI$$$();
    setContentPane(main);
    setAlwaysOnTop(true);
    setResizable(false);
    setTitle("OsancTools");
    setIconImage(new ImageIcon(Utilities.getImageResource("images/gui/Gravestone.png")).getImage());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setMinimumSize(new Dimension(screenSize.width / 4, screenSize.height / 4));
    setVisible(true);
    discordInfo.setIcon(raider.getAvatar());
    realmeye.setText("<html>" + raider.getAccounts().get(0).printAccount().replace("\n", "<br/>") + "</html>");
    discordInfo.setText("<html>" + raider.printRaider().replace("\n", "<br/>") + "</html>");
    pack();
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
    discordTab = new JTabbedPane();
    main.add(discordTab, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    discordTab.addTab("Discord", panel1);
    discordInfo = new JLabel();
    discordInfo.setHorizontalTextPosition(4);
    discordInfo.setText("Label");
    panel1.add(discordInfo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    discordTab.addTab("Realmeye", panel2);
    realmeye = new JLabel();
    realmeye.setText("Label");
    panel2.add(realmeye, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
   * To be documented.
   */
  private void createUIComponents() {
    // todo: place custom component creation code here
  }
}

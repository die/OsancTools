package com.github.waifu.gui.panels;

import com.github.waifu.debug.KeyListener;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * To be documented.
 */
public class CreditsPanel extends JPanel {

  /**
   * To be documented.
   */
  public CreditsPanel() {
    setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
    addCreditLabel();
    addKeyListener(new KeyListener());
  }

  /**
   * Constructs JLabel to display credits.
   */
  private void addCreditLabel() {
    final JLabel creditsImage = new JLabel();
    creditsImage.setHorizontalAlignment(0);
    creditsImage.setHorizontalTextPosition(0);
    creditsImage.setIcon(new ImageIcon(Utilities.getClassResource("images/gui/Gravestone.png")));
    creditsImage.setText("<html>Made with ♡ by Su<br><center>su#4008</html>");
    creditsImage.setToolTipText("https://discord.gg/oryx");
    creditsImage.setVerticalAlignment(0);
    creditsImage.setVerticalTextPosition(3);
    add(creditsImage, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
  }
}

package com.github.waifu.gui.panels;

import com.github.waifu.debug.Debug;
import com.github.waifu.debug.KeyListener;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.*;

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
    addKeyListener(new KeyListener(Arrays.asList('g', 'e', 'r', 'p', 'e', 'p'), "images/gui/bluecat.png", "how did you get here?"));
    addKeyListener(new KeyListener(Arrays.asList('g', 'i', 'm', 'r', 'e', 'a', 'p', 'e', 'r'), "images/gui/obito.png", "can i have a drink of water"));
    getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK), "debug");
    getActionMap().put("debug", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new Debug();
      }
    });
  }

  /**
   * Constructs JLabel to display credits.
   */
  private void addCreditLabel() {
    final JLabel creditsImage = new JLabel();
    creditsImage.setHorizontalAlignment(0);
    creditsImage.setHorizontalTextPosition(0);
    creditsImage.setIcon(new ImageIcon(Utilities.getClassResource("images/gui/Gravestone.png")));
    creditsImage.setText("<html><center>Version " + Utilities.getApplicationVersion() + "<br><br>Made with <a style=color:red;\">♥</a> by su<br>vampillia.</html>");
    creditsImage.setToolTipText("https://discord.gg/oryx");
    creditsImage.setVerticalAlignment(0);
    creditsImage.setVerticalTextPosition(3);
    add(creditsImage, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
  }
}

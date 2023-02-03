package com.github.waifu.debug;

import com.github.waifu.gui.Gui;
import com.github.waifu.util.Utilities;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * KeyListener class to listen for key combinations.
 */
public class KeyListener implements java.awt.event.KeyListener {

  /**
   * List of characters to listen for.
   */
  private List<Character> gerpep = Arrays.asList('g', 'e', 'r', 'p', 'e', 'p');
  /**
   * List of characters to keep track of inputs.
   */
  private List<Character> keys = new ArrayList<>();

  /**
   * Function that fires when a key is typed.
   *
   * @param e Keyevent object.
   */
  @Override
  public void keyTyped(final KeyEvent e) {

  }

  /**
   * Function that fires when a key is pressed.
   *
   * @param e KeyEvent object.
   */
  @Override
  public void keyPressed(final KeyEvent e) {

  }

  /**
   * Function that fires when a key is released.
   *
   * @param e KeyEvent object.
   */
  @Override
  public void keyReleased(final KeyEvent e) {
    final int index = keys.size();

    keys.add(e.getKeyChar());
    final boolean reachedLimit = keys.size() == gerpep.size();
    final boolean hasKey = gerpep.contains(e.getKeyChar());
    final boolean characterAtIndex = gerpep.get(index).equals(e.getKeyChar());
    if (keys.equals(gerpep)) {
      final URL imageUrl = Utilities.getClassResource("images/gui/bluecat.png");
      final ImageIcon blueCatIcon = new ImageIcon(imageUrl);
      final Image image = blueCatIcon.getImage();
      final int width = 50;
      final int height = 50;
      final int scaleMethod = Image.SCALE_SMOOTH;
      final Image scaledImage = image.getScaledInstance(width, height, scaleMethod);
      final ImageIcon blueCat = new ImageIcon(scaledImage);
      final String message = "how did you get here?";
      final JLabel label = new JLabel(message, blueCat, JLabel.TRAILING);
      JOptionPane.showMessageDialog(Gui.getFrames()[0], label);
      keys.clear();
    } else if (reachedLimit || !hasKey || !characterAtIndex) {
      keys.clear();
    }
  }
}

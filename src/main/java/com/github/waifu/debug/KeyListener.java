package com.github.waifu.debug;

import com.github.waifu.gui.Gui;
import com.github.waifu.util.Utilities;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
  private final List<Character> characters;
  /**
   * Image to show.
   */
  private final ImageIcon imageIcon;
  /**
   * Message to show.
   */
  private final String message;
  /**
   * List of characters to keep track of inputs.
   */
  private final List<Character> keys = new ArrayList<>();

  public KeyListener(final List<Character> characters, final String path, final String message) {
    final URL imageUrl = Utilities.getClassResource(path);
    this.characters = characters;
    this.imageIcon = new ImageIcon(imageUrl);;
    this.message = message;
  }

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
    final boolean reachedLimit = keys.size() == characters.size();
    final boolean hasKey = characters.contains(e.getKeyChar());
    final boolean characterAtIndex = characters.get(index).equals(e.getKeyChar());
    if (keys.equals(characters)) {
      final Image image = imageIcon.getImage();
      final int width = 50;
      final int height = 50;
      final int scaleMethod = Image.SCALE_SMOOTH;
      final Image scaledImage = image.getScaledInstance(width, height, scaleMethod);
      final ImageIcon newIcon = new ImageIcon(scaledImage);
      final JLabel label = new JLabel(message, newIcon, JLabel.CENTER);
      JOptionPane.showMessageDialog(Gui.getFrames()[0], label, characters.stream().map(String::valueOf).collect(Collectors.joining()), JOptionPane.PLAIN_MESSAGE);
      keys.clear();
    } else if (reachedLimit || !hasKey || !characterAtIndex) {
      keys.clear();
    }
  }
}

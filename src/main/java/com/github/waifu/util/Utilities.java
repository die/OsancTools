package com.github.waifu.util;

import com.github.waifu.gui.Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

/**
 * Utilities class for the program.
 */
public final class Utilities {

  /**
   * To be documented.
   */
  private Utilities() {

  }

  /**
   * Requirement sheet JSON.
   */
  private static JSONObject json;

  /**
   * markImage method.
   *
   * <p>Returns a modified ImageIcon that is marked with a background color.
   *
   * @param image image of the specific item.
   * @param color color of the background image
   * @return marked ImageIcon
   */
  public static ImageIcon markImage(final ImageIcon image, final Color color) {
    final BufferedImage bufferedImage = new BufferedImage(image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_RGB);
    final Graphics g = bufferedImage.createGraphics();
    g.setColor(color);
    g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
    g.drawImage(image.getImage(), 0, 0, null);
    g.dispose();
    return new ImageIcon(bufferedImage);
  }

  /**
   * To be documented.
   *
   * @param path to be documented.
   * @return to be documented.
   */
  public static URL getImageResource(final String path) {
    if (Utilities.class.getClassLoader().getResource("resources/" + path) != null) {
      return Utilities.class.getClassLoader().getResource("resources/" + path);
    } else {
      return Utilities.class.getClassLoader().getResource(path);
    }
  }

  /**
   * To be documented.
   *
   * @param file to be documented.
   * @return to be documented.
   */
  public static boolean isImage(final File file) {
    final String[] extensions = {"jpg", "jpeg", "png"};
    return Arrays.stream(extensions).anyMatch(e -> e.equals(FilenameUtils.getExtension(file.getName())));
  }

  /**
   * To be documented.
   *
   * @return to be documented.
   */
  public static Color getTextColorFromTheme() {
    if (Main.getSettings().getTheme().equals("dark")) {
      return Color.LIGHT_GRAY;
    } else {
      return Color.BLACK;
    }
  }

  /**
   * Inverts a provided color object.
   *
   * @param color the color to invert.
   * @return inverted color.
   */
  public static Color invertColor(final Color color) {
    final int max = 255;
    final int invR = max - color.getRed();
    final int invG = max - color.getGreen();
    final int invB = max - color.getBlue();
    return new Color(invR, invG, invB);
  }

  /**
   * To be documented.
   *
   * @param serverNickname to be documented.
   * @return to be documented.
   */
  public static List<String> parseUsernamesFromNickname(final String serverNickname) {
    final List<String> usernames = new ArrayList<>();
    final String[] split = serverNickname.split(" ");
    for (final String s : split) {
      final String username = s.replaceAll("[^\\p{L}]", "");
      if (!username.equals("")) {
        usernames.add(username);
      }
    }
    return usernames;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public static JSONObject getJson() {
    return json;
  }

  /**
   * To be documented.
   *
   * @param json To be documented.
   */
  public static void setJson(final JSONObject json) {
    Utilities.json = json;
  }
}

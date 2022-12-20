package com.github.waifu.util;

import com.github.waifu.gui.Main;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 *
 */
public class Utilities {

    public static JSONObject json;

    /**
     * markImage method.
     *
     * Returns a modified ImageIcon that is marked with a background color.
     *
     * @param image image of the specific item.
     * @param color color of the background image
     */
    public static ImageIcon markImage(ImageIcon image, Color color) {
        BufferedImage bufferedImage = new BufferedImage(image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        g.drawImage(image.getImage(), 0, 0, null);
        g.dispose();
        return new ImageIcon(bufferedImage);
    }

    /**
     *
     * @param path
     * @return
     */
    public static URL getImageResource(String path) {
        if (Utilities.class.getClassLoader().getResource("resources/" + path) != null) {
            return Utilities.class.getClassLoader().getResource("resources/" + path);
        } else {
            return Utilities.class.getClassLoader().getResource(path);
        }
    }

    /**
     *
     * @param file
     * @return
     */
    public static boolean isImage(File file) {
        String[] extensions = {"jpg", "jpeg", "png"};
        return Arrays.stream(extensions).anyMatch(e -> e.equals(FilenameUtils.getExtension(file.getName())));
    }

    public static Color getTextColorFromTheme() {
        if (Main.settings.getTheme().equals("dark")) {
            return Color.LIGHT_GRAY;
        } else {
            return Color.BLACK;
        }
    }

    public static List<String> parseUsernamesFromNickname(String serverNickname) {
        List<String> usernames = new ArrayList<>();
        String[] split = serverNickname.split(" ");
        for (String s : split) {
            String username = s.replaceAll("[^\\p{L}]", "");
            if (!username.equals(""))
            {
                usernames.add(username);
            }
        }
        return usernames;
    }
}

package com.github.waifu.gui.actions;

import com.github.waifu.gui.Gui;
import com.github.waifu.handlers.WebAppHandler;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * To be documented.
 */
public class ParseVoiceChannelAction implements ActionListener {

  /**
   * To be documented.
   */
  private JSONArray members;
  /**
   * To be documented.
   */
  private Image image;

  /**
   * To be documented.
   *
   * @param e To be documented.
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    // JDialog dialog = new JDialog()
        /*switch (Gui.getMode()) {
            case Gui.NORMAL_MODE, Gui.DEBUG_MODE -> parseVoiceChannelFromClipboard();
            case Gui.LAN_MODE -> parseVoiceChannelFromFile();
        }
        try {
            new VcParse(image, members);
        } catch (IOException | TesseractException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }*/
  }

  /**
   * To be documented.
   */
  private void parseVoiceChannelFromClipboard() {
    try {
      image = (Image) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.imageFlavor);
      final JSONObject newJson = WebAppHandler.getData(Gui.getJson().getJSONObject("raid").getInt("id"));
      members = newJson.getJSONObject("raid").getJSONArray("members");
      final String id = String.valueOf(newJson.getJSONObject("raid").getInt("id"));
      final FileWriter raidJSON = new FileWriter("src/test/resources/raids" + "/" + id + "/" + id + " [WC].json");
      raidJSON.write(newJson.toString(3));
      raidJSON.close();
      try {
        final BufferedImage image = (BufferedImage) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.imageFlavor);
        final File outputfile = new File("src/test/resources/raids" + "/" + id + "/who.png");
        if (outputfile.exists()) {
          return;
        }
        ImageIO.write(image, "png", outputfile);
      } catch (final Exception e) {
        e.printStackTrace();
      }
    } catch (final UnsupportedFlavorException | IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * To be documented.
   */
  private void parseVoiceChannelFromFile() {
    try {
      final File outputfile = new File("src/test/resources/raids" + "/" + Gui.getJson().getJSONObject("raid").getInt("id") + "/who.png");
      if (!outputfile.exists()) {
        System.out.println("Image does not exist");
      } else {
        image = ImageIO.read(outputfile);
        final File jsonFile = new File("src/test/resources/raids" + "/" + Gui.getJson().getJSONObject("raid").getInt("id") + "/" + Gui.getJson().getJSONObject("raid").getInt("id") + " [WC].json");
        final JSONObject json = new JSONObject(new JSONTokener(new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8)));
        members = json.getJSONObject("raid").getJSONArray("members");
      }
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }
}

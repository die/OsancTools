package com.github.waifu.gui.actions;

import com.github.waifu.gui.GUI;
import com.github.waifu.handlers.WebAppHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class ParseVoiceChannelAction implements ActionListener {

    private JSONArray members;
    private Image image;

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
       // JDialog dialog = new JDialog()
        /*switch (GUI.getMode()) {
            case GUI.NORMAL_MODE, GUI.DEBUG_MODE -> parseVoiceChannelFromClipboard();
            case GUI.LAN_MODE -> parseVoiceChannelFromFile();
        }
        try {
            new VCParse(image, members);
        } catch (IOException | TesseractException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }*/
    }

    /**
     *
     */
    private void parseVoiceChannelFromClipboard() {
        try {
            image = (Image) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.imageFlavor);
            JSONObject newJson = WebAppHandler.getData(GUI.getJson().getJSONObject("raid").getInt("id"));
            members = newJson.getJSONObject("raid").getJSONArray("members");
            String id = String.valueOf(newJson.getJSONObject("raid").getInt("id"));
            FileWriter raidJSON = new FileWriter("src/test/resources/raids" + "/" + id + "/" + id + " [WC].json");
            raidJSON.write(newJson.toString(3));
            raidJSON.close();
            try {
                BufferedImage image = (BufferedImage) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.imageFlavor);
                File outputfile = new File("src/test/resources/raids" + "/" + id + "/who.png");
                if (outputfile.exists()) {
                    System.out.println("Image already exists");
                    return;
                }
                ImageIO.write(image, "png", outputfile);
                System.out.println("/who saved");
            } catch (Exception e) {
                System.out.println("No image in clipboard.");
            }
        } catch (UnsupportedFlavorException | IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     */
    private void parseVoiceChannelFromFile() {
        try {
            File outputfile = new File("src/test/resources/raids" + "/" + GUI.getJson().getJSONObject("raid").getInt("id") + "/who.png");
            if (!outputfile.exists()) {
                System.out.println("Image does not exist");
            } else {
                image = ImageIO.read(outputfile);
                File jsonFile = new File("src/test/resources/raids" + "/" + GUI.getJson().getJSONObject("raid").getInt("id") + "/" + GUI.getJson().getJSONObject("raid").getInt("id") + " [WC].json");
                JSONObject json = new JSONObject(new JSONTokener(new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8)));
                members = json.getJSONObject("raid").getJSONArray("members");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

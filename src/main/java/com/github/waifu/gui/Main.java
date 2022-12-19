package com.github.waifu.gui;

import com.github.waifu.config.Settings;
import com.github.waifu.util.Utilities;
import org.json.JSONObject;
import org.json.JSONTokener;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;

/**
 * Main class to initialize the UI.
 */
public final class Main {

    public static Settings settings = new Settings();

    private Main() { }

    /**
     * Main method.
     *
     * Initializes the program.
     *
     * @param args input arguments that are not used.
     */
    public static void main(final String[] args) throws IOException {
        //try {
           // BufferedImage image = ImageIO.read(Utilities.getImageResource("images/items/renders.png"));
            //BufferedImage image1 = image.getSubimage(16698, 230, 46, 46);
           // File outputfile = new File("testimage.png");
          //  ImageIO.write(image1, "PNG", outputfile);
            //System.out.println(outputfile.getAbsolutePath());
       // } catch (Exception exception) {
          //  exception.printStackTrace();
       // }

        try {
            loadResources();
            SwingUtilities.invokeLater(GUI::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * loadResources method.
     *
     * Loads Oryx Sanctuary.json from the repository.
     */
    public static void loadResources() throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/Waifu/OsancTools/sniffer/src/main/resources/" + Main.settings.getRequirementSheetName() + ".json");
        JSONTokener tokener = new JSONTokener(url.openStream());
        Utilities.json = new JSONObject(tokener);

    }
}

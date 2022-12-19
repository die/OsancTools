package com.github.waifu.gui;

import com.github.waifu.config.Settings;
import com.github.waifu.handlers.PacketHandler;
import com.github.waifu.util.Utilities;
import org.json.JSONObject;
import org.json.JSONTokener;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

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
        try {
            String item = "Lumiare T12";
            System.out.println(item.substring(item.length()- 2));
            settings.setRequirementSheetName("osanc");
            BufferedImage image = ImageIO.read(Utilities.getImageResource("images/items/renders.png"));
            BufferedImage image1 = image.getSubimage(16698, 230, 46, 46);
            File outputfile = new File("testimage.png");
            ImageIO.write(image1, "PNG", outputfile);
            System.out.println(outputfile.getAbsolutePath());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

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
        //URL url = new URL("https://raw.githubusercontent.com/Waifu/OsancTools/master/src/main/resources/" + Main.settings.getRequirementSheetName() + ".json");
        //URL url = new URL("https://raw.githubusercontent.com/Waifu/OsancTools/sniffer/src/main/resources/losthalls.json");
       // Main.class.getClassLoader().getResourceAsStream("Oryx Sanctuary.json")
        JSONTokener tokener = new JSONTokener(new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("Pub Halls Advanced.json")), StandardCharsets.UTF_8));

       /// JSONTokener tokener = new JSONTokener(url.openStream());
        Utilities.json = new JSONObject(tokener);
        PacketHandler.loadEquipmentData();

    }
}

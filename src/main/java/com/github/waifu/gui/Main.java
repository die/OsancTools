package com.github.waifu.gui;

import com.github.waifu.config.Settings;
import com.github.waifu.util.Utilities;
import org.json.JSONObject;
import org.json.JSONTokener;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.net.*;

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
    public static void main(final String[] args) {
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
     * Loads osanc.json from the repository.
     */
    public static void loadResources() throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/Waifu/OsancTools/master/src/main/resources/osanc.json");
        JSONTokener tokener = new JSONTokener(url.openStream());
        Utilities.json  = new JSONObject(tokener);
    }
}

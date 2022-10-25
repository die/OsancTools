package com.github.waifu.gui;

import com.github.waifu.util.Utilities;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

class GUITest {

    @Test
    void runGUI() throws InterruptedException {
        loadResources();
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setMode(GUI.LAN_MODE);
        });
        while (true) {
            Thread.sleep(2000);
        }
    }

    public static void loadResources() {
        JSONTokener tokener = new JSONTokener(new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("osanc.json")), StandardCharsets.UTF_8));
        Utilities.json = new JSONObject(tokener);
    }
}
package com.github.waifu.gui;

import com.github.waifu.entities.Account;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.util.Utilities;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GUITest {

    @Test
    void runGUI() throws InterruptedException {
        loadResources();
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setMode(GUI.DEBUG_MODE);
            
        });
        while (true) {
            Thread.sleep(2000);
        }
    }

    public static void loadResources() {
        JSONTokener tokener = new JSONTokener(new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("ROTMG.json")), StandardCharsets.UTF_8));
        Utilities.json = new JSONObject(tokener);
    }
}
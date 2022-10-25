package com.github.waifu.util;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.React;
import com.github.waifu.gui.GUI;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.handlers.WebAppHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

class UtilitiesTest {

    private String id = "14993";

    /*@Test
    void logData() throws IOException, InterruptedException {
        File file = new File("src/test/resources/raids");
        if (!file.exists()){
            file.mkdirs();
        }
        File idpath = new File("src/test/resources/raids" + "/" + id + "/");
        if (idpath.exists()) {
            System.out.println("Raid data already exists.");
            return;
        }
        JSONObject json = WebAppHandler.getRaid(id);

        if (json != null) {
            File raid = new File(file.getPath() + "/" + id);
            if (!raid.exists()) {
                raid.mkdirs();
            }
            FileWriter raidJSON = new FileWriter(raid.getPath() + "/" + id + ".json");
            raidJSON.write(json.toString(3));
            raidJSON.close();
            File reacts = new File (raid.getPath() + "/reacts");
            if (!reacts.exists()) {
                reacts.mkdirs();
            }
            JSONArray reactArray = json.getJSONObject("raid").getJSONArray("reacts");
            for (int r = 0; r < reactArray.length(); r++) {

                    String reactId = String.valueOf(reactArray.getJSONObject(r).getInt("id"));
                    String reactUrl = reactArray.getJSONObject(r).getString("icon");

                    try {
                        URL rURL = new URL(reactUrl);
                        String reactExtension = reactUrl.split(".com")[1].split("\\.")[1].substring(0, 3);
                        try (InputStream in = rURL.openStream()) {
                            Files.copy(in, new File("src/test/resources/raids" + "/" + id + "/reacts/" + reactId + "." + reactExtension).toPath());
                        }
                    } catch (Exception e) {
                        System.out.println("Error with react: " + reactId);
                    }
            }
            Set<String> usernames = Utilities.parseRaiders(json);

            System.out.println("Saving player information...");
            int count = 1;
            for (String u : usernames) {
                System.out.println("\r" + "Parsed " + count + "/" + usernames.size());
                File player = new File(raid.getPath() + "/players/" + u);
                if (!player.exists()) {
                    player.mkdirs();
                }
                Document document = RealmeyeRequestHandler.getRealmeyeData(u);
                FileWriter playerHTML = new FileWriter(player.getPath() + "/data.html");
                playerHTML.write(Jsoup.parse(document.html()).toString());
                playerHTML.close();
                count++;
                TimeUnit.SECONDS.sleep(1);
            }


            JSONArray array = json.getJSONObject("raid").getJSONArray("members");
            for (int i = 0; i < array.length(); i++) {
                List<String> raiderUsernames = new ArrayList<>();
                String[] username = array.getJSONObject(i).getString("server_nickname").split(" ");
                for (String alts : username) {
                    String replace = alts.replaceAll("[^\\p{L}]", "");
                    if (!replace.equals(""))
                    {
                        raiderUsernames.add(replace);
                    }
                }

                for (String s : raiderUsernames) {
                    try {
                        File raider = new File("src/test/resources/raids/" + id + "/players/" + s + "/");
                        if (raider.exists()) {
                            String url = array.getJSONObject(i).getString("avatar");
                            String extension = url.split(".com")[1].split("\\.")[1].substring(0, 3);
                            URL url1 = new URL(url);
                            try (InputStream in = url1.openStream()) {
                                Files.copy(in, new File("src/test/resources/raids/" + id + "/players/" + s + "/" + "avatar." + extension).toPath());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error with:" + s);
                    }
                }
            }
        }
    }

    @Test
    void saveImage() {
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
    }*/
}
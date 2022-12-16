package com.github.waifu.handlers;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Character;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Item;
import com.github.waifu.gui.GUI;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * RealmeyeRequestHandler class to retrieve data from Realmeye.
 */
public class RealmeyeRequestHandler {

    /**
     *
     * @param username
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static Document getRealmeyeData(String username) throws IOException, InterruptedException {
        if (GUI.getMode() == GUI.LAN_MODE) {
            File html = new File(GUI.TEST_RESOURCE_PATH + "raids/" + GUI.getJson().getJSONObject("raid").getInt("id") + "/players/" + username + "/data.html");
            return Jsoup.parse(html, "UTF-8");
        } else {
            Document doc;
            try {
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9150));
                doc = Jsoup.connect("https://www.realmeye.com/player/" + username).proxy(proxy).get();
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                doc = Jsoup.connect("https://www.realmeye.com/player/" + username).get();
                TimeUnit.SECONDS.sleep(1);
            }
            if (GUI.getMode() == GUI.DEBUG_MODE) {
                File file = new File("src/test/resources/raids" + "/" + String.valueOf(GUI.getJson().getJSONObject("raid").getInt("id")) + "/" + "players/" + username + "/data.html");
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    FileWriter playerHTML = new FileWriter("src/test/resources/raids" + "/" + String.valueOf(GUI.getJson().getJSONObject("raid").getInt("id")) + "/" + "players/" + username + "/data.html");
                    playerHTML.write(Jsoup.parse(doc.html()).toString());
                    playerHTML.close();
                }
            }
            return doc;
        }
    }

    public static JLabel checkRealmeyeStatus() throws InterruptedException {
        Document document = null;
        try {
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9150));
            document = Jsoup.connect("https://www.realmeye.com").proxy(proxy).get();
        } catch (IOException e) {
            try {
                document = Jsoup.connect("https://www.realmeye.com").get();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (document != null) {
            String query = "div[class=help-block alert alert-warning col-md-8 col-md-offset-2]";
            if (document.select(query).hasText()) {
                JLabel label;
                if (document.select(query).select("strong").hasText()) {
                    String serverList = document.select(query).select("strong").text();
                    label = new JLabel("<html>Parsing will not work for the following servers:<br><p align=center><b>" + serverList + "<br>");
                    return label;
                } else {
                    label = new JLabel("<html>Parsing currently does not work.<br><p align=center>Realmeye cannot access <b>any</b> RoTMG servers.<br>");
                    return label;
                }
            }
        }
        return null;
    }
    /**
     *
     * @return
     */
    public static boolean checkDirectConnect() {
        try {
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9150));
            Jsoup.connect("https://en.wikipedia.org/").proxy(proxy).get();
            return true;
        } catch (Exception e) {
            Component rootPane = GUI.getFrames()[0].getComponents()[0];
            int confirm = JOptionPane.showConfirmDialog(rootPane,
                    "Warning: you have chosen to use Direct Connect. Are you sure?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            return confirm == 0;
        }
    }

    /**
     * GET method.
     *
     * Returns an Account object of the given username.
     * If the account is private or has no characters,
     * a default Account object is returned.
     *
     * @param username username of the raider.
     */
    public static Account parseHTML(Document doc, String username) {
        if (doc.select("h2").text().equals("Sorry, but we either:")) {
            return getPrivateAccount(username);
        } else if (doc.select("h3").text().equals("Characters are hidden")) {
            return getPrivateAccount(username);
        } else if (doc.select("table").eachText().get(0).split(" ")[1].equals("0")) {
            return getPrivateAccount(username);
        } else {
            try {
                String numberOfSkins = getElementFromSummary(doc, "Skins");
                String exaltations = getElementFromSummary(doc, "Exaltations");
                String stars = Jsoup.parse(doc.html()).select("div[class=star-container]").text();
                String accountFame = getElementFromSummary(doc, "Account fame");
                String guild = getElementFromSummary(doc, "Guild");
                String guildRank = getElementFromSummary(doc, "Guild Rank");
                String creationDate = getElementFromSummary(doc, "First seen");
                String lastSeen = getElementFromSummary(doc, "Last seen");
                if (creationDate.isEmpty()) {
                    creationDate = getElementFromSummary(doc, "Created");
                }
                List<String> characterData = Jsoup.parse(doc.html()).select("table[class=table table-striped tablesorter] tr").eachText();
                List<String> characterSkinData = Jsoup.parse(doc.html()).select("a.character").eachAttr("data-skin");
                List<String> itemData = Jsoup.parse(doc.html()).select("span.item").eachAttr("title");
                List<String> itemImageData = Jsoup.parse(doc.html()).select("span.item").eachAttr("style");
                List<Character> characters = new ArrayList<>();
                String headers = characterData.get(0);
                for (int i = 1; i < characterData.size(); i++) {
                    String[] metadata = characterData.get(i).split(" ");
                    String type = getElementFromCharacterTable(metadata, headers, "Class");
                    String level = getElementFromCharacterTable(metadata, headers, "L");
                    String cqc = getElementFromCharacterTable(metadata, headers, "CQC");
                    String fame = getElementFromCharacterTable(metadata, headers, "Fame");
                    String exp = getElementFromCharacterTable(metadata, headers, "Exp");
                    String place = getElementFromCharacterTable(metadata, headers, "Pl.");
                    String stats = getElementFromCharacterTable(metadata, headers, "Stats");
                    String skin = characterSkinData.get(i-1);
                    List<String> items = itemData.subList(4 * i - 4, 4 * i);
                    List<String> itemsImage = itemImageData.subList(4 * i - 4, 4 * i);
                    String characterLastSeen = getElementFromCharacterTable(metadata, headers, "Last seen");
                    String server = getElementFromCharacterTable(metadata, headers, "Srv.");
                    List<Item> inventory = new ArrayList<>();
                    for (int j = 0; j < items.size(); j++) {
                        String itemType = switch (j) {
                            case 0 -> "weapon";
                            case 1 -> "ability";
                            case 2 -> "armor";
                            case 3 -> "ring";
                            default -> "";
                        };
                        String[] positions = itemsImage.get(j).replace("background-position:", "").replace("px", "").split(" ");
                        int x = Math.abs(Integer.parseInt(positions[0]));
                        int y = Math.abs(Integer.parseInt(positions[1]));
                        System.out.println(x + " " + y);
                        inventory.add(new Item(items.get(j), itemType, type, x, y));
                    }
                    Character character = new Character(type, skin, level, cqc, fame, exp, place, stats, characterLastSeen, server, new Inventory(inventory));
                    characters.add(character);
                }
                return new Account(username, stars, numberOfSkins, exaltations, accountFame, guild, guildRank, creationDate, lastSeen, characters);
            } catch (Exception e) {
                System.out.println(username);
                e.printStackTrace();
                return getPrivateAccount(username);
            }
        }
    }

    /**
     * getPrivateAccount method.
     *
     * Returns an empty Account object that contains:
     * The username of the private profile.
     * One wizard character with all empty slots.
     *
     * @param username username of the raider.
     */
    private static Account getPrivateAccount(String username) {
        List<Character> characters = new ArrayList<>();
        characters.add(new Character());
        return new Account(username, characters);
    }

    /**
     * GETExalts method.
     *
     * Returns a list of character exalts formatted as:
     * [Knight, 18, +25, +10, +0, +2, +2, +5, +1, +1]
     *
     * @param username username of the account to get the exalts from.
     */
    public static List<String[]> GETExalts(String username) throws IOException {
        Document document = null;
        List<String[]> collection = new ArrayList<>();
        if (GUI.getMode() == GUI.LAN_MODE) {
            File html = new File(GUI.TEST_RESOURCE_PATH + "exalts/" + username + "/data.html");
            document = Jsoup.parse(html, "UTF-8");
        } else {
            if (checkDirectConnect()) {
                try {
                    Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9150));
                    document = Jsoup.connect("https://www.realmeye.com/exaltations-of/" + username).proxy(proxy).get();
                } catch (Exception e) {
                    document = Jsoup.connect("https://www.realmeye.com/exaltations-of/" + username).get();
                }
                if (GUI.getMode() == GUI.DEBUG_MODE) {
                    File file = new File("src/test/resources/exalts" + "/" + username + "/data.html");
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        FileWriter playerHTML = new FileWriter("src/test/resources/exalts" + "/" + username + "/data.html");
                        playerHTML.write(Jsoup.parse(document.html()).toString());
                        playerHTML.close();
                    }
                }
            }
        }
        /* Handles exaltations */
        if (document == null || document.select("h2").text().equals("Sorry, but we either:")) {
            return null;
        }
        Elements exaltationTable = document.select("#f").select("tr");
        // table label is either #f or #e
        if (!exaltationTable.hasText()) {
            exaltationTable = document.select("#e").select("tr");
        }
        for (int i = 1; i < exaltationTable.size(); i++) {
            Elements data = exaltationTable.get(i).select("td");
            String[] Exalts = new String[10];

            for (int j = 1; j < data.size(); j++) {
                if (!data.get(j).hasText()) {
                    Exalts[j - 1] = "+0";
                } else {
                    Exalts[j - 1] = data.get(j).text();
                }
            }
            collection.add(Exalts);
        }
        return collection;
    }

    /**
     *
     * @param doc
     * @param request
     * @return
     */
    private static String getElementFromSummary(Document doc, String request) {
        List<String> summary = Jsoup.parse(doc.html()).select("table[class=summary] tr").eachText();
        for (String s : summary) {
            if (s.contains(request)) {
                if (request.equals("Guild") && s.contains("Guild Rank")) {
                    continue;
                }
                StringBuilder stringBuilder = new StringBuilder();
                /* Remove place for summary data ex. Skins 30 (35919th)*/
                String[] strings = s.split(" \\(")[0].split(" ");
                for (int i = request.split(" ").length; i < strings.length; i++) {
                    if (i == strings.length - 1 || (StringUtil.isNumeric(strings[i]) && (i+1) < strings.length && StringUtil.isNumeric(strings[i+1]))) {
                        stringBuilder.append(strings[i]);
                    } else {
                        stringBuilder.append(strings[i]).append(" ");
                    }
                }
                return stringBuilder.toString();
            }
        }
        return "";
    }

    /**
     *
     * @param characterData
     * @param headers
     * @param request
     * @return
     */
    private static String getElementFromCharacterTable(String[] characterData, String headers, String request) {
        if ((request.equals("Last seen") || request.equals("Srv.")) && !headers.contains(request)) {
            return "";
        } else if ((request.equals("Last seen") || request.equals("Srv.")) && characterData.length < 8) {
            return "";
        } else if (request.equals("Srv.") && characterData.length <= 9) {
            return "";
        } else {
            try {
                switch (request) {
                    case "Class":
                        return characterData[0];
                    case "L":
                        return characterData[1];
                    case "CQC":
                        return characterData[2];
                    case "Fame":
                        return characterData[3];
                    case "Exp":
                        return characterData[4];
                    case "Pl.":
                        return characterData[5];
                    case "Stats":
                        return characterData[6];
                    case "Last seen":
                        return characterData[7] + " " + characterData[8];
                    case "Srv.":
                        return characterData[9];
                }
            } catch (Exception e){
                return "";
            }
        }
        return "";
    }
}

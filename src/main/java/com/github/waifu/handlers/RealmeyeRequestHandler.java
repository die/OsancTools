package com.github.waifu.handlers;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Character;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Item;
import com.github.waifu.gui.Gui;
import java.awt.Component;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * RealmeyeRequestHandler class to retrieve data from Realmeye.
 */
public final class RealmeyeRequestHandler {

  /**
   * To be documented.
   */
  private RealmeyeRequestHandler() {

  }

  /**
   * To be documented.
   *
   * @param username To be documented.
   * @return To be documented.
   * @throws IOException To be documented.
   * @throws InterruptedException To be documented.
   */
  public static Document getRealmeyeData(final String username) throws IOException, InterruptedException {
    Document doc;
    try {
      final Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9150));
      doc = Jsoup.connect("https://www.realmeye.com/player/" + username).proxy(proxy).get();
      TimeUnit.SECONDS.sleep(1);
    } catch (final Exception e) {
      doc = Jsoup.connect("https://www.realmeye.com/player/" + username).get();
      TimeUnit.SECONDS.sleep(1);
    }
    return doc;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public static JLabel checkRealmeyeStatus() {
    Document document = null;
    try {
      final Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9150));
      document = Jsoup.connect("https://www.realmeye.com").proxy(proxy).get();
    } catch (final IOException e) {
      try {
        document = Jsoup.connect("https://www.realmeye.com").get();
      } catch (final IOException ex) {
        ex.printStackTrace();
      }
    }
    if (document != null) {
      final String query = "div[class=help-block alert alert-warning col-md-8 col-md-offset-2]";
      if (document.select(query).hasText()) {
        final JLabel label;
        if (document.select(query).select("strong").hasText()) {
          final String serverList = document.select(query).select("strong").text();
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
   * To be documented.
   *
   * @return To be documented.
   */
  public static boolean checkDirectConnect() {
    try {
      final Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9150));
      Jsoup.connect("https://en.wikipedia.org/").proxy(proxy).get();
      return true;
    } catch (final Exception e) {
      final Component rootPane = Gui.getFrames()[0].getComponents()[0];
      final int confirm = JOptionPane.showConfirmDialog(rootPane,
              "Warning: you have chosen to use Direct Connect. Are you sure?",
              "Confirmation",
              JOptionPane.YES_NO_OPTION);
      return confirm == 0;
    }
  }

  /**
   * GET method.
   *
   * <p>Returns an Account object of the given username.
   * If the account is private or has no characters,
   * a default Account object is returned.
   *
   * @param username username of the raider.
   * @param doc To be documented.
   * @return To be documented.
   */
  public static Account parseHTML(final Document doc, final String username) {
    if (doc.select("h2").text().equals("Sorry, but we either:")) {
      return getPrivateAccount(username);
    } else if (doc.select("h3").text().equals("Characters are hidden")) {
      return getPrivateAccount(username);
    } else if (doc.select("table").eachText().get(0).split(" ")[1].equals("0")) {
      return getPrivateAccount(username);
    } else {
      try {
        final String numberOfSkins = getElementFromSummary(doc, "Skins");
        final String exaltations = getElementFromSummary(doc, "Exaltations");
        final String stars = Jsoup.parse(doc.html()).select("div[class=star-container]").text();
        final String accountFame = getElementFromSummary(doc, "Account fame");
        final String guild = getElementFromSummary(doc, "Guild");
        final String guildRank = getElementFromSummary(doc, "Guild Rank");
        String creationDate = getElementFromSummary(doc, "First seen");
        final String lastSeen = getElementFromSummary(doc, "Last seen");
        if (creationDate.isEmpty()) {
          creationDate = getElementFromSummary(doc, "Created");
        }
        final List<String> characterData = Jsoup.parse(doc.html()).select("table[class=table table-striped tablesorter] tr").eachText();
        final List<String> characterSkinData = Jsoup.parse(doc.html()).select("a.character").eachAttr("data-skin");
        final List<String> itemData = Jsoup.parse(doc.html()).select("span.item").eachAttr("title");
        final List<String> itemImageData = Jsoup.parse(doc.html()).select("span.item").eachAttr("style");
        final List<Character> characters = new ArrayList<>();
        final String headers = characterData.get(0);
        for (int i = 1; i < characterData.size(); i++) {
          final String[] metadata = characterData.get(i).split(" ");
          final String type = getElementFromCharacterTable(metadata, headers, "Class");
          final String level = getElementFromCharacterTable(metadata, headers, "L");
          final String cqc = getElementFromCharacterTable(metadata, headers, "CQC");
          final String fame = getElementFromCharacterTable(metadata, headers, "Fame");
          final String exp = getElementFromCharacterTable(metadata, headers, "Exp");
          final String place = getElementFromCharacterTable(metadata, headers, "Pl.");
          final String stats = getElementFromCharacterTable(metadata, headers, "Stats");
          final String skin = characterSkinData.get(i - 1);
          final List<String> items = itemData.subList(4 * i - 4, 4 * i);
          //List<String> itemsImage = itemImageData.subList(4 * i - 4, 4 * i);
          final String characterLastSeen = getElementFromCharacterTable(metadata, headers, "Last seen");
          final String server = getElementFromCharacterTable(metadata, headers, "Srv.");
          final List<Item> inventory = new ArrayList<>();
          for (int j = 0; j < items.size(); j++) {
            final String itemType = switch (j) {
              case 0 -> "weapon";
              case 1 -> "ability";
              case 2 -> "armor";
              case 3 -> "ring";
              default -> "";
            };
            inventory.add(new Item(0, items.get(j), itemType, type));
          }
          final Character character = new Character(type, new Inventory(inventory));
          characters.add(character);
        }
        return new Account(username, stars, numberOfSkins, exaltations, accountFame, guild, guildRank, creationDate, lastSeen, characters);
      } catch (final Exception e) {
        e.printStackTrace();
        return getPrivateAccount(username);
      }
    }

  }

  /**
   * getPrivateAccount method.
   *
   * <p>Returns an empty Account object that contains:
   * The username of the private profile.
   * One wizard character with all empty slots.
   *
   * @param username username of the raider.
   * @return To be documented.
   */
  private static Account getPrivateAccount(final String username) {
    final List<Character> characters = new ArrayList<>();
    characters.add(new Character());
    return new Account(username, characters);
  }

  /**
   * GETExalts method.
   *
   * <p>Returns a list of character exalts formatted as:
   * [Knight, 18, +25, +10, +0, +2, +2, +5, +1, +1]
   *
   * @param username username of the account to get the exalts from.
   * @return To be documented.
   */
  public static List<String[]> GETExalts(final String username) throws IOException {
    Document document = null;
    final List<String[]> collection = new ArrayList<>();
    if (checkDirectConnect()) {
      try {
        final Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9150));
        document = Jsoup.connect("https://www.realmeye.com/exaltations-of/" + username).proxy(proxy).get();
      } catch (final Exception e) {
        document = Jsoup.connect("https://www.realmeye.com/exaltations-of/" + username).get();
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
      final Elements data = exaltationTable.get(i).select("td");
      final String[] Exalts = new String[10];

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
   * To be documented.
   *
   * @param doc To be documented.
   * @param request To be documented.
   * @return To be documented.
   */
  private static String getElementFromSummary(final Document doc, final String request) {
    final List<String> summary = Jsoup.parse(doc.html()).select("table[class=summary] tr").eachText();
    for (final String s : summary) {
      if (s.contains(request)) {
        if (request.equals("Guild") && s.contains("Guild Rank")) {
          continue;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        /* Remove place for summary data ex. Skins 30 (35919th)*/
        final String[] strings = s.split(" \\(")[0].split(" ");
        for (int i = request.split(" ").length; i < strings.length; i++) {
          if (i == strings.length - 1 || (StringUtil.isNumeric(strings[i]) && (i + 1) < strings.length && StringUtil.isNumeric(strings[i + 1]))) {
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
   * To be documented.
   *
   * @param characterData To be documented.
   * @param headers To be documented.
   * @param request To be documented.
   * @return To be documented.
   */
  private static String getElementFromCharacterTable(final String[] characterData, final String headers, final String request) {
    if ((request.equals("Last seen") || request.equals("Srv.")) && !headers.contains(request)) {
      return "";
    } else if ((request.equals("Last seen") || request.equals("Srv.")) && characterData.length < 8) {
      return "";
    } else if (request.equals("Srv.") && characterData.length <= 9) {
      return "";
    } else {
      try {
        return switch (request) {
          case "Class" -> characterData[0];
          case "L" -> characterData[1];
          case "CQC" -> characterData[2];
          case "Fame" -> characterData[3];
          case "Exp" -> characterData[4];
          case "Pl." -> characterData[5];
          case "Stats" -> characterData[6];
          case "Last seen" -> characterData[7] + " " + characterData[8];
          case "Srv." -> characterData[9];
          default -> "";
        };
      } catch (final Exception e) {
        return "";
      }
    }
  }
}

package com.github.waifu.handlers;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Character;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Item;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.Main;
import com.github.waifu.packets.Packet;
import com.github.waifu.packets.data.StatData;
import com.github.waifu.packets.data.enums.UnknownItem;
import com.github.waifu.packets.incoming.UpdatePacket;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * To be documented.
 */
public final class PacketHandler {

  /**
   * To be documented.
   */
  private PacketHandler() {

  }

  /**
   * To be documented.
   */
  private static Document equipmentData;
  /**
   * To be documented.
   */
  private static Document classData;

  /**
   * To be documented.
   *
   * @param packet To be documented.
   */
  public static void handlePacket(final Packet packet) {
    final UpdatePacket updatePacket = (UpdatePacket) packet;
    boolean maxedMp = false;
    boolean maxedHp = false;
    int maxMpValue = 0;
    int maxHpValue = 0;
    int currentHpValue = 0;
    int currentMpValue = 0;
    int stars = 0;
    int level = 0;
    String charClass = "";
    String weapon = "";
    String ability = "";
    String armor = "";
    String ring = "";
    String userName = "";
    String guildName = "";
    String guildRank = "";
    int fame = 0;
    int currentFame = 0;
    int exaltedHp = 0;
    int exaltedMp = 0;
    int dexterity = 0;

    for (int i = 0; i < updatePacket.getNewObjects().length; i++) {
      final StatData[] stats = updatePacket.getNewObjects()[i].getStatus().getStatData();

      for (final StatData stat : stats) {
        switch (stat.getStatType()) {
          case MAX_MP_STAT -> maxMpValue = stat.getStatValue();
          case MP_STAT -> currentMpValue = stat.getStatValue();
          case NUM_STARS_STAT -> stars = stat.getStatValue();
          case MAX_HP_STAT -> maxHpValue = stat.getStatValue();
          case HP_STAT -> currentHpValue = stat.getStatValue();
          case PLAYER_ID -> charClass = getClassName(String.valueOf(updatePacket.getNewObjects()[i].getObjectType()));
          case INVENTORY_0_STAT -> weapon = getItemName(String.valueOf(stat.getStatValue()));
          case INVENTORY_1_STAT -> ability = getItemName(String.valueOf(stat.getStatValue()));
          case INVENTORY_2_STAT -> armor = getItemName(String.valueOf(stat.getStatValue()));
          case INVENTORY_3_STAT -> ring = getItemName(String.valueOf(stat.getStatValue()));
          case NAME_STAT -> userName = stat.getStringStatValue();
          case GUILD_NAME_STAT -> guildName = stat.getStringStatValue();
          case GUILD_RANK_STAT -> guildRank = stat.getStringStatValue();
          case FAME_STAT -> fame = stat.getStatValue();
          case CURR_FAME_STAT -> currentFame = stat.getStatValue();
          case EXALTED_HP -> exaltedHp = stat.getStatValue();
          case EXALTED_MP -> exaltedMp = stat.getStatValue();
          case DEXTERITY_STAT -> dexterity = stat.getStatValue();
          case LEVEL_STAT -> level = stat.getStatValue();
          default -> {

          }
        }
      }
    }
    if (userName.equals("") || weapon.equals("")) {
      return;
    }
    if (maxHpValue != 0 || maxHpValue == currentHpValue) {
      maxedHp = true;
    }
    if (maxMpValue != 0 || maxMpValue == currentMpValue) {
      maxedMp = true;
    }
    final Item weaponItem = new Item(weapon, "weapon", charClass);
    final Item abilityItem = new Item(ability, "ability", charClass);
    final Item armorItem = new Item(armor, "armor", charClass);
    final Item ringItem = new Item(ring, "ring", charClass);
    final List<Item> items = new ArrayList<>();
    Collections.addAll(items, weaponItem, abilityItem, armorItem, ringItem);
    final Account account = createAccount(userName, stars, fame, guildName, guildRank, items, level, maxedMp, maxedHp, currentFame, exaltedHp, exaltedMp, dexterity);
    Gui.getRaid().addSnifferAccount(account);
  }

  /**
   * To be documented.
   *
   * @param id To be documented.
   * @return To be documented.
   */
  public static String getClassName(final String id) {
    final NodeList nodeList = classData.getElementsByTagName("Object");

    for (int i = 0; i < nodeList.getLength(); i++) {
      final Node nNode = nodeList.item(i);

      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        final Element eElement = (Element) nNode;
        if (eElement.getElementsByTagName("Class").item(0) != null) {
          final long foundId = Long.parseLong(eElement.getAttribute("type").replace("0x", ""), 16);
          if (String.valueOf(foundId).equals(id)) {
            return eElement.getAttribute("id");
          }
        }
      }
    }
    return "Wizard";
  }

  /**
   * To be documented.
   *
   * @param id To be documented.
   * @return To be documented.
   */
  public static String getItemName(final String id) {
    if (id.equals("-1")) {
      return "Empty slot";
    } else {
      final NodeList nodeList = equipmentData.getElementsByTagName("Object");

      for (int i = 0; i < nodeList.getLength(); i++) {
        final Node nNode = nodeList.item(i);

        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          String label = "";
          final Element eElement = (Element) nNode;

          final long found_id = Long.parseLong(eElement.getAttribute("type").replace("0x", ""), 16);

          if (String.valueOf(found_id).equals(id)) {
            String name = "";

            if (eElement.getElementsByTagName("DisplayId").item(0) != null) {
              name = eElement.getElementsByTagName("DisplayId").item(0).getTextContent();
            } else {
              name = eElement.getAttribute("id");
            }

            final NodeList tiers = eElement.getElementsByTagName("Tier");
            if (tiers.item(0) != null) {
              label = "T" + tiers.item(0).getTextContent();
            }

            final NodeList labels = eElement.getElementsByTagName("Labels");
            if (labels.item(0) != null) {
              final List<String> labelsList = Arrays.stream(labels.item(0).getTextContent().split(",")).toList();

              if (labelsList.contains("ST")) {
                label = "ST";
              } else if (labelsList.contains("UT")) {
                label = "UT";
              }
            }

            if (!label.equals("")) {
              return name + " " + label;
            }
          }
        }
      }
      return UnknownItem.getItemById(id);
    }
  }

  /**
   * To be documented.
   *
   * @param userName To be documented.
   * @param stars To be documented.
   * @param fame To be documented.
   * @param guildName To be documented.
   * @param guildRank To be documented.
   * @param items To be documented.
   * @param level To be documented.
   * @param maxedMp To be documented.
   * @param maxedHp To be documented.
   * @param currentFame To be documented.
   * @param exaltedHp To be documented.
   * @param exaltedMp To be documented.
   * @param dexterity To be documented.
   * @return To be documented.
   */
  public static Account createAccount(final String userName, final int stars, final int fame, final String guildName, final String guildRank, final List<Item> items, final int level, final boolean maxedMp, final boolean maxedHp, final int currentFame, final int exaltedHp, final int exaltedMp, final int dexterity) {

    final Inventory inventory = new Inventory(items);
    final Character character = new Character(inventory, level, currentFame, maxedHp, maxedMp, dexterity, exaltedHp, exaltedMp);

    final List<Character> characterlist = new ArrayList<>();
    characterlist.add(character);

    return new Account(userName, characterlist, stars, fame, guildName, guildRank);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public static boolean loadXML() {
    final File file = new File(Main.getSettings().getResourceDir());

    if (!file.getName().equals("resources.assets")) {
      JOptionPane.showMessageDialog(Gui.getFrames()[0], "Make sure you select the correct resources file!");
      return false;
    }

    final StringBuilder equipXml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    final StringBuilder classXml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
      String line;
      boolean writeEquip = false;
      boolean writeClass = false;
      boolean checkEquip = true;
      boolean checkClass = true;
      while ((line = br.readLine()) != null) {
        if (writeEquip) {
          if (line.contains("</Objects>")) {
            equipXml.append("</Objects>");
            writeEquip = false;
            checkEquip = false;
          }
          if (writeEquip) {
            equipXml.append(line + "\n");
          }
        } else if (checkEquip) {
          if (line.contains("equip") && line.contains("<?xml version=\"1.0\" encoding=\"utf-8\"?>")) {
            final byte[] bytes = line.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < bytes.length; i++) {
              // check for equip
              if (bytes[i] == 101 && bytes[i + 1] == 113) {
                if (bytes[i + 5] == 0) {
                  writeEquip = true;
                }
              }
            }
          }
        }

        if (writeClass) {
          if (line.contains("</Objects>")) {
            classXml.append("</Objects>");
            writeClass = false;
            checkClass = false;
          }
          if (writeClass) {
            classXml.append(line + "\n");
          }
        } else if (checkClass) {
          if (line.contains("player") && line.contains("<?xml version=\"1.0\" encoding=\"utf-8\"?>")) {
            final byte[] bytes = line.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < bytes.length; i++) {
              // check for player
              if (bytes[i] == 112 && bytes[i + 1] == 108) {
                if (bytes[i + 7] == 0) {
                  writeClass = true;
                }
              }
            }
          }
        }
      }
    } catch (final IOException e) {
      return false;
    }

    try {
      // ignore fatal error: premature end of file
      final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      final DocumentBuilder builder = factory.newDocumentBuilder();
      final InputSource is = new InputSource(new StringReader(equipXml.toString()));
      equipmentData = builder.parse(is);
      final InputSource is1 = new InputSource(new StringReader(classXml.toString()));
      classData = builder.parse(is1);
    } catch (final Exception e) {
      JOptionPane.showMessageDialog(Gui.getFrames()[0], "Select the resources.assets in your Documents folder!");
      return false;
    }
    return true;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public static boolean isXMLNull() {
    return equipmentData == null || classData == null;
  }
}

package com.github.waifu.handlers;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Character;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Item;
import com.github.waifu.gui.GUI;
import com.github.waifu.gui.Main;
import com.github.waifu.packets.Packet;
import com.github.waifu.packets.data.StatData;
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


public class PacketHandler {

  private static Document equipmentData;
  private static Document classData;

  public static void handlePacket(Packet packet) {
    UpdatePacket updatePacket = (UpdatePacket) packet;

    boolean maxedMP = false;
    boolean maxedHP = false;
    int maxMPValue = 0;
    int maxHPValue = 0;
    int currentHPValue = 0;
    int currentMPValue = 0;
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
    int exaltedHP = 0;
    int exaltedMP = 0;
    int dexterity = 0;

    for (int i = 0; i < updatePacket.newObjects.length; i++) {
      StatData[] stats = updatePacket.newObjects[i].getStatus().getStatData();

      for (StatData stat : stats) {
        switch (stat.getStatType()) {
          case MAX_MP_STAT -> maxMPValue = stat.getStatValue();
          case MP_STAT -> currentMPValue = stat.getStatValue();
          case NUM_STARS_STAT -> stars = stat.getStatValue();
          case MAX_HP_STAT -> maxHPValue = stat.getStatValue();
          case HP_STAT -> currentHPValue = stat.getStatValue();
          case LEVEL_STAT -> level = stat.getStatValue();
          case PLAYER_ID -> charClass = getClassName(String.valueOf(updatePacket.newObjects[i].getObjectType()));
          case INVENTORY_0_STAT -> weapon = getItemName(String.valueOf(stat.getStatValue()));
          case INVENTORY_1_STAT -> ability = getItemName(String.valueOf(stat.getStatValue()));
          case INVENTORY_2_STAT -> armor = getItemName(String.valueOf(stat.getStatValue()));
          case INVENTORY_3_STAT -> ring = getItemName(String.valueOf(stat.getStatValue()));
          case NAME_STAT -> userName = stat.getStringStatValue();
          case GUILD_NAME_STAT -> guildName = stat.getStringStatValue();
          case GUILD_RANK_STAT -> guildRank = stat.getStringStatValue();
          case FAME_STAT -> fame = stat.getStatValue();
          case CURR_FAME_STAT -> currentFame = stat.getStatValue();
          case EXALTED_HP -> exaltedHP = stat.getStatValue();
          case EXALTED_MP -> exaltedMP = stat.getStatValue();
          case DEXTERITY_STAT -> dexterity = stat.getStatValue();
        }
      }
    }
    if (userName.equals("") || weapon.equals("")) return;
    if (maxHPValue != 0 || maxHPValue == currentHPValue) {
      maxedHP = true;
    }
    if (maxMPValue != 0 || maxMPValue == currentMPValue) {
      maxedMP = true;
    }
    Item weaponItem = new Item(weapon, "weapon", charClass);
    Item abilityItem = new Item(ability, "ability", charClass);
    Item armorItem = new Item(armor, "armor", charClass);
    Item ringItem = new Item(ring, "ring", charClass);
    List<Item> items = new ArrayList<>();
    Collections.addAll(items, weaponItem, abilityItem, armorItem, ringItem);
    Account account = createAccount(userName, stars, fame, guildName, guildRank, items, level, maxedMP, maxedHP, currentFame, exaltedHP, exaltedMP, dexterity);
    System.out.println(account.getName() + " : " + account.getRecentCharacter().getInventory().printInventory());
    GUI.raid.addSnifferAccount(account);
  }

  public static String getClassName(String id) {
    NodeList nodeList = classData.getElementsByTagName("Object");

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node nNode = nodeList.item(i);

      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;
        if (eElement.getElementsByTagName("Class").item(0) != null) {
          long foundId = Long.parseLong(eElement.getAttribute("type").replace("0x", ""), 16);
          if (String.valueOf(foundId).equals(id)) {
            return eElement.getAttribute("id");
          }
        }
      }
    }
    return "Wizard";
  }

  public static String getItemName(String id) {
    if (id.equals("-1")) {
      return "Empty slot";
    } else {
      NodeList nodeList = equipmentData.getElementsByTagName("Object");

      for (int i = 0; i < nodeList.getLength(); i++) {
        Node nNode = nodeList.item(i);

        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          String label = "";
          Element eElement = (Element) nNode;

          long found_id = Long.parseLong(eElement.getAttribute("type").replace("0x", ""), 16);

          if (String.valueOf(found_id).equals(id)) {
            String name = "";

            if (eElement.getElementsByTagName("DisplayId").item(0) != null) {
              name = eElement.getElementsByTagName("DisplayId").item(0).getTextContent();
            } else {
              name = eElement.getAttribute("id");
            }

            NodeList tiers = eElement.getElementsByTagName("Tier");
            if (tiers.item(0) != null) {
              label = "T" + tiers.item(0).getTextContent();
            }

            NodeList labels = eElement.getElementsByTagName("Labels");
            if (labels.item(0) != null) {
              List<String> labelsList = Arrays.stream(labels.item(0).getTextContent().split(",")).toList();

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
      return "Failed To Parse Item UT";
    }
  }

  /**
   * @param userName
   * @param stars
   * @param fame
   * @param guildName
   * @param guildRank
   * @param items
   * @param level
   * @param maxedMP
   * @param maxedHP
   * @param currentFame
   * @param exaltedHP
   * @param exaltedMP
   * @param dexterity
   * @return
   */
  public static Account createAccount(String userName, int stars, int fame, String guildName, String guildRank, List<Item> items, int level, boolean maxedMP, boolean maxedHP, int currentFame, int exaltedHP, int exaltedMP, int dexterity) {

    Inventory inventory = new Inventory(items);
    Character character = new Character(inventory, level, currentFame, maxedHP, maxedMP, dexterity, exaltedHP, exaltedMP);

    List<Character> characterlist = new ArrayList<>();
    characterlist.add(character);

    return new Account(userName, characterlist, stars, fame, guildName, guildRank);
  }

  /**
   *
   */
  public static boolean loadXML() {
    File file = new File(Main.settings.getResourceDir());

    if (!file.getName().equals("resources.assets")) {
      JOptionPane.showMessageDialog(GUI.getFrames()[0], "Make sure you select the correct resources file!");
      return false;
    }

    StringBuilder equipXml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    StringBuilder classXml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
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
            byte[] bytes = line.getBytes(StandardCharsets.UTF_8);
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
            byte[] bytes = line.getBytes(StandardCharsets.UTF_8);
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
    } catch (IOException e) {
      return false;
    }

    try {
      // ignore fatal error: premature end of file
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      InputSource is = new InputSource(new StringReader(equipXml.toString()));
      equipmentData = builder.parse(is);
      InputSource is1 = new InputSource(new StringReader(classXml.toString()));
      classData = builder.parse(is1);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(GUI.getFrames()[0], "Select the resources.assets in your Documents folder!");
      return false;
    }
    return true;
  }

  /**
   * @return
   */
  public static boolean isXMLNull() {
    return equipmentData == null || classData == null;
  }
}

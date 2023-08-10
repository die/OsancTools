package com.github.waifu.handlers;

import com.github.waifu.assets.RotmgAssets;
import com.github.waifu.assets.objects.EquipXMLObject;
import com.github.waifu.assets.objects.PlayerXmlObject;
import com.github.waifu.assets.objects.PortalXmlObject;
import com.github.waifu.entities.Account;
import com.github.waifu.entities.Character;
import com.github.waifu.entities.CharacterStats;
import com.github.waifu.entities.ClassData;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Item;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.Main;
import com.github.waifu.gui.panels.PopsPanel;
import com.github.waifu.packets.Packet;
import com.github.waifu.packets.data.StatData;
import com.github.waifu.packets.data.enums.UnknownItem;
import com.github.waifu.packets.incoming.NotificationPacket;
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
   * Get equipment document.
   *
   * @return Document object.
   */
  public static Document getEquipmentData() {
    return equipmentData;
  }

  /**
   * Get class document.
   *
   * @return Document object.
   */
  public static Document getClassData() {
    return classData;
  }

  /**
   * To be documented.
   *
   * @param packet To be documented.
   */
  public static void logKeyPops(final Packet packet) {
    if (packet instanceof final NotificationPacket notificationPacket) {
      if (notificationPacket.getEffect() == null) return;

      switch (notificationPacket.getEffect()) {
        case DungeonOpened -> {
          final PortalXmlObject portalXMLObject = notificationPacket.getDungeon();
          if (portalXMLObject == null) return;
          PopsPanel.addPop(portalXMLObject, notificationPacket.getKeyPopper());
        }
        case ServerMessage -> {
          final String name = notificationPacket.getName();
          int key = -1;
          if (name.contains("The Void")) key = 583;
          if (name.contains("Wine Cellar")) key = 1826;
          if (name.contains("The Sword Monument")) key = 10022;
          if (name.contains("The Shield Monument")) key = 10023;
          if (name.contains("The Helmet Monument")) key = 10024;
          final EquipXMLObject equipXMLObject = RotmgAssets.equipXMLObjectList.get(key);
          if (equipXMLObject == null) return;
          PopsPanel.addPop(equipXMLObject, notificationPacket.getKeyPopper());
        }
      }
    }
  }

  /**
   * To be documented.
   *
   * @param packet To be documented.
   */
  public static void handlePacket(final Packet packet) {

    final UpdatePacket updatePacket = (UpdatePacket) packet;
    int stars = 0;
    int level = 0;
    String charClass = "";
    int weaponId = -2;
    int abilityId = -2;
    int armorId = -2;
    int ringId = -2;
    String userName = "";
    String guildName = "";
    String guildRank = "";
    int fame = 0;
    int currentFame = 0;
    int boostHp = 0;
    int boostMp = 0;
    int boostAtt = 0;
    int boostDef = 0;
    int boostSpd = 0;
    int boostDex = 0;
    int boostVit = 0;
    int boostWis = 0;
    int hp = 0;
    int mp = 0;
    int att = 0;
    int def = 0;
    int spd = 0;
    int dex = 0;
    int vit = 0;
    int wis = 0;
    int skin = 0;

    for (int i = 0; i < updatePacket.getNewObjects().length; i++) {
      final StatData[] stats = updatePacket.getNewObjects()[i].getStatus().getStatData();
      for (final StatData stat : stats) {
        switch (stat.getStatType()) {
          case NUM_STARS_STAT -> stars = stat.getStatValue();
          case PLAYER_ID -> {
            charClass = String.valueOf(updatePacket.getNewObjects()[i].getObjectType());
          }
          case INVENTORY_0_STAT -> weaponId = stat.getStatValue();
          case INVENTORY_1_STAT -> abilityId = stat.getStatValue();
          case INVENTORY_2_STAT -> armorId = stat.getStatValue();
          case INVENTORY_3_STAT -> ringId = stat.getStatValue();
          case NAME_STAT -> userName = stat.getStringStatValue();
          case GUILD_NAME_STAT -> guildName = stat.getStringStatValue();
          case GUILD_RANK_STAT -> guildRank = stat.getStringStatValue();
          case FAME_STAT -> fame = stat.getStatValue();
          case CURR_FAME_STAT -> currentFame = stat.getStatValue();
          case LEVEL_STAT -> level = stat.getStatValue();
          case MAX_HP_BOOST_STAT -> boostHp = stat.getStatValue();
          case MAX_MP_BOOST_STAT -> boostMp = stat.getStatValue();
          case ATTACK_BOOST_STAT -> boostAtt = stat.getStatValue();
          case DEFENSE_BOOST_STAT -> boostDef = stat.getStatValue();
          case SPEED_BOOST_STAT -> boostSpd = stat.getStatValue();
          case DEXTERITY_BOOST_STAT -> boostDex = stat.getStatValue();
          case VITALITY_BOOST_STAT -> boostVit = stat.getStatValue();
          case WISDOM_BOOST_STAT -> boostWis = stat.getStatValue();
          case MAX_HP_STAT -> hp = stat.getStatValue();
          case MAX_MP_STAT -> mp = stat.getStatValue();
          case ATTACK_STAT -> att = stat.getStatValue();
          case DEFENSE_STAT -> def = stat.getStatValue();
          case SPEED_STAT -> spd = stat.getStatValue();
          case DEXTERITY_STAT -> dex = stat.getStatValue();
          case VITALITY_STAT -> vit = stat.getStatValue();
          case WISDOM_STAT -> wis = stat.getStatValue();
          case SKIN_STAT -> skin = stat.getStatValue();

        }
      }
    }
    if (userName.equals("") || weaponId == -2) {
      return;
    }

    final Account account = new Account(userName);
    final Item weaponItem = new Item(weaponId, getItemName(weaponId), "weapon", getClassName(Integer.parseInt(charClass)));
    final Item abilityItem = new Item(abilityId, getItemName(abilityId), "ability", getClassName(Integer.parseInt(charClass)));
    final Item armorItem = new Item(armorId, getItemName(armorId), "armor", getClassName(Integer.parseInt(charClass)));
    final Item ringItem = new Item(ringId, getItemName(ringId), "ring", getClassName(Integer.parseInt(charClass)));
    final List<Item> items = new ArrayList<>();
    Collections.addAll(items, weaponItem, abilityItem, armorItem, ringItem);
    final Inventory inventory = new Inventory(items);
    final List<Integer> stats = new ArrayList<>();
    Collections.addAll(stats, hp, mp, att, def, spd, dex, vit, wis);
    final List<Integer> statBoosts = new ArrayList<>();
    Collections.addAll(statBoosts, boostHp, boostMp, boostAtt, boostDef, boostSpd, boostDex, boostVit, boostWis);
    final CharacterStats characterStats = new CharacterStats(stats, statBoosts);
    account.getCharacters().add(new Character(getClassName(Integer.parseInt(charClass)), skin, inventory, characterStats));
    Gui.getRaid().getGroup().addAccount(account);
  }

  /**
   * To be documented.
   *
   * @param id To be documented.
   * @return To be documented.
   */
  public static String getClassName(final int id) {
    for (final PlayerXmlObject playerXMLObject : RotmgAssets.playerXmlObjectList) {
      if (Integer.parseInt(playerXMLObject.getType().replace("0x", ""), 16) == id) {
        return playerXMLObject.getId();
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
  public static String getItemName(final int id) {
    if (id == -1) {
      return "Empty slot";
    } else {
      final EquipXMLObject equipXMLObject = RotmgAssets.equipXMLObjectList.get(id);

      if (equipXMLObject == null) return UnknownItem.getItemById(id);

      String label = "";
      if (equipXMLObject.getLabels() == null) return equipXMLObject + " UT";

      final List<String> labels = Arrays.asList(equipXMLObject.getLabels());
      if (labels.contains("ST") || labels.contains("TAB_ST")) {
        label = "ST";
        return equipXMLObject.getDisplayId() + " " + label;
      } else if (labels.contains("UT") || labels.contains("TAB_UT")) {
        label = "UT";
      } else {
        for (final String s : equipXMLObject.getLabels()) {
          if (s.charAt(0) == 'T' && s.matches(".*\\d.*")) {
            label = s;
          }
        }
      }
      return equipXMLObject + " " + label;
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
   * @param currentFame To be documented.
   * @param characterClass To be documented.
   * @param characterStats stats.
   * @return To be documented.
   */
  public static Account createAccount(final String userName, final int stars, final int fame, final String guildName, final String guildRank, final List<Item> items, final int level, final int currentFame, final ClassData characterClass, final CharacterStats characterStats) {

    final Inventory inventory = new Inventory(items);
    final Character character = new Character(inventory, level, currentFame, characterClass, characterStats);

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
    try (final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
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
      createClassDataObjects();
    } catch (final Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(Gui.getFrames()[0], "Select the resources.assets in your Documents folder!");
      return false;
    }
    return true;
  }

  private static void createClassDataObjects() throws Exception {
    final NodeList nodeList = classData.getElementsByTagName("Object");
    for (int i = 0; i < nodeList.getLength(); i++) {
      final Node currentNode = nodeList.item(i);
      if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
        //calls this method for all the children which is Element
        final int id = Integer.decode(currentNode.getAttributes().getNamedItem("type").getTextContent());
        final String name = currentNode.getAttributes().getNamedItem("id").getTextContent();
        int hp = 0;
        int mp = 0;
        int att = 0;
        int def = 0;
        int spd = 0;
        int dex = 0;
        int vit = 0;
        int wis = 0;
        int index = 0;
        for (int j = 0; j < currentNode.getChildNodes().getLength(); j++) {
          if (currentNode.getChildNodes().item(j).hasAttributes() && !currentNode.getChildNodes().item(j).getNodeName().equals("LevelIncrease")) {
            if (currentNode.getChildNodes().item(j).getAttributes().getNamedItem("max") != null) {
              final int maxStat = Integer.parseInt(currentNode.getChildNodes().item(j).getAttributes().getNamedItem("max").getTextContent());

              switch (index) {
                case 0 -> hp = maxStat;
                case 1 -> mp = maxStat;
                case 2 -> att = maxStat;
                case 3 -> def = maxStat;
                case 4 -> spd = maxStat;
                case 5 -> dex = maxStat;
                case 6 -> vit = maxStat;
                case 7 -> wis = maxStat;
                default -> throw new Exception();
              }
              index++;
            }
          }
        }
        ClassDataHandler.getClassDataList().add(new ClassData(id, name, hp, mp, att, def, spd, dex, vit, wis));
      }
    }
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

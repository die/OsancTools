package com.github.waifu.entities;

import com.github.waifu.assets.RotmgAssets;
import com.github.waifu.assets.objects.PlayerXmlObject;
import com.github.waifu.assets.objects.SkinXmlObject;
import com.github.waifu.enums.Stat;
import com.github.waifu.handlers.ClassDataHandler;
import com.github.waifu.handlers.RequirementSheetHandler;
import com.github.waifu.util.Utilities;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Character class to store character data.
 */
public class Character {

  /**
   * Class enum.
   */
  private final ClassData characterClass;
  /**
   * Class of the character.
   */
  private final String type;
  /**
   * Skin ID the character has.
   */
  private final int skin;
  /**
   * Level of the character.
   */
  private final String level;
  /**
   * Class Quests Completed of the character (Deprecated).
   */
  private final String cqc;
  /**
   * Base fame of the character.
   */
  private final String fame;
  /**
   * Amount of Exp the Character has.
   */
  private final String exp;
  /**
   * The place in fame the character has.
   */
  private final String place;
  /**
   * The stats of the Character (Currently just ?/8).
   */
  private final String stats;
  /**
   * Time the character was last seen.
   */
  private final String lastSeen;
  /**
   * Last seen server initials.
   */
  private final String server;
  /**
   * Inventory of the Character.
   */
  private final Inventory inventory;
  /**
   * The image of the Character's skin.
   */
  private ImageIcon skinImage;
  /**
   * Character stats.
   */
  private CharacterStats characterStats;

  public ImageIcon[] characterStatImages = new ImageIcon[8];

  /**
   * Character method.
   *
   * <p>Constructs a default Character as a level 0 Wizard.
   */
  public Character() {
    this.characterClass = new ClassData();
    this.type = "Wizard";
    this.skin = -1;
    this.skinImage = new ImageIcon(Utilities.getClassResource("images/skins/Wizard.png"));
    this.level = "";
    this.cqc = "";
    this.fame = "";
    this.exp = "";
    this.place = "";
    this.stats = "";
    this.lastSeen = "";
    this.server = "";
    this.inventory = new Inventory();
  }

  /**
   * Character method.
   *
   * <p>Constructs a Character with all information.
   *
   * @param type      class of the Character.
   * @param inventory Inventory of the Character.
   */
  public Character(final String type, final Inventory inventory) {
    this.characterClass = ClassDataHandler.findClassByName(type);
    this.type = type;
    this.skin = -1;
    this.skinImage = new ImageIcon(Utilities.getClassResource("images/skins/" + type + ".png"));
    this.level = "";
    this.cqc = "";
    this.fame = "";
    this.exp = "";
    this.place = "";
    this.stats = "";
    this.lastSeen = "";
    this.server = "";
    this.inventory = inventory;
  }

  /**
   * Character method.
   *
   * <p>Constructs a Character with all information.
   *
   * @param type      class of the Character.
   * @param inventory Inventory of the Character.
   */
  public Character(final String type, final int skin, final Inventory inventory, final CharacterStats characterStats) {
    this.characterClass = ClassDataHandler.findClassByName(type);
    this.type = type;
    this.skin = skin;
    this.skinImage = new ImageIcon(Utilities.getClassResource("images/skins/" + type + ".png"));
    this.level = "";
    this.cqc = "";
    this.fame = "";
    this.exp = "";
    this.place = "";
    this.stats = "";
    this.lastSeen = "";
    this.server = "";
    this.inventory = inventory;
    this.characterStats = characterStats;
  }

  /**
   * To be documented.
   *
   * @param inventory To be documented.
   * @param level To be documented.
   * @param fame To be documented.
   * @param characterClass To be documented.
   * @param characterStats stats.
   */
  public Character(final Inventory inventory, final int level, final int fame, final ClassData characterClass, final CharacterStats characterStats) {
    this.type = characterClass.getName();
    this.skin = -1;
    this.skinImage = new ImageIcon(Utilities.getClassResource("images/skins/Wizard.png"));
    this.level = String.valueOf(level);
    this.cqc = "";
    this.fame = String.valueOf(fame);
    this.exp = "";
    this.place = "";
    this.stats = "";
    this.lastSeen = "";
    this.server = "";
    this.inventory = inventory;
    this.characterClass = characterClass;
    this.characterStats = characterStats;
  }

  /**
   * Character method.
   *
   * <p>Constructs a Character with all information.
   *
   * @param type      type/class of the Character.
   * @param skin      skin the Character has equipped.
   * @param level     level of the Character.
   * @param cqc       number of class quests completed the Character has.
   * @param fame      number of fame the Character has.
   * @param exp       number of exp the Character has.
   * @param place     rank of the Character among others of its type.
   * @param stats     stats of the Character, currently as ?/8.
   * @param lastSeen last seen
   * @param server server
   * @param inventory Inventory of the Character.
   */
  public Character(final String type, final int skin, final String level, final String cqc, final String fame, final String exp, final String place, final String stats, final String lastSeen, final String server, final Inventory inventory) {
    this.type = type;
    this.skin = skin;
    this.skinImage = new ImageIcon(Utilities.getClassResource("images/skins/" + type + ".png"));
    this.level = level;
    this.cqc = cqc;
    this.fame = fame;
    this.exp = exp;
    this.place = place;
    this.stats = stats;
    this.lastSeen = lastSeen;
    this.server = server;
    this.inventory = inventory;
    this.characterClass = ClassDataHandler.findClassByName(type);
  }

  /**
   * Returns the Character object as a String.
   *
   * @return Character object as a String.
   */
  @Override
  public String toString() {
    return "Type: "
            + this.type
            + "\nSkin: "
            + this.skin
            + "\nLevel: "
            + this.level
            + "\nCQC: "
            + this.cqc
            + "\nFame: "
            + this.fame
            + "\nExp: "
            + this.exp
            + "\nPlace: "
            + this.place
            + "\nStats: "
            + this.stats
            + "\nLast Seen: "
            + this.lastSeen
            + "\nServer: "
            + this.server
            + "\n";
  }

  /**
   * Get character class.
   *
   * @return class enum.
   */
  public ClassData getCharacterClass() {
    return characterClass;
  }

  /**
   * Get character stats.
   *
   * @return character stats.
   */
  public CharacterStats getCharacterStats() {
    return characterStats;
  }

  /**
   * Gets the class of the character.
   *
   * @return class name as a String.
   */
  public String getType() {
    return this.type;
  }

  /**
   * Gets the skin of the character.
   *
   * @return skin id as a String.
   */
  public int getSkin() {
    return this.skin;
  }

  /**
   * Gets the image of the skin.
   *
   * @return skin as an ImageIcon.
   */
  public ImageIcon getSkinImage() {
    return this.skinImage;
  }

  /**
   * Sets the skin image.
   *
   * @param s skin image as ImageIcon.
   */
  public void setSkinImage(final ImageIcon s) {
    this.skinImage = s;
  }

  /**
   * Gets the level.
   *
   * @return level as a String.
   */
  public String getLevel() {
    return this.level;
  }

  /**
   * Gets the CQC.
   *
   * @return CQC as a String.
   */
  public String getCqc() {
    return this.cqc;
  }

  /**
   * Gets the alive fame.
   *
   * @return alive fame as a String.
   */
  public String getFame() {
    return this.fame;
  }

  /**
   * Gets the Exp.
   *
   * @return exp as a String.
   */
  public String getExp() {
    return this.exp;
  }

  /**
   * Gets the character place in alive fame.
   *
   * @return place as a String
   */
  public String getPlace() {
    return this.place;
  }

  /**
   * Gets the stats (?/8).
   *
   * @return stats as a String.
   */
  public String getStats() {
    return this.stats;
  }

  /**
   * Gets the inventory.
   *
   * @return Inventory as an Inventory object.
   */
  public Inventory getInventory() {
    return this.inventory;
  }

  /**
   * Parses character stat.
   *
   */
  public void parseCharacter() {
    for (final Stat stat : Stat.values()) {
      characterStatImages[stat.getIndex()] = stat.getIcon();
    }
    RequirementSheetHandler.parseMaxedStats(this);
    inventory.parseInventory();
  }

  /**
   * Checks if an account is private (no data).
   *
   * @return true if private, else false.
   */
  public boolean isPrivate() {
    return this.inventory.getItems().stream().allMatch(item -> item.getName().equals("Empty slot"));
  }

  public boolean hasDefaultSkin() {
    return skin == 0;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public ImageIcon getCharacterImage() {
    final ImageIcon weapon = new ImageIcon(inventory.getWeapon().getImage().getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    final ImageIcon ability = new ImageIcon(inventory.getAbility().getImage().getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    final ImageIcon armor = new ImageIcon(inventory.getArmor().getImage().getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    final ImageIcon ring = new ImageIcon(inventory.getRing().getImage().getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    java.util.List<Integer> widths = new ArrayList<>();
    widths.add(weapon.getIconWidth());
    widths.add(ability.getIconWidth());
    widths.add(armor.getIconWidth());
    widths.add(ring.getIconWidth());
    int height = weapon.getIconHeight();
    ImageIcon clazz = null;
    ImageIcon skin = null;
    for (final PlayerXmlObject playerXMLObject : RotmgAssets.playerXmlObjectList) {
      if (playerXMLObject.getId().equals(type)) {
        clazz = playerXMLObject.getImage();
        widths.add(clazz.getIconWidth());
      }
    }

    if (!hasDefaultSkin()) {
      for (final SkinXmlObject skinXMLObject : RotmgAssets.skinXmlObjectList) {
        //System.out.println(skinXMLObject.getTypeAsInt());
        if (skinXMLObject.getTypeAsInt() == this.skin) {
          skin = skinXMLObject.getImage();
          widths.add(skin.getIconWidth());
          height = skin.getIconHeight();
        }
      }
    }

    int inventoryWidth = 0;

    int padding = 2;
    for (int w : widths) {
      // padding = 2
      inventoryWidth += w + padding;
    }
    BufferedImage combined = new BufferedImage(inventoryWidth, height, BufferedImage.TYPE_INT_ARGB);

    int pos = 0;
    if (clazz != null) {
       combined = appendImageIcon(combined, clazz, pos);
       pos += clazz.getIconWidth() + padding;
    }

    if (skin != null) {
      combined = appendImageIcon(combined, skin, pos);
      pos += skin.getIconWidth() + padding;
    }

    combined = appendImageIcon(combined, weapon, pos);
    pos += weapon.getIconWidth() + padding;

    combined = appendImageIcon(combined, ability, pos);
    pos += ability.getIconWidth() + padding;

    combined = appendImageIcon(combined, armor, pos);
    pos += armor.getIconWidth() + padding;

    combined = appendImageIcon(combined, ring, pos);
    pos += ring.getIconWidth() + padding;

    ImageIcon imageIcon = new ImageIcon(combined);
    imageIcon.setDescription(inventory.printInventory());
    return imageIcon;
  }

  public ImageIcon getMaxedStatsImage() {
    Integer[] maxedStats = characterStats.getMaxedStatIndices(type);

    for (int i = 0; i < maxedStats.length; i++) {
      if (characterStatImages[i] == null && maxedStats[i] == 0) {
        final Stat stat = Stat.getStatByIndex(i);
        if (stat == null) continue;
        characterStatImages[i] = stat.getIcon();
      }
    }

    int width = 0;
    int pos = 0;
    for (int i : maxedStats) {
      if (i == 0) {
        width += 16;
      }
    }

    if (width == 0) {
      return null;
    }

    BufferedImage bufferedImage = new BufferedImage(width, 16, BufferedImage.TYPE_INT_ARGB);
    Graphics g = bufferedImage.createGraphics();
    for (int i = 0; i < characterStatImages.length; i++) {
      if (characterStatImages[i] == null) continue;

      g.drawImage(characterStatImages[i].getImage(), pos, 0, null);
      pos += characterStatImages[i].getIconWidth();
    }
    g.dispose();
    return new ImageIcon(bufferedImage);
  }

  private BufferedImage appendImageIcon(final BufferedImage bufferedImage, ImageIcon imageIcon, final int pos) {
    Graphics g = bufferedImage.getGraphics();
    g.drawImage(imageIcon.getImage(), pos, 0, null);
    g.dispose();
    return bufferedImage;
  }
}

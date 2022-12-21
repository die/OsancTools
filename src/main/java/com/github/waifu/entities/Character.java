package com.github.waifu.entities;

import com.github.waifu.util.Utilities;
import javax.swing.ImageIcon;

/**
 * Character class to store character data.
 */
public class Character {

  /**
   * Class of the character.
   */
  private final String type;
  /**
   * Skin ID the character has.
   */
  private final String skin;
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
   * Boolean if the HP stat is maxed.
   */
  private boolean maxedHp;
  /**
   * Boolean if the MP stat is maxed.
   */
  private boolean maxedMp;
  /**
   * Unknown.
   */
  private String exaltedHp;
  /**
   * Unknown.
   */
  private String exaltedMp;
  /**
   * Unknown.
   */
  private String dexterity;

  /**
   * Character method.
   *
   * <p>Constructs a default Character as a level 0 Wizard.
   */
  public Character() {
    this.type = "Wizard";
    this.skin = "";
    this.skinImage = new ImageIcon(Utilities.getImageResource("images/skins/Wizard.png"));
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
    this.type = type;
    this.skin = "";
    this.skinImage = new ImageIcon(Utilities.getImageResource("images/skins/" + type + ".png"));
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
   * To be documented.
   *
   * @param inventory To be documented.
   * @param level To be documented.
   * @param fame To be documented.
   * @param maxedHp To be documented.
   * @param maxedMp To be documented.
   * @param dexterity To be documented.
   * @param exaltedHp To be documented.
   * @param exaltedMp To be documented.
   */
  public Character(final Inventory inventory, final int level, final int fame, final boolean maxedHp, final boolean maxedMp, final int dexterity, final int exaltedHp, final int exaltedMp) {
    this.type = "Wizard";
    this.skin = "";
    this.skinImage = new ImageIcon(Utilities.getImageResource("images/skins/Wizard.png"));
    this.level = String.valueOf(level);
    this.cqc = "";
    this.fame = String.valueOf(fame);
    this.exp = "";
    this.place = "";
    this.stats = "";
    this.lastSeen = "";
    this.server = "";
    this.inventory = inventory;
    this.maxedHp = maxedHp;
    this.maxedMp = maxedMp;
    this.exaltedHp = String.valueOf(exaltedHp);
    this.exaltedMp = String.valueOf(exaltedMp);
    this.dexterity = String.valueOf(dexterity);
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
  public Character(final String type, final String skin, final String level, final String cqc, final String fame, final String exp, final String place, final String stats, final String lastSeen, final String server, final Inventory inventory) {
    this.type = type;
    this.skin = skin;
    this.skinImage = new ImageIcon(Utilities.getImageResource("images/skins/" + type + ".png"));
    this.level = level;
    this.cqc = cqc;
    this.fame = fame;
    this.exp = exp;
    this.place = place;
    this.stats = stats;
    this.lastSeen = lastSeen;
    this.server = server;
    this.inventory = inventory;
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
  public String getSkin() {
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
}

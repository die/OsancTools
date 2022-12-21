package com.github.waifu.entities;

import com.github.waifu.util.Utilities;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Item class to store item data.
 */
public class Item {

  /**
   * To be documented.
   */
  private final String name;
  /**
   * To be documented.
   */
  private final String type;
  /**
   * To be documented.
   */
  private String itemClass;
  /**
   * To be documented.
   */
  private ImageIcon image;

  /**
   * Item method.
   *
   * <p>Constructs a default Item.
   */
  public Item() {
    this.name = "";
    this.type = "";
    this.image = null;
  }

  /**
   * Item method.
   *
   * <p>Constructs an Item.
   *
   * @param name      name of the Item.
   * @param type      type of Item (Weapon/Ability/Armor/Ring).
   * @param itemClass class that can use the Item (Wizard/etc).
   */
  public Item(final String name, final String type, final String itemClass) {
    this.name = name.replace(":", "");
    this.type = type;
    this.itemClass = itemClass;
    createImage();
  }

  /**
   * To be documented.
   *
   * @param name To be documented.
   * @param type To be documented.
   * @param itemClass To be documented.
   * @param x To be documented.
   * @param y To be documented.
   * @throws IOException To be documented.
   */
  public Item(final String name, final String type, final String itemClass, final int x, final int y) throws IOException {
    this.name = name.replace(":", "");
    this.type = type;
    this.itemClass = itemClass;
    final BufferedImage image = ImageIO.read(Utilities.getImageResource("images/items/renders.png"));
    final BufferedImage image1 = image.getSubimage(x, y, 46, 46);
    System.out.println("obtained image");
    this.image = new ImageIcon(image1);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Gets the item's label (UT/ST/T1/T12).
   *
   * @return To be documented.
   */
  public String getLabel() {
    final String label = name.substring(name.length() - 2);
    if (label.contains("T")) {
      return label;
    } else {
      return name.substring(name.length() - 3);
    }
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getTier() {
    if (getLabel().contains("UT") || getLabel().contains("ST") || getName().equals("Empty slot")) {
      return -1;
    }

    final String label = getLabel().substring(getLabel().length() - 2);
    int tier = -1;
    if (label.contains("T")) {
      tier = Integer.parseInt(label.substring(label.length() - 1));
    } else {
      tier = Integer.parseInt(label);
    }
    return tier;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getNameWithoutLabel() {
    return name.replace(" " + getLabel(), "");
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getType() {
    return this.type;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getItemClass() {
    return itemClass;
  }

  /**
   * createImage method.
   *
   * <p>Constructs the appropriate image for the item using given information.
   */
  public void createImage() {
    if (Utilities.getImageResource("images/items/" + name + ".png") != null) {
      image = new ImageIcon(Utilities.getImageResource("images/items/" + name + ".png"));
    } else if (Utilities.getImageResource("images/items/" + name.replace("UT", "ST") + ".png") != null) {
      image = new ImageIcon(Utilities.getImageResource("images/items/" + name.replace("UT", "ST") + ".png"));
    } else {
      if (itemClass != null) {
        switch (type) {
          case "weapon" -> setEmptyWeaponImage(itemClass);
          case "ability" -> setEmptyAbilityImage(itemClass);
          case "armor" -> setEmptyArmorImage(itemClass);
          case "ring" -> setEmptyRingImage();
          default -> {

          }
        }
      } else {
        switch (type) {
          case "weapon" -> setEmptyWeaponImage("Wizard");
          case "ability" -> setEmptyAbilityImage("Wizard");
          case "armor" -> setEmptyArmorImage("Wizard");
          case "ring" -> setEmptyRingImage();
          default -> {

          }
        }
      }
    }
  }

  /**
   * setEmptyWeaponImage method.
   *
   * <p>Sets the appropriate image for the item using given information.
   *
   * @param itemClass class that can use the Item
   */
  private void setEmptyWeaponImage(final String itemClass) {
    switch (itemClass) {
      case "Rogue", "Assassin", "Trickster" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyDagger.png"));
      case "Archer", "Huntress", "Bard" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyBow.png"));
      case "Wizard", "Necromancer", "Mystic" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyStaff.png"));
      case "Priest", "Sorcerer", "Summoner" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyWand.png"));
      case "Warrior", "Knight", "Paladin" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptySword.png"));
      case "Ninja", "Samurai", "Kensei" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyKatana.png"));
      default -> {

      }
    }
  }

  /**
   * setEmptyAbilityImage method.
   *
   * <p>Sets the appropriate image for the item using given information.
   *
   * @param itemClass class that can use the Item
   */
  private void setEmptyAbilityImage(final String itemClass) {
    image = new ImageIcon(Utilities.getImageResource("images/items/Empty" + itemClass + "Ability.png"));
  }

  /**
   * setEmptyArmorImage method.
   *
   * <p>Sets the appropriate image for the item using given information.
   *
   * @param itemClass class that can use the Item
   */
  private void setEmptyArmorImage(final String itemClass) {
    switch (itemClass) {
      case "Rogue", "Assassin", "Trickster", "Archer", "Huntress", "Ninja" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyLeatherArmor.png"));
      case "Wizard", "Necromancer", "Mystic", "Priest", "Sorcerer", "Summoner", "Bard" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyRobe.png"));
      case "Warrior", "Knight", "Paladin", "Samurai", "Kensei" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyHeavyArmor.png"));
      default -> {

      }
    }
  }

  /**
   * setEmptyRingImage method.
   *
   * <p>Sets the appropriate image for the item, where all classes can use rings.
   */
  private void setEmptyRingImage() {
    image = new ImageIcon(Utilities.getImageResource("images/items/EmptyRing.png"));
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public ImageIcon getImage() {
    return this.image;
  }

  /**
   * To be documented.
   *
   * @param i To be documented.
   */
  public void setImage(final ImageIcon i) {
    this.image = i;
  }

}

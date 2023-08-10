package com.github.waifu.entities;

import com.github.waifu.assets.RotmgAssets;
import com.github.waifu.assets.objects.EquipXMLObject;
import com.github.waifu.util.Utilities;
import java.awt.Color;
import javax.swing.ImageIcon;

/**
 * Item class to store item data.
 */
public class Item {

  /**
   * To be documented.
   */
  private int id;
  /**
   * To be documented.
   */
  private String name;
  /**
   * To be documented.
   */
  private String type;
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
    this.id = -1;
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
  public Item(final int id, final String name, final String type, final String itemClass) {
    this.id = id;
    this.name = name.replace(":", "");
    this.type = type;
    this.itemClass = itemClass;
    createImage();

  }

  /**
   * Get the item's assigned id.
   *
   * @return id as an Integer.
   */
  public int getId() {
    return id;
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
   * Sets the name of the item.
   *
   * @param name the name as a String, which should include the label (UT/ST/TX).
   */
  public void setName(final String name) {
    this.name = name;
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
   * Sets the type of item.
   *
   * @param type type as in weapon, ability, armor, and ring.
   */
  public void setType(final String type) {
    this.type = type;
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
   * Checks if an item name is an id and marks accordingly.
   *
   * @return boolean if the item is an id.
   */
  public boolean isUnknown() {
    try {
      Integer.parseInt(getNameWithoutLabel());
      setImage(Utilities.markImage(getImage(), Color.ORANGE));
      return true;
    } catch (final Exception ignored) {
      return false;
    }
  }

  /**
   * createImage method.
   *
   * <p>Constructs the appropriate image for the item using given information.
   */
  public void createImage() {
    boolean found = false;
    final EquipXMLObject equipXMLObject = RotmgAssets.equipXMLObjectList.get(id);

    if (equipXMLObject != null) this.image = RotmgAssets.equipXMLObjectList.get(id).getImage();
    if (image != null) found = true;

    if (!found) {
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

    isUnknown();
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
      case "Rogue", "Assassin", "Trickster" -> image = new ImageIcon(Utilities.getClassResource("images/items/EmptyDagger.png"));
      case "Archer", "Huntress", "Bard" -> image = new ImageIcon(Utilities.getClassResource("images/items/EmptyBow.png"));
      case "Wizard", "Necromancer", "Mystic" -> image = new ImageIcon(Utilities.getClassResource("images/items/EmptyStaff.png"));
      case "Priest", "Sorcerer", "Summoner" -> image = new ImageIcon(Utilities.getClassResource("images/items/EmptyWand.png"));
      case "Warrior", "Knight", "Paladin" -> image = new ImageIcon(Utilities.getClassResource("images/items/EmptySword.png"));
      case "Ninja", "Samurai", "Kensei" -> image = new ImageIcon(Utilities.getClassResource("images/items/EmptyKatana.png"));
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
    image = new ImageIcon(Utilities.getClassResource("images/items/Empty" + itemClass + "Ability.png"));
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
      case "Rogue", "Assassin", "Trickster", "Archer", "Huntress", "Ninja" -> image = new ImageIcon(Utilities.getClassResource("images/items/EmptyLeatherArmor.png"));
      case "Wizard", "Necromancer", "Mystic", "Priest", "Sorcerer", "Summoner", "Bard" -> image = new ImageIcon(Utilities.getClassResource("images/items/EmptyRobe.png"));
      case "Warrior", "Knight", "Paladin", "Samurai", "Kensei" -> image = new ImageIcon(Utilities.getClassResource("images/items/EmptyHeavyArmor.png"));
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
    image = new ImageIcon(Utilities.getClassResource("images/items/EmptyRing.png"));
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

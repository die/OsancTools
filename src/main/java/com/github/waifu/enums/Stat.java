package com.github.waifu.enums;

import com.github.waifu.assets.RotmgAssets;
import javax.swing.ImageIcon;

/**
 * To be documented.
 */
public enum Stat {

  /**
   * To be documented.
   */
  LIFE(0, RotmgAssets.equipXMLObjectList.get(2793).getImage()),
  /**
   * To be documented.
   */
  MANA(1, RotmgAssets.equipXMLObjectList.get(2794).getImage()),
  /**
   * To be documented.
   */
  ATTACK(2, RotmgAssets.equipXMLObjectList.get(2591).getImage()),
  /**
   * To be documented.
   */
  DEFENSE(3, RotmgAssets.equipXMLObjectList.get(2592).getImage()),
  /**
   * To be documented.
   */
  SPEED(4, RotmgAssets.equipXMLObjectList.get(2593).getImage()),
  /**
   * To be documented.
   */
  DEXTERITY(5, RotmgAssets.equipXMLObjectList.get(2636).getImage()),
  /**
   * To be documented.
   */
  VITALITY(6, RotmgAssets.equipXMLObjectList.get(2612).getImage()),
  /**
   * To be documented.
   */
  WISDOM(7, RotmgAssets.equipXMLObjectList.get(2613).getImage());

  /**
   * To be documented.
   */
  private final int index;
  /**
   * To be documented.
   */
  private final ImageIcon icon;

  /**
   * To be documented.
   *
   * @param index To be documented.
   */
  Stat(final int index, final ImageIcon icon) {
    this.index = index;
    this.icon = icon;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getIndex() {
    return index;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public ImageIcon getIcon() {
    return icon;
  }

  /**
   * Gets a stat by an index.
   *
   * @param index stat index.
   * @return stat enum.
   */
  public static Stat getStatByIndex(final int index) {
    for (Stat s : values()) {
      if (s.index == index) {
        return s;
      }
    }
    return null;
  }

  /**
   * Gets a stat by an index.
   *
   * @param name stat name.
   * @return stat enum.
   */
  public static Stat getStatByName(final String name) {
    for (Stat s : values()) {
      if (s.name().equals(name)) {
        return s;
      }
    }
    return null;
  }
}

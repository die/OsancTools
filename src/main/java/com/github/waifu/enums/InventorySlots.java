package com.github.waifu.enums;

/**
 * To be documented.
 */
public enum InventorySlots {

  /**
   * To be documented.
   */
  WEAPON(0),
  /**
   * To be documented.
   */
  ABILITY(1),
  /**
   * To be documented.
   */
  ARMOR(2),
  /**
   * To be documented.
   */
  RING(3),
  /**
   * The skin being used.
   */
  SKIN(4);

  /**
   * To be documented.
   */
  private final int index;

  /**
   * To be documented.
   *
   * @param index To be documented.
   */
  InventorySlots(final int index) {
    this.index = index;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getIndex() {
    return index;
  }

  public static InventorySlots getEnumByType(final String type) {
    return switch (type) {
      case "weapon" -> WEAPON;
      case "ability" -> ABILITY;
      case "armor" -> ARMOR;
      case "ring" -> RING;
      case "skin" -> SKIN;
      default -> null;
    };
  }
}

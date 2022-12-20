package com.github.waifu.enums;

/**
 *
 */
public enum InventorySlots {

  WEAPON(0), ABILITY(1), ARMOR(2), RING(3);

  private final int index;

  /**
   * @param index
   */
  InventorySlots(int index) {
    this.index = index;
  }

  /**
   * @return
   */
  public int getIndex() {
    return index;
  }
}

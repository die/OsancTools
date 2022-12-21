package com.github.waifu.enums;

/**
 * To be documented.
 */
public enum Stat {

  /**
   * To be documented.
   */
  LIFE(0),
  /**
   * To be documented.
   */
  MANA(1),
  /**
   * To be documented.
   */
  ATTACK(2),
  /**
   * To be documented.
   */
  DEFENSE(3),
  /**
   * To be documented.
   */
  SPEED(4),
  /**
   * To be documented.
   */
  DEXTERITY(5),
  /**
   * To be documented.
   */
  VITALITY(6),
  /**
   * To be documented.
   */
  WISDOM(7);

  /**
   * To be documented.
   */
  private final int index;

  /**
   * To be documented.
   *
   * @param index To be documented.
   */
  Stat(final int index) {
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
}

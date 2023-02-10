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

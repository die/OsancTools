package com.github.waifu.packets.data.enums;

/**
 * Enum that contains item ids that aren't stored in resources.assets.
 */
public enum UnknownItem {

  /**
   * Kensei Ability: Snowman's Sheath.
   */
  SNOWMAN_SHEATH(14662, "Snowman's Sheath", "UT"),
  /**
   * Ninja ability: Spider Shuriken.
    */
  SPIDER_SHURIKEN(14149, "Spider Shuriken", "UT"),
  /**
   * Ring: Corsair Ring.
   */
  CORSAIR_RING(8841, "Corsair Ring", "UT");


  /**
   * The id of the item.
   */
  private final int id;
  /**
   * The name of the item.
   */
  private final String name;
  /**
   * The label of the item.
   */
  private final String label;

  UnknownItem(final int id, final String name, final String label) {
    this.id = id;
    this.name = name;
    this.label = label;
  }

  /**
   * Finds an unknown item by an id.
   *
   * @param id the id of an item as an Integer.
   * @return the name of the item.
   */
  public static String getItemById(final int id) {
    for (final UnknownItem u : values()) {
      if (u.id == id) {
        return u.name + " " + u.label;
      }
    }
    return id + " UT";
  }
}

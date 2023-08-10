package com.github.waifu.entities;

/**
 * Stores class data.
 */
public class ClassData {

  /**
   * The id the game uses to indicate a class.
   */
  private final int id;
  /**
   * The name of the class.
   */
  private final String name;
  /**
   * The non-boost hp required to be maxed.
   */
  private final int maxHp;
  /**
   * The non-boost mp required to be maxed.
   */
  private final int maxMp;
  /**
   * The non-boost speed required to be maxed.
   */
  private final int maxAtt;
  /**
   * The non-boost defense required to be maxed.
   */
  private final int maxDef;
  /**
   * The non-boost speed required to be maxed.
   */
  private final int maxSpd;
  /**
   * The non-boost dexterity required to be maxed.
   */
  private final int maxDex;
  /**
   * The non-boost vitality required to be maxed.
   */
  private final int maxVit;
  /**
   * The non-boost wisdom required to be maxed.
   */
  private final int maxWis;

  /**
   * Construct default ClassData.
   */
  public ClassData() {
    this.id = 0;
    this.name = "Wizard";
    this.maxHp = 0;
    this.maxMp = 0;
    this.maxAtt = 0;
    this.maxDef = 0;
    this.maxDex = 0;
    this.maxSpd = 0;
    this.maxVit = 0;
    this.maxWis = 0;
  }

  /**
   * Stores data about a RoTMG class.
   *
   * @param id the id of the class.
   * @param name the name of the class.
   * @param maxHp the max hp stat.
   * @param maxMp the max mp stat.
   * @param maxAtt the max att stat.
   * @param maxDef the max def stat.
   * @param maxSpd the max spd stat.
   * @param maxDex the max dex stat.
   * @param maxVit the max vit stat.
   * @param maxWis the max wis stat.
   */
  public ClassData(final int id, final String name, final int maxHp, final int maxMp, final int maxAtt, final int maxDef, final int maxSpd, final int maxDex, final int maxVit, final int maxWis) {
    this.id = id;
    this.name = name;
    this.maxHp = maxHp;
    this.maxMp = maxMp;
    this.maxAtt = maxAtt;
    this.maxDef = maxDef;
    this.maxDex = maxDex;
    this.maxSpd = maxSpd;
    this.maxVit = maxVit;
    this.maxWis = maxWis;
  }

  /**
   * Get class id.
   *
   * @return class id as an int.
   */
  public int getId() {
    return id;
  }

  /**
   * Get class name.
   *
   * @return class name as a String.
   */
  public String getName() {
    return name;
  }

  /**
   * Get max hp stat.
   *
   * @return max hp stat as an int.
   */
  public int getMaxHp() {
    return maxHp;
  }

  /**
   * Get max hp stat.
   *
   * @return max hp stat as an int.
   */
  public int getMaxMp() {
    return maxMp;
  }

  /**
   * Get max hp stat.
   *
   * @return max hp stat as an int.
   */
  public int getMaxAtt() {
    return maxAtt;
  }

  /**
   * Get max hp stat.
   *
   * @return max hp stat as an int.
   */
  public int getMaxDef() {
    return maxDef;
  }

  /**
   * Get max hp stat.
   *
   * @return max hp stat as an int.
   */
  public int getMaxSpd() {
    return maxSpd;
  }

  /**
   * Get max hp stat.
   *
   * @return max hp stat as an int.
   */
  public int getMaxDex() {
    return maxDex;
  }

  /**
   * Get max hp stat.
   *
   * @return max hp stat as an int.
   */
  public int getMaxVit() {
    return maxVit;
  }

  /**
   * Get max hp stat.
   *
   * @return max hp stat as an int.
   */
  public int getMaxWis() {
    return maxWis;
  }

  /**
   * Prints out the stats.
   *
   * @return string containing stat data.
   */
  @Override
  public String toString() {
    return "Max HP: " + maxHp + "\n"
            + "Max MP: " + maxMp + "\n"
            + "Max Att: " + maxAtt + "\n"
            + "Max Def: " + maxDef + "\n"
            + "Max Spd: " + maxSpd + "\n"
            + "Max Dex: " + maxDex + "\n"
            + "Max Vit: " + maxVit + "\n"
            + "Max Wis: " + maxWis;
  }
}
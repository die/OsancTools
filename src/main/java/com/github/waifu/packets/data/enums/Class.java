package com.github.waifu.packets.data.enums;

import com.github.waifu.enums.Stat;
import java.util.Arrays;

/**
 * Holds information for RoTMG classes.
 */
public enum Class {

  /**
   * Rogue class.
   */
  Rogue(768, 720, 252, 50, 25, 75, 75, 40, 50),
  /**
   * Archer class.
   */
  Archer(775, 700, 252, 75, 25, 50, 50, 40, 50),
  /**
   * Wizard class.
   */
  Wizard(782, 670, 385, 75, 25, 50, 75, 40, 60),
  /**
   * Priest class.
   */
  Priest(784, 670, 385, 50, 25, 55, 55, 40, 75),
  /**
   * Warrior class.
   */
  Warrior(797, 770, 252, 75, 25, 50, 50, 75, 50),
  /**
   * Knight class.
   */
  Knight(798, 770, 252, 50, 40, 50, 50, 75, 50),
  /**
   * Paladin class.
   */
  Paladin(799, 770, 252, 55, 30, 55, 55, 60, 75),
  /**
   * Assassin class.
   */
  Assassin(800, 720, 305, 65, 25, 65, 75, 40, 60),
  /**
   * Necromancer class.
   */
  Necromancer(801, 670, 385, 75, 25, 50, 60, 40, 75),
  /**
   * Huntress class.
   */
  Huntress(802, 700, 252, 75, 25, 50, 50, 40, 50),
  /**
   * Mystic class.
   */
  Mystic(803, 670, 385, 65, 25, 60, 65, 40, 75),
  /**
   * Trickster class.
   */
  Trickster(804, 720, 252, 65, 25, 75, 75, 40, 60),
  /**
   * Sorcerer class.
   */
  Sorcerer(805, 670, 385, 75, 25, 60, 65, 75, 60),
  /**
   * Ninja class.
   */
  Ninja(806, 720, 252, 70, 25, 60, 70, 60, 70),
  /**
   * Samurai class.
   */
  Samurai(785, 720, 252, 75, 30, 55, 50, 60, 60),
  /**
   * Bard class.
   */
  Bard(796,  670, 385, 55, 25, 55, 70, 45, 75),
  /**
   * Summoner class.
   */
  Summoner(817, 670, 385, 50, 25, 60, 75, 40, 75),
  /**
   * Kensei class.
   */
  Kensei(818, 720, 252, 65, 25, 60, 65, 60, 50);

  /**
   * The id the game uses to indicate a class.
   */
  private final int id;
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

  Class(final int id, final int maxHp, final int maxMp, final int maxAtt, final int maxDef, final int maxSpd, final int maxDex, final int maxVit, final int maxWis) {
    this.id = id;
    this.maxHp = maxHp;
    this.maxMp = maxMp;
    this.maxAtt = maxAtt;
    this.maxDef = maxDef;
    this.maxSpd = maxSpd;
    this.maxDex = maxDex;
    this.maxVit = maxVit;
    this.maxWis = maxWis;
  }

  /**
   * Finds a class enum using a given id.
   *
   * @param id id of the class.
   * @return the class enum.
   */
  public static Class findClassById(final int id) {
    for (Class c : values()) {
      if (c.id == id) {
        return c;
      }
    }
    return null;
  }

  /**
   * Finds a class enum using a given name.
   *
   * @param name name of the class.
   * @return the class enum.
   */
  public static Class findClassByName(final String name) {
    for (Class c : values()) {
      if (c.name().equals(name)) {
        return c;
      }
    }
    return null;
  }

  /**
   * Calculates ?/8 a given class is.
   *
   * @param c the class enum found.
   * @param hp the current total hp stat.
   * @param mp the current total mp stat.
   * @param att the current att stat.
   * @param def the current def stat.
   * @param spd the current spd stat.
   * @param dex the current dex stat.
   * @param vit the current vit stat.
   * @param wis the current wis stat.
   * @return the number out of 8 of stats maxed.
   */
  public static int getMaxStatCount(final Class c, final int hp, final int mp, final int att, final int def, final int spd, final int dex, final int vit, final int wis) {
    int count = 0;

    if (hp >= c.maxHp) {
      count++;
    }
    if (mp >= c.maxMp) {
      count++;
    }
    if (att >= c.maxAtt) {
      count++;
    }
    if (def >= c.maxDef) {
      count++;
    }
    if (spd >= c.maxSpd) {
      count++;
    }
    if (dex >= c.maxDex) {
      count++;
    }
    if (vit >= c.maxVit) {
      count++;
    }
    if (wis >= c.maxWis) {
      count++;
    }

    return count;
  }

  /**
   * Calculates ?/8 a given class is.
   *
   * @param c the class enum found.
   * @param hp the current total hp stat.
   * @param mp the current total mp stat.
   * @param att the current att stat.
   * @param def the current def stat.
   * @param spd the current spd stat.
   * @param dex the current dex stat.
   * @param vit the current vit stat.
   * @param wis the current wis stat.
   * @return the number out of 8 of stats maxed.
   */
  public static String getMaxedStatNames(final Class c, final int hp, final int mp, final int att, final int def, final int spd, final int dex, final int vit, final int wis) {
    String max = "Stats maxed:\n";

    if (hp >= c.maxHp) {
      max += "HP\n";
    }
    if (mp >= c.maxMp) {
      max += "MP\n";
    }
    if (att >= c.maxAtt) {
      max += "Att\n";
    }
    if (def >= c.maxDef) {
      max += "Def\n";
    }
    if (spd >= c.maxSpd) {
      max += "Speed\n";
    }
    if (dex >= c.maxDex) {
      max += "Dexterity\n";
    }
    if (vit >= c.maxVit) {
      max += "Vitality\n";
    }
    if (wis >= c.maxWis) {
      max += "Wisdom\n";
    }

    return max;
  }

  /**
   * Calculates ?/8 a given class is.
   *
   * @param c the class enum found.
   * @param hp the current total hp stat.
   * @param mp the current total mp stat.
   * @param att the current att stat.
   * @param def the current def stat.
   * @param spd the current spd stat.
   * @param dex the current dex stat.
   * @param vit the current vit stat.
   * @param wis the current wis stat.
   * @return the number out of 8 of stats maxed.
   */
  public static Integer[] getMaxedStatIndices(final Class c, final int hp, final int mp, final int att, final int def, final int spd, final int dex, final int vit, final int wis) {
    final Integer[] stats = new Integer[8];
    Arrays.fill(stats, 0);

    if (hp >= c.maxHp) {
      stats[Stat.LIFE.getIndex()] = 1;
    }
    if (mp >= c.maxMp) {
      stats[Stat.MANA.getIndex()] = 1;
    }
    if (att >= c.maxAtt) {
      stats[Stat.ATTACK.getIndex()] = 1;
    }
    if (def >= c.maxDef) {
      stats[Stat.DEFENSE.getIndex()] = 1;
    }
    if (spd >= c.maxSpd) {
      stats[Stat.SPEED.getIndex()] = 1;
    }
    if (dex >= c.maxDex) {
      stats[Stat.DEXTERITY.getIndex()] = 1;
    }
    if (vit >= c.maxVit) {
      stats[Stat.VITALITY.getIndex()] = 1;
    }
    if (wis >= c.maxWis) {
      stats[Stat.WISDOM.getIndex()] = 1;
    }

    return stats;
  }

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

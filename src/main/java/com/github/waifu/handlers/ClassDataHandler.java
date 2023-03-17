package com.github.waifu.handlers;

import com.github.waifu.entities.ClassData;
import com.github.waifu.enums.Stat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles class data.
 */
public final class ClassDataHandler {

  /**
   * List of all class data.
   */
  private static List<ClassData> classDataList = new ArrayList<>();

  /**
   * Private constructor.
   */
  private ClassDataHandler() {

  }

  /**
   * Finds a class enum using a given id.
   *
   * @param id id of the class.
   * @return the class enum.
   */
  public static ClassData findClassById(final int id) {
    for (final ClassData c : classDataList) {
      if (c.getId() == id) {
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
  public static ClassData findClassByName(final String name) {
    for (final ClassData c : classDataList) {
      if (c.getName().equals(name)) {
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
  public static int getMaxStatCount(final ClassData c, final int hp, final int mp, final int att, final int def, final int spd, final int dex, final int vit, final int wis) {
    int count = 0;

    if (hp >= c.getMaxHp()) {
      count++;
    }
    if (mp >= c.getMaxMp()) {
      count++;
    }
    if (att >= c.getMaxAtt()) {
      count++;
    }
    if (def >= c.getMaxDef()) {
      count++;
    }
    if (spd >= c.getMaxSpd()) {
      count++;
    }
    if (dex >= c.getMaxDex()) {
      count++;
    }
    if (vit >= c.getMaxVit()) {
      count++;
    }
    if (wis >= c.getMaxWis()) {
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
  public static String getMaxedStatNames(final ClassData c, final int hp, final int mp, final int att, final int def, final int spd, final int dex, final int vit, final int wis) {
    String max = "Stats maxed:\n";

    if (hp >= c.getMaxHp()) {
      max += "HP\n";
    }
    if (mp >= c.getMaxMp()) {
      max += "MP\n";
    }
    if (att >= c.getMaxAtt()) {
      max += "Att\n";
    }
    if (def >= c.getMaxDef()) {
      max += "Def\n";
    }
    if (spd >= c.getMaxSpd()) {
      max += "Speed\n";
    }
    if (dex >= c.getMaxDex()) {
      max += "Dexterity\n";
    }
    if (vit >= c.getMaxVit()) {
      max += "Vitality\n";
    }
    if (wis >= c.getMaxWis()) {
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
  public static Integer[] getMaxedStatIndices(final ClassData c, final int hp, final int mp, final int att, final int def, final int spd, final int dex, final int vit, final int wis) {
    final Integer[] stats = new Integer[8];
    Arrays.fill(stats, 0);

    if (hp >= c.getMaxHp()) {
      stats[Stat.LIFE.getIndex()] = 1;
    }
    if (mp >= c.getMaxMp()) {
      stats[Stat.MANA.getIndex()] = 1;
    }
    if (att >= c.getMaxAtt()) {
      stats[Stat.ATTACK.getIndex()] = 1;
    }
    if (def >= c.getMaxDef()) {
      stats[Stat.DEFENSE.getIndex()] = 1;
    }
    if (spd >= c.getMaxSpd()) {
      stats[Stat.SPEED.getIndex()] = 1;
    }
    if (dex >= c.getMaxDex()) {
      stats[Stat.DEXTERITY.getIndex()] = 1;
    }
    if (vit >= c.getMaxVit()) {
      stats[Stat.VITALITY.getIndex()] = 1;
    }
    if (wis >= c.getMaxWis()) {
      stats[Stat.WISDOM.getIndex()] = 1;
    }

    return stats;
  }

  /**
   * Gets the class data list.
   *
   * @return list object.
   */
  public static List<ClassData> getClassDataList() {
    return classDataList;
  }
}

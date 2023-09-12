package com.github.waifu.entities;

import com.github.waifu.assets.RotmgAssets;
import com.github.waifu.assets.objects.PlayerXmlObject;
import com.github.waifu.enums.Stat;
import com.github.waifu.handlers.ClassDataHandler;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * Stores stats of a character.
 */
public class CharacterStats {

  /**
   * Stats list.
   */
  private final List<Integer> stats;
  /**
   * Boosts list.
   */
  private final List<Integer> statBoosts;
  /**
   * Stats that are against the requirement sheet.
   */
  private final Integer[] problemStats;

  /**
   * To be documented.
   *
   * @param stats holds raw stat values.
   * @param statBoosts holds raw boost stats.
   */
  public CharacterStats(final List<Integer> stats, final List<Integer> statBoosts) {
    this.stats = stats;
    this.statBoosts = statBoosts;
    this.problemStats = new Integer[8];
    Arrays.fill(problemStats, 0);
  }

  /**
   * Get problem stats.
   * @return list of stats (0-7), 1 if it is a problem.
   */
  public Integer[] getProblemStats() {
    return problemStats;
  }

  /**
   * Marks which stat is a problem with a value of 1.
   * @param index stat index.
   */
  public void addProblemStat(final int index) {
    if (index > problemStats.length) return;

    problemStats[index] = 1;
  }

  /**
   * Removes a stat that was a problem with a value of 0.
   * @param index stat index.
   */
  public void removeProblemStat(final int index) {
    if (index > problemStats.length) return;

    problemStats[index] = 0;
  }

  /**
   * Reset problem stats.
   */
  public void resetProblemStats() {
    Arrays.fill(problemStats, 0);
  }

  /**
   * Get maxed stats out of 8.
   *
   * @param clazz class enum.
   * @return int.
   */
  public Integer[] getMaxedStatIndices(final String clazz) {
    final int maxHp = this.stats.get(Stat.LIFE.getIndex()) - this.statBoosts.get(Stat.LIFE.getIndex());
    final int maxMp = this.stats.get(Stat.MANA.getIndex()) - this.statBoosts.get(Stat.MANA.getIndex());
    final int maxAtt = this.stats.get(Stat.ATTACK.getIndex()) - this.statBoosts.get(Stat.ATTACK.getIndex());
    final int maxDef = this.stats.get(Stat.DEFENSE.getIndex()) - this.statBoosts.get(Stat.DEFENSE.getIndex());
    final int maxSpd = this.stats.get(Stat.SPEED.getIndex()) - this.statBoosts.get(Stat.SPEED.getIndex());
    final int maxDex = this.stats.get(Stat.DEXTERITY.getIndex()) - this.statBoosts.get(Stat.DEXTERITY.getIndex());
    final int maxVit = this.stats.get(Stat.VITALITY.getIndex()) - this.statBoosts.get(Stat.VITALITY.getIndex());
    final int maxWis = this.stats.get(Stat.WISDOM.getIndex()) - this.statBoosts.get(Stat.WISDOM.getIndex());

    PlayerXmlObject playerXmlObject = null;
    for (final PlayerXmlObject p : RotmgAssets.playerXmlObjectList) {
      if (p.getId().equals(clazz)) {
        playerXmlObject = p;
      }
    }

    return ClassDataHandler.getMaxedStatIndices(playerXmlObject, maxHp, maxMp, maxAtt, maxDef, maxSpd, maxDex, maxVit, maxWis);
  }

  @Override
  public String toString() {
    final StringBuilder stringBuilder = new StringBuilder("Stats: [");
    StringJoiner stringJoiner = new StringJoiner(", ");
    for (int i = 0; i < stats.size(); i++) {
      final Stat stat = Stat.getStatByIndex(i);
      stringJoiner.add(stat + ": " + stats.get(i));
    }
    stringBuilder.append(stringJoiner).append("]\nStat Boosts: [");

    stringJoiner = new StringJoiner(", ");
    for (int i = 0; i < statBoosts.size(); i++) {
      final Stat stat = Stat.getStatByIndex(i);
      stringJoiner.add(stat + ": " + statBoosts.get(i));
    }
    stringBuilder.append(stringJoiner).append("]\n");

    return stringBuilder.toString();
  }
}

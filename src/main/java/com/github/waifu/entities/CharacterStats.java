package com.github.waifu.entities;

import com.github.waifu.enums.Stat;
import com.github.waifu.packets.data.enums.Class;
import java.util.List;

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
   * To be documented.
   *
   * @param stats holds raw stat values.
   * @param statBoosts holds raw boost stats.
   */
  public CharacterStats(final List<Integer> stats, final List<Integer> statBoosts) {
    this.stats = stats;
    this.statBoosts = statBoosts;
  }

  /**
   * Get maxed stats out of 8.
   *
   * @param c class enum.
   * @return int.
   */
  public Integer[] getMaxedStatIndices(final Class c) {
    final int maxHp = this.stats.get(Stat.LIFE.getIndex()) - this.statBoosts.get(Stat.LIFE.getIndex());
    final int maxMp = this.stats.get(Stat.MANA.getIndex()) - this.statBoosts.get(Stat.MANA.getIndex());
    final int maxAtt = this.stats.get(Stat.ATTACK.getIndex()) - this.statBoosts.get(Stat.ATTACK.getIndex());
    final int maxDef = this.stats.get(Stat.DEFENSE.getIndex()) - this.statBoosts.get(Stat.DEFENSE.getIndex());
    final int maxSpd = this.stats.get(Stat.SPEED.getIndex()) - this.statBoosts.get(Stat.SPEED.getIndex());
    final int maxDex = this.stats.get(Stat.DEXTERITY.getIndex()) - this.statBoosts.get(Stat.DEXTERITY.getIndex());
    final int maxVit = this.stats.get(Stat.VITALITY.getIndex()) - this.statBoosts.get(Stat.VITALITY.getIndex());
    final int maxWis = this.stats.get(Stat.WISDOM.getIndex()) - this.statBoosts.get(Stat.WISDOM.getIndex());

    return Class.getMaxedStatIndices(c, maxHp, maxMp, maxAtt, maxDef, maxSpd, maxDex, maxVit, maxWis);
  }
}

package com.github.waifu.util;

import com.github.waifu.gui.Main;
import java.util.Arrays;
import java.util.List;

/**
 * To be documented.
 */
public class ExaltCalculator {

  /**
   * To be documented.
   */
  private int stat;

  /**
   * To be documented.
   */
  public ExaltCalculator() {
    this.stat = Main.getSettings().getStat();
  }

  /**
   * To be documented.
   *
   * @param exalts to be documented.
   * @return to be documented.
   */
  public int calculateCompletions(final List<String[]> exalts) {
    int completes = 0;
    for (final String[] s : exalts) {
      final String exaltString = Arrays.asList(s).subList(2, s.length).get(stat);
      final int exalt = Integer.parseInt(exaltString.replace("+", ""));
      final int multiplier;
      switch (stat) {
        case 0, 1 -> multiplier = 5;
        default -> multiplier = 1;
      }
      int i = 1;
      while (i <= (exalt / multiplier)) {
        completes += 5 * i;
        i++;
      }
    }
    return completes;
  }

  /**
   * To be documented.
   *
   * @return to be documented.
   */
  public int getStat() {
    return this.stat;
  }

  /**
   * To be documented.
   *
   * @param s to be documented.
   */
  public void setStat(final int s) {
    this.stat = s;
  }
}

package com.github.waifu.util;

import com.github.waifu.gui.Main;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class ExaltCalculator {

    private int stat;

    /**
     *
     */
    public ExaltCalculator() {
        this.stat = Main.settings.getStat();
    }

    /**
     *
     * @param exalts
     * @return
     */
    public int calculateCompletions(List<String[]> exalts) {
        int completes = 0;
        for (String[] s : exalts) {
            int exalt = Integer.parseInt(Arrays.asList(s).subList(2, s.length).get(stat).replace("+", ""));
            int multiplier = 1;
            switch (stat) {
                case 0, 1 -> multiplier = 5;
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
     *
     * @return
     */
    public int getStat() {
        return this.stat;
    }

    /**
     *
     * @param stat
     */
    public void setStat(int stat) {
        this.stat = stat;
    }
}

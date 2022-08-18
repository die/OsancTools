package com.github.waifu.enums;

import java.awt.*;

public enum Problem {

    PRIVATE_PROFILE("Private Profile", 0, Color.WHITE),
    MISSING_REACT("Missing React", 1, Color.RED),
    MISSING_REACT_TRADE("Missing React Trade", 1, Color.RED),
    MISSING_REACT_DPS("Missing React Dps", 1, Color.RED),
    EMPTY_SLOT("Empty Slot", 1, Color.RED),
    UNDER_REQS("Under Reqs", 1, Color.RED),
    BANNED_ITEM("Banned Item", 2, Color.RED),
    SWAPOUT_ITEM("Swapout Item", 3, Color.YELLOW),
    NONE("None", 4, Color.GREEN);

    private final String problem;
    private final int level;
    private final Color color;

    Problem(String problem, int level, Color color) {
        this.problem = problem;
        this.level = level;
        this.color = color;
    }

    public String getProblem() {
        return problem;
    }

    public int getLevel() {
        return level;
    }

    public Color getColor() {
        return color;
    }
}

package com.github.waifu.enums;

/**
 *
 */
public enum Stat {

    LIFE(0),
    MANA(1),
    ATTACK(2),
    DEFENSE(3),
    SPEED(4),
    DEXTERITY(5),
    VITALITY(6),
    WISDOM(7);

    private final int index;

    /**
     *
     * @param index
     */
    Stat(int index) {
        this.index = index;
    }

    /**
     *
     * @return
     */
    public int getIndex() {
        return index;
    }
}

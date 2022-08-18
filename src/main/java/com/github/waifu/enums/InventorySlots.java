package com.github.waifu.enums;

public enum InventorySlots {

    WEAPON(0),
    ABILITY(1),
    ARMOR(2),
    RING(3);

    private final int index;

    InventorySlots(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}

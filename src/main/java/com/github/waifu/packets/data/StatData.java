package com.github.waifu.packets.data;

import com.github.waifu.packets.data.enums.StatType;
import com.github.waifu.packets.reader.BufferReader;

public class StatData {
    /**
     * The type of stat
     */
    public int statTypeNum;
    public StatType statType;
    /**
     * The number value of this stat, if this is not a string stat
     */
    public int statValue;
    /**
     * The string value of this stat, if this is a string stat
     */
    public String stringStatValue;
    /**
     * The secondary stat value
     */
    public int statValueTwo;

    /**
     * Deserializer method to extract data from the buffer.
     *
     * @param buffer Data that needs deserializing.
     * @return Returns this object after deserializing.
     */
    public StatData deserialize(BufferReader buffer) {
        statTypeNum = buffer.readUnsignedByte();
        statType = StatType.byOrdinal(statTypeNum);

        if (isStringStat()) {
            stringStatValue = buffer.readString();
        } else {
            statValue = buffer.readCompressedInt();
        }
        statValueTwo = buffer.readCompressedInt();

        return this;
    }

    private boolean isStringStat() {
        // 121
        return StatType.EXP_STAT.get() == statTypeNum // 6
                || StatType.NAME_STAT.get() == statTypeNum // 31
                || StatType.ACCOUNT_ID_STAT.get() == statTypeNum // 38
                || StatType.OWNER_ACCOUNT_ID_STAT.get() == statTypeNum // 54
                || StatType.GUILD_NAME_STAT.get() == statTypeNum // 62
                || StatType.TEXTURE_STAT.get() == statTypeNum // 80
                || StatType.PET_NAME_STAT.get() == statTypeNum // 82
                || StatType.GRAVE_ACCOUNT_ID.get() == statTypeNum // 115
                || StatType.UNKNOWN121.get() == statTypeNum;
    }
}

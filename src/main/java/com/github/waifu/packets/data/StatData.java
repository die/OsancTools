package com.github.waifu.packets.data;

import com.github.waifu.packets.data.enums.StatType;
import com.github.waifu.packets.reader.BufferReader;

/**
 * Stores stat data.
 */
public class StatData {
  /**
   * The type of stat.
   */
  private int statTypeNum;
  /**
   * The type of stat as an enum.
   */
  private StatType statType;
  /**
   * The number value of this stat, if this is not a string stat.
   */
  private int statValue;
  /**
   * The string value of this stat, if this is a string stat.
   */
  private String stringStatValue;
  /**
   * The secondary stat value.
   */
  private int statValueTwo;

  /**
   * Deserializer method to extract data from the buffer.
   *
   * @param buffer Data that needs deserializing.
   * @return Returns this object after deserializing.
   */
  public StatData deserialize(final BufferReader buffer) {
    statTypeNum = buffer.readUnsignedByte();
    statType = StatType.byOrdinal(statTypeNum);

    if (isStringStat()) {
      try {
        stringStatValue = buffer.readString();
      } catch (Exception e) {
        System.out.println(this);
      }
    } else {
      statValue = buffer.readCompressedInt();
    }
    statValueTwo = buffer.readCompressedInt();

    return this;
  }

  private boolean isStringStat() {
    return StatType.EXP_STAT.get() == statTypeNum // 6
            || StatType.NAME_STAT.get() == statTypeNum // 31
            || StatType.ACCOUNT_ID_STAT.get() == statTypeNum // 38
            // || StatType.OWNER_ACCOUNT_ID_STAT.get() == statTypeNum // 54
            || StatType.GUILD_NAME_STAT.get() == statTypeNum // 62
            || StatType.TEXTURE_STAT.get() == statTypeNum // 80
            || StatType.PET_NAME_STAT.get() == statTypeNum // 82
            // || StatType.GRAVE_ACCOUNT_ID.get() == statTypeNum // 115
            || StatType.UNKNOWN121.get() == statTypeNum
            || StatType.ENCHANTMENT.get() == statTypeNum;
  }

  /**
   * Gets the stat type.
   *
   * @return stat type as StatType enum.
   */
  public StatType getStatType() {
    return statType;
  }

  /**
   * Get the stat value.
   *
   * @return stat value as an int.
   */
  public int getStatValue() {
    return statValue;
  }

  /**
   * Get the stat value.
   *
   * @return stat value as a String.
   */
  public String getStringStatValue() {
    return stringStatValue;
  }

  @Override
  public String toString() {
    return "StatData{" +
            "statTypeNum=" + statTypeNum +
            ", statType=" + statType +
            ", statValue=" + statValue +
            ", stringStatValue='" + stringStatValue + '\'' +
            ", statValueTwo=" + statValueTwo +
            "}\n";
  }
}

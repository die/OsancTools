package com.github.waifu.packets.data;

import com.github.waifu.packets.Util;
import com.github.waifu.packets.reader.BufferReader;

/**
 * Stores ObjectStatusData.
 */
public class ObjectStatusData {
  /**
   * The object id of the object which this status is for.
   */
  private int objectId;
  /**
   * The position of the object which this status is for.
   */
  private WorldPosData pos;
  /**
   * A list of stats for the object which this status is for.
   */
  private StatData[] stats;

  /**
   * Deserializer method to extract data from the buffer.
   *
   * @param buffer Data that needs deserializing.
   * @return Returns this object after deserializing.
   */
  public ObjectStatusData deserialize(final BufferReader buffer) {
    objectId = buffer.readCompressedInt();
    pos = new WorldPosData().deserialize(buffer);
    final int num = buffer.readCompressedInt();
    if (num >= 0) {
      stats = new StatData[num];
      for (int i = 0; i < stats.length; i++) {
        stats[i] = new StatData().deserialize(buffer);
      }
    }
    return this;
  }

  /**
   * Returns the ObjectStatusData object as a String.
   *
   * @return ObjectStatusData object as a String.
   */
  @Override
  public String toString() {
    return "\n    Id="
            + objectId
            + " Loc=("
            + pos.getPositionX()
            + ", "
            + pos.getPositionY()
            + ")"
            + (stats.length == 0 ? "" : Util.showAll(stats));
  }

  /**
   * Gets the StatData.
   *
   * @return stat data as a StatData object.
   */
  public StatData[] getStatData() {
    return this.stats;
  }
}

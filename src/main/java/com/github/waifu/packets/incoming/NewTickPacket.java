package com.github.waifu.packets.incoming;

import com.github.waifu.packets.Packet;
import com.github.waifu.packets.Util;
import com.github.waifu.packets.data.ObjectStatusData;
import com.github.waifu.packets.reader.BufferReader;

/**
 * Received to notify the player of a new game tick.
 */
public class NewTickPacket extends Packet {
  /**
   * The id of the tick.
   */
  private int tickId;
  /**
   * The time between the last tick and this tick, in milliseconds.
   */
  private int tickTime;
  /**
   * Server realtime in ms.
   */
  private long serverRealTimeMs;
  /**
   * Last server realtime in ms.
   */
  private int serverLastTimeRttMs;
  /**
   * An array of statuses for objects which are currently visible to the player.
   */
  private ObjectStatusData[] status;

  /**
   * Deserializes the NewTickPacket.
   *
   * @param buffer The data of the packet in a rotmg buffer format.
   */
  @Override
  public void deserialize(final BufferReader buffer) {
    tickId = buffer.readInt();
    tickTime = buffer.readInt();
    serverRealTimeMs = buffer.readUnsignedInt();
    serverLastTimeRttMs = buffer.readUnsignedShort();
    status = new ObjectStatusData[buffer.readShort()];
    for (int i = 0; i < status.length; i++) {
      status[i] = new ObjectStatusData().deserialize(buffer);
    }
  }

  /**
   * Constructs a String to show NewTickPacket.
   *
   * @return packet representation as String.
   */
  @Override
  public String toString() {
    return "NewTickPacket"
            + "\n  tickId="
            + tickId
            + "\n  tickTime="
            + tickTime
            + "\n  serverRealTimeMS="
            + serverRealTimeMs
            + "\n  serverLastTimeRTTMS="
            + serverLastTimeRttMs
            + (status.length == 0 ? "" : Util.showAll(status));
  }
}

package com.github.waifu.packets.incoming;

import com.github.waifu.packets.Packet;
import com.github.waifu.packets.Util;
import com.github.waifu.packets.data.GroundTileData;
import com.github.waifu.packets.data.ObjectData;
import com.github.waifu.packets.data.WorldPosData;
import com.github.waifu.packets.reader.BufferReader;
import java.util.Arrays;

/**
 * Received when an update even occurs. Some events include
 * + One or more new objects have entered the map (become visible)
 * + One or more objects have left the map (become invisible)
 * + New tiles are visible.
 */
public class UpdatePacket extends Packet {
  /**
   * Unknown level byte.
   */
  private byte levelType;
  /**
   * The player pos if the player have moved, otherwise sends (0,0).
   */
  private WorldPosData pos;
  /**
   * The new tiles which are visible.
   */
  private GroundTileData[] tiles;
  /**
   * The new objects which have entered the map (become visible).
   */
  private ObjectData[] newObjects;
  /**
   * The visible objects which have left the map (become invisible).
   */
  private int[] drops;

  /**
   * Deserializes the UpdatePacket.
   *
   * @param buffer The data of the packet in a rotmg buffer format.
   */
  @Override
  public void deserialize(final BufferReader buffer) {
    pos = new WorldPosData().deserialize(buffer);
    levelType = buffer.readByte();

    tiles = new GroundTileData[buffer.readCompressedInt()];
    for (int i = 0; i < tiles.length; i++) {
      tiles[i] = new GroundTileData().deserialize(buffer);
    }

    newObjects = new ObjectData[buffer.readCompressedInt()];
    for (int i = 0; i < newObjects.length; i++) {
      newObjects[i] = new ObjectData().deserialize(buffer);
    }

    // todo: handle drops
    buffer.readRemainingBytes();
    /*
      final int compressedInt = buffer.readCompressedInt();
      drops = new int[compressedInt];
      for (int i = 0; i < drops.length; i++) {
        drops[i] = buffer.readCompressedInt();
      }
    */
  }

  /**
   * Constructs a String to show UpdatePacket.
   *
   * @return packet representation as String.
   */
  @Override
  public String toString() {
    return "UpdatePacket"
            + "\n   levelType="
            + levelType
            + "\n   playerPos= ("
            + pos.getPositionX()
            + ", "
            + pos.getPositionY()
            + ")"
            + (tiles.length == 0 ? "" : Util.showAll(tiles))
            + (newObjects.length == 0 ? "" : Util.showAll(newObjects))
            + (drops.length == 0 ? "" : Arrays.toString(drops));
  }

  /**
   * Gets the new object data.
   *
   * @return new object data as ObjectData
   */
  public ObjectData[] getNewObjects() {
    return newObjects;
  }

}

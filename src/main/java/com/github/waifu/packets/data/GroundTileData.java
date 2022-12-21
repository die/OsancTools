package com.github.waifu.packets.data;

import com.github.waifu.packets.reader.BufferReader;

/**
 * Tile data class storing tile coordinates (x and y) and type of each tile.
 */
public class GroundTileData {
  /**
   * The X coordinate of this tile.
   */
  private short positionX;
  /**
   * The Y coordinate of this tile.
   */
  private short positionY;
  /**
   * The tile type of this tile.
   */
  private int type;

  /**
   * Deserializer method to extract data from the buffer.
   *
   * @param buffer Data that needs deserializing.
   * @return Returns this object after deserializing.
   */
  public GroundTileData deserialize(final BufferReader buffer) {
    positionX = buffer.readShort();
    positionY = buffer.readShort();
    type = buffer.readUnsignedShort();
    return this;
  }
}

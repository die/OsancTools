package com.github.waifu.packets.data;

import com.github.waifu.packets.reader.BufferReader;

/**
 * Coordinate data of world objects.
 */
public class WorldPosData {
  /**
   * Position x.
   */
  private float positionX;
  /**
   * Position y.
   */
  private float positionY;

  /**
   * Deserializer method to extract data from the buffer.
   *
   * @param buffer Data that needs deserializing.
   * @return Returns this object after deserializing.
   */
  public WorldPosData deserialize(final BufferReader buffer) {
    positionX = buffer.readFloat();
    positionY = buffer.readFloat();

    return this;
  }

  /**
   * Returns the WorldPosData object as a String.
   *
   * @return WorldPosData object as a String.
   */
  @Override
  public String toString() {
    return "WorldPosData{"
            + "\n   x="
            + positionX
            + "\n   y="
            + positionY;
  }

  /**
   * Gets the x position.
   *
   * @return x position as a float.
   */
  public float getPositionX() {
    return positionX;
  }

  /**
   * Gets the y position.
   *
   * @return y position as a float.
   */
  public float getPositionY() {
    return positionY;
  }
}

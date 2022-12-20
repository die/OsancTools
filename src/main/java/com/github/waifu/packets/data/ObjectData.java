package com.github.waifu.packets.data;

import com.github.waifu.packets.reader.BufferReader;

/**
 * Stores Object data.
 */
public class ObjectData {
  /**
   * The type of this object.
   */
  private int objectType;
  /**
   * The status of this object.
   */
  private ObjectStatusData status;

  /**
   * Deserializer method to extract data from the buffer.
   *
   * @param buffer Data that needs deserializing.
   * @return Returns this object after deserializing.
   */
  public ObjectData deserialize(final BufferReader buffer) {
    objectType = buffer.readUnsignedShort();
    status = new ObjectStatusData().deserialize(buffer);

    return this;
  }

  /**
   * Gets object type.
   *
   * @return object type as int.
   */
  public int getObjectType() {
    return objectType;
  }

  /**
   * Gets status of an object.
   *
   * @return status as an ObjectStatusData.
   */
  public ObjectStatusData getStatus() {
    return status;
  }
}

package com.github.waifu.packets;

import com.github.waifu.packets.reader.BufferReader;

/**
 * Abstract packet class for all incoming or outgoing packets.
 */
public abstract class Packet {

  /**
   * Data as a byte array.
   */
  private byte[] data;

  /**
   * To be documented.
   *
   * @return to be documented.
   */
  public byte[] getPayload() {
    return data;
  }

  /**
   * To be documented.
   *
   * @param newData to be documented.
   */
  public void setData(final byte[] newData) {
    this.data = newData;
  }

  /**
   * Deserialize method to deserialize the data for each packet type.
   *
   * @param buffer The data of the packet in a rotmg buffer format.
   */
  public abstract void deserialize(BufferReader buffer);

  /**
   * An interface to be used as a class factory for different packet types.
   */
  public interface PacketInterface {
    /**
     * To be documented.
     *
     * @return to be documented.
     */
    Packet factory();
  }
}

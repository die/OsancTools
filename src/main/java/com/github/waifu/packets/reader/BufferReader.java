package com.github.waifu.packets.reader;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Custom buffer class to deserialize the rotmg packets.
 */
public class BufferReader {

  /**
   * ByteBuffer object.
   */
  private ByteBuffer buffer;

  /**
   * Construct BufferReader with data.
   *
   * @param data ByteBuffer data.
   */
  public BufferReader(final ByteBuffer data) {
    buffer = data;
  }

  /**
   * Returns the buffer size.
   *
   * @return size of the buffer.
   */
  public int size() {
    return buffer.capacity();
  }

  /**
   * Internal index of the buffer.
   *
   * @return Returns the internal index the buffer is at.
   */
  public int getIndex() {
    return buffer.position();
  }

  /**
   * Deserialize a boolean.
   *
   * @return Returns a boolean that have been deserialized.
   */
  public boolean readBoolean() {
    return buffer.get() != 0;
  }

  /**
   * Deserialize a byte.
   *
   * @return Returns the byte that have been deserialized.
   */
  public byte readByte() {
    return buffer.get();
  }

  /**
  * Deserialize an unsigned byte.
  *
  * @return Returns an integer containing an unsigned byte.
  */
  public int readUnsignedByte() {
    return Byte.toUnsignedInt(buffer.get());
  }

  /**
   * Deserialize a short.
   *
   * @return Returns the short that have been deserialized.
   */
  public short readShort() {
    return buffer.getShort();
  }

  /**
   * Deserialize an unsigned short.
   *
   * @return Returns an integer containing an unsigned short.
   */
  public int readUnsignedShort() {
    return Short.toUnsignedInt(buffer.getShort());
  }

  /**
   * Deserialize an integer.
   *
   * @return Returns the integer that have been deserialized.
   */
  public int readInt() {
    return buffer.getInt();
  }

  /**
   * Deserialize an unsigned integer.
   *
   * @return Returns an integer containing an unsigned integer.
   */
  public long readUnsignedInt() {
    return Integer.toUnsignedLong(buffer.getInt());
  }

  /**
   * Deserialize a float.
   *
   * @return Returns the float that have been deserialized.
   */
  public float readFloat() {
    return buffer.getFloat();
  }

  /**
   * Deserialize a string.
   *
   * @return Returns the string that have been deserialized.
   */
  public String readString() {
    final short len = readShort();
    final byte[] str = new byte[len];
    buffer.get(str);
    return new String(str);
  }

  /**
   * Deserialize a string using UTF32 (more characters that is
   * never found in-game) not used as far as I'm aware.
   *
   * @return Returns the string that have been deserialized.
   */
  public String readStringUtf32() {
    final int len = readInt();
    final byte[] str = new byte[len];
    buffer.get(str);
    return new String(str);
  }

  /**
   * Deserialize a byte array.
   *
   * @param bytes Number of bytes that is contained in the array.
   * @return Returns a byte array that have been deserialized.
   */
  public byte[] readBytes(final int bytes) {
    final byte[] out = new byte[readShort()];
    buffer.get(out);
    return out;
  }

  /**
   * Rotmg deserializer of a compressed long.
   *
   * @return Returns a long that have been deserialized.
   */
  public int readCompressedInt() {
    int ubyte = readUnsignedByte();
    final int bits = 64;
    final boolean isNegative = (ubyte & bits) != 0;
    final int initShift = 6;
    int shift = initShift;
    int value = ubyte & (bits - 1);

    final int max = 128;
    while ((ubyte & max) != 0) {
      ubyte = readUnsignedByte();
      value |= (ubyte & (max - 1)) << shift;
      shift += (initShift + 1);
    }

    if (isNegative) {
      value = -value;
    }
    return value;
  }

  /**
   * Clears buffer by reading all bytes.
   */
  public void readRemainingBytes() {
    for (int i = 0; i < getRemainingBytes(); i++) {
      readByte();
    }
  }

  /**
   * Checks if the buffer have finished reading all bytes.
   *
   * @return boolean if buffer is fully parsed.
   */
  public boolean isBufferFullyParsed() {
    return buffer.capacity() == buffer.position();
  }

  /**
   * Converts the buffer array to a String.
   *
   * @return buffer array as a String.
   */
  public String toString() {
    return Arrays.toString(buffer.array());
  }

  /**
   * Gets the remaining bytes from current index.
   *
   * @return number of bytes remaining from current index.
   */
  public int getRemainingBytes() {
    return buffer.capacity() - buffer.position();
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public ByteBuffer getBuffer() {
    return buffer;
  }
}

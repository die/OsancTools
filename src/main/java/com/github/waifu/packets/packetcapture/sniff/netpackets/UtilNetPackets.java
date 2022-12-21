package com.github.waifu.packets.packetcapture.sniff.netpackets;

/**
 * Util class for Raw, Ether, Ip4 and TCP packets.
 */
public final class UtilNetPackets {

  /**
   * To be documented.
   */
  private UtilNetPackets() {

  }

  /**
   * To be documented.
   */
  public static final int BYTE_SIZE_IN_BYTES = 1;
  /**
   * To be documented.
   */
  public static final int SHORT_SIZE_IN_BYTES = 2;
  /**
   * To be documented.
   */
  public static final int INT_SIZE_IN_BYTES = 4;
  /**
   * To be documented.
   */
  public static final int LONG_SIZE_IN_BYTES = 8;

  /**
   * To be documented.
   *
   * @param array To be documented.
   * @param offset To be documented.
   * @param len To be documented.
   */
  public static void validateBounds(final byte[] array, final int offset, final int len) {
    if (array == null) {
      throw new NullPointerException("Array is null");
    }
    if (array.length == 0) {
      throw new IllegalArgumentException("Array is empty");
    }
    if (len == 0) {
      throw new IllegalArgumentException("Zero len error");
    }
    if (offset < 0) {
      throw new ArrayIndexOutOfBoundsException("Offset negative " + offset);
    }
    if (len < 0) {
      throw new ArrayIndexOutOfBoundsException("Len negative " + len);
    }
    if (offset + len > array.length) {
      throw new ArrayIndexOutOfBoundsException("Len plus offset larger than array offset: " + offset + " len: " + len + " array: " + array.length);
    }
  }

  /**
   * To be documented.
   *
   * @param data To be documented.
   * @param offset To be documented.
   * @param length To be documented.
   * @return To be documented.
   */
  public static byte[] getBytes(final byte[] data, final int offset, final int length) {
    validateBounds(data, offset, length);

    final byte[] subArray = new byte[length];
    System.arraycopy(data, offset, subArray, 0, length);
    return subArray;
  }

  /**
   * To be documented.
   *
   * @param data To be documented.
   * @param typeOffset To be documented.
   * @return To be documented.
   */
  public static int getByte(final byte[] data, final int typeOffset) {
    return 0xFF & Byte.toUnsignedInt(data[typeOffset]);
  }

  /**
   * To be documented.
   *
   * @param data To be documented.
   * @param typeOffset To be documented.
   * @return To be documented.
   */
  public static int getShort(final byte[] data, final int typeOffset) {
    return 0xFFFF & ((Byte.toUnsignedInt(data[typeOffset]) << 8) | (Byte.toUnsignedInt(data[typeOffset + 1])));
  }

  /**
   * To be documented.
   *
   * @param data To be documented.
   * @param typeOffset To be documented.
   * @return To be documented.
   */
  public static int getInt(final byte[] data, final int typeOffset) {
    return (Byte.toUnsignedInt(data[typeOffset]) << 24) | (Byte.toUnsignedInt(data[typeOffset + 1]) << 16) | (Byte.toUnsignedInt(data[typeOffset + 2]) << 8) | Byte.toUnsignedInt(data[typeOffset + 3]);
  }

  /**
   * To be documented.
   *
   * @param data To be documented.
   * @param typeOffset To be documented.
   * @return To be documented.
   */
  public static long getIntAsLong(final byte[] data, final int typeOffset) {
    return Integer.toUnsignedLong((Byte.toUnsignedInt(data[typeOffset]) << 24) | (Byte.toUnsignedInt(data[typeOffset + 1]) << 16) | (Byte.toUnsignedInt(data[typeOffset + 2]) << 8) | Byte.toUnsignedInt(data[typeOffset + 3]));
  }
}

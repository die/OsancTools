package com.github.waifu.packets;

/**
 * Generic utility class for utility methods.
 */
public final class Util {

  /**
   * To be documented.
   */
  private Util() {

  }

  /**
   * Fast method to return an integer from the first 4 bytes of a byte array.
   *
   * @param bytes The byte array to extract the integer from.
   * @return The integer converted from the first 4 bytes of an array.
   */
  public static int decodeInt(final byte[] bytes) {
    return (Byte.toUnsignedInt(bytes[0]) << 24) | (Byte.toUnsignedInt(bytes[1]) << 16) | (Byte.toUnsignedInt(bytes[2]) << 8) | Byte.toUnsignedInt(bytes[3]);
  }

  /**
   * Receives a hex string and returns it in byte array format.
   *
   * @param hex String of hex data where a pair of numbers represents a byte.
   * @return Returns a byte array converted from the passed hex string.
   */
  public static byte[] hexStringToByteArray(final String hex) {
    final int l = hex.length();
    final byte[] data = new byte[l / 2];
    for (int i = 0; i < l; i += 2) {
      data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
    }
    return data;
  }

  /**
   * String output of all objects in the list.
   *
   * @param list List all objects to be printed.
   * @return String output of the list.
   */
  public static String showAll(final Object[] list) {
    final StringBuilder sb = new StringBuilder();
    for (final Object o : list) {
      sb.append(o);
    }
    return sb.toString();
  }
}

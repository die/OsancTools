package com.github.waifu.packets.packetcapture.encryption;

/**
 * Contains known Rc4 Keys.
 */
public final class RotmgRc4Keys {

  /**
   * To be documented.
   */
  private RotmgRc4Keys() {

  }

  /**
   * Incoming packet key from the servers to the client.
   */
  public static final String INCOMING_STRING = "c91d9eec420160730d825604e0";
  /**
   * Outgoing packet key from the client to the servers.
   */
  public static final String OUTGOING_STRING = "5a4d2016bc16dc64883194ffd9";
}

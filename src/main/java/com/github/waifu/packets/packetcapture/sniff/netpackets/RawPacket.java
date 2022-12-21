package com.github.waifu.packets.packetcapture.sniff.netpackets;

import java.time.Instant;
import java.util.Arrays;

/**
 * Raw packet constructor for retrieving packets of the wire.
 */
public class RawPacket {

  /**
   * To be documented.
   */
  private final Instant instant;
  /**
   * To be documented.
   */
  private final int payloadSize;
  /**
   * To be documented.
   */
  private final byte[] payload;

  /**
   * To be documented.
   *
   * @param data To be documented.
   * @param ins To be documented.
   */
  public RawPacket(final byte[] data, final Instant ins) {
    instant = ins;
    payloadSize = data.length;
    payload = data;
  }

  /**
   * To be documented.
   *
   * @param rawData To be documented.
   * @param ts To be documented.
   * @return To be documented.
   */
  public static RawPacket newPacket(final byte[] rawData, final Instant ts) {
    return new RawPacket(rawData, ts);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public byte[] getPayload() {
    return payload;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public EthernetPacket getNewEthernetPacket() {
    return new EthernetPacket(payload, this);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  @Override
  public String toString() {
    return "RawPacket{" + "\n instant=" + instant + "\n payloadSize=" + payloadSize + "\n payload=" + Arrays.toString(payload);
  }
}

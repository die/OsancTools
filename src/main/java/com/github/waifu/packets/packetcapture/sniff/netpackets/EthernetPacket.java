package com.github.waifu.packets.packetcapture.sniff.netpackets;

import java.util.Arrays;


/**
 * Packet building inspired by work done by Pcap4j (https://github.com/kaitoy/pcap4j)
 * and Network programming in Linux (http://tcpip.marcolavoie.ca/ip.html)
 *
 * <p>Ethernet packet constructor.
 */
public class EthernetPacket {

  /**
   * To be documented.
   */
  public static final int IEEE802_3_MAX_LENGTH = 1500;
  /**
   * To be documented.
   */
  public static final int MAC_SIZE_IN_BYTES = 6;
  /**
   * To be documented.
   */
  private static final int ETHERNET_HEADER_SIZE = 14;
  /**
   * To be documented.
   */
  private static final int ETHERNET_HEADER_SIZE_WITH_Q802_1HEADER = 18;
  /**
   * To be documented.
   */
  private static final int DST_ADDR_OFFSET_ETHER = 0;
  /**
   * To be documented.
   */
  private static final int SRC_ADDR_OFFSET_ETHER = 6;
  /**
   * To be documented.
   */
  private static final int TYPE_OFFSET = 12;
  /**
   * To be documented.
   */
  private static final int Q802_1HEADER_SIZE = 4;
  /**
   * To be documented.
   */
  private static final int Q802_1HEADER_TYPE_OFFSET = 16;

  /**
   * To be documented.
   */
  private final RawPacket rawPacket;
  /**
   * To be documented.
   */
  private final byte[] macDest;
  /**
   * To be documented.
   */
  private final byte[] macSrc;
  /**
   * To be documented.
   */
  private final int etherType;
  /**
   * To be documented.
   */
  private final int etherRawPayloadOffset;
  /**
   * To be documented.
   */
  private final int payloadSize;
  /**
   * To be documented.
   */
  private final byte[] q8021Header;
  /**
   * To be documented.
   */
  private final byte[] payload;

  /**
   * To be documented.
   *
   * @param data To be documented.
   * @param packet To be documented.
   */
  public EthernetPacket(final byte[] data, final RawPacket packet) {
    rawPacket = packet;
    macDest = UtilNetPackets.getBytes(data, DST_ADDR_OFFSET_ETHER, MAC_SIZE_IN_BYTES);
    macSrc = UtilNetPackets.getBytes(data, SRC_ADDR_OFFSET_ETHER, MAC_SIZE_IN_BYTES);
    final int type = UtilNetPackets.getShort(data, TYPE_OFFSET);

    if (type == 0x8100) {
      etherType = UtilNetPackets.getShort(data, Q802_1HEADER_TYPE_OFFSET);
      etherRawPayloadOffset = ETHERNET_HEADER_SIZE_WITH_Q802_1HEADER;
      q8021Header = UtilNetPackets.getBytes(data, TYPE_OFFSET, Q802_1HEADER_SIZE);
    } else {
      etherType = type;
      q8021Header = new byte[0];
      etherRawPayloadOffset = ETHERNET_HEADER_SIZE;
    }

    final int currentPayloadSize = data.length - etherRawPayloadOffset;
    if (etherType <= IEEE802_3_MAX_LENGTH) {
      if (etherType > currentPayloadSize) {
        payloadSize = currentPayloadSize;
      } else {
        payloadSize = etherType;
      }
    } else {
      payloadSize = currentPayloadSize;
    }
    payload = UtilNetPackets.getBytes(data, etherRawPayloadOffset, payloadSize);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Ip4Packet getNewIp4Packet() {
    if (payload != null && etherType == 0x800) {
      return new Ip4Packet(payload, this);
    }
    return null;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  @Override
  public String toString() {
    return "EthernetPacket{" + "\n macDest=" + macString(macDest) + "\n macSrc=" + macString(macSrc) + "\n etherType=" + String.format("%04x", etherType) + "\n etherPayloadOffset=" + etherRawPayloadOffset + "\n payloadEther=" + Arrays.toString(payload);
  }

  private String macString(final byte[] mac) {
    final StringBuilder sb = new StringBuilder();
    boolean f = false;
    for (final byte b : mac) {
      if (f) {
        sb.append(":");
      }
      final String str = String.format("%02x", b);
      sb.append(str);
      f = true;
    }
    return sb.toString();
  }
}

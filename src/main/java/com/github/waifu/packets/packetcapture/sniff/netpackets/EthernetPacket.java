package com.github.waifu.packets.packetcapture.sniff.netpackets;

import java.util.Arrays;


/**
 * Packet building inspired by work done by Pcap4j (https://github.com/kaitoy/pcap4j)
 * and Network programming in Linux (http://tcpip.marcolavoie.ca/ip.html)
 * <p>
 * Ethernet packet constructor.
 */
public class EthernetPacket {

  public static final int IEEE802_3_MAX_LENGTH = 1500;
  public static final int MAC_SIZE_IN_BYTES = 6;
  private static final int ETHERNET_HEADER_SIZE = 14;
  private static final int ETHERNET_HEADER_SIZE_WITH_Q802_1HEADER = 18;
  private static final int DST_ADDR_OFFSET_ETHER = 0;
  private static final int SRC_ADDR_OFFSET_ETHER = 6;
  private static final int TYPE_OFFSET = 12;
  private static final int Q802_1HEADER_SIZE = 4;
  private static final int Q802_1HEADER_TYPE_OFFSET = 16;

  private final RawPacket rawPacket;
  private final byte[] macDest;
  private final byte[] macSrc;
  private final int etherType;
  private final int etherRawPayloadOffset;
  private final int payloadSize;
  private final byte[] Q802_1Header;
  private final byte[] payload;

  public EthernetPacket(byte[] data, RawPacket packet) {
    rawPacket = packet;
    macDest = UtilNetPackets.getBytes(data, DST_ADDR_OFFSET_ETHER, MAC_SIZE_IN_BYTES);
    macSrc = UtilNetPackets.getBytes(data, SRC_ADDR_OFFSET_ETHER, MAC_SIZE_IN_BYTES);
    int type = UtilNetPackets.getShort(data, TYPE_OFFSET);

    if (type == 0x8100) {
      etherType = UtilNetPackets.getShort(data, Q802_1HEADER_TYPE_OFFSET);
      etherRawPayloadOffset = ETHERNET_HEADER_SIZE_WITH_Q802_1HEADER;
      Q802_1Header = UtilNetPackets.getBytes(data, TYPE_OFFSET, Q802_1HEADER_SIZE);
    } else {
      etherType = type;
      Q802_1Header = new byte[0];
      etherRawPayloadOffset = ETHERNET_HEADER_SIZE;
    }

    int currentPayloadSize = data.length - etherRawPayloadOffset;
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

  public Ip4Packet getNewIp4Packet() {
    if (payload != null && etherType == 0x800) {
      return new Ip4Packet(payload, this);
    }
    return null;
  }

  @Override
  public String toString() {
    return "EthernetPacket{" + "\n macDest=" + macString(macDest) + "\n macSrc=" + macString(macSrc) + "\n etherType=" + String.format("%04x", etherType) + "\n etherPayloadOffset=" + etherRawPayloadOffset + "\n payloadEther=" + Arrays.toString(payload);
  }

  private String macString(byte[] mac) {
    StringBuilder sb = new StringBuilder();
    boolean f = false;
    for (byte b : mac) {
      if (f) sb.append(":");
      String str = String.format("%02x", b);
      sb.append(str);
      f = true;
    }
    return sb.toString();
  }
}

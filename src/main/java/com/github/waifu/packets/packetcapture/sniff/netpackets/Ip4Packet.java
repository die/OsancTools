package com.github.waifu.packets.packetcapture.sniff.netpackets;

import java.util.Arrays;

/**
 * Packet building inspired by work done by Pcap4j (https://github.com/kaitoy/pcap4j)
 * and Network programming in Linux (http://tcpip.marcolavoie.ca/ip.html)
 *
 * <p>Ip4 packet constructor.
 */
public class Ip4Packet {

  /**
   * To be documented.
   */
  private static final int VERSION_AND_IHL_OFFSET = 0;
  /**
   * To be documented.
   */
  private static final int TOS_OFFSET = 1;
  /**
   * To be documented.
   */
  private static final int TOTAL_LENGTH_OFFSET = 2;
  /**
   * To be documented.
   */
  private static final int IDENTIFICATION_OFFSET = 4;
  /**
   * To be documented.
   */
  private static final int FLAGS_AND_FRAGMENT_OFFSET_OFFSET = 6;
  /**
   * To be documented.
   */
  private static final int TTL_OFFSET = 8;
  /**
   * To be documented.
   */
  private static final int PROTOCOL_OFFSET = 9;
  /**
   * To be documented.
   */
  private static final int HEADER_CHECKSUM_OFFSET = 10;
  /**
   * To be documented.
   */
  private static final int IP_ADDRESS_SIZE = 4;
  /**
   * To be documented.
   */
  private static final int SRC_ADDR_OFFSET_IP = 12;
  /**
   * To be documented.
   */
  private static final int DST_ADDR_OFFSET_IP = 16;
  /**
   * To be documented.
   */
  private static final int OPTIONS_OFFSET_IP = 20;
  /**
   * To be documented.
   */
  private static final int MIN_IPV4_HEADER_SIZE = 20;
  /**
   * To be documented.
   */
  private final EthernetPacket ethernetPacket;
  /**
   * To be documented.
   */
  private final byte[] rawData;
  /**
   * To be documented.
   */
  private final int version;
  /**
   * To be documented.
   */
  private final int ihl;
  /**
   * To be documented.
   */
  private final int precedence;
  /**
   * To be documented.
   */
  private final int tos;
  /**
   * To be documented.
   */
  private final boolean mbz;
  /**
   * To be documented.
   */
  private final int totalLength;
  /**
   * To be documented.
   */
  private final int identification;
  /**
   * To be documented.
   */
  private final boolean reservedFlag;
  /**
   * To be documented.
   */
  private final boolean dontFragmentFlag;
  /**
   * To be documented.
   */
  private final boolean moreFragmentFlag;
  /**
   * To be documented.
   */
  private final int fragmentOffset;
  /**
   * To be documented.
   */
  private final int ttl;
  /**
   * To be documented.
   */
  private final int protocol;
  /**
   * To be documented.
   */
  private final int headerChecksum;
  /**
   * To be documented.
   */
  private final byte[] srcAddr;
  /**
   * To be documented.
   */
  private final byte[] dstAddr;
  /**
   * To be documented.
   */
  private final byte[] optionsIP;
  /**
   * To be documented.
   */
  private final int payloadLength;
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
  public Ip4Packet(final byte[] data, final EthernetPacket packet) {
    ethernetPacket = packet;
    rawData = data;
    final int versionAndIhl = UtilNetPackets.getByte(data, VERSION_AND_IHL_OFFSET);
    version = (byte) ((versionAndIhl & 0xF0) >> 4);
    ihl = (byte) (versionAndIhl & 0x0F);

    final byte tosByte = (byte) UtilNetPackets.getByte(data, TOS_OFFSET);
    precedence = (byte) ((tosByte & 0xE0) >> 5);
    tos = (0x0F & (tosByte >> 1));
    mbz = (tosByte & 0x01) != 0;
    totalLength = UtilNetPackets.getShort(data, TOTAL_LENGTH_OFFSET);
    identification = UtilNetPackets.getShort(data, IDENTIFICATION_OFFSET);

    final short flagsAndFragmentOffset = (short) UtilNetPackets.getShort(data, FLAGS_AND_FRAGMENT_OFFSET_OFFSET);
    reservedFlag = (flagsAndFragmentOffset & 0x8000) != 0;
    dontFragmentFlag = (flagsAndFragmentOffset & 0x4000) != 0;
    moreFragmentFlag = (flagsAndFragmentOffset & 0x2000) != 0;
    fragmentOffset = (short) (flagsAndFragmentOffset & 0x1FFF);

    ttl = UtilNetPackets.getByte(data, TTL_OFFSET);
    protocol = UtilNetPackets.getByte(data, PROTOCOL_OFFSET);
    headerChecksum = UtilNetPackets.getShort(data, HEADER_CHECKSUM_OFFSET);
    srcAddr = UtilNetPackets.getBytes(data, SRC_ADDR_OFFSET_IP, IP_ADDRESS_SIZE);
    dstAddr = UtilNetPackets.getBytes(data, DST_ADDR_OFFSET_IP, IP_ADDRESS_SIZE);

    int headerLengthIp = ihl * 4;
    if (headerLengthIp < 20) {
      headerLengthIp = 20;
    }

    if (headerLengthIp != OPTIONS_OFFSET_IP) {
      optionsIP = UtilNetPackets.getBytes(data, OPTIONS_OFFSET_IP, headerLengthIp - MIN_IPV4_HEADER_SIZE);
    } else {
      optionsIP = new byte[0];
    }

    final int dataLength = data.length - headerLengthIp;
    int length = totalLength - headerLengthIp;
    if (length > dataLength || length < 0) {
      length = dataLength;
    }
    payloadLength = length;

    if (payloadLength != 0) {
      payload = UtilNetPackets.getBytes(data, headerLengthIp, payloadLength);
    } else {
      payload = new byte[0];
    }
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getVersion() {
    return version;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getIhl() {
    return ihl;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getTotalLength() {
    return totalLength;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getIdentification() {
    return identification;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public boolean isDontFragmentFlag() {
    return dontFragmentFlag;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public boolean isMoreFragmentFlag() {
    return moreFragmentFlag;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getFragmentOffset() {
    return fragmentOffset;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public byte[] getSrcAddr() {
    return srcAddr;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getPayloadLength() {
    return payloadLength;
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
  public TcpPacket getNewTcpPacket() {
    if (payload != null && protocol == 6) {
      return new TcpPacket(payload, payloadLength, this);
    }
    return null;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public EthernetPacket getEthernetPacket() {
    return ethernetPacket;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  @Override
  public String toString() {
    return "Ip4Packet{" + "\n version=" + version + "\n ihl=" + ihl + "\n precedence=" + precedence + "\n tos=" + tos + "\n mbz=" + mbz + "\n totalLength=" + totalLength + "\n identification=" + identification + "\n reservedFlag=" + reservedFlag + "\n dontFragmentFlag=" + dontFragmentFlag + "\n moreFragmentFlag=" + moreFragmentFlag + "\n fragmentOffset=" + fragmentOffset + "\n ttl=" + ttl + "\n protocol=" + protocol + "\n headerChecksum=" + headerChecksum + "\n srcAddr=" + ipToString(srcAddr) + "\n dstAddr=" + ipToString(dstAddr) + "\n optionsIP=" + Arrays.toString(optionsIP) + "\n dataLength=" + payloadLength + "\n IPdata=" + Arrays.toString(UtilNetPackets.getBytes(rawData, 0, ihl * 4)) + "\n payloadIP=" + Arrays.toString(payload);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public byte[] rawData() {
    return rawData;
  }

  /**
   * To be documented.
   *
   * @param ip To be documented.
   * @return To be documented.
   */
  private String ipToString(final byte[] ip) {
    final StringBuilder sb = new StringBuilder();
    boolean f = false;
    for (final byte b : ip) {
      if (f) {
        sb.append(".");
      }
      final String str = String.format("%d", Byte.toUnsignedInt(b));
      sb.append(str);
      f = true;
    }
    return sb.toString();
  }
}

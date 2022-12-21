package com.github.waifu.packets.packetcapture.sniff.netpackets;

import java.util.Arrays;

/**
 * Packet building inspired by work done by Pcap4j (https://github.com/kaitoy/pcap4j)
 * and Network programming in Linux (http://tcpip.marcolavoie.ca/ip.html)
 *
 * <p>Tcp packet constructor.
 */
public class TcpPacket {

  /**
   * To be documented.
   */
  private static final int SRC_PORT_OFFSET = 0;
  /**
   * To be documented.
   */
  private static final int DST_PORT_OFFSET = 2;
  /**
   * To be documented.
   */
  private static final int SEQUENCE_NUMBER_OFFSET = 4;
  /**
   * To be documented.
   */
  private static final int ACKNOWLEDGMENT_NUMBER_OFFSET = 8;
  /**
   * To be documented.
   */
  private static final int DATA_OFFSET_BITS_OFFSET = 12;
  /**
   * To be documented.
   */
  private static final int CONTROL_BITS_OFFSET = 13;
  /**
   * To be documented.
   */
  private static final int WINDOW_OFFSET = 14;
  /**
   * To be documented.
   */
  private static final int CHECKSUM_OFFSET = 16;
  /**
   * To be documented.
   */
  private static final int URGENT_POINTER_OFFSET = 18;
  /**
   * To be documented.
   */
  private static final int OPTIONS_OFFSET_TCP = 20;
  /**
   * To be documented.
   */
  private static final int MIN_TCP_HEADER_SIZE = 20;
  /**
   * To be documented.
   */
  private final Ip4Packet ip4Packet;
  /**
   * To be documented.
   */
  private final byte[] rawData;
  /**
   * To be documented.
   */
  private final int srcPort;
  /**
   * To be documented.
   */
  private final int dstPort;
  /**
   * To be documented.
   */
  private final long sequenceNumber;
  /**
   * To be documented.
   */
  private final long acknowledgmentNumber;
  /**
   * To be documented.
   */
  private final int dataOffset;
  /**
   * To be documented.
   */
  private final int reserved;
  /**
   * To be documented.
   */
  private final boolean urg;
  /**
   * To be documented.
   */
  private final boolean ack;
  /**
   * To be documented.
   */
  private final boolean psh;
  /**
   * To be documented.
   */
  private final boolean rst;
  /**
   * To be documented.
   */
  private final boolean syn;
  /**
   * To be documented.
   */
  private final boolean fin;
  /**
   * To be documented.
   */
  private final int window;
  /**
   * To be documented.
   */
  private final int checksum;
  /**
   * To be documented.
   */
  private final int urgentPointer;
  /**
   * To be documented.
   */
  private final byte[] optionsTCP;
  /**
   * To be documented.
   */
  private final byte[] payload;
  /**
   * To be documented.
   */
  private final int payloadSize;

  /**
   * To be documented.
   *
   * @param data To be documented.
   * @param length To be documented.
   * @param packet To be documented.
   */
  public TcpPacket(final byte[] data, final int length, final Ip4Packet packet) {
    ip4Packet = packet;
    rawData = data;
    srcPort = UtilNetPackets.getShort(data, SRC_PORT_OFFSET);
    dstPort = UtilNetPackets.getShort(data, DST_PORT_OFFSET);
    sequenceNumber = UtilNetPackets.getIntAsLong(data, SEQUENCE_NUMBER_OFFSET);
    acknowledgmentNumber = UtilNetPackets.getIntAsLong(data, ACKNOWLEDGMENT_NUMBER_OFFSET);

    final int sizeControlBits = UtilNetPackets.getByte(data, DATA_OFFSET_BITS_OFFSET);
    dataOffset = (sizeControlBits & 0xF0) >> 4;
    reserved = sizeControlBits & 0xF;

    final int reservedAndControlBits = UtilNetPackets.getByte(data, CONTROL_BITS_OFFSET);
    urg = (reservedAndControlBits & 0x0020) != 0;
    ack = (reservedAndControlBits & 0x0010) != 0;
    psh = (reservedAndControlBits & 0x0008) != 0;
    rst = (reservedAndControlBits & 0x0004) != 0;
    syn = (reservedAndControlBits & 0x0002) != 0;
    fin = (reservedAndControlBits & 0x0001) != 0;

    window = UtilNetPackets.getShort(data, WINDOW_OFFSET);
    checksum = UtilNetPackets.getShort(data, CHECKSUM_OFFSET);
    urgentPointer = UtilNetPackets.getShort(data, URGENT_POINTER_OFFSET);

    int headerLengthTcp = (0xFF & dataOffset) * 4;
    if (headerLengthTcp < 20) {
      headerLengthTcp = 20;
    }

    if (headerLengthTcp != OPTIONS_OFFSET_TCP) {
      optionsTCP = UtilNetPackets.getBytes(data, OPTIONS_OFFSET_TCP, headerLengthTcp - MIN_TCP_HEADER_SIZE);
    } else {
      optionsTCP = new byte[0];
    }

    payloadSize = length - headerLengthTcp;
    if (payloadSize != 0) {
      payload = UtilNetPackets.getBytes(data, headerLengthTcp, payloadSize);
    } else {
      payload = new byte[0];
    }
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getSrcPort() {
    return srcPort;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getDstPort() {
    return dstPort;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public long getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public boolean isRst() {
    return rst;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public boolean isSyn() {
    return syn;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public boolean isResetBit() {
    return rst || syn || fin;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getWindow() {
    return window;
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
  public int getPayloadSize() {
    return payloadSize;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Ip4Packet getIp4Packet() {
    return ip4Packet;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  @Override
  public String toString() {
    return "TcpPacket{" + "\n srcPort=" + srcPort + "\n dstPort=" + dstPort + "\n sequenceNumber=" + sequenceNumber + "\n acknowledgmentNumber=" + acknowledgmentNumber + "\n dataOffset=" + dataOffset + "\n reserved=" + reserved + "\n urg=" + urg + "\n ack=" + ack + "\n psh=" + psh + "\n rst=" + rst + "\n syn=" + syn + "\n fin=" + fin + "\n window=" + window + "\n checksum=" + checksum + "\n urgentPointer=" + urgentPointer + "\n optionsTCP=" + Arrays.toString(optionsTCP) + "\n TCPdata=" + Arrays.toString(UtilNetPackets.getBytes(rawData, 0, (0xFF & dataOffset) * 4)) + "\n payloadTCP=" + Arrays.toString(payload) + "\n payloadSize=" + payloadSize;
  }
}

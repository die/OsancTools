package com.github.waifu.packets.packetcapture.pconstructor;

import com.github.waifu.packets.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Rotmg packet constructor appending bytes into a packet
 * based on the size at the header of each sequence.
 */
public class RotmgPacketConstructor {

  /**
   * To be documented.
   */
  private final int size = 200000;
  /**
   * To be documented.
   */
  private final PacketConstructor packetConstructor;
  /**
   * To be documented.
   */
  private final byte[] bytes = new byte[size];
  /**
   * To be documented.
   */
  private int index;
  /**
   * To be documented.
   */
  private int packetSize = 0;

  /**
   * ROMGPacketConstructor needing the PacketConstructor class
   * to send correctly stitched packets.
   *
   * @param pc PacketConstructor class needed to
   *           send correctly stitched packets.
   */
  public RotmgPacketConstructor(final PacketConstructor pc) {
    packetConstructor = pc;
  }

  /**
   * Build method used to stitch individual bytes in the data
   * in the TCP packets according to
   * specified size at the header of the data.
   * Only start listen after the next packet less
   * than MTU(maximum transmission unit packet) is received.
   *
   * @param data data inside TCP packet.
   */
  public void build(final byte[] data) {
    for (final byte b : data) {
      bytes[index++] = b;
      final int minIndex = 4;
      if (index >= minIndex) {
        if (packetSize == 0) {
          packetSize = Util.decodeInt(bytes);
          if (packetSize > size) {
            packetSize = 0;
            return;
          }
        }

        if (index == packetSize) {
          index = 0;
          final byte[] realmPacket = Arrays.copyOfRange(bytes, 0, packetSize);
          packetSize = 0;
          final ByteBuffer byteBuffer = ByteBuffer.wrap(realmPacket);
          final ByteBuffer packetData = byteBuffer.order(ByteOrder.BIG_ENDIAN);
          packetConstructor.packetReceived(packetData);
        }
      }
    }
  }

  /**
   * Resets the byte index and the packet size.
   */
  public void reset() {
    index = 0;
    packetSize = 0;
  }
}

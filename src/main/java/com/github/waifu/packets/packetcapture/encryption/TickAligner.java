package com.github.waifu.packets.packetcapture.encryption;

import com.github.waifu.packets.PacketType;
import com.github.waifu.packets.Util;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * The RC4 cipher can be aligned using Tick packets. The packets send ticks regularly
 * from the server incremented by one between each tick packet. The aligner first aligns
 * the RC4 cipher using a simple brute force check from two consecutive tick packets.
 * If the tick counts miss matches relative to next tick packet, it clears all alignments
 * and re-aligns.
 */
public class TickAligner {

  /**
   * To be documented.
   */
  private final Rc4 rc4;
  /**
   * To be documented.
   */
  private boolean synced = false;
  /**
   * To be documented.
   */
  private int packetBytes = 0;
  /**
   * To be documented.
   */
  private byte[] firstTick;
  /**
   * To be documented.
   */
  private int currentTick;

  /**
   * Tick aligner constructor to a RC4 cipher.
   *
   * @param r The cipher to align.
   */
  public TickAligner(final Rc4 r) {
    rc4 = r;
  }

  /**
   * A comprehensive method that keeps track of tick packets and ensures each tick paket
   * is aligned correctly against the CURRENT_TICK index counter. When tick packets arrive
   * they are expected to have the same number as CURRENT_TICK + 1. If miss match is found
   * an error is thrown. The cipher is reset and a brute force method is used to re-align
   * the cipher with two consecutive tick packets.
   *
   * @param encryptedData Data of the current receiving packet.
   * @param size          Size of the packet data.
   * @param type          Type of the packet
   * @return Returns the state of the cipher alignment being synced.
   */
  public boolean checkRC4Alignment(final ByteBuffer encryptedData, final int size, final byte type) {
    if (synced) {
      if (type == PacketType.NEWTICK.getIndex()) {
        final byte[] duplicate = Arrays.copyOfRange(encryptedData.array(), 5, encryptedData.capacity());
        rc4.fork().decrypt(duplicate);
        currentTick++;
        final int tick = Util.decodeInt(duplicate);
        if (currentTick != tick) {
          rc4.reset();
          synced = false;
          firstTick = null;
        }
      }
      if (synced) {
        packetBytes += size - 5;
      }
    }

    if (!synced) {
      if (type == PacketType.NEWTICK.getIndex()) {
        final byte[] tick = Arrays.copyOfRange(encryptedData.array(), 5, 5 + 4);
        if (firstTick != null) {
          rc4.reset();
          System.out.println("Packet bytes between sync packets: " + packetBytes);
          final int i = Rc4Aligner.syncCipher(rc4, firstTick, tick, packetBytes);
          if (i != -1) {
            synced = true;
            rc4.skip(packetBytes).decrypt(tick);
            rc4.skip(size - 5 - 4);
            currentTick = Util.decodeInt(tick);
            System.out.println("Synced. offset: " + i + " tick: " + currentTick);
          }
          firstTick = null;
          packetBytes = 0;
        } else {
          firstTick = tick;
          packetBytes = size - 5;
        }
      } else {
        packetBytes += size - 5;
      }
      return false;
    }
    return true;
  }

  /**
   * A reset method for resenting the tick counter. Called when changing game sessions.
   */
  public void reset() {
    currentTick = -1;
  }
}

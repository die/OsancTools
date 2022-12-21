package com.github.waifu.packets.packetcapture.pconstructor;

import com.github.waifu.packets.packetcapture.PacketProcessor;
import com.github.waifu.packets.packetcapture.encryption.Rc4;
import com.github.waifu.packets.packetcapture.encryption.TickAligner;
import java.nio.ByteBuffer;

/**
 * Packet constructor sending the TCP packets to the stream
 * constructor that in turn sends the ordered packets to the
 * rotmg constructor. The packets are then sent back to be decrypted with
 * an RC4 cipher. If a tick packet is sent an aligner is used to
 * check the RC4 alignment using a simple increment from the
 * previous tick packet. If a new session packet is received then it
 * resets the cipher.
 */
public class PacketConstructor {

  /**
   * To be documented.
   */
  private final Rc4 rc4Cipher;
  /**
   * To be documented.
   */
  private final PacketProcessor packetProcessor;
  /**
   * To be documented.
   */
  private final RotmgPacketConstructor rotmgConst;
  /**
   * To be documented.
   */
  private final TickAligner tickAligner;
  /**
   * To be documented.
   */
  private boolean firstNonLargePacket;

  /**
   * Packet constructor with specific cipher.
   *
   * @param pp Parent class to send constructed packets back too.
   * @param r  The cipher used to decode packets.
   */
  public PacketConstructor(final PacketProcessor pp, final Rc4 r) {
    packetProcessor = pp;
    rc4Cipher = r;
    rotmgConst = new RotmgPacketConstructor(this);
    tickAligner = new TickAligner(rc4Cipher);
  }

  /**
   * Build method to send the packets retrieved by the sniffer for constructing.
   *
   * @param data Raw packet data incoming from the net tap.
   */
  public void build(final byte[] data) {
    if (firstNonLargePacket) {  // start listening after a non-max packet
      // prevents errors in pSize.
      final int length = 1460;
      if (data.length < length) {
        firstNonLargePacket = false;
      }
      return;
    }
    rotmgConst.build(data);
  }

  /**
   * Rotmg packets constructed by the rotmg constructor are sent back after they
   * are correctly assembled. If the cipher is correctly aligned
   * then the packets are decrypted and sent to the packet processor.
   * If the cipher isn't aligned then the Tick packets are
   * used to re-align the cipher.
   *
   * @param encryptedData Encrypted packets for aligning cipher and decryption.
   */
  public void packetReceived(final ByteBuffer encryptedData) {
    try {
      final int size = encryptedData.getInt();
      final byte type = encryptedData.get();

      final boolean sync = tickAligner.checkRC4Alignment(encryptedData, size, type);

      if (sync) {
        final int offset = 5;
        // encryptedData is decrypted in this method
        rc4Cipher.decrypt(offset, encryptedData);
        packetProcessor.processPackets(type, size, encryptedData);
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Reset method to reset both cipher and the aligner tick counter
   * when a reset packet is received.
   */
  public void reset() {
    rc4Cipher.reset();
    tickAligner.reset();
    rotmgConst.reset();
  }

  /**
   * Reset when starting the sniffer. Given the program can start
   * at any time then any packet which follows a non-max packet
   * will most likely contain the rotmg-packet header which contains the
   * packet size. If ignoring this flag, any random
   * MTU (maximum transmission unit packet) packet in
   * a sequence of concatenated packets could produce a random packet
   * size from its first 4 bytes resulting in a de-sync.
   */
  public void startResets() {
    firstNonLargePacket = true;
  }
}

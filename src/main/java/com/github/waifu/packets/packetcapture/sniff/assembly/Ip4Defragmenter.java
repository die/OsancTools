package com.github.waifu.packets.packetcapture.sniff.assembly;

import com.github.waifu.packets.packetcapture.sniff.netpackets.Ip4Packet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Ip4 de-fragmenter class to re-assemble ip4 packets that have
 * been fragmented through sertan routes over the net.
 *
 * <p>Implementation based on article:
 * https://packetpushers.net/ip-fragmentation-in-detail/
 * </p>
 * todo: test this class properly. WARNING UNTESTED!
 */
public final class Ip4Defragmenter {

  /**
   * To be documented.
   */
  private Ip4Defragmenter() {

  }

  /**
   * To be documented.
   */
  private static final HashMap<Integer, ArrayList<Ip4Packet>> FRAGMENTS = new HashMap<>();

  /**
   * Main method used to de-fragment packets. If the packet doesn't need re-assembly
   * then it returns the packet without any changes. If the packet however is part
   * of a fragmented ip4 packet then it stores it and checks if all other fragments
   * of the same id is present. If all parts the fragmented packet is present then
   * it re-assembles them into the original ip4 packet and returns it. Otherwise,
   * returns null.
   *
   * @param ip4packet Ip4 packet that needs to be checked if it needs to be re-assembled.
   * @return de-fragmented packet if it needs to be re-assembled. Otherwise, returns the same ip4 packet.
   */
  public static Ip4Packet defragment(final Ip4Packet ip4packet) {
    if (ip4packet == null || ip4packet.isDontFragmentFlag() || (!ip4packet.isMoreFragmentFlag() && ip4packet.getFragmentOffset() == 0)) {
      return ip4packet;
    }
    final int id = ip4packet.getIdentification();
    FRAGMENTS.computeIfAbsent(id, k -> new ArrayList<>(5)).add(ip4packet);
    return assemble(id, ip4packet);
  }

  /**
   * Assembler that puts the packets back into the original Ip4 packet before it was fragmented.
   *
   * @param id The id of the packet
   * @param ip4packet Ip4Packet object
   * @return Assembled packet if it needs to be re-assemble or returns null if all fragments haven't arrived.
   */
  private static Ip4Packet assemble(final int id, final Ip4Packet ip4packet) {
    Ip4Packet head = null;
    Ip4Packet tail = null;
    final List<Ip4Packet> frags = FRAGMENTS.get(id);
    int math = 0;
    for (final Ip4Packet ip : frags) {
      if (!ip.isMoreFragmentFlag()) {
        tail = ip;
        math += ip.getFragmentOffset() * 8;
        continue;
      } else if (ip.getFragmentOffset() == 0) {
        head = ip;
      }
      math -= ip.getPayloadLength();
    }
    if (math != 0 || head == null || tail == null) {
      return null;
    }

    final byte[] data = new byte[head.getIhl() * 4 + tail.getFragmentOffset() * 8 + tail.getPayloadLength()];

    System.arraycopy(head.rawData(), 0, data, 0, head.getTotalLength());
    for (final Ip4Packet ip : frags) {
      if (ip != head) {
        System.arraycopy(ip.getPayload(), 0, data, ip.getFragmentOffset() * 8, ip.getPayloadLength());
      }
    }
    FRAGMENTS.remove(id);
    return new Ip4Packet(data, ip4packet.getEthernetPacket());
  }
}

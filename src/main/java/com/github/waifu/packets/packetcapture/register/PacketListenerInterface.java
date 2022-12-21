package com.github.waifu.packets.packetcapture.register;

import com.github.waifu.packets.Packet;

/**
 * Listener interface used in the registry class matching subscribed classes to packet classes.
 *
 * @param <T>To be documented.
 */
public interface PacketListenerInterface<T extends Packet> {
  /**
   * To be documented.
   *
   * @param packet To be documented.
   */
  void process(T packet);
}

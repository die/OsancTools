package com.github.waifu.packets.packetcapture.register;

import com.github.waifu.packets.Packet;
import com.github.waifu.packets.PacketType;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The registry class is used to subscribe to either all or specific packets. If registered packets
 * are received the emit method will send an update and trigger the lambda used.
 */
public class Register {

  /**
   * To be documented.
   */
  private static Register instance = new Register();
  /**
   * To be documented.
   */
  private final HashMap<Class<? extends Packet>, ArrayList<PacketListenerInterface<Packet>>> packetListeners = new HashMap<>();

  /**
   * Emitter for sending packets to any subscriber which matches the packets the subscriber have subbed too.
   *
   * @param packet The packet being received and emitted.
   */
  public void emit(final Packet packet) {
    if (packetListeners.containsKey(packet.getClass())) {
      for (final PacketListenerInterface<Packet> processor : packetListeners.get(packet.getClass())) {
        processor.process(packet);
      }
    }

    if (packetListeners.containsKey(Packet.class)) {
      for (final PacketListenerInterface<Packet> processor : packetListeners.get(Packet.class)) {
        processor.process(packet);
      }
    }
  }

  /**
   * Register method to subscribe to packets that are being received from the network tap.
   *
   * @param type      The type of class wanting to be subscribed too.
   * @param processor The lambda needed to trigger what event should happen if packet is received.
   * @param <T>       Class type.
   */
  public <T extends Class<? extends Packet>> void register(final PacketType type, final PacketListenerInterface<Packet> processor) {
    packetListeners.computeIfAbsent(type.getPacketClass(), (a) -> new ArrayList<>()).add(processor);
  }

  /**
   * Register method to subscribe to all packets that are being received from the network tap.
   *
   * @param processor The lambda needed to trigger what event should happen if packet is received.
   * @param <T>       Class type.
   */
  public <T extends Class<? extends Packet>> void registerAll(final PacketListenerInterface<Packet> processor) {
    packetListeners.computeIfAbsent(Packet.class, (a) -> new ArrayList<>()).add(processor);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public static Register getInstance() {
    return instance;
  }

  /**
   * To be documented.
   *
   * @param instance To be documented.
   */
  public static void setInstance(final Register instance) {
    Register.instance = instance;
  }
}

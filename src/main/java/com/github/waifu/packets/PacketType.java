package com.github.waifu.packets;

import static com.github.waifu.packets.PacketType.Direction.Incoming;

import com.github.waifu.packets.Packet.PacketInterface;
import com.github.waifu.packets.incoming.MapInfoPacket;
import com.github.waifu.packets.incoming.NewTickPacket;
import com.github.waifu.packets.incoming.NotificationPacket;
import com.github.waifu.packets.incoming.RealmHeroesLeftPacket;
import com.github.waifu.packets.incoming.TextPacket;
import com.github.waifu.packets.incoming.UpdatePacket;
import java.util.HashMap;


/**
 * Packet are matched with the packet index sent as a
 * header of packets and returned.
 */
public enum PacketType {

  /**
   * To be documented.
   */
  NEWTICK(10, Incoming, NewTickPacket::new),
  /**
   * To be documented.
   */
  TEXT(44, Incoming, TextPacket::new),
  /**
   * To be documented.
   */
  UPDATE(42, Incoming, UpdatePacket::new),
  /**
   * To be documented.
   */
  NOTIFICATION(67, Incoming, NotificationPacket::new),
  /**
   * To be documented.
   */
  REALM_HERO_LEFT_MSG(84, Incoming, RealmHeroesLeftPacket::new),
  /**
   * To be documented.
   */
  MAPINFO(92, Incoming, MapInfoPacket::new);

  /**
   * To be documented.
   */
  private static final HashMap<Integer, PacketType> PACKET_TYPE = new HashMap<>();
  /**
   * To be documented.
   */
  private static final HashMap<Integer, PacketInterface> PACKET_TYPE_FACTORY = new HashMap<>();
  /**
   * To be documented.
   */
  private static final HashMap<Class, PacketType> PACKET_CLASS = new HashMap<>();

  static {
    try {
      for (final PacketType o : PacketType.values()) {
        PACKET_TYPE.put(o.index, o);
        PACKET_TYPE_FACTORY.put(o.index, o.packet);
        PACKET_CLASS.put(o.packet.factory().getClass(), o);
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * To be documented.
   */
  private final int index;
  /**
   * To be documented.
   */
  private final Direction dir;
  /**
   * To be documented.
   */
  private final PacketInterface packet;

  /**
   * To be documented.
   *
   * @param i to be documented.
   * @param d to be documented.
   * @param p to be documented.
   */
  PacketType(final int i, final Direction d, final Packet.PacketInterface p) {
    index = i;
    dir = d;
    packet = p;
  }

  /**
   * Get the enum by index.
   *
   * @param index The index.
   * @return Enum by index.
   */
  public static PacketType byOrdinal(final int index) {
    return PACKET_TYPE.get(index);
  }

  /**
   * Get the enum type by class.
   *
   * @param packet The packet to be returned the type of.
   * @return Enum type.
   */
  public static PacketType byClass(final Packet packet) {
    return PACKET_CLASS.get(packet.getClass());
  }

  /**
   * Retrieves the packet type from the PACKET_TYPE list.
   *
   * @param type Index of the packet needing to be retrieved.
   * @return Interface PacketInterface of the class being retrieved.
   */
  public static Packet.PacketInterface getPacket(final int type) {
    return PACKET_TYPE_FACTORY.get(type);
  }

  /**
   * Checks if packet type exists in the PACKET_TYPE list.
   *
   * @param type Index of the packet.
   * @return True if the packet exists in the list of packets in PACKET_TYPE.
   */
  public static boolean containsKey(final int type) {
    return PACKET_TYPE_FACTORY.containsKey(type);
  }

  /**
   * Get the index of the packet.
   *
   * @return Index of the enum.
   */
  public int getIndex() {
    return index;
  }

  /**
   * Returns the class of the enum.
   *
   * @return Class of the enum.
   */
  public Class<? extends Packet> getPacketClass() {
    return packet.factory().getClass();
  }

  /**
   * To be documented.
   */
  public enum Direction {
    /**
     * To be documented.
     */
    Incoming,
    /**
     * To be documented.
     */
    Outgoing
  }
}

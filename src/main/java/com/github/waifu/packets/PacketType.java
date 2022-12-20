package com.github.waifu.packets;

import com.github.waifu.packets.Packet.IPacket;
import com.github.waifu.packets.incoming.MapInfoPacket;
import com.github.waifu.packets.incoming.NewTickPacket;
import com.github.waifu.packets.incoming.NotificationPacket;
import com.github.waifu.packets.incoming.RealmHeroesLeftPacket;
import com.github.waifu.packets.incoming.TextPacket;
import com.github.waifu.packets.incoming.UpdatePacket;
import java.util.HashMap;

import static com.github.waifu.packets.PacketType.Direction.Incoming;

/**
 * Packet are matched with the packet index sent as a header of packets and returned.
 */
public enum PacketType {
  NEWTICK(9, Incoming, NewTickPacket::new),
  TEXT(44, Incoming, TextPacket::new),
  UPDATE(62, Incoming, UpdatePacket::new),
  NOTIFICATION(67, Incoming, NotificationPacket::new),
  REALM_HERO_LEFT_MSG(84, Incoming, RealmHeroesLeftPacket::new),
  MAPINFO(92, Incoming, MapInfoPacket::new);


  private static final HashMap<Integer, PacketType> PACKET_TYPE = new HashMap<>();
  private static final HashMap<Integer, IPacket> PACKET_TYPE_FACTORY = new HashMap<>();
  private static final HashMap<Class, PacketType> PACKET_CLASS = new HashMap<>();

  static {
    try {
      for (PacketType o : PacketType.values()) {
        PACKET_TYPE.put(o.index, o);
        PACKET_TYPE_FACTORY.put(o.index, o.packet);
        PACKET_CLASS.put(o.packet.factory().getClass(), o);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private final int index;
  private final Direction dir;
  private final IPacket packet;

  PacketType(int i, Direction d, IPacket p) {
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
  public static PacketType byOrdinal(int index) {
    return PACKET_TYPE.get(index);
  }

  /**
   * Get the enum type by class.
   *
   * @param packet The packet to be returned the type of.
   * @return Enum type.
   */
  public static PacketType byClass(Packet packet) {
    return PACKET_CLASS.get(packet.getClass());
  }

  /**
   * Retrieves the packet type from the PACKET_TYPE list.
   *
   * @param type Index of the packet needing to be retrieved.
   * @return Interface IPacket of the class being retrieved.
   */
  public static IPacket getPacket(int type) {
    return PACKET_TYPE_FACTORY.get(type);
  }

  /**
   * Checks if packet type exists in the PACKET_TYPE list.
   *
   * @param type Index of the packet.
   * @return True if the packet exists in the list of packets in PACKET_TYPE.
   */
  public static boolean containsKey(int type) {
    return PACKET_TYPE_FACTORY.containsKey(type);
  }

  /**
   * Get the index of the packet
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

  public enum Direction {
    Incoming, Outgoing
  }
}

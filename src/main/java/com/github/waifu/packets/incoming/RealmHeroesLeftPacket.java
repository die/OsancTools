package com.github.waifu.packets.incoming;

import com.github.waifu.packets.Packet;
import com.github.waifu.packets.reader.BufferReader;

/**
 * Received to tell the client how many heroes are left in the current realm.
 */
public class RealmHeroesLeftPacket extends Packet {
  /**
   * The int of heroes remaining.
   */
  private int realmHeroesLeft;

  /**
   * Deserializes the RealmHeroesLeftPacket.
   *
   * @param buffer The data of the packet in a rotmg buffer format.
   */
  @Override
  public void deserialize(final BufferReader buffer) {
    realmHeroesLeft = buffer.readInt();
  }

  /**
   * Constructs a String to show RealmHeroesLeftPacket.
   *
   * @return packet representation as String.
   */
  @Override
  public String toString() {
    return "RealmHeroesLeftPacket{"
            + "\n   realmHeroesLeft="
            + realmHeroesLeft;
  }
}

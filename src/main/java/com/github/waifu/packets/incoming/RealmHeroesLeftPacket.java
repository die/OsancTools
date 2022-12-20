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

  @Override
  public void deserialize(final BufferReader buffer) {
    realmHeroesLeft = buffer.readInt();
  }

  @Override
  public String toString() {
    return "RealmHeroesLeftPacket{" + "\n   realmHeroesLeft=" + realmHeroesLeft;
  }
}

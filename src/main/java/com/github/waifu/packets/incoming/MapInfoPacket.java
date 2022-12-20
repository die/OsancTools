package com.github.waifu.packets.incoming;

import com.github.waifu.packets.Packet;
import com.github.waifu.packets.reader.BufferReader;
import java.util.Arrays;

/**
 * Received in response to the `HelloPacket`.
 */
public class MapInfoPacket extends Packet {
  /**
   * The width of the map.
   */
  private int width;
  /**
   * The height of the map.
   */
  private int height;
  /**
   * The name of the map.
   */
  private String name;
  /**
   * > Unknown.
   */
  private String displayName;
  /**
   * The name of the realm.
   */
  private String realmName;
  /**
   * The difficulty rating of the map.
   */
  private float difficulty;
  /**
   * The seed value for the client's PRNG.
   */
  private long seed;
  /**
   * > Unknown.
   */
  private int background;
  /**
   * Whether or not players can teleport in the map.
   */
  private boolean allowPlayerTeleport;
  /**
   * > Unknown.
   */
  private boolean showDisplays;
  /**
   * unknown.
   */
  private boolean unknownBoolean;
  /**
   * The int of players allowed in this map.
   */
  private short maxPlayers;
  /**
   * The time the connection to the game was started.
   */
  private long gameOpenedTime;
  /**
   * Build version.
   */
  private String buildVersion;
  /**
   * unknown.
   */
  private int unknownInt;
  /**
   * String of all modifiers the dungeon has.
   */
  private String[] dungeonModifiers;

  /**
   * Deserializes the MapInfoPacket.
   *
   * @param buffer The data of the packet in a rotmg buffer format.
   */
  @Override
  public void deserialize(final BufferReader buffer) {
    width = buffer.readInt();
    height = buffer.readInt();
    name = buffer.readString();
    displayName = buffer.readString();
    realmName = buffer.readString();
    seed = buffer.readUnsignedInt();
    background = buffer.readInt();
    difficulty = buffer.readFloat();
    allowPlayerTeleport = buffer.readBoolean();
    showDisplays = buffer.readBoolean();
    unknownBoolean = buffer.readBoolean();
    maxPlayers = buffer.readShort();
    gameOpenedTime = buffer.readUnsignedInt();
    buildVersion = buffer.readString();
    unknownInt = buffer.readInt();
    String dungeonMods = buffer.readString();
    dungeonModifiers = dungeonMods.split(";");
  }

  /**
   * Constructs a String to show MapInfoPacket.
   *
   * @return packet representation as String.
   */
  @Override
  public String toString() {
    return "MapInfoPacket{"
            + "\n   width="
            + width
            + "\n   height="
            + height
            + "\n   name="
            + name
            + "\n   displayName="
            + displayName
            + "\n   realmName="
            + realmName
            + "\n   difficulty="
            + difficulty
            + "\n   seed="
            + seed
            + "\n   background="
            + background
            + "\n   allowPlayerTeleport="
            + allowPlayerTeleport
            + "\n   showDisplays="
            + showDisplays
            + "\n   unknownBoolean="
            + unknownBoolean
            + "\n   maxPlayers="
            + maxPlayers
            + "\n   gameOpenedTime="
            + gameOpenedTime
            + "\n   buildVersion="
            + buildVersion
            + "\n   unknownInt="
            + unknownInt
            + "\n   dungeonModifiers="
            + Arrays.toString(dungeonModifiers);
  }
}

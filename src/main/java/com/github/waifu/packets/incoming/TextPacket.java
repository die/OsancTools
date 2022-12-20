package com.github.waifu.packets.incoming;

import com.github.waifu.packets.Packet;
import com.github.waifu.packets.reader.BufferReader;

/**
 * Received when a chat message is sent by another player or NPC.
 */
public class TextPacket extends Packet {
  /**
   * The sender of the message.
   */
  private String name;
  /**
   * The object id of the sender.
   */
  private int objectId;
  /**
   * The int of stars of the sender.
   */
  private short numStars;
  /**
   * The length of time to display the chat bubble for.
   */
  private int bubbleTime;
  /**
   * The recipient of the message.
   */
  private String recipient;
  /**
   * The content of the message.
   */
  private String text;
  /**
   * > Unknown.
   */
  private String cleanText;
  /**
   * Whether or not the sender of the message is a supporter.
   */
  private boolean isSupporter;
  /**
   * The star background of the player.
   */
  private int starBackground;

  /**
   * Deserializes the TextPacket.
   *
   * @param buffer The data of the packet in a rotmg buffer format.
   */
  @Override
  public void deserialize(final BufferReader buffer) {
    name = buffer.readString();
    objectId = buffer.readInt();
    numStars = buffer.readShort();
    bubbleTime = buffer.readUnsignedByte();
    recipient = buffer.readString();
    text = buffer.readString();
    cleanText = buffer.readString();
    isSupporter = buffer.readBoolean();
    starBackground = buffer.readInt();
  }

  /**
   * Constructs a String to show TextPacket.
   *
   * @return packet representation as String.
   */
  @Override
  public String toString() {
    return "TextPacket{"
            + "\n   name="
            + name
            + "\n   objectId="
            + objectId
            + "\n   numStars="
            + numStars
            + "\n   bubbleTime="
            + bubbleTime
            + "\n   recipient="
            + recipient
            + "\n   text="
            + text
            + "\n   cleanText="
            + cleanText
            + "\n   isSupporter="
            + isSupporter
            + "\n   starBackground="
            + starBackground;
  }
}

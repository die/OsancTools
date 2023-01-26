package com.github.waifu.packets.incoming;

import com.github.waifu.packets.Packet;
import com.github.waifu.packets.data.enums.NotificationEffectType;
import com.github.waifu.packets.reader.BufferReader;

/**
 * Received when a notification is received by the player.
 */
public class NotificationPacket extends Packet {
  /**
   * Notification effect type.
   */
  private NotificationEffectType effect;
  /**
   * Unknown.
   */
  private byte extra;
  /**
   * The object id of the object which this status is for.
   */
  private int objectId;
  /**
   * The notification message.
   */
  private String message;
  /**
   * ... no idea.
   */
  private int uiExtra;
  /**
   * Position in the queue when queueing for servers.
   */
  private int queuePos;
  /**
   * The color of the notification text.
   */
  private int color;
  /**
   * The picture type of the notification.
   */
  private int pictureType;
  /**
   * unknown.
   */
  private int unknownInt1;
  /**
   * unknown.
   */
  private short unknownShort1;
  /**
   * unknown.
   */
  private int unknownInt2;
  /**
   * unknown.
   */
  private int unknownInt3;

  /**
   * Deserializes the NotificationPacket.
   *
   * @param buffer The data of the packet in a rotmg buffer format.
   */
  @Override
  public void deserialize(final BufferReader buffer) {
    effect = NotificationEffectType.byOrdinal(buffer.readByte());
    extra = buffer.readByte();

    if (effect != null) {
      switch (effect) {
        case StatIncrease, ServerMessage, ErrorMessage, KeepMessage, TeleportationError -> {
          message = buffer.readString();
        }
        case UI -> {
          message = buffer.readString();
          uiExtra = buffer.readShort();
        }
        case Queue -> {
          objectId = buffer.readInt();
          queuePos = buffer.readShort();
        }
        case ObjectText -> {
          message = buffer.readString();
          objectId = buffer.readInt();
          color = buffer.readInt();
        }
        case Death, DungeonOpened -> {
          message = buffer.readString();
          pictureType = buffer.readInt();
        }
        case DungeonCall, ProgressBar -> {
          message = buffer.readString();
          unknownInt1 = buffer.readInt();
          unknownShort1 = buffer.readShort();
        }
        case Behavior -> {
          message = buffer.readString();
          unknownInt2 = buffer.readInt();
          unknownInt3 = buffer.readInt();
        }
        default -> {
        }
      }
    }
  }

  /**
   * Constructs a String to show NotificationPacket.
   *
   * @return packet representation as String.
   */
  @Override
  public String toString() {
    return "NotificationPacket{"
            + "\n   effect="
            + effect
            + "\n   extra="
            + extra
            + "\n   objectId="
            + objectId
            + "\n   message="
            + message
            + "\n   uiExtra="
            + uiExtra
            + "\n   queuePos="
            + queuePos
            + "\n   color="
            + color
            + "\n   pictureType="
            + pictureType
            + "\n   unknownInt1="
            + unknownInt1
            + "\n   unknownShort1="
            + unknownShort1
            + "\n   unknownInt2="
            + unknownInt2
            + "\n   unknownInt3="
            + unknownInt3;
  }
}

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

  @Override
  public void deserialize(final BufferReader buffer) {
    effect = NotificationEffectType.byOrdinal(buffer.readByte());
    extra = buffer.readByte();

    switch (effect.get()) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 9:
        message = buffer.readString();
        return;
      case 4:
        message = buffer.readString();
        uiExtra = buffer.readShort();
        return;
      case 5:
        objectId = buffer.readInt();
        queuePos = buffer.readShort();
        return;
      case 6:
        message = buffer.readString();
        objectId = buffer.readInt();
        color = buffer.readInt();
        return;
      case 7:
      case 8:
        message = buffer.readString();
        pictureType = buffer.readInt();
        return;
      case 10:
      case 11:
        message = buffer.readString();
        unknownInt1 = buffer.readInt();
        unknownShort1 = buffer.readShort();
        return;
      case 12:
        message = buffer.readString();
        unknownInt2 = buffer.readInt();
        unknownInt3 = buffer.readInt();
        return;
      default:
    }
  }

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

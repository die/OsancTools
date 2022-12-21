package com.github.waifu.packets.data.enums;

/**
 * Stores enums for Notification types.
 */
public enum NotificationEffectType {

  /**
   * Stat increase.
   */
  StatIncrease(0),
  /**
   * Message from the server.
   */
  ServerMessage(1),
  /**
   * Error message.
   */
  ErrorMessage(2),
  /**
   * To be documented.
   */
  KeepMessage(3),
  /**
   * To be documented.
   */
  UI(4),
  /**
   * Message about the queue.
   */
  Queue(5),
  /**
   * To be documented.
   */
  ObjectText(6),
  /**
   * Death occurred.
   */
  Death(7),
  /**
   * Dungeon opened.
   */
  DungeonOpened(8),
  /**
   * To be documented.
   */
  TeleportationError(9),
  /**
   * When a player calls a dungeon.
   */
  DungeonCall(10),
  /**
   * To be documented.
   */
  ProgressBar(11),
  /**
   * To be documented.
   */
  Behavior(12),
  /**
   * Unlocked blueprint.
   */
  BlueprintUnlock(20),
  /**
   * To be documented.
   */
  WithIcon(21),
  /**
   * To be documented.
   */
  FameBonus(22),
  /**
   * To be documented.
   */
  ForgeFire(23);
  /**
   * Index of the notification type.
   */
  private final int index;

  /**
   * Creates notification type by index.
   *
   * @param i index of the NotificationEffectType.
   */
  NotificationEffectType(final int i) {
    index = i;
  }

  /**
   * Get Notification type by ordinal.
   *
   * @param ord ordinal of the notification type.
   * @return notification type as NotificationEffectType.
   */
  public static NotificationEffectType byOrdinal(final byte ord) {
    for (final NotificationEffectType o : NotificationEffectType.values()) {
      if (o.index == ord) {
        return o;
      }
    }
    return null;
  }

  /**
   * Gets the index.
   *
   * @return index of the NotificationEffectType.
   */
  public int get() {
    return index;
  }
}

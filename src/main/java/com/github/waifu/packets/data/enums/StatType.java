package com.github.waifu.packets.data.enums;

/**
 * Ordinal of stats.
 */
public enum StatType {

  /**
   * Maximum value of the HP stat.
   */
  MAX_HP_STAT(0),
  /**
   * Current HP stat.
   */
  HP_STAT(1),
  /**
   * To be documented.
   */
  SIZE_STAT(2),
  /**
   * Maximum value of the MP stat.
   */
  MAX_MP_STAT(3),
  /**
   * Current MP stat.
   */
  MP_STAT(4),
  /**
   * Number of Exp to the next level.
   */
  NEXT_LEVEL_EXP_STAT(5),
  /**
   * Number of Exp.
   */
  EXP_STAT(6),
  /**
   * Current level.
   */
  LEVEL_STAT(7),
  /**
   * Item in slot 0.
   */
  INVENTORY_0_STAT(8),
  /**
   * Item in slot 1.
   */
  INVENTORY_1_STAT(9),
  /**
   * Item in slot 2.
   */
  INVENTORY_2_STAT(10),
  /**
   * Item in slot 3.
   */
  INVENTORY_3_STAT(11),
  /**
   * Item in slot 4.
   */
  INVENTORY_4_STAT(12),
  /**
   * Item in slot 5.
   */
  INVENTORY_5_STAT(13),
  /**
   * Item in slot 6.
   */
  INVENTORY_6_STAT(14),
  /**
   * Item in slot 7.
   */
  INVENTORY_7_STAT(15),
  /**
   * Item in slot 8.
   */
  INVENTORY_8_STAT(16),
  /**
   * Item in slot 9.
   */
  INVENTORY_9_STAT(17),
  /**
   * Item in slot 10.
   */
  INVENTORY_10_STAT(18),
  /**
   * Item in slot 11.
   */
  INVENTORY_11_STAT(19),
  /**
   * Current attack stat.
   */
  ATTACK_STAT(20),
  /**
   * Current defense stat.
   */
  DEFENSE_STAT(21),
  /**
   * Current speed stat.
   */
  SPEED_STAT(22),
  /**
   * Unknown.
   */
  UNKNOWN23(23),
  /**
   * Unknown.
   */
  UNKNOWN24(24),
  /**
   * The id of the skin being used.
   */
  SKIN_STAT(25),
  /**
   * Current vitality stat.
   */
  VITALITY_STAT(26),
  /**
   * Current wisdom stat.
   */
  WISDOM_STAT(27),
  /**
   * Current dexterity stat.
   */
  DEXTERITY_STAT(28),
  /**
   * Current condition stat.
   */
  CONDITION_STAT(29),
  /**
   * Number of stars.
   */
  NUM_STARS_STAT(30),
  /**
   * Username.
   */
  NAME_STAT(31),
  /**
   * To be documented.
   */
  TEX1_STAT(32),
  /**
   * To be documented.
   */
  TEX2_STAT(33),
  /**
   * To be documented.
   */
  MERCHANDISE_TYPE_STAT(34),
  /**
   * To be documented.
   */
  CREDITS_STAT(35),
  /**
   * To be documented.
   */
  MERCHANDISE_PRICE_STAT(36),
  /**
   * To be documented.
   */
  ACTIVE_STAT(37),
  /**
   * Id of the account.
   */
  ACCOUNT_ID_STAT(38),
  /**
   * Number of fame.
   */
  FAME_STAT(39),
  /**
   * To be documented.
   */
  MERCHANDISE_CURRENCY_STAT(40),
  /**
   * To be documented.
   */
  CONNECT_STAT(41),
  /**
   * To be documented.
   */
  MERCHANDISE_COUNT_STAT(42),
  /**
   * To be documented.
   */
  MERCHANDISE_MINS_LEFT_STAT(43),
  /**
   * To be documented.
   */
  MERCHANDISE_DISCOUNT_STAT(44),
  /**
   * To be documented.
   */
  MERCHANDISE_RANK_REQ_STAT(45),
  /**
   * Maximum HP boost.
   */
  MAX_HP_BOOST_STAT(46),
  /**
   * Maximum MP boost.
   */
  MAX_MP_BOOST_STAT(47),
  /**
   * Maximum attack boost.
   */
  ATTACK_BOOST_STAT(48),
  /**
   * Maximum defense boost.
   */
  DEFENSE_BOOST_STAT(49),
  /**
   * Maximum speed boost.
   */
  SPEED_BOOST_STAT(50),
  /**
   * Maximum vitality boost.
   */
  VITALITY_BOOST_STAT(51),
  /**
   * Maximum wisdom boost.
   */
  WISDOM_BOOST_STAT(52),
  /**
   * Maximum dexterity boost.
   */
  DEXTERITY_BOOST_STAT(53),
  /**
   * Owner's account id.
   */
  OWNER_ACCOUNT_ID_STAT(54),
  /**
   * To be documented.
   */
  RANK_REQUIRED_STAT(55),
  /**
   * To be documented.
   */
  NAME_CHOSEN_STAT(56),
  /**
   * Current number of fame.
   */
  CURR_FAME_STAT(57),
  /**
   * Fame needed for the next class quest.
   */
  NEXT_CLASS_QUEST_FAME_STAT(58),
  /**
   * To be documented.
   */
  LEGENDARY_RANK_STAT(59),
  /**
   * To be documented.
   */
  SINK_LEVEL_STAT(60),
  /**
   * To be documented.
   */
  ALT_TEXTURE_STAT(61),
  /**
   * Guild name.
   */
  GUILD_NAME_STAT(62),
  /**
   * Guild rank.
   */
  GUILD_RANK_STAT(63),
  /**
   * To be documented.
   */
  BREATH_STAT(64),
  /**
   * To be documented.
   */
  XP_BOOSTED_STAT(65),
  /**
   * To be documented.
   */
  XP_TIMER_STAT(66),
  /**
   * To be documented.
   */
  LD_TIMER_STAT(67),
  /**
   * To be documented.
   */
  LT_TIMER_STAT(68),
  /**
   * To be documented.
   */
  HEALTH_POTION_STACK_STAT(69),
  /**
   * To be documented.
   */
  MAGIC_POTION_STACK_STAT(70),
  /**
   * Item in the backpack slot 0.
   */
  BACKPACK_0_STAT(71),
  /**
   * Item in the backpack slot 1.
   */
  BACKPACK_1_STAT(72),
  /**
   * Item in the backpack slot 2.
   */
  BACKPACK_2_STAT(73),
  /**
   * Item in the backpack slot 3.
   */
  BACKPACK_3_STAT(74),
  /**
   * Item in the backpack slot 4.
   */
  BACKPACK_4_STAT(75),
  /**
   * Item in the backpack slot 5.
   */
  BACKPACK_5_STAT(76),
  /**
   * Item in the backpack slot 6.
   */
  BACKPACK_6_STAT(77),
  /**
   * Item in the backpack slot 7.
   */
  BACKPACK_7_STAT(78),
  /**
   * If the player has a backpack.
   */
  HASBACKPACK_STAT(79),
  /**
   * To be documented.
   */
  TEXTURE_STAT(80),
  /**
   * To be documented.
   */
  PET_INSTANCEID_STAT(81),
  /**
   * Name of the pet.
   */
  PET_NAME_STAT(82),
  /**
   * The type of pet.
   */
  PET_TYPE_STAT(83),
  /**
   * The rarity of the pet.
   */
  PET_RARITY_STAT(84),
  /**
   * To be documented.
   */
  PET_MAXABILITYPOWER_STAT(85),
  /**
   * The family of the pet.
   */
  PET_FAMILY_STAT(86),
  /**
   * To be documented.
   */
  PET_FIRSTABILITY_POINT_STAT(87),
  /**
   * To be documented.
   */
  PET_SECONDABILITY_POINT_STAT(88),
  /**
   * To be documented.
   */
  PET_THIRDABILITY_POINT_STAT(89),
  /**
   * To be documented.
   */
  PET_FIRSTABILITY_POWER_STAT(90),
  /**
   * To be documented.
   */
  PET_SECONDABILITY_POWER_STAT(91),
  /**
   * To be documented.
   */
  PET_THIRDABILITY_POWER_STAT(92),
  /**
   * To be documented.
   */
  PET_FIRSTABILITY_TYPE_STAT(93),
  /**
   * To be documented.
   */
  PET_SECONDABILITY_TYPE_STAT(94),
  /**
   * To be documented.
   */
  PET_THIRDABILITY_TYPE_STAT(95),
  /**
   * To be documented.
   */
  NEW_CON_STAT(96),
  /**
   * To be documented.
   */
  FORTUNE_TOKEN_STAT(97),
  /**
   * To be documented.
   */
  SUPPORTER_POINTS_STAT(98),
  /**
   * To be documented.
   */
  SUPPORTER_STAT(99),
  /**
   * To be documented.
   */
  CHALLENGER_STARBG_STAT(100),
  /**
   * To be documented.
   */
  PLAYER_ID(101),
  /**
   * To be documented.
   */
  PROJECTILE_SPEED_MULT(102),
  /**
   * To be documented.
   */
  PROJECTILE_LIFE_MULT(103),
  /**
   * To be documented.
   */
  OPENED_AT_TIMESTAMP(104),
  /**
   * To be documented.
   */
  EXALTED_ATT(105),
  /**
   * To be documented.
   */
  EXALTED_DEF(106),
  /**
   * To be documented.
   */
  EXALTED_SPEED(107),
  /**
   * To be documented.
   */
  EXALTED_VIT(108),
  /**
   * To be documented.
   */
  EXALTED_WIS(109),
  /**
   * To be documented.
   */
  EXALTED_DEX(110),
  /**
   * To be documented.
   */
  EXALTED_HP(111),
  /**
   * To be documented.
   */
  EXALTED_MP(112),
  /**
   * To be documented.
   */
  EXALTATION_BONUS_DAMAGE(113),
  /**
   * To be documented.
   */
  EXALTATION_IC_REDUCTION(114),
  /**
   * To be documented.
   */
  GRAVE_ACCOUNT_ID(115),
  /**
   * To be documented.
   */
  POTION_ONE_TYPE(116),
  /**
   * To be documented.
   */
  POTION_TWO_TYPE(117),
  /**
   * To be documented.
   */
  POTION_THREE_TYPE(118),
  /**
   * To be documented.
   */
  POTION_BELT(119),
  /**
   * To be documented.
   */
  FORGEFIRE(120),
  /**
   * Unknown.
   */
  UNKNOWN121(121),
  /**
   * Unknown.
   */
  UNKNOWN122(122),
  /**
   * Unknown.
   */
  UNKNOWN123(123),
  /**
   * Unknown.
   */
  UNKNOWN124(124),
  /**
   * Unknown.
   */
  UNKNOWN125(125),
  /**
   * Unknown.
   */
  UNKNOWN126(126),
  /**
   * Enchantment stat.
   */
  ENCHANTMENT(127);

  /**
   * Index of the Stat type.
   */
  private final int index;

  /**
   * Constructs a stat type.
   *
   * @param i ordinal of the stat
   */
  StatType(final int i) {
    index = i;
  }

  /**
   * Gets a StatType by Ordinal.
   *
   * @param ord the ordinal of the StatType.
   * @return the StatType that matches the ordinal.
   */
  public static StatType byOrdinal(final int ord) {
    for (final StatType o : StatType.values()) {
      if (o.index == ord) {
        return o;
      }
    }
    return null;
  }

  /**
   * Get the index.
   *
   * @return the index of the StatType.
   */
  public int get() {
    return index;
  }
}

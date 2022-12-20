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
   * Current level;
   */
  LEVEL_STAT(7),
  /**
   * Item in slot 0
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
   * Unknown.
   */
  UNKNOWN25(25),
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
   *
   */
  MAGIC_POTION_STACK_STAT(70),
  /**
   *
   */
  BACKPACK_0_STAT(71),
  /**
   *
   */
  BACKPACK_1_STAT(72),
  /**
   *
   */
  BACKPACK_2_STAT(73),
  /**
   *
   */
  BACKPACK_3_STAT(74),
  /**
   *
   */
  BACKPACK_4_STAT(75),
  /**
   *
   */
  BACKPACK_5_STAT(76),
  /**
   *
   */
  BACKPACK_6_STAT(77),
  /**
   *
   */
  BACKPACK_7_STAT(78),
  /**
   *
   */
  HASBACKPACK_STAT(79),
  /**
   *
   */
  TEXTURE_STAT(80),
  /**
   *
   */
  PET_INSTANCEID_STAT(81),
  /**
   *
   */
  PET_NAME_STAT(82),
  /**
   *
   */
  PET_TYPE_STAT(83),
  /**
   *
   */
  PET_RARITY_STAT(84),
  /**
   *
   */
  PET_MAXABILITYPOWER_STAT(85),
  /**
   *
   */
  PET_FAMILY_STAT(86),
  /**
   *
   */
  PET_FIRSTABILITY_POINT_STAT(87),
  /**
   *
   */
  PET_SECONDABILITY_POINT_STAT(88),
  /**
   *
   */
  PET_THIRDABILITY_POINT_STAT(89),
  /**
   *
   */
  PET_FIRSTABILITY_POWER_STAT(90),
  /**
   *
   */
  PET_SECONDABILITY_POWER_STAT(91),
  /**
   *
   */
  PET_THIRDABILITY_POWER_STAT(92),
  /**
   *
   */
  PET_FIRSTABILITY_TYPE_STAT(93),
  /**
   *
   */
  PET_SECONDABILITY_TYPE_STAT(94),
  /**
   *
   */
  PET_THIRDABILITY_TYPE_STAT(95),
  /**
   *
   */
  NEW_CON_STAT(96),
  /**
   *
   */
  FORTUNE_TOKEN_STAT(97),
  /**
   *
   */
  SUPPORTER_POINTS_STAT(98),
  /**
   *
   */
  SUPPORTER_STAT(99),
  /**
   *
   */
  CHALLENGER_STARBG_STAT(100),
  /**
   *
   */
  PLAYER_ID(101),
  /**
   *
   */
  PROJECTILE_SPEED_MULT(102),
  /**
   *
   */
  PROJECTILE_LIFE_MULT(103),
  /**
   *
   */
  OPENED_AT_TIMESTAMP(104),
  /**
   *
   */
  EXALTED_ATT(105),
  /**
   *
   */
  EXALTED_DEF(106),
  /**
   *
   */
  EXALTED_SPEED(107),
  /**
   *
   */
  EXALTED_VIT(108),
  /**
   *
   */
  EXALTED_WIS(109),
  /**
   *
   */
  EXALTED_DEX(110),
  /**
   *
   */
  EXALTED_HP(111),
  /**
   *
   */
  EXALTED_MP(112),
  /**
   *
   */
  EXALTATION_BONUS_DAMAGE(113),
  /**
   *
   */
  EXALTATION_IC_REDUCTION(114),
  /**
   *
   */
  GRAVE_ACCOUNT_ID(115),
  /**
   *
   */
  POTION_ONE_TYPE(116),
  /**
   *
   */
  POTION_TWO_TYPE(117),
  /**
   *
   */
  POTION_THREE_TYPE(118),
  /**
   *
   */
  POTION_BELT(119),
  /**
   *
   */
  FORGEFIRE(120),
  /**
   *
   */
  UNKNOWN121(121),
  /**
   *
   */
  UNKNOWN122(122),
  /**
   *
   */
  UNKNOWN123(123),
  /**
   *
   */
  UNKNOWN124(124),
  /**
   *
   */
  UNKNOWN125(125),
  /**
   *
   */
  UNKNOWN126(126);

  /**
   *
   */
  private final int index;

  /**
   *
   */
  StatType(int i) {
    index = i;
  }

  /**
   *
   */
  public static StatType byOrdinal(int ord) {
    for (StatType o : StatType.values()) {
      if (o.index == ord) {
        return o;
      }
    }
    return null;
  }

  /**
   *
   */
  public int get() {
    return index;
  }
}

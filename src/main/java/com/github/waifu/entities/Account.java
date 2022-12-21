package com.github.waifu.entities;

import java.util.List;

/**
 * Account class to store ROTMG account data.
 */
public class Account {

  /**
   * To be documented.
   */
  private final String name;
  /**
   * To be documented.
   */
  private final String stars;
  /**
   * To be documented.
   */
  private final String skins;
  /**
   * To be documented.
   */
  private final String exaltations;
  /**
   * To be documented.
   */
  private final String accountFame;
  /**
   * To be documented.
   */
  private final String guild;
  /**
   * To be documented.
   */
  private final String guildRank;
  /**
   * To be documented.
   */
  private final String creationDate;
  /**
   * To be documented.
   */
  private final String lastSeen;
  /**
   * To be documented.
   */
  private List<Character> characters;

  /**
   * Account Constructor
   *
   * <p>Constructs an Account with limited information.
   *
   * @param name username of the Account.
   */
  public Account(final String name) {
    this.name = name;
    this.stars = null;
    this.skins = null;
    this.exaltations = null;
    this.accountFame = null;
    this.guild = null;
    this.guildRank = null;
    this.creationDate = null;
    this.lastSeen = null;
    this.characters = null;
  }

  /**
   * Account Constructor
   *
   * <p>Constructs an Account with limited information.
   *
   * @param name       username of the Account.
   * @param characters list of characters the Account has.
   */
  public Account(final String name, final List<Character> characters) {
    this.name = name;
    this.stars = "";
    this.skins = "";
    this.exaltations = "";
    this.accountFame = "";
    this.guild = "";
    this.guildRank = "";
    this.creationDate = "";
    this.lastSeen = "";
    this.characters = characters;
  }

  /**
   * To be documented.
   *
   * @param name       To be documented.
   * @param characters To be documented.
   * @param stars      To be documented.
   * @param fame       To be documented.
   * @param guild      To be documented.
   * @param guildRank  To be documented.
   */
  public Account(final String name, final List<Character> characters, final int stars, final int fame, final String guild, final String guildRank) {
    this.name = name;
    this.stars = String.valueOf(stars);
    this.skins = "";
    this.exaltations = "";
    this.accountFame = String.valueOf(fame);
    this.guild = guild;
    this.guildRank = guildRank;
    this.creationDate = "";
    this.lastSeen = "";
    this.characters = characters;
  }

  /**
   * To be documented.
   *
   * @param name         To be documented.
   * @param stars        To be documented.
   * @param skins        To be documented.
   * @param exaltations  To be documented.
   * @param accountFame  To be documented.
   * @param guild        To be documented.
   * @param guildRank    To be documented.
   * @param creationDate To be documented.
   * @param lastSeen     To be documented.
   * @param characters   To be documented.
   */
  public Account(final String name, final String stars, final String skins, final String exaltations, final String accountFame, final String guild, final String guildRank, final String creationDate, final String lastSeen, final List<Character> characters) {
    this.name = name;
    this.stars = stars;
    this.skins = skins;
    this.exaltations = exaltations;
    this.accountFame = accountFame;
    this.guild = guild;
    this.guildRank = guildRank;
    this.creationDate = creationDate;
    this.lastSeen = lastSeen;
    this.characters = characters;
  }

  /**
   * Get a string that contains object data to print.
   *
   * @return a string that contains all variable values.
   */
  public String printAccount() {
    return "Name: " + this.name + "\n" + "Stars: " + this.stars + "\n" + "Skins: " + this.skins + "\n" + "Exaltations: " + this.exaltations + "\n" + "Account Fame: " + this.accountFame + "\n" + "Guild: " + this.guild + "\n" + "Guild Rank: " + this.guildRank + "\n" + "Creation Date: " + this.creationDate + "\n" + "Last Seen: " + this.lastSeen + "\n" + "Characters: " + this.characters.size() + "\n";
  }

  /**
   * Gets the name of the ROTMG account.
   *
   * <p>Example:
   * <blockquote>
   * Su
   * </blockquote>
   *
   * @return the name as a string
   */
  public String getName() {
    return this.name;
  }

  /**
   * Gets the number of stars of the ROTMG account.
   *
   * <p>Example:
   * <blockquote>
   * 70
   * </blockquote>
   *
   * @return the number of stars as a string
   */
  public String getStars() {
    return this.stars;
  }

  /**
   * Gets the number of skins of the ROTMG account.
   *
   * <p>Example:
   * <blockquote>
   * 202
   * </blockquote>
   *
   * @return the number of skins as a string
   */
  public String getSkins() {
    return this.skins;
  }

  /**
   * Gets the number of exaltations of the ROTMG account.
   *
   * <p>Example:
   * <blockquote>
   * 185
   * </blockquote>
   *
   * @return the number of exaltations as a string
   */
  public String getExaltations() {
    return this.exaltations;
  }

  /**
   * Gets the number of account fame of the ROTMG account.
   *
   * <p>Example:
   * <blockquote>
   * 30721
   * </blockquote>
   *
   * @return the number of account fame as a string
   */
  public String getAccountFame() {
    return this.accountFame;
  }

  /**
   * Gets the name of the guild the ROTMG account is in.
   *
   * <p>Example:
   * <blockquote>
   * Dim
   * </blockquote>
   *
   * @return the name of the guild as a string
   */
  public String getGuild() {
    return this.guild;
  }

  /**
   * Gets the name of the guild rank the ROTMG account has.
   *
   * <p>Example:
   * <blockquote>
   * Founder
   * </blockquote>
   *
   * @return the name of the guild rank as a string
   */
  public String getGuildRank() {
    return this.guildRank;
  }

  /**
   * Gets the creation date of the ROTMG account.
   *
   * <p>Example:
   * <blockquote>
   * ~4 years and 77 days ago
   * </blockquote>
   *
   * @return the creation date as a string
   */
  public String getCreationDate() {
    return this.creationDate;
  }

  /**
   * Gets the last seen date of the ROTMG account.
   *
   * <p>Example:
   * <blockquote>
   * 2022-11-13 02:45:34 at USSouthWest as Assassin
   * </blockquote>
   *
   * @return the last seen date as a string
   */
  public String getLastSeen() {
    return this.lastSeen;
  }

  /**
   * Gets the list of visible characters a ROTMG account has.
   *
   * @return list of Character objects.
   */
  public List<Character> getCharacters() {
    return this.characters;
  }

  /**
   * To be documented.
   *
   * @param c To be documented.
   */
  public void setCharacters(final List<Character> c) {
    this.characters = c;
  }

  /**
   * Gets the most recently logged in character of a ROTMG account.
   *
   * @return a Character object in the front of the account's character list.
   */
  public Character getRecentCharacter() {
    return this.characters.get(0);
  }
}

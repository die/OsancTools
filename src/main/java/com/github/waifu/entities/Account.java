package com.github.waifu.entities;

import java.util.List;

/**
 * Account class to store ROTMG account data.
 */
public class Account {

    private final String name;
    private final String stars;
    private final String skins;
    private final String exaltations;
    private final String accountFame;
    private final String guild;
    private final String guildRank;
    private final String creationDate;
    private final String lastSeen;
    private List<Character> characters;

    /**
     * Account Constructor
     *
     * Constructs an Account with limited information.
     *
     * @param name username of the Account.
     */
    public Account(String name) {
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
     * Constructs an Account with limited information.
     *
     * @param name username of the Account.
     * @param characters list of characters the Account has.
     */
    public Account(String name, List<Character> characters) {
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
     * Account Constructor
     *
     * Constructs an Account with information by the Update Packet
     *
     * @param name = name
     *
     */
    public Account(String name, List<Character> characters, int stars, int fame, String guildName, String guildRank) {
        this.name = name;
        this.stars = String.valueOf(stars);
        this.skins = "";
        this.exaltations = "";
        this.accountFame = String.valueOf(fame);
        this.guild = guildName;
        this.guildRank = guildRank;
        this.creationDate = "";
        this.lastSeen = "";
        this.characters = characters;
    }

    /**
     * Account Constructor.
     *
     * Constructs an Account with all information.
     *
     * @param name username of the Account.
     * @param stars number of stars the Account has.
     * @param skins number of skins the Account has.
     * @param exaltations number of exaltations the Account has.
     * @param accountFame number of account fame the Account has.
     * @param guild name of the guild the Account is in.
     * @param guildRank name of the guild rank the Account has.
     * @param creationDate creation date of the Account.
     * @param lastSeen last seen date of the Account.
     * @param characters list of characters the Account has.
     */
    public Account(String name, String stars, String skins, String exaltations, String accountFame, String guild, String guildRank, String creationDate, String lastSeen, List<Character> characters) {
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
     * @return a string that contains all variable values.
     */
    public String printAccount() {
        return "Name: " + this.name + "\n" +
                           "Stars: " + this.stars + "\n" +
                           "Skins: " + this.skins + "\n" +
                           "Exaltations: " + this.exaltations + "\n" +
                           "Account Fame: " + this.accountFame + "\n" +
                           "Guild: " + this.guild + "\n" +
                           "Guild Rank: " + this.guildRank + "\n" +
                           "Creation Date: " + this.creationDate + "\n" +
                           "Last Seen: " + this.lastSeen + "\n" +
                           "Characters: " + this.characters.size() + "\n";
    }

    /**
     * Gets the name of the ROTMG account.
     * <p>
     * Example:
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
     * <p>
     * Example:
     * <blockquote>
     * 70
     * </blockquote>
     * @return the number of stars as a string
     */
    public String getStars() {
        return this.stars;
    }

    /**
     * Gets the number of skins of the ROTMG account.
     * <p>
     * Example:
     * <blockquote>
     * 202
     * </blockquote>
     * @return the number of skins as a string
     */
    public String getSkins() {
        return this.skins;
    }

    /**
     * Gets the number of exaltations of the ROTMG account.
     * <p>
     * Example:
     * <blockquote>
     * 185
     * </blockquote>
     * @return the number of exaltations as a string
     */
    public String getExaltations() {
        return this.exaltations;
    }

    /**
     * Gets the number of account fame of the ROTMG account.
     * <p>
     * Example:
     * <blockquote>
     * 30721
     * </blockquote>
     * @return the number of account fame as a string
     */
    public String getAccountFame() {
        return this.accountFame;
    }

    /**
     * Gets the name of the guild the ROTMG account is in.
     * <p>
     * Example:
     * <blockquote>
     * Dim
     * </blockquote>
     * @return the name of the guild as a string
     */
    public String getGuild() {
        return this.guild;
    }

    /**
     * Gets the name of the guild rank the ROTMG account has.
     * <p>
     * Example:
     * <blockquote>
     * Founder
     * </blockquote>
     * @return the name of the guild rank as a string
     */
    public String getGuildRank() {
        return this.guildRank;
    }

    /**
     * Gets the creation date of the ROTMG account.
     * <p>
     * Example:
     * <blockquote>
     * ~4 years and 77 days ago
     * </blockquote>
     * @return the creation date as a string
     */
    public String getCreationDate() {
        return this.creationDate;
    }

    /**
     * Gets the last seen date of the ROTMG account.
     * <p>
     * Example:
     * <blockquote>
     * 2022-11-13 02:45:34 at USSouthWest as Assassin
     * </blockquote>
     * @return the last seen date as a string
     */
    public String getLastSeen() {
        return this.lastSeen;
    }

    /**
     * Gets the list of visible characters a ROTMG account has.
     * @return list of Character objects.
     */
    public List<Character> getCharacters() {
        return this.characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    /**
     * Gets the most recently logged in character of a ROTMG account.
     * @return a Character object in the front of the account's character list.
     */
    public Character getRecentCharacter() {
        return this.characters.get(0);
    }
}

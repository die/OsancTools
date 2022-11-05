package com.github.waifu.entities;

import java.util.List;

/**
 * Account class to store account data.
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
    private final List<Character> characters;

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
     * Account method.
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
     * Account method.
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
     *
     * @return
     */
    public String printAccount() {
        String account = "Name: " + this.name + "\n" +
                           "Stars: " + this.stars + "\n" +
                           "Skins: " + this.skins + "\n" +
                           "Exaltations: " + this.exaltations + "\n" +
                           "Account Fame: " + this.accountFame + "\n" +
                           "Guild: " + this.guild + "\n" +
                           "Guild Rank: " + this.guildRank + "\n" +
                           "Creation Date: " + this.creationDate + "\n" +
                           "Last Seen: " + this.lastSeen + "\n" +
                           "Characters: " + this.characters.size() + "\n";
        return account;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return
     */
    public String getStars() {
        return this.stars;
    }

    /**
     *
     * @return
     */
    public String getSkins() {
        return this.skins;
    }

    /**
     *
     * @return
     */
    public String getExaltations() {
        return this.exaltations;
    }

    /**
     *
     * @return
     */
    public String getAccountFame() {
        return this.accountFame;
    }

    /**
     *
     * @return
     */
    public String getGuild() {
        return this.guild;
    }

    /**
     *
     * @return
     */
    public String getGuildRank() {
        return this.guildRank;
    }

    /**
     *
     * @return
     */
    public String getCreationDate() {
        return this.creationDate;
    }

    /**
     *
     * @return
     */
    public String getLastSeen() {
        return this.lastSeen;
    }

    /**
     *
     * @return
     */
    public List<Character> getCharacters() {
        return this.characters;
    }

    /**
     *
     * @return
     */
    public Character getRecentCharacter() {
        return this.characters.get(0);
    }
}

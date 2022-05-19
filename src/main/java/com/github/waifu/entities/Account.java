package com.github.waifu.entities;

import java.util.List;

/**
 * Account class to store account data.
 */
public class Account {

    private final String name;
    private final int stars;
    private final int skins;
    private final int exaltations;
    private final long accountFame;
    private final String guild;
    private final String guildRank;
    private final String creationDate;
    private final String lastSeen;
    private final List<Character> characters;

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
        this.stars = 0;
        this.skins = 0;
        this.exaltations = 0;
        this.accountFame = 0;
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
    public Account(String name, int stars, int skins, int exaltations, long accountFame, String guild, String guildRank, String creationDate, String lastSeen, List<Character> characters) {
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

    public String getName() { return this.name; }

    public int getStars() {
        return this.stars;
    }

    public int getSkins() {
        return this.skins;
    }

    public int getExaltations() {
        return this.exaltations;
    }

    public long getAccountFame() {
        return this.accountFame;
    }

    public String getGuild() { return this.guild; }

    public String getGuildRank() { return this.guildRank; }

    public String getCreationDate() { return this.creationDate; }

    public String getLastSeen() { return this.lastSeen; }

    public List<Character> getCharacters() {
        return this.characters;
    }
}

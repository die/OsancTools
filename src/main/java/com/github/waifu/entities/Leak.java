package com.github.waifu.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Leak object.
 */
public class Leak {

  /**
   * Name of the associated guild.
   */
  private String guild;
  /**
   * Raider profile for the leaker.
   */
  private Raider leaker;
  /**
   * List of accounts that crash due to the leaked loc.
   */
  private final List<Account> crashers;

  /**
   * Leak constructor.
   */
  public Leak() {
    this.guild = "";
    this.leaker = null;
    this.crashers = new ArrayList<>();
  }

  /**
   * Adds a crasher to the list.
   *
   * @param account account of a crasher.
   */
  public void addCrasher(final Account account) {
    final int index = hasCrasher(account.getName());
    if (index == -1) {
      this.crashers.add(account);
    } else {
      this.crashers.set(index, account);
    }
  }

  /**
   * Returns the guild name.
   *
   * @return guild name.
   */
  public String getGuild() {
    return guild;
  }

  /**
   * Sets the guild name.
   *
   * @param guild guild name.
   */
  public void setGuild(final String guild) {
    this.guild = guild;
  }

  /**
   * Returns the raider object of the leaker.
   *
   * @return raider object.
   */
  public Raider getLeaker() {
    return leaker;
  }

  /**
   * Sets the leaker.
   *
   * @param leaker raider object.
   */
  public void setLeaker(final Raider leaker) {
    this.leaker = leaker;
  }

  /**
   * Gets list of crashers.
   *
   * @return list of accounts.
   */
  public List<Account> getCrashers() {
    return this.crashers;
  }

  /**
   * Looks to see if a crasher's username is already in the list.
   *
   * @param name username of the crasher.
   * @return index of the found account.
   */
  private int hasCrasher(final String name) {
    for (final Account a : crashers) {
      if (a.getName().equalsIgnoreCase(name)) {
        return crashers.indexOf(a);
      }
    }
    return -1;
  }
}

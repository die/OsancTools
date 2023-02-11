package com.github.waifu.entities;

import com.github.waifu.util.Pair;
import com.github.waifu.util.Utilities;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * To be documented.
 */
public class Raid {

  /**
   * To be documented.
   */
  private final List<Raider> raiders;

  /**
   * List of those found not in the raid.
   */
  private List<Account> crashers;
  /**
   * To be documented.
   */
  private JSONObject json;
  /**
   * To be documented.
   */
  private String type;
  /**
   * To be documented.
   */
  private String description;
  /**
   * To be documented.
   */
  private int maxMembers;
  /**
   * To be documented.
   */
  private Raider raidLeader;
  /**
   * To be documented.
   */
  private String location;
  /**
   * To be documented.
   */
  private String name;
  /**
   * To be documented.
   */
  private String id;
  /**
   * To be documented.
   */
  private String status;

  /**
   * To be documented.
   *
   * @param json To be documented.
   */
  public Raid(final JSONObject json) {
    this.json = json;
    this.type = json.getString("raid_type");
    this.description = String.valueOf(json.get("description"));
    this.maxMembers = json.getInt("max_members");
    this.raidLeader = createRaidLeader(json.getJSONObject("creator"));
    this.location = String.valueOf(json.get("location"));
    this.name = json.getString("name");
    this.id = String.valueOf(json.getInt("id"));
    this.status = json.getString("status");
    this.raiders = new ArrayList<>();
    addWebAppRaiders(json);
  }

  /**
   * To be documented.
   */
  public Raid() {
    this.json = null;
    this.type = "";
    this.description = "";
    this.maxMembers = -1;
    this.raidLeader = null;
    this.location = "";
    this.name = "";
    this.id = "";
    this.status = "";
    this.raiders = new ArrayList<>();
  }

  /**
   * To be documented.
   *
   * @param json To be documented.
   */
  public void deepCopy(final JSONObject json) {
    this.json = json;
    this.type = json.getString("raid_type");
    this.description = String.valueOf(json.get("description"));
    this.maxMembers = json.getInt("max_members");
    this.raidLeader = createRaidLeader(json.getJSONObject("creator"));
    this.location = String.valueOf(json.get("location"));
    this.name = json.getString("name");
    this.id = String.valueOf(json.getInt("id"));
    this.status = json.getString("status");
    addWebAppRaiders(json);
  }

  /**
   * To be documented.
   *
   * @param creator To be documented.
   * @return To be documented.
   */
  private Raider createRaidLeader(final JSONObject creator) {
    return new Raider(creator.getString("id"), creator.getString("avatar"), creator.getString("server_nickname"));
  }

  /**
   * To be documented.
   *
   * @param account To be documented.
   */
  public void addSnifferAccount(final Account account) {
    final Pair<Integer, Integer> pair = findRaiderAccountByUsername(account.getName());
    if (pair == null) {
      if (json == null) {
        raiders.add(new Raider(account));
      } else {
        if (crashers == null) {
          crashers = new ArrayList<>();
        }
        crashers.add(account);
        System.out.println("added crasher " + account.getName());
      }
    } else {
      raiders.get(pair.left()).getAccounts().set(pair.right(), account);
      System.out.println("added raider " + account.getName());
    }
  }

  /**
   * To be documented.
   *
   * @param json To be documented.
   */
  public void addWebAppRaiders(final JSONObject json) {
    final JSONArray members = json.getJSONArray("members");

    for (int i = 0; i < members.length(); i++) {

      final boolean inWaitingList = members.getJSONObject(i).getBoolean("in_waiting_list");

      if (!inWaitingList) {
        final boolean gotPriority = members.getJSONObject(i).getBoolean("got_priority");
        final boolean gotEarlyLocation = members.getJSONObject(i).getBoolean("got_earlyloc");
        final boolean inVC = members.getJSONObject(i).getBoolean("in_vc");
        final JSONArray reacts = members.getJSONObject(i).getJSONArray("reacts");
        final JSONArray roles = members.getJSONObject(i).getJSONArray("roles");
        final String id = members.getJSONObject(i).getString("user_id");
        final String avatar = members.getJSONObject(i).getString("avatar");
        final String timestampJoined = members.getJSONObject(i).getString("timestamp_joined");

        final List<Account> accounts = new ArrayList<>();
        final String serverNickname = members.getJSONObject(i).getString("server_nickname");
        final List<String> usernames = Utilities.parseUsernamesFromNickname(serverNickname);
        for (final String s : usernames) {
          accounts.add(new Account(s));
        }
        raiders.add(new Raider(id, serverNickname, timestampJoined, avatar, gotPriority, gotEarlyLocation, inWaitingList, inVC, roles, reacts, accounts));
      }
    }
  }

  /**
   * To be documented.
   */
  public void printRaiders() {
    for (final Raider r : raiders) {
      for (final Account a : r.getAccounts()) {
        System.out.print(a.getName() + " ");
      }
      System.out.println();
    }
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public JSONObject getJson() {
    return json;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getType() {
    return type;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getDescription() {
    return description;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getMaxMembers() {
    return maxMembers;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Raider getRaidLeader() {
    return raidLeader;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getLocation() {
    return location;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getId() {
    return id;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getName() {
    return name;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getStatus() {
    return status;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public List<Raider> getRaiders() {
    return raiders;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public List<Account> getCrashers() {
    return crashers;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public List<String> getAllUsernames() {
    final List<String> usernames = new ArrayList<>();
    for (final Raider r : raiders) {
      for (final Account a : r.getAccounts()) {
        usernames.add(a.getName());
      }
    }
    return usernames;
  }

  /**
   * To be documented.
   *
   * @param serverNickname To be documented.
   * @return To be documented.
   */
  public Raider findRaiderByServerNickname(final String serverNickname) {
    for (final Raider r : raiders) {
      if (r.getServerNickname().equals(serverNickname)) {
        return r;
      }
    }
    return null;
  }

  /**
   * To be documented.
   *
   * @param username To be documented.
   * @return To be documented.
   */
  public Raider findRaiderByUsername(final String username) {
    for (final Raider r : raiders) {
      for (final Account a : r.getAccounts()) {
        if (a.getName().equalsIgnoreCase(username)) {
          return r;
        }
      }
    }
    return null;
  }

  /**
   * To be documented.
   *
   * @param username To be documented.
   * @return To be documented.
   */
  public Pair<Integer, Integer> findRaiderAccountByUsername(final String username) {
    for (final Raider r : raiders) {
      for (final Account a : r.getAccounts()) {
        if (a.getName().equalsIgnoreCase(username)) {
          return new Pair<>(raiders.indexOf(r), r.getAccounts().indexOf(a));
        }
      }
    }
    return null;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getNumberOfAccounts() {
    int accounts = 0;
    for (final Raider r : raiders) {
      accounts += r.getAccounts().size();
    }
    return accounts;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public boolean sniffed() {
    return !this.getRaiders().stream().allMatch(raider -> raider.getAccounts().stream().allMatch(account -> account.getCharacters() == null));
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public boolean isWebAppRaid() {
    return json != null;
  }
}

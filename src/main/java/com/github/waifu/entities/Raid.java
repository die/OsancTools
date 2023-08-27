package com.github.waifu.entities;

import com.github.waifu.gui.listeners.GroupListener;
import com.github.waifu.gui.listeners.RaidListener;
import com.github.waifu.util.Pair;
import com.github.waifu.util.Utilities;
import java.awt.*;
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
  private Group group;
  /**
   * ViBot raiders.
   */
  private List<ViBotRaider> viBotRaiders;
  /**
   * To be documented.
   */
  public List<RaidListener> listeners;
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
  private Raider raidLeader;
  /**
   * To be documented.
   */
  private String name;

  private String guild;

  private String vcName;

  private Color color;

  private final List<React> reacts;

  private String startTime;



  /**
   * To be documented.
   */
  public Raid() {
    group = new Group();
    this.viBotRaiders = new ArrayList<>();
    this.raidLeader = null;
    this.name = "";
    this.guild = "";
    this.raiders = new ArrayList<>();
    this.listeners = new ArrayList<>();
    this.reacts = new ArrayList<>();
  }

  public String getGuild() {
    return guild;
  }

  public Color getColor() {
    return color;
  }

  public String getVcName() {
    return vcName;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setGuild(final String guildId) {
    switch (guildId) {
      case "451171819672698920" -> guild = "Shatters/Moonlight";
      case "343704644712923138" -> guild = "Lost Halls";
      case "708026927721480254" -> guild = "Oryx Sanctuary";
    }
  }

  public void setColor(final Color color) {
    this.color = color;
  }

  public void setVcName(final String vcName) {
    this.vcName = vcName;
  }

  public List<React> getReacts() {
    return reacts;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(final String startTime) {
    this.startTime = startTime;
  }

  public void addReact(final React react) {
    for (final React r : reacts) {
      if (r.getName().equals(react.getName())) {
        reacts.set(reacts.indexOf(r), react);
        return;
      }
    }
    reacts.add(react);
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
  public Group getGroup() {
    return group;
  }

  public void addViBotRaiders(final ViBotRaider viBotRaider) {
    final ViBotRaider viBotRaider1 = getViBotRaiderByNickname(viBotRaider.getNickname());
    if (viBotRaider1 == null) {
      viBotRaiders.add(viBotRaider);
    } else {
      viBotRaiders.set(viBotRaiders.indexOf(viBotRaider1), viBotRaider);
    }
    for (final RaidListener raidListener : listeners) {
      raidListener.update(viBotRaider);
    }
  }


  /**
   * To be documented.
   * @return To be documented.
   */
  public List<ViBotRaider> getViBotRaiders() {
    return viBotRaiders;
  }

  public ViBotRaider getViBotRaider(String ign) {
    for (final ViBotRaider viBotRaider : viBotRaiders) {
      if (viBotRaider.hasIGN(ign)) {
        return viBotRaider;
      }
    }
    return null;
  }

  public ViBotRaider getViBotRaiderByNickname(final String nickname) {
    for (final ViBotRaider viBotRaider : viBotRaiders) {
      if (viBotRaider.getNickname().equals(nickname)) {
        return viBotRaider;
      }
    }
    return null;
  }

  public ViBotRaider getViBotRaiderById(final String id) {
    for (final ViBotRaider viBotRaider : viBotRaiders) {
      if (viBotRaider.getId().equals(id)) {
        return viBotRaider;
      }
    }
    return null;
  }

  /**
   * To be documented.
   *
   * @param raidListener To be documented.
   */
  public void addListener(final RaidListener raidListener) {
    this.listeners.add(raidListener);
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
  public String getName() {
    return name;
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
}

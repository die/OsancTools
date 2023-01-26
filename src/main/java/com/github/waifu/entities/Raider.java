package com.github.waifu.entities;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import org.json.JSONArray;

/**
 * To be documented.
 */
public class Raider {

  /**
   * To be documented.
   */
  private final String id;
  /**
   * To be documented.
   */
  private final String serverNickname;
  /**
   * To be documented.
   */
  private final ImageIcon avatar;
  /**
   * To be documented.
   */
  private final List<Account> accounts;
  /**
   * To be documented.
   */
  private String timestampJoined;
  /**
   * To be documented.
   */
  private boolean gotPriority;
  /**
   * To be documented.
   */
  private boolean gotEarlyLocation;
  /**
   * To be documented.
   */
  private boolean inWaitingList;
  /**
   * To be documented.
   */
  private boolean inVc;
  /**
   * To be documented.
   */
  private JSONArray roles;
  /**
   * To be documented.
   */
  private JSONArray reacts;

  /**
   * To be documented.
   */
  public Raider() {
    this.id = "";
    this.serverNickname = "";
    this.timestampJoined = "";
    this.avatar = null;
    this.gotPriority = false;
    this.gotEarlyLocation = false;
    this.inWaitingList = false;
    this.inVc = false;
    this.roles = null;
    this.reacts = null;
    this.accounts = new ArrayList<>();
  }

  /**
   * To be documented.
   *
   * @param account To be documented.
   */
  public Raider(final Account account) {
    this.id = "";
    this.serverNickname = "";
    this.timestampJoined = "";
    this.avatar = null;
    this.gotPriority = false;
    this.gotEarlyLocation = false;
    this.inWaitingList = false;
    this.inVc = false;
    this.roles = null;
    this.reacts = null;
    this.accounts = new ArrayList<>();
    accounts.add(account);
  }

  /**
   * To be documented.
   *
   * @param id To be documented.
   * @param avatar To be documented.
   * @param serverNickname To be documented.
   */
  public Raider(final String id, final String avatar, final String serverNickname) {
    this.id = id;
    this.avatar = setAvatar(avatar);
    this.serverNickname = serverNickname;
    this.accounts = null;
  }

  /**
   * To be documented.
   *
   * @param id To be documented.
   * @param serverNickname To be documented.
   * @param timestampJoined To be documented.
   * @param avatar To be documented.
   * @param gotPriority To be documented.
   * @param gotEarlyLocation To be documented.
   * @param inWaitingList To be documented.
   * @param inVc To be documented.
   * @param roles To be documented.
   * @param reacts To be documented.
   * @param accounts To be documented.
   */
  public Raider(final String id, final String serverNickname, final String timestampJoined, final String avatar, final boolean gotPriority, final boolean gotEarlyLocation, final boolean inWaitingList, final boolean inVc, final JSONArray roles, final JSONArray reacts, final List<Account> accounts) {
    this.id = id;
    this.serverNickname = serverNickname;
    this.timestampJoined = timestampJoined;
    this.gotPriority = gotPriority;
    this.gotEarlyLocation = gotEarlyLocation;
    this.inWaitingList = inWaitingList;
    this.inVc = inVc;
    this.roles = roles;
    this.reacts = reacts;
    this.accounts = accounts;
    this.avatar = setAvatar(avatar);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String printRaider() {
    return "ID: "
            + this.id
            + "\n"
            + "Joined: "
            + new Date(Long.parseLong(this.timestampJoined))
            + "\nGot Priority: "
            + this.gotPriority
            + "\nGot Early Location: "
            + this.gotEarlyLocation
            + "\nIn Waiting List: "
            + this.inWaitingList
            + "\nIn Voice Channel: "
            + this.inVc
            + "\nReacts: "
            + this.reacts.length()
            + "\nRoles: "
            + this.roles.length()
            + "\nAccounts: "
            + this.accounts.size()
            + "\n";
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
  public String getServerNickname() {
    return serverNickname;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public boolean isCelestial() {
    return roles.toList().contains("907008641079586817");
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public boolean isInVc() {
    return inVc;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public JSONArray getRoles() {
    return this.roles;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public JSONArray getReacts() {
    return this.reacts;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public ImageIcon getAvatar() {
    return this.avatar;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public List<Account> getAccounts() {
    return this.accounts;
  }

  /**
   * To be documented.
   *
   * @param avatar To be documented.
   * @return To be documented.
   */
  public ImageIcon setAvatar(final String avatar) {
    try {
      final Image image;
      if (avatar.contains(".gif")) {
        image = new ImageIcon(new URL(avatar)).getImage().getScaledInstance(128, 128, Image.SCALE_REPLICATE);
      } else {
        image = new ImageIcon(new URL(avatar)).getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
      }
      final ImageIcon icon = new ImageIcon(image);
      icon.setDescription(id);
      return icon;
    } catch (final Exception e) {
      return null;
    }
  }

  /**
   * To be documented.
   *
   * @param username To be documented.
   * @return To be documented.
   */
  public Account findAccount(final String username) {
    for (final Account a : accounts) {
      if (a.getName().equals(username)) {
        return a;
      }
    }
    return null;
  }

  /**
   * To be documented.
   *
   * @param width To be documented.
   * @param height To be documented.
   * @return To be documented.
   */
  public ImageIcon getResizedAvatar(final int width, final int height) {
    final ImageIcon resized = new ImageIcon(this.avatar.getImage().getScaledInstance(width, height, Image.SCALE_REPLICATE));
    resized.setDescription(avatar.getDescription());
    return resized;
  }
}

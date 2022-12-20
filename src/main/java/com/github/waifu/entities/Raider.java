package com.github.waifu.entities;

import com.github.waifu.gui.GUI;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.json.JSONArray;

/**
 *
 */
public class Raider {

  private final String id;
  private final String serverNickname;
  private final ImageIcon avatar;
  private final List<Account> accounts;
  private String timestampJoined;
  private boolean gotPriority;
  private boolean gotEarlyLocation;
  private boolean inWaitingList;
  private boolean inVC;
  private JSONArray roles;
  private JSONArray reacts;

  /**
   *
   */
  public Raider() {
    this.id = "";
    this.serverNickname = "";
    this.timestampJoined = "";
    this.avatar = null;
    this.gotPriority = false;
    this.gotEarlyLocation = false;
    this.inWaitingList = false;
    this.inVC = false;
    this.roles = null;
    this.reacts = null;
    this.accounts = new ArrayList<>();
  }

  public Raider(Account account) {
    this.id = "";
    this.serverNickname = "";
    this.timestampJoined = "";
    this.avatar = null;
    this.gotPriority = false;
    this.gotEarlyLocation = false;
    this.inWaitingList = false;
    this.inVC = false;
    this.roles = null;
    this.reacts = null;
    this.accounts = new ArrayList<>();
    accounts.add(account);
  }

  public Raider(String id, String avatar, String serverNickname) {
    this.id = id;
    this.avatar = setAvatar(avatar);
    this.serverNickname = serverNickname;
    this.accounts = null;
  }

  /**
   * @param id
   * @param serverNickname
   * @param timestampJoined
   * @param avatar
   * @param gotPriority
   * @param gotEarlyLocation
   * @param inWaitingList
   * @param inVC
   * @param roles
   * @param reacts
   * @param accounts
   */
  public Raider(String id, String serverNickname, String timestampJoined, String avatar, boolean gotPriority, boolean gotEarlyLocation, boolean inWaitingList, boolean inVC, JSONArray roles, JSONArray reacts, List<Account> accounts) {
    this.id = id;
    this.serverNickname = serverNickname;
    this.timestampJoined = timestampJoined;
    this.gotPriority = gotPriority;
    this.gotEarlyLocation = gotEarlyLocation;
    this.inWaitingList = inWaitingList;
    this.inVC = inVC;
    this.roles = roles;
    this.reacts = reacts;
    this.accounts = accounts;
    this.avatar = setAvatar(avatar);
  }

  /**
   * @return
   */
  public String printRaider() {
    return "ID: " + this.id + "\n" +
            "Joined: " + new Date(Long.parseLong(this.timestampJoined)) + "\n" +
            "Got Priority: " + this.gotPriority + "\n" +
            "Got Early Location: " + this.gotEarlyLocation + "\n" +
            "In Waiting List: " + this.inWaitingList + "\n" +
            "In Voice Channel: " + this.inVC + "\n" +
            "Reacts: " + this.reacts.length() + "\n" +
            "Roles: " + this.roles.length() + "\n" +
            "Accounts: " + this.accounts.size() + "\n";
  }

  public String getId() {
    return id;
  }

  public String getServerNickname() {
    return serverNickname;
  }

  public boolean isCelestial() {
    return roles.toList().contains("907008641079586817");
  }

  public boolean isInVC() {
    return inVC;
  }

  public JSONArray getRoles() {
    return this.roles;
  }

  public JSONArray getReacts() {
    return this.reacts;
  }

  /**
   * @return
   */
  public ImageIcon getAvatar() {
    return this.avatar;
  }

  /**
   * @return
   */
  public List<Account> getAccounts() {
    return this.accounts;
  }

  /**
   * @param avatar
   * @return
   */
  public ImageIcon setAvatar(String avatar) {
    if (GUI.getMode() == GUI.LAN_MODE) {
      if (this.accounts != null) {
        String[] extensions = {".png", ".jpg", ".gif"};
        for (String s : extensions) {
          File file = new File(GUI.TEST_RESOURCE_PATH + "raids/" + GUI.getJson().getJSONObject("raid").getInt("id") + "/players/" + this.accounts.get(0).getName() + "/avatar" + s);
          if (file.exists()) {
            try {
              return new ImageIcon(ImageIO.read(file));
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }
    } else {
      try {
        Image image;
        if (avatar.contains(".gif")) {
          image = new ImageIcon(new URL(avatar)).getImage().getScaledInstance(128, 128, Image.SCALE_REPLICATE);
        } else {
          image = new ImageIcon(new URL(avatar)).getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
        }
        ImageIcon icon = new ImageIcon(image);
        icon.setDescription(id);
        return icon;
      } catch (Exception e) {
        return null;
      }
    }
    return null;
  }

  public Account findAccount(String username) {
    for (Account a : accounts) {
      if (a.getName().equals(username)) {
        return a;
      }
    }
    return null;
  }

  /**
   * @param width
   * @param height
   */
  public ImageIcon getResizedAvatar(int width, int height) {
    ImageIcon resized = new ImageIcon(this.avatar.getImage().getScaledInstance(width, height, Image.SCALE_REPLICATE));
    resized.setDescription(avatar.getDescription());
    return resized;
  }
}

package com.github.waifu.entities;

import com.github.waifu.gui.GUI;
import org.json.JSONArray;
import org.jsoup.Jsoup;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class Raider {

    private String id;
    private String timestampJoined;
    private ImageIcon avatar;
    private boolean gotPriority;
    private boolean gotEarlyLocation;
    private boolean inWaitingList;
    private boolean inVC;
    private JSONArray roles;
    private JSONArray reacts;
    private final List<Account> accounts;
    //private final Account account;

    public Raider() {
        this.id = "";
        this.timestampJoined = "";
        this.avatar = null;
        this.gotPriority = false;
        this.gotEarlyLocation = false;
        this.inWaitingList = false;
        this.inVC = false;
        this.roles = null;
        this.reacts = null;
        this.accounts = null;
        //account = null;
    }

    public Raider(String id, String timestampJoined, String avatar, boolean gotPriority, boolean gotEarlyLocation, boolean inWaitingList, boolean inVC, JSONArray roles, JSONArray reacts, List<Account> accounts) {
        this.id = id;
        this.timestampJoined = timestampJoined;
        this.gotPriority = gotPriority;
        this.gotEarlyLocation = gotEarlyLocation;
        this.inWaitingList = inWaitingList;
        this.inVC = inVC;
        this.roles = roles;
        this.reacts = reacts;
        this.accounts = accounts;
        this.avatar = setAvatar(avatar);
        //this.account = account;
    }

    public String printRaider() {
        String raider = "ID: " + this.id + "\n" +
                        "Joined: " + new Date(Long.parseLong(this.timestampJoined)) + "\n" +
                        "Got Priority: " + this.gotPriority + "\n" +
                        "Got Early Location: " + this.gotEarlyLocation + "\n" +
                        "In Waiting List: " + this.inWaitingList + "\n" +
                        "In Voice Channel: " + this.inVC + "\n" +
                        "Reacts: " + this.reacts.length() + "\n" +
                        "Roles: " + this.roles.length() + "\n" +
                        "Accounts: " + this.accounts.size() + "\n";
        return raider;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public ImageIcon getAvatar() {
        return this.avatar;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

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
                return new ImageIcon(new URL(avatar));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setAvatarSize(int width, int height) {
        this.avatar = new ImageIcon(this.avatar.getImage().getScaledInstance(width, height, Image.SCALE_REPLICATE));
    }
}

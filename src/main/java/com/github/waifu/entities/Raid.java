package com.github.waifu.entities;

import com.github.waifu.util.Utilities;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Raid {

    private JSONObject json;
    private String type;
    private String description;
    private int maxMembers;
    private Raider raidLeader;
    private String location;
    private String name;
    private String id;
    private String status;
    private List<Raider> raiders;

    public Raid(JSONObject json) {
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
        addRaiders(json);
    }

    private Raider createRaidLeader(JSONObject creator) {
        return new Raider(creator.getString("id"), creator.getString("avatar"), creator.getString("server_nickname"));
    }

    public void addRaiders(JSONObject json) {
        JSONArray members = json.getJSONArray("members");

        for (int i = 0; i < members.length(); i++) {

            boolean inWaitingList = members.getJSONObject(i).getBoolean("in_waiting_list");

            if (!inWaitingList) {
                boolean gotPriority = members.getJSONObject(i).getBoolean("got_priority");
                boolean gotEarlyLocation = members.getJSONObject(i).getBoolean("got_earlyloc");
                boolean inVC = members.getJSONObject(i).getBoolean("in_vc");
                JSONArray reacts = members.getJSONObject(i).getJSONArray("reacts");
                JSONArray roles = members.getJSONObject(i).getJSONArray("roles");
                String id = members.getJSONObject(i).getString("user_id");
                String avatar = members.getJSONObject(i).getString("avatar");
                String timestampJoined = members.getJSONObject(i).getString("timestamp_joined");

                List<Account> accounts = new ArrayList<>();
                String serverNickname = members.getJSONObject(i).getString("server_nickname");
                List<String> usernames = Utilities.parseUsernamesFromNickname(serverNickname);
                for (String s : usernames) {
                    accounts.add(new Account(s));
                }
                raiders.add(new Raider(id, serverNickname, timestampJoined, avatar, gotPriority, gotEarlyLocation, inWaitingList, inVC, roles, reacts, accounts));
            }
        }
    }

    public void printRaiders() {
        for (Raider r : raiders) {
            for (Account a : r.getAccounts()) {
                System.out.print(a.getName() + " ");
            }
            System.out.println();
        }
    }

    public JSONObject getJson() {
        return json;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public Raider getRaidLeader() {
        return raidLeader;
    }

    public String getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public List<Raider> getRaiders() {
        return raiders;
    }

    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        for (Raider r : raiders) {
            for (Account a : r.getAccounts()) {
                usernames.add(a.getName());
            }
        }
        return usernames;
    }

    public Raider findRaiderByServerNickname(String serverNickname) {
        for (Raider r : raiders) {
            if (r.getServerNickname().equals(serverNickname)) {
                return r;
            }
        }
        return null;
    }

    public Raider findRaiderByUsername(String username) {
        for (Raider r : raiders) {
            for (Account a : r.getAccounts()) {
                if (a.getName().equals(username)) {
                    return r;
                }
            }
        }
        return null;
    }

    public int getNumberOfAccounts() {
        int accounts = 0;
        for (Raider r : raiders) {
            accounts += r.getAccounts().size();
        }
        return accounts;
    }
}

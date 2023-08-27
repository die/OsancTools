package com.github.waifu.handlers;

import com.github.waifu.entities.ViBotRaider;
import com.github.waifu.entities.VoiceChannel;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.Main;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Database Handler.
 */
public final class DatabaseHandler {

  public static Map<String, JSONObject> getRequirementSheets(final String token) {
    if (token == null || token.equals("")) return null;

    try {
      final URL url = new URL("https://data.mongodb-api.com/app/data-vpdth/endpoint/requirementSheets");
      final HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "application/json");
      con.setRequestProperty("api-key", token);
      final JSONArray response = new JSONArray(new JSONTokener(con.getInputStream()));
      con.getInputStream().close();
      con.disconnect();
      final Map<String, JSONObject> requirementSheets = new HashMap<>();
      for (int i = 0; i < response.length(); i++) {
        JSONObject sheet = response.getJSONObject(i);
        if (!sheet.has("name")) continue;
        requirementSheets.put(sheet.getString("name"), sheet);
      }
      return requirementSheets;
    } catch (final Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Map<String, JSONObject> getAFKChecks() {
    final String token = Main.getSettings().getToken();
    if (token == null || token.equals("")) return null;

    try {
      final URL url = new URL("https://data.mongodb-api.com/app/data-vpdth/endpoint/afkchecks");
      final HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "application/json");
      con.setRequestProperty("api-key", token);
      con.setDoOutput(true);

      final JSONObject raids = new JSONObject(new JSONTokener(con.getInputStream()));
      con.getInputStream().close();
      con.disconnect();

      Map<String, JSONObject> map = new HashMap<>();

      for (final String key : raids.keySet()) {
        final JSONObject raid = raids.getJSONObject(key);
        if (!raid.getJSONArray("members").isEmpty()) {
          final String name;
          if (raid.isNull("channel")) {
            name = raid.getJSONObject("raidStatusMessage").getJSONArray("embeds").getJSONObject(0).getJSONObject("author").getString("name").replace("AFK for ", "");
          } else {
            name = raid.getJSONObject("channel").getString("name");
          }
          map.put(name, raid);
        }
      }
      return map;
    } catch (final Exception e) {
      Gui.displayMessage("Failed to connect to db, did you input your token?", JOptionPane.ERROR_MESSAGE);
      return null;
    }
  }

  public static JSONArray getDiscordMembers(final String guild, final JSONArray ids) {
    final String token = Main.getSettings().getToken();
    if (token == null || token.equals("")) return null;

    if (Gui.getRaid() == null || Gui.getRaid().getViBotRaiders() == null) return null;
    try {
      final URL url = new URL("https://data.mongodb-api.com/app/data-vpdth/endpoint/members");
      final HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
      con.setRequestProperty("Accept", "application/json");
      con.setRequestProperty("Guild", guild);
      con.setRequestProperty("api-key", token);
      con.setDoOutput(true);
      final OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
      osw.write(ids.toString(4));
      osw.flush();
      osw.close();
      con.connect();
      final JSONArray members = new JSONArray(new JSONTokener(con.getInputStream()));
      con.getInputStream().close();
      con.disconnect();

      for (int i = 0; i < members.length(); i++) {
        JSONObject member = members.getJSONObject(i);

        VoiceChannel voiceChannel = member.isNull("vc") ? null : new VoiceChannel(member.getJSONObject("vc").getString("id"), member.getJSONObject("vc").getString("name"));
        String id = member.getString("id");
        String nickname = member.getString("nickname");
        String avatar = member.getString("avatar");
        JSONArray roles = member.getJSONArray("roles");
        boolean deaf = member.getBoolean("deaf");
        ViBotRaider viBotRaider = new ViBotRaider(id, nickname, avatar, roles, voiceChannel, deaf);
        Gui.getRaid().addViBotRaiders(viBotRaider);
      }
      return null;
    } catch (final Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}

package com.github.waifu.handlers;

import com.github.waifu.gui.Main;
import org.json.JSONObject;
import org.jsoup.Jsoup;

/**
 * Handler to work with the WebApp API.
 */
public class WebAppHandler {

  /**
   * getRaid method.
   *
   * <p>Returns the resulting JSONObject from getData
   *
   * @param raidId raid identification number as an user-input String.
   * @return JSONObject containing raid data.
   */
  public static JSONObject getRaid(final String raidId) {
    try {
      int id = Integer.parseInt(raidId);
      /* Oldest Valid ID is 9 */
      final int minId = 9;
      if (id >= minId) {
        return getData(id);
      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * getData method
   *
   * <p>Returns a JSONObject of data pertaining to an Oryx Sanctuary raid.
   *
   * @param id raid identification number as an Integer.
   * @return JSONObject containing raid data.
   */
  public static JSONObject getData(final int id) {
    JSONObject request = new JSONObject();
    String token = Main.settings.getToken();
    if (!token.equals("")) {
      request.put("access_token", token);
      request.put("raid_id", id);
      try {
        String data = Jsoup.connect("https://api.osanc.net/getRaid").requestBody(request.toString()).header("Content-Type", "application/json").ignoreContentType(true).post().body().text();
        return new JSONObject(data);
      } catch (Exception e) {
        return null;
      }
    } else {
      return null;
    }
  }
}

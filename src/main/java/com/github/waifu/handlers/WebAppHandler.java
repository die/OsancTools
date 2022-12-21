package com.github.waifu.handlers;

import com.github.waifu.gui.Main;
import org.json.JSONObject;
import org.jsoup.Jsoup;

/**
 * Handler to work with the WebApp API.
 */
public final class WebAppHandler {

  /**
   * To be documented.
   */
  private WebAppHandler() {

  }

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
      final int id = Integer.parseInt(raidId);
      /* Oldest Valid ID is 9 */
      final int minId = 9;
      if (id >= minId) {
        return getData(id);
      } else {
        return null;
      }
    } catch (final Exception e) {
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
    final JSONObject request = new JSONObject();
    final String token = Main.getSettings().getToken();
    if (!token.equals("")) {
      request.put("access_token", token);
      request.put("raid_id", id);
      try {
        final String data = Jsoup.connect("https://api.osanc.net/getRaid").requestBody(request.toString()).header("Content-Type", "application/json").ignoreContentType(true).post().body().text();
        return new JSONObject(data);
      } catch (final Exception e) {
        return null;
      }
    } else {
      return null;
    }
  }
}

package com.github.waifu.handlers;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

/**
 * Handler to work with the WebApp API
 */
public class WebAppHandler {

    /**
     * getRaid method.
     *
     * Returns the resulting JSONObject from getData
     *
     * @param raidId raid identification number as an user-input String.
     */
    public static JSONObject getRaid(String raidId) {
        try {
            int id = Integer.parseInt(raidId);
            return getData(id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * getData method
     *
     * Returns a JSONObject of data pertaining to an Oryx Sanctuary raid.
     *
     * @param id raid identification number as an Integer.
     */
    public static JSONObject getData(int id) {
        JSONObject request = new JSONObject();
        request.put("access_token", Preferences.userRoot().get("token", ""));
        request.put("raid_id", id);
        try {
           String data = Jsoup.connect("https://api.osanc.net/getRaid")
                    .requestBody(request.toString())
                    .header("Content-Type", "application/json")
                    .ignoreContentType(true)
                    .post()
                    .body()
                    .text();
           TimeUnit.SECONDS.sleep(1);
           return new JSONObject(data);
        } catch (Exception e) {
            return null;
        }
    }
}
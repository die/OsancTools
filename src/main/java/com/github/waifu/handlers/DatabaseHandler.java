package com.github.waifu.handlers;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Database Handler.
 */
public final class DatabaseHandler {

  /**
   * API Key todo: remove on release.
   */
  private static String key = "";
  /**
   * Insertion url.
   */
  private static String insertURL = "";
  /**
   * Get url.
   */
  private static String getURL = "";

  /**
   * To be documented.
   *
   * @param names To be documented.
   * @return To be documented.
   */
  public static boolean insert(final String names) {

    try {
      final URL url = new URL(insertURL);
      final HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "application/json");
      con.setRequestProperty("api-key", key);
      final JSONObject request = new JSONObject();
      request.put("document", new JSONObject().put("raiders", names.replace(",", "").replace("\n", " ")));
      con.setDoOutput(true);
      final OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
      osw.write(request.toString());
      osw.flush();
      osw.close();
      final JSONObject response = new JSONObject(new JSONTokener(con.getInputStream()));
      con.getInputStream().close();
      con.disconnect();
      return response.toString().contains("insertedId");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Get all entries.
   *
   * @return JSONArray.
   */
  public static JSONArray getAll() {
    try {
      final URL url = new URL(getURL);
      final HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "application/json");
      con.setRequestProperty("api-key", key);
      final JSONObject request = new JSONObject();
      con.setDoOutput(true);
      final OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
      osw.write(request.toString());
      osw.flush();
      osw.close();
      final JSONArray response = new JSONArray(new JSONTokener(con.getInputStream()));
      con.getInputStream().close();
      con.disconnect();
      return response;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}

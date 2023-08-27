package com.github.waifu.entities;

import com.github.waifu.util.Utilities;
import org.json.JSONArray;

public class ViBotRaider {

  private final String id;

  private final String nickname;

  private final String avatar;

  private final JSONArray roles;

  private final VoiceChannel voiceChannel;

  private final boolean deaf;

  public ViBotRaider(final String id, final String nickname, final String avatar, final JSONArray roles, final VoiceChannel voiceChannel, final boolean deaf) {
    this.id = id;
    this.nickname = nickname;
    this.avatar = avatar;
    this.roles = roles;
    this.voiceChannel = voiceChannel;
    this.deaf = deaf;
  }

  public String getId() {
    return id;
  }

  public String getNickname() {
    return nickname;
  }

  public String getAvatar() {
    return avatar;
  }

  public JSONArray getRoles() {
    return roles;
  }

  public VoiceChannel getVoiceChannel() {
    return voiceChannel;
  }

  public boolean inVC() {
    return voiceChannel != null;
  }

  public boolean isCelestial() {
    if (roles == null || roles.isEmpty()) return false;
    return roles.toList().contains("907008641079586817");
  }

  public boolean isDeaf() {
    return deaf;
  }

  public boolean hasIGN(final String ign) {
    return Utilities.parseUsernamesFromNickname(nickname).stream().anyMatch(ign::equalsIgnoreCase);
  }
}

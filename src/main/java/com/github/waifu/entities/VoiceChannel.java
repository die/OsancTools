package com.github.waifu.entities;

public class VoiceChannel {

  /**
   * Discord id of the voice channel.
   */
  private final String id;

  /**
   * Name of the voice channel.
   */
  private final String name;

  public VoiceChannel(final String id, final String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Get id.
   * @return id as a String.
   */
  public String getId() {
    return id;
  }

  /**
   * Get name.
   * @return name as a String.
   */
  public String getName() {
    return name;
  }
}

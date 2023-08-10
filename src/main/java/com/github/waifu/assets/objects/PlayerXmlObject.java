package com.github.waifu.assets.objects;

import com.github.waifu.assets.RotmgAssets;
import java.awt.Image;
import javax.swing.ImageIcon;
import org.json.JSONObject;

/**
 * To be documented.
 */
public class PlayerXmlObject {

  /**
   * The id of the class.
   */
  private String type;
  /**
   * The name of the class.
   */
  private String id;
  /**
   * The class of the xml object.
   */
  private String clazz;
  /**
   * The name of the spritesheet.
   */
  private String file;
  /**
   * The index of the sprite.
   */
  private String index;
  /**
   * The max amount of base hp.
   */
  private int maxHp;
  /**
   * The max amount of base mp.
   */
  private int maxMp;
  /**
   * The max amount of base attack.
   */
  private int maxAtt;
  /**
   * The max amount of base defense.
   */
  private int maxDef;
  /**
   * The max amount of base speed.
   */
  private int maxSpd;
  /**
   * The max amount of base dexterity.
   */
  private int maxDex;
  /**
   * The max amount of base vitality.
   */
  private int maxVit;
  /**
   * The max amount of base wisdom.
   */
  private int maxWis;
  /**
   * Image of the class.
   */
  private ImageIcon image;

  /**
   * Construct a player object.
   *
   * @param type the id of the object.
   * @param id the name of the object.
   */
  public PlayerXmlObject(final String type, final String id, final String clazz) {
    this.type = type;
    this.id = id;
    this.clazz = clazz;
  }

  /**
   * Get the type.
   *
   * @return type as a String.
   */
  public String getType() {
    return type;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getTypeAsInt() {
    if (this.type.contains("0x")) {
      return Integer.parseInt(this.type.replace("0x", ""), 16);
    } else {
      return Integer.parseInt(this.type);
    }
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getId() {
    return id;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getClazz() {
    return clazz;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getFile() {
    return file;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getIndex() {
    return index;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getMaxHp() {
    return maxHp;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getMaxMp() {
    return maxMp;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getMaxAtt() {
    return maxAtt;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getMaxDef() {
    return maxDef;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getMaxSpd() {
    return maxSpd;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getMaxDex() {
    return maxDex;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getMaxVit() {
    return maxVit;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getMaxWis() {
    return maxWis;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public ImageIcon getImage() {
    return image;
  }

  /**
   * To be documented.
   *
   * @param type To be documented.
   */
  public void setType(final String type) {
    this.type = type;
  }

  /**
   * To be documented.
   *
   * @param id To be documented.
   */
  public void setId(final String id) {
    this.id = id;
  }

  /**
   * To be documented.
   *
   * @param clazz To be documented.
   */
  public void setClazz(final String clazz) {
    this.clazz = clazz;
  }

  /**
   * To be documented.
   *
   * @param file To be documented.
   */
  public void setFile(final String file) {
    this.file = file;
  }

  /**
   * To be documented.
   *
   * @param index To be documented.
   */
  public void setIndex(final String index) {
    this.index = index;
  }

  /**
   * To be documented.
   *
   * @param maxHp To be documented.
   */
  public void setMaxHp(final int maxHp) {
    this.maxHp = maxHp;
  }

  /**
   * To be documented.
   *
   * @param maxMp To be documented.
   */
  public void setMaxMp(final int maxMp) {
    this.maxMp = maxMp;
  }

  /**
   * To be documented.
   *
   * @param maxAtt To be documented.
   */
  public void setMaxAtt(final int maxAtt) {
    this.maxAtt = maxAtt;
  }

  /**
   * To be documented.
   *
   * @param maxDef To be documented.
   */
  public void setMaxDef(final int maxDef) {
    this.maxDef = maxDef;
  }

  /**
   * To be documented.
   *
   * @param maxSpd To be documented.
   */
  public void setMaxSpd(final int maxSpd) {
    this.maxSpd = maxSpd;
  }

  /**
   * To be documented.
   *
   * @param maxDex To be documented.
   */
  public void setMaxDex(final int maxDex) {
    this.maxDex = maxDex;
  }

  /**
   * To be documented.
   *
   * @param maxVit To be documented.
   */
  public void setMaxVit(final int maxVit) {
    this.maxVit = maxVit;
  }

  /**
   * To be documented.
   *
   * @param maxWis To be documented.
   */
  public void setMaxWis(final int maxWis) {
    this.maxWis = maxWis;
  }

  /**
   * To be documented.
   */
  public void setImage() {
    for (int i = 0; i < RotmgAssets.spritesheet.getJSONArray("animatedSprites").length(); i++) {
      if (RotmgAssets.spritesheet.getJSONArray("animatedSprites").getJSONObject(i).getString("spriteSheetName").equals(file)) {
        int parsedIndex;
        if (this.index.contains("0x")) {
          parsedIndex = Integer.parseInt(this.index.replace("0x", ""), 16);
        } else {
          parsedIndex = Integer.parseInt(this.index);
        }
        if (RotmgAssets.spritesheet.getJSONArray("animatedSprites").getJSONObject(i).getInt("index") == parsedIndex && RotmgAssets.spritesheet.getJSONArray("animatedSprites").getJSONObject(i).getInt("direction") == 0) {
          int atlasId = RotmgAssets.spritesheet.getJSONArray("animatedSprites").getJSONObject(i).getJSONObject("spriteData").getInt("aId");
          final JSONObject pos = RotmgAssets.spritesheet.getJSONArray("animatedSprites").getJSONObject(i).getJSONObject("spriteData").getJSONObject("position");
          try {
            switch (atlasId) {
              case 1 -> image = new ImageIcon(RotmgAssets.groundTiles.getSubimage(pos.getInt("x"), pos.getInt("y"), pos.getInt("w"), pos.getInt("h")).getScaledInstance(16, 16, Image.SCALE_DEFAULT));
              case 2 -> image = new ImageIcon(RotmgAssets.characters.getSubimage(pos.getInt("x"), pos.getInt("y"), pos.getInt("w"), pos.getInt("h")).getScaledInstance(16, 16, Image.SCALE_DEFAULT));
              case 4 -> image = new ImageIcon(RotmgAssets.mapObjects.getSubimage(pos.getInt("x"), pos.getInt("y"), pos.getInt("w"), pos.getInt("h")).getScaledInstance(16, 16, Image.SCALE_DEFAULT));
              default -> image = null;
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  @Override
  public String toString() {
    return id;
  }
}

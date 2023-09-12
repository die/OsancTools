package com.github.waifu.assets.objects;

import com.github.waifu.assets.RotmgAssets;
import com.github.waifu.debug.Debug;
import java.awt.Image;
import javax.swing.ImageIcon;
import org.json.JSONObject;

/**
 * To be documented.
 */
public class SkinXmlObject {

  /**
   * To be documented.
   */
  private String type;
  /**
   * To be documented.
   */
  private String id;
  /**
   * To be documented.
   */
  private String file;
  /**
   * To be documented.
   */
  private String index;
  /**
   * To be documented.
   */
  private ImageIcon image;

  /**
   * To be documented.
   */
  public SkinXmlObject() {
    this.type = "";
    this.id = "";
    this.file = "";
    this.index = "";
  }

  /**
   * To be documented.
   *
   * @param type To be documented.
   * @param id To be documented.
   */
  public SkinXmlObject(final String type, final String id) {
    this.type = type;
    this.id = id;
    this.file = "";
    this.index = "";
  }

  /**
   * To be documented.
   *
   * @return To be documented.
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
   */
  public void setImage() {
    for (int i = 0; i < RotmgAssets.spritesheet.getJSONArray("animatedSprites").length(); i++) {
      if (RotmgAssets.spritesheet.getJSONArray("animatedSprites").getJSONObject(i).getString("spriteSheetName").equals(file)) {
        final int parsedIndex;
        if (this.index.contains("0x")) {
          parsedIndex = Integer.parseInt(this.index.replace("0x", ""), 16);
        } else {
          parsedIndex = Integer.parseInt(this.index);
        }
        if (RotmgAssets.spritesheet.getJSONArray("animatedSprites").getJSONObject(i).getInt("index") == parsedIndex && RotmgAssets.spritesheet.getJSONArray("animatedSprites").getJSONObject(i).getInt("direction") == 0) {
          final int atlasId = RotmgAssets.spritesheet.getJSONArray("animatedSprites").getJSONObject(i).getJSONObject("spriteData").getInt("aId");
          final JSONObject pos = RotmgAssets.spritesheet.getJSONArray("animatedSprites").getJSONObject(i).getJSONObject("spriteData").getJSONObject("position");
          try {
            switch (atlasId) {
              case 1 -> image = new ImageIcon(RotmgAssets.groundTiles.getSubimage(pos.getInt("x"), pos.getInt("y"), pos.getInt("w"), pos.getInt("h")).getScaledInstance(16, 16, Image.SCALE_DEFAULT));
              case 2 -> image = new ImageIcon(RotmgAssets.characters.getSubimage(pos.getInt("x"), pos.getInt("y"), pos.getInt("w"), pos.getInt("h")).getScaledInstance(16, 16, Image.SCALE_DEFAULT));
              case 4 -> image = new ImageIcon(RotmgAssets.mapObjects.getSubimage(pos.getInt("x"), pos.getInt("y"), pos.getInt("w"), pos.getInt("h")).getScaledInstance(16, 16, Image.SCALE_DEFAULT));
            }
          } catch (Exception e) {
            Debug.printStacktrace(e.toString());
            System.out.println(this.id + " " + e);
          }
        }
      }
    }
  }
}

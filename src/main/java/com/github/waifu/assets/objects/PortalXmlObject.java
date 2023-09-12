package com.github.waifu.assets.objects;

import com.github.waifu.assets.RotmgAssets;
import com.github.waifu.debug.Debug;
import java.awt.Image;
import javax.swing.ImageIcon;
import org.json.JSONObject;

/**
 * To be documented.
 */
public class PortalXmlObject {

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
  private String clazz;
  /**
   * The name of the item if it's part of an ST set.
   */
  private String displayId;
  /**
   * To be documented.
   */
  private String dungeonName;
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
  public PortalXmlObject() {
    this.type = "";
    this.id = "";
    this.clazz = "";
    this.displayId = "";
    this.dungeonName = "";
    this.file = "";
    this.index = "";
  }

  /**
   * To be documented.
   *
   * @param type To be documented.
   * @param id To be documented.
   * @param displayId To be documented.
   * @param clazz To be documented.
   */
  public PortalXmlObject(final String type, final String id, final String displayId, final String clazz) {
    this.type = type;
    this.id = id;
    this.clazz = clazz;
    this.displayId = displayId;
    this.dungeonName = "";
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
  public String getDisplayId() {
    return displayId;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getDungeonName() {
    return dungeonName;
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
   * @param displayId To be documented.
   */
  public void setDisplayId(final String displayId) {
    this.displayId = displayId;
  }

  /**
   * To be documented.
   *
   * @param dungeonName To be documented.
   */
  public void setDungeonName(final String dungeonName) {
    this.dungeonName = dungeonName;
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
    for (int i = 0; i < RotmgAssets.spritesheet.getJSONArray("sprites").length(); i++) {
      if (RotmgAssets.spritesheet.getJSONArray("sprites").getJSONObject(i).getString("spriteSheetName").equals(file)) {
        for (int j = 0; j < RotmgAssets.spritesheet.getJSONArray("sprites").getJSONObject(i).getJSONArray("elements").length(); j++) {
          int parsedIndex;

          if (this.index.contains("0x")) {
            parsedIndex = Integer.parseInt(this.index.replace("0x", ""), 16);
          } else {
            parsedIndex = Integer.parseInt(this.index);
          }

          if (RotmgAssets.spritesheet.getJSONArray("sprites").getJSONObject(i).getJSONArray("elements").getJSONObject(j).getInt("index") == parsedIndex) {

            int atlasId = RotmgAssets.spritesheet.getJSONArray("sprites").getJSONObject(i).getJSONArray("elements").getJSONObject(j).getInt("aId");
            final JSONObject pos = RotmgAssets.spritesheet.getJSONArray("sprites").getJSONObject(i).getJSONArray("elements").getJSONObject(j).getJSONObject("position");

            try {
              switch (atlasId) {
                case 1 -> image = new ImageIcon(RotmgAssets.groundTiles.getSubimage(pos.getInt("x"), pos.getInt("y"), pos.getInt("w"), pos.getInt("h")).getScaledInstance(16, 16, Image.SCALE_DEFAULT));
                case 2 -> image = new ImageIcon(RotmgAssets.characters.getSubimage(pos.getInt("x"), pos.getInt("y"), pos.getInt("w"), pos.getInt("h")).getScaledInstance(16, 16, Image.SCALE_DEFAULT));
                case 4 -> image = new ImageIcon(RotmgAssets.mapObjects.getSubimage(pos.getInt("x"), pos.getInt("y"), pos.getInt("w"), pos.getInt("h")).getScaledInstance(16, 16, Image.SCALE_DEFAULT));
              }
            } catch (Exception e) {
              Debug.printStacktrace(e.toString());
              e.printStackTrace();
            }
          }
        }
      }
    }
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  @Override
  public String toString() {
    if (!dungeonName.equals("")) return dungeonName;
    if (!displayId.equals("")) return displayId;
    return id.replace(" Portal", "");
  }
}

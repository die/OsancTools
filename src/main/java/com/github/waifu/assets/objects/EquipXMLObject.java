package com.github.waifu.assets.objects;

import com.github.waifu.assets.RotmgAssets;
import com.github.waifu.debug.Debug;
import java.awt.Image;
import javax.swing.ImageIcon;
import org.json.JSONObject;

public class EquipXMLObject {

  /**
   * The id of the equipment.
   */
  private String type;
  /**
   * The name of the equipment.
   */
  private String id;
  /**
   * The class of the xml object.
   */
  private String clazz;
  /**
   * The name of the item if it's part of an ST set.
   */
  private String displayId;
  /**
   * The name of the sprite sheet for its sprite.
   */
  private String file;
  /**
   * The index in the sprite sheet.
   */
  private String index;
  /**
   * The equipment's labels.
   */
  private String[] labels;
  /**
   * The image of the equipment.
   */
  private ImageIcon image;

  /**
   * To be documented.
   */
  public EquipXMLObject() {
    this.type = "";
    this.id = "";
    this.clazz = "";
    this.displayId = "";
    this.file = "";
    this.index = "";
    this.labels = null;
    this.image = null;
  }

  /**
   * To be documented.
   *
   * @param type To be documented.
   * @param id To be documented.
   * @param displayId To be documented.
   * @param clazz To be documented.
   */
  public EquipXMLObject(final String type, final String id, final String displayId, final String clazz) {
    this.type = type;
    this.id = id;
    this.clazz = clazz;
    this.displayId = displayId;
    this.file = "";
    this.index = "";
    this.labels = null;
    this.image = null;
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
  public String getDisplayId() {
    return displayId;
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
  public String[] getLabels() {
    return labels;
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
   * @param displayId To be documented.
   */
  public void setDisplayId(final String displayId) {
    this.displayId = displayId;
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
   * @param labels To be documented.
   */
  public void setLabels(final String[] labels) {
    this.labels = labels;
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
  public boolean isEquipment() {
    if (labels == null) {
      return false;
    } else {
      for (final String s : labels) {
        if (s.equalsIgnoreCase("CONSUMABLE")) {
          return false;
        } else if (s.equalsIgnoreCase("EFFECT")) {
          return false;
        } else if (s.equalsIgnoreCase("MARK")) {
          return false;
        } else if (s.equalsIgnoreCase("MATERIAL")) {
          return false;
        } else if (s.equalsIgnoreCase("MISC")) {
          return false;
        }
      }
      return true;
    }
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getEquipmentType() {
    if (labels != null) {
      for (String s : labels) {
        if (s.equalsIgnoreCase("WEAPON")) {
          return 0;
        } else if (s.equalsIgnoreCase("ABILITY")) {
          return 1;
        } else if (s.equalsIgnoreCase("ARMOR")) {
          return 2;
        } else if (s.equalsIgnoreCase("RING")) {
          return 3;
        }
      }
    }
    return -1;
  }

  @Override
  public String toString() {
    if (!displayId.equals("")) return displayId;
    if (!id.equals("")) return id;
    return type;
  }
}

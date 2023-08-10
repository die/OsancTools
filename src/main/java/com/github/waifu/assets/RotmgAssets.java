package com.github.waifu.assets;

import com.github.waifu.assets.objects.EquipXMLObject;
import com.github.waifu.assets.objects.PlayerXmlObject;
import com.github.waifu.assets.objects.PortalXmlObject;
import com.github.waifu.assets.objects.SkinXmlObject;
import com.github.waifu.assets.resextractor.ClassIDType;
import com.github.waifu.assets.resextractor.FileHeader;
import com.github.waifu.assets.resextractor.ObjectReader;
import com.github.waifu.assets.resextractor.SerializedFile;
import com.github.waifu.assets.resextractor.TextAsset;
import com.github.waifu.assets.resextractor.Texture2D;
import com.github.waifu.gui.Main;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.json.JSONObject;

/**
 * To be documented.
 */
public class RotmgAssets {

  /**
   * To be documented.
   */
  public static Map<Integer, EquipXMLObject> equipXMLObjectList = new HashMap<>();
  /**
   * To be documented.
   */
  public static List<PlayerXmlObject> playerXmlObjectList = new ArrayList<>();
  /**
   * To be documented.
   */
  public static List<SkinXmlObject> skinXmlObjectList = new ArrayList<>();
  /**
   * To be documented.
   */
  public static List<PortalXmlObject> portalXmlObjectList = new ArrayList<>();
  /**
   * To be documented.
   */
  public static JSONObject spritesheet;
  /**
   * To be documented.
   */
  public static BufferedImage mapObjects;
  /**
   * To be documented.
   */
  public static BufferedImage characters;
  /**
   * To be documented.
   */
  public static BufferedImage groundTiles;
  /**
   * To be documented.
   */
  SerializedFile serializedFile;

  /**
   * To be documented.
   *
   * @throws IOException To be documented.
   */
  public RotmgAssets() throws IOException {
    final File resources = new File(Main.getSettings().getResourceDir());
    final FileHeader header = new FileHeader(resources);
    if (header.type == FileHeader.AssetsFile) {
      serializedFile = new SerializedFile(resources, header);
    }
  }

  public boolean loadAssets(final JProgressBar progressBar, final JLabel label) {
    if (serializedFile == null || serializedFile.objects.length == 0) {
      return false;
    } else {
      /* Load spritesheet first. */
      final ObjectReader[] objects = serializedFile.objects;
      for (int i = 0; i < objects.length; i++) {
        if (objects[i].type != ClassIDType.TextAsset) continue;
        TextAsset textAsset = new TextAsset(objects[i]);
        if (!textAsset.getName().equals("spritesheet")) continue;
        label.setText("Loading spritesheet");
        if (parseSpritesheet(textAsset)) {
          progressBar.setValue(progressBar.getValue() + 1);
        }
        objects[i] = null;
        break;
      }

      /* Load everything else */
      for (final ObjectReader objectReader : objects) {
        if (objectReader == null) continue;
        switch (objectReader.type) {
          case TextAsset -> {
            final TextAsset textAsset = new TextAsset(objectReader);
            label.setText("Loading " + textAsset.getName());
            if (parseXML(textAsset)) {
              progressBar.setValue(progressBar.getValue() + 1);
            }
          }
          case Texture2D -> {
            final Texture2D texture2D = new Texture2D(objectReader);
            label.setText("Loading " + texture2D.getName());
            if (texture2D.getName().equals("mapObjects")) {
              if (parseMapObjects(texture2D)) {
                progressBar.setValue(progressBar.getValue() + 1);
              }
            } else if (texture2D.getName().equals("groundTiles")) {
              if (parseGroundTiles(texture2D)) {
                progressBar.setValue(progressBar.getValue() + 1);
              }
            } else if (texture2D.getName().equalsIgnoreCase("characters")) {
              if (parseCharacters(texture2D)) {
                progressBar.setValue(progressBar.getValue() + 1);
              }
            }
          }
        }
      }
      spritesheet = null;
      mapObjects = null;
      characters = null;
      groundTiles = null;
      return true;
    }
  }

  private boolean parseXML(final TextAsset textAsset) {
    try {
      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
      XMLEventReader reader = xmlInputFactory.createXMLEventReader(new ByteArrayInputStream(textAsset.getmScript()));
      String type = "";
      String id = "";
      String displayId = "";
      boolean next = false;
      while (reader.hasNext()) {
        XMLEvent nextEvent = reader.nextEvent();

        if (nextEvent.isStartElement()) {
          StartElement startElement = nextEvent.asStartElement();

          switch (startElement.getName().getLocalPart()) {
            case "Object" -> {
              type = startElement.getAttributeByName(new QName("type")).getValue().strip().trim();
              id = startElement.getAttributeByName(new QName("id")).getValue().strip().trim();
            }
            case "DisplayId" -> {
              nextEvent = reader.nextEvent();
              displayId = nextEvent.asCharacters().getData();
              next = true;
            }
            case "Class" -> {
              nextEvent = reader.nextEvent();
              String clazz = nextEvent.asCharacters().getData();

              if (!next) displayId = "";
              switch (clazz) {
                case "Equipment" -> parseEquipmentXml(reader, new EquipXMLObject(type, id, displayId, clazz));
                case "Player" -> parsePlayerXml(reader, new PlayerXmlObject(type, id, clazz));
                case "Portal" -> parsePortalXml(reader, new PortalXmlObject(type, id, displayId, clazz));
              }
              next = false;
            }
          }
        }
      }

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean parseEquipmentXml(XMLEventReader reader, EquipXMLObject equipXMLObject) {
    if (equipXMLObject == null) return false;
    try {
      while (reader.hasNext()) {
        XMLEvent nextEvent = reader.nextEvent();
        if (nextEvent.isStartElement()) {
          StartElement startElement = nextEvent.asStartElement();

          switch (startElement.getName().getLocalPart()) {
            case "File" -> {
              nextEvent = reader.nextEvent();
              final String file = nextEvent.asCharacters().getData().strip().trim();
              equipXMLObject.setFile(file);
            }
            case "Index" -> {
              nextEvent = reader.nextEvent();
              final String index = nextEvent.asCharacters().getData().strip().trim();
              equipXMLObject.setIndex(index);
            }
            case "DisplayId" -> {
              nextEvent = reader.nextEvent();
              final String displayId = nextEvent.asCharacters().getData();
              equipXMLObject.setDisplayId(displayId);
            }
            case "Labels" -> {
              nextEvent = reader.nextEvent();
              equipXMLObject.setLabels(nextEvent.asCharacters().getData().split(","));
            }
          }
        }
        if (nextEvent.isEndElement()) {
          final EndElement endElement = nextEvent.asEndElement();
          if (endElement.getName().getLocalPart().equals("Object")) {
            // set the image
            equipXMLObject.setImage();
            equipXMLObjectList.put(equipXMLObject.getTypeAsInt(), equipXMLObject);
            return true;
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean parsePlayerXml(XMLEventReader reader, PlayerXmlObject playerXMLObject) {
    if (playerXMLObject == null) return false;
    try {
      while (reader.hasNext()) {
        XMLEvent nextEvent = reader.nextEvent();
        if (nextEvent.isStartElement()) {
          StartElement startElement = nextEvent.asStartElement();
          switch (startElement.getName().getLocalPart()) {
            case "File":
              nextEvent = reader.nextEvent();
              String file = nextEvent.asCharacters().getData().strip().trim();
              playerXMLObject.setFile(file);
              break;
            case "Index":
              nextEvent = reader.nextEvent();
              String index = nextEvent.asCharacters().getData().strip().trim();
              playerXMLObject.setIndex(index);
              break;
            case "MaxHitPoints":
              nextEvent = reader.nextEvent();
              playerXMLObject.setMaxHp(Integer.parseInt(startElement.getAttributeByName(new QName("max")).getValue()));
              break;
            case "MaxMagicPoints":
              nextEvent = reader.nextEvent();
              playerXMLObject.setMaxMp(Integer.parseInt(startElement.getAttributeByName(new QName("max")).getValue()));
              break;
            case "Attack":
              nextEvent = reader.nextEvent();
              playerXMLObject.setMaxAtt(Integer.parseInt(startElement.getAttributeByName(new QName("max")).getValue()));
              break;
            case "Defense":
              nextEvent = reader.nextEvent();
              playerXMLObject.setMaxDef(Integer.parseInt(startElement.getAttributeByName(new QName("max")).getValue()));
              break;
            case "Speed":
              nextEvent = reader.nextEvent();
              playerXMLObject.setMaxSpd(Integer.parseInt(startElement.getAttributeByName(new QName("max")).getValue()));
              break;
            case "Dexterity":
              nextEvent = reader.nextEvent();
              playerXMLObject.setMaxDex(Integer.parseInt(startElement.getAttributeByName(new QName("max")).getValue()));
              break;
            case "HpRegen":
              nextEvent = reader.nextEvent();
              playerXMLObject.setMaxVit(Integer.parseInt(startElement.getAttributeByName(new QName("max")).getValue()));
              break;
            case "MpRegen":
              nextEvent = reader.nextEvent();
              playerXMLObject.setMaxWis(Integer.parseInt(startElement.getAttributeByName(new QName("max")).getValue()));
              break;
          }
        }
        if (nextEvent.isEndElement()) {
          EndElement endElement = nextEvent.asEndElement();
          if (endElement.getName().getLocalPart().equals("Object")) {
            playerXMLObject.setImage();
            playerXmlObjectList.add(playerXMLObject);
            return true;
          }
        }
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean parsePortalXml(XMLEventReader reader, PortalXmlObject portalXMLObject) {
    if (portalXMLObject == null) return false;
    try {
      while (reader.hasNext()) {
        XMLEvent nextEvent = reader.nextEvent();
        if (nextEvent.isStartElement()) {
          StartElement startElement = nextEvent.asStartElement();
          switch (startElement.getName().getLocalPart()) {
            case "File":
              nextEvent = reader.nextEvent();
              String file = nextEvent.asCharacters().getData().strip().trim();
              portalXMLObject.setFile(file);
              break;
            case "Index":
              nextEvent = reader.nextEvent();
              String index = nextEvent.asCharacters().getData().strip().trim();
              portalXMLObject.setIndex(index);
              break;
            case "DisplayId":
              nextEvent = reader.nextEvent();
              final String displayId = nextEvent.asCharacters().getData();
              portalXMLObject.setDisplayId(displayId);
              break;
            case "DungeonName":
              nextEvent = reader.nextEvent();
              final String dungeonName = nextEvent.asCharacters().getData();
              portalXMLObject.setDungeonName(dungeonName);
              break;
          }
        }
        if (nextEvent.isEndElement()) {
          EndElement endElement = nextEvent.asEndElement();
          if (endElement.getName().getLocalPart().equals("Object")) {
            portalXMLObject.setImage();
            portalXmlObjectList.add(portalXMLObject);
            return true;
          }
        }
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean parseSkinXml(final TextAsset textAsset) {
    try {
      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
      XMLEventReader reader = xmlInputFactory.createXMLEventReader(new ByteArrayInputStream(textAsset.getmScript()));
      skinXmlObjectList = new ArrayList<>();
      SkinXmlObject skinXMLObject = null;
      while (reader.hasNext()) {
        XMLEvent nextEvent = reader.nextEvent();
        if (nextEvent.isStartElement()) {
          StartElement startElement = nextEvent.asStartElement();
          switch (startElement.getName().getLocalPart()) {
            case "Object":
              String type = startElement.getAttributeByName(new QName("type")).getValue().strip().trim();
              String id = startElement.getAttributeByName(new QName("id")).getValue().strip().trim();
              skinXMLObject = new SkinXmlObject(type, id);
              break;
            case "File":
              nextEvent = reader.nextEvent();
              String file = nextEvent.asCharacters().getData().strip().trim();
              skinXMLObject.setFile(file);
              break;
            case "Index":
              nextEvent = reader.nextEvent();
              String index = nextEvent.asCharacters().getData().strip().trim();
              skinXMLObject.setIndex(index);
              break;
          }
        }
        if (nextEvent.isEndElement()) {
          EndElement endElement = nextEvent.asEndElement();
          if (endElement.getName().getLocalPart().equals("Object")) {
            skinXmlObjectList.add(skinXMLObject);
          }
        }
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean parseMapObjects(final Texture2D texture2D) {
    try {
      final int width = texture2D.getMWidth();
      final int height = texture2D.getMHeight();
      final byte[] data = texture2D.getImageData();

      final DataBuffer buffer = new DataBufferByte(data, data.length);
      final WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, 4 * width, 4, new int[]{0, 1, 2, 3}, null);
      final ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), true, true, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
      final BufferedImage image = new BufferedImage(cm, raster, true, null);
      BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = flippedImage.createGraphics();
      g.drawImage(image, 0, height, width, -height,  null);
      g.dispose();
      mapObjects = flippedImage;
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean parseGroundTiles(final Texture2D texture2D) {
    try {
      final int width = texture2D.getMWidth();
      final int height = texture2D.getMHeight();
      final byte[] data = texture2D.getImageData();

      final DataBuffer buffer = new DataBufferByte(data, data.length);
      final WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, 4 * width, 4, new int[]{0, 1, 2, 3}, null);
      final ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), true, true, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
      final BufferedImage image = new BufferedImage(cm, raster, true, null);
      BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = flippedImage.createGraphics();
      g.drawImage(image, 0, height, width, -height,  null);
      g.dispose();
      groundTiles = flippedImage;
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean parseCharacters(final Texture2D texture2D) {
    try {
      final int width = texture2D.getMWidth();
      final int height = texture2D.getMHeight();
      final byte[] data = texture2D.getImageData();

      final DataBuffer buffer = new DataBufferByte(data, data.length);
      final WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, 4 * width, 4, new int[]{0, 1, 2, 3}, null);
      final ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), true, true, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
      final BufferedImage image = new BufferedImage(cm, raster, true, null);
      BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = flippedImage.createGraphics();
      g.drawImage(image, 0, height, width, -height,  null);
      g.dispose();
      characters = flippedImage;
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean parseSpritesheet(final TextAsset textAsset) {
    try {
      spritesheet = new JSONObject(new String(textAsset.getmScript()));
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}

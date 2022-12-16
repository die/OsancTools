package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Id to name class. Used to convert incoming realm IDs to the resources names.
 */
public class IdToName {
    private final String l;
    private final int id;
    private final String idName;
    private final String display;
    private final String clazz;
    private final String group;
    private final String projectile;
    private Projectile[] projectiles = null;
    private final String texture;
    private Texture[] textures = null;
    private static final HashMap<Integer, IdToName> objectID = new HashMap<>();
    private static final HashMap<Integer, IdToName> tileID = new HashMap<>();

    /**
     * Constructor for the object resources.
     *
     * @param l          Base string before parsing
     * @param id         Id of the resource
     * @param idName     Name of the resource
     * @param display    Display name of the resource
     * @param clazz      Class of the resource
     * @param projectile Projectile min,max,armorPiercing,(repeated) listed
     * @param texture    Texture name and index used to find the image
     * @param group      Group of the resource
     */
    public IdToName(String l, int id, String idName, String display, String clazz, String projectile, String texture, String group) {
        this.l = l;
        this.id = id;
        this.idName = idName;
        this.display = display;
        this.clazz = clazz;
        this.projectile = projectile;
        this.texture = texture;
        this.group = group;
    }

    /**
     * Constructor for the tile resources.
     *
     * @param l       Base string before parsing
     * @param id      Id of the resource
     * @param idName  Name of the resource
     * @param texture Texture name and index used to find the image
     */
    public IdToName(String l, int id, String idName, String texture) {
        this.l = l;
        this.id = id;
        this.idName = idName;
        this.texture = texture;

        display = "";
        clazz = "";
        group = "";
        projectile = "";
    }

    /**
     * Construct the list on start of using this class.
     */
    static {
        readObjectList();
    }

    /**
     * Method to grab the full list of object resource's from file and construct the hashmap.
     */
    private static void readObjectList() {
        String fileName = "ObjectID5.list";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Util.resourceFilePath(fileName), StandardCharsets.UTF_8));
            String line;

            while ((line = br.readLine()) != null) {
                String[] l = line.split(":");
                int id = Integer.parseInt(l[0]);
                String display = l[1];
                String clazz = l[2];
                String group = l[3];
                String projectile = l[4];
                String texture = l[5];
                String idName = l[6];
                objectID.put(id, new IdToName(line, id, idName, display, clazz, projectile, texture, group));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        objectID.put(-1, new IdToName("", -1, "Unloaded", "Unloaded", "", "", "", "Unloaded"));
    }

    /**
     * Method to grab the name of the object resource.
     * If display name is not present, use the regular name.
     *
     * @param id Id of the object.
     * @return Best descriptive name of the resource
     */
    public static String objectName(int id) {
        IdToName i = objectID.get(id);
        if (i == null) return "";
        if (i.display.equals("")) return i.idName;
        return i.display;
    }

    /**
     * Method to grab the name of the tile resource.
     * If display name is not present, use the regular name.
     *
     * @param id Id of the tile.
     * @return Best descriptive name of the resource
     */
    public static String tileName(int id) {
        IdToName i = objectID.get(id);
        if (i == null) {
            return "";
        }
        return i.idName;
    }

    /**
     * Common name of the object.
     *
     * @param id Id of the object.
     * @return Regular name of the object.
     */
    public static String getOjbectIdName(int id) {
        IdToName i = objectID.get(id);
        return i.idName;
    }

    /**
     * Display name of the object.
     *
     * @param id Id of the object.
     * @return Display name of the object.
     */
    public static String getDisplayName(int id) {
        IdToName i = objectID.get(id);
        return i.display;
    }




    /**
     * Parses the texture string to the texture object.
     *
     * @param entity that should be texture parsed
     * @return List of parsed textures
     */
    private static Texture[] parseObjectTexture(IdToName entity) {
        String[] l = entity.texture.split(",");
        Texture[] t = new Texture[l.length / 2];
        int index = 0;
        try {
            for (int i = 0; i < l.length; i += 2) {
                String name = l[i];
                int ix = Integer.parseInt(l[1 + i]);
                t[index] = new Texture(name, ix);
                index++;
            }
        } catch (Exception e) {
            System.out.println(entity);
        }

        return t;
    }

    /**
     * Object texture file name.
     *
     * @param id  Id of the object.
     * @param num Sub texture number
     * @return File name of the texture
     */
    public static String getObjectTextureName(int id, int num) {
        IdToName i = objectID.get(id);
        if (i.textures == null) i.textures = parseObjectTexture(i);
        return i.textures[num].name;
    }

    /**
     * Object texture file index.
     *
     * @param id  Id of the object.
     * @param num Sub texture number
     * @return File index of the texture
     */
    public static int getObjectTextureIndex(int id, int num) {
        IdToName i = objectID.get(id);
        if (i.textures == null) i.textures = parseObjectTexture(i);
        return i.textures[num].index;
    }

    /**
     * Tile texture file name.
     *
     * @param id  Id of the object.
     * @param num Sub texture number
     * @return File name of the texture
     */
    public static String getTileTextureName(int id, int num) {
        IdToName i = objectID.get(id);
        if (i.textures == null) i.textures = parseObjectTexture(i);
        return i.textures[num].name;
    }

    /**
     * Simple class to store projectile info
     */
    private static class Projectile {
        int min; // min dmg
        int max; // max dmg
        boolean ap; // armor piercing

        public Projectile(int min, int max, boolean ap) {
            this.min = min;
            this.max = max;
            this.ap = ap;
        }
    }

    /**
     * Simple class to store texture info
     */
    private static class Texture {
        String name;
        int index;

        public Texture(String name, int index) {
            this.name = name;
            this.index = index;
        }
    }

    public String toString() {
        return l;
    }
}

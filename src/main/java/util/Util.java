package util;

import java.io.*;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Generic utility class for utility methods.
 */
public class Util {

    public static boolean saveLogs = true;
    private static HashMap<String, PrintWriter> printWriter = new HashMap<>();

    /**
     * Fast method to return an integer from the first 4 bytes of a byte array.
     *
     * @param bytes The byte array to extract the integer from.
     * @return The integer converted from the first 4 bytes of an array.
     */
    public static int decodeInt(byte[] bytes) {
        return (Byte.toUnsignedInt(bytes[0]) << 24) | (Byte.toUnsignedInt(bytes[1]) << 16) | (Byte.toUnsignedInt(bytes[2]) << 8) | Byte.toUnsignedInt(bytes[3]);
    }

    /**
     * Receives a hex string and returns it in byte array format.
     *
     * @param hex String of hex data where a pair of numbers represents a byte.
     * @return Returns a byte array converted from the passed hex string.
     */
    public static byte[] hexStringToByteArray(String hex) {
        int l = hex.length();
        byte[] data = new byte[l / 2];
        for (int i = 0; i < l; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * String output of all objects in the list.
     *
     * @param list List all objects to be printed.
     * @return String output of the list.
     */
    public static String showAll(Object[] list) {
        StringBuilder sb = new StringBuilder();
        for (Object o : list) {
            sb.append(o);
        }
        return sb.toString();
    }

    /**
     * Returns the resource file as stream in the resource's folder.
     *
     * @param fileName Name of resource file.
     * @return The resource file as stream.
     */
    public static InputStream resourceFilePath(String fileName) throws URISyntaxException {
        return IdToName.class.getClassLoader().getResourceAsStream(fileName);
    }
}

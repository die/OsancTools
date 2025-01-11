package com.github.waifu.assets.resextractor;

import com.github.waifu.gui.SplashScreen;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHeader {
    public static final int ResourceFile = 1;

    public static final int AssetsFile = 2;

    public long metadataSize;

    public long fileSize;

    public long version;

    public long dataOffset;

    public long size;

    public boolean bigEndian;

    public byte[] reserved;

    public long unknown;

    public int type;

    public FileHeader(File f) throws IOException {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(f);
        } catch (FileNotFoundException exception) {
            SplashScreen.chooseResourcesFile();
        }
        if (fin == null)
            return;
        DataInputStream reader = new DataInputStream(fin);
        this.size = f.length();
        this.metadataSize = Integer.toUnsignedLong(reader.readInt());
        this.fileSize = Integer.toUnsignedLong(reader.readInt());
        this.version = Integer.toUnsignedLong(reader.readInt());
        this.dataOffset = Integer.toUnsignedLong(reader.readInt());
        if (this.version >= 22L) {
            this.bigEndian = reader.readBoolean();
            this.reserved = new byte[3];
            reader.read(this.reserved, 0, 3);
            this.metadataSize = Integer.toUnsignedLong(reader.readInt());
            this.fileSize = reader.readLong();
            this.dataOffset = reader.readLong();
            this.unknown = reader.readLong();
        }
        if (this.version > 100L || this.fileSize < 0L || this.dataOffset < 0L || this.fileSize > this.size || this.metadataSize > this.size || this.version > this.size || this.dataOffset > this.size || this.fileSize < this.metadataSize || this.fileSize < this.dataOffset) {
            this.type = 1;
        } else {
            this.type = 2;
        }
    }
}

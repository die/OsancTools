package com.github.waifu.assets.resextractor;

public class TextAsset {
    private DataReader reader;

    private String name;

    private byte[] mScript;

    public TextAsset(ObjectReader o) {
        this.reader = o.reader;
        this.reader.setPosition((int)o.byteStart);
        this.name = this.reader.readAlignedString();
        int bytes = this.reader.readInt();
        this.mScript = this.reader.readByte(bytes);
    }

    public String getName() {
        return this.name;
    }

    public byte[] getmScript() {
        return this.mScript;
    }
}

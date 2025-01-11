package com.github.waifu.assets.resextractor;

import com.github.waifu.packets.reader.BufferReader;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;

public class DataReader extends BufferReader {
    public DataReader(ByteBuffer buffer) {
        super(buffer);
    }

    public static DataReader getReader(File f, boolean endian) throws IOException {
        byte[] data = Files.readAllBytes(f.toPath());
        ByteBuffer buffer = ByteBuffer.wrap(data).order(endian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        return new DataReader(buffer);
    }

    public long readLong() {
        return getBuffer().getLong();
    }

    public void read(byte[] reserved) {
        getBuffer().get(reserved);
    }

    public void alignStream() {
        int skip = (4 - getBuffer().position() % 4) % 4;
        for (int i = 0; i < skip; i++)
            getBuffer().get();
    }

    public byte[] readByte(int count) {
        byte[] bytes = new byte[count];
        getBuffer().get(bytes);
        return bytes;
    }

    public long getPosition() {
        return getBuffer().position();
    }

    public void setPosition(int pos) {
        getBuffer().position(pos);
    }

    public String readStringToNull() {
        int c = 0;
        StringBuilder s = new StringBuilder();
        while (true) {
            if (c != 0)
                s.append((char)c);
            c = getBuffer().get();
            if (c == 0)
                return s.toString();
        }
    }

    public String readAlignedString() {
        int length = readInt();
        if (0 < length && length < getRemainingBytes()) {
            byte[] str = new byte[length];
            getBuffer().get(str);
            alignStream();
            return new String(str);
        }
        return "";
    }

    public String[] readStringArray() {
        int num = readInt();
        String[] strs = new String[num];
        for (int i = 0; i < num; i++)
            strs[i] = readAlignedString();
        return strs;
    }

    public byte[] readByteArrayInt() {
        byte[] out = new byte[readInt()];
        getBuffer().get(out);
        return out;
    }
}

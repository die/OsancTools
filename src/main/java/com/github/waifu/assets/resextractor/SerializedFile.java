package com.github.waifu.assets.resextractor;

import java.io.File;
import java.io.IOException;

public class SerializedFile {
    public long version;

    public long dataOffset;

    public String unityVersion;

    public Platform mTargetPlatform;

    public boolean enableTypeTree = false;

    public int bigIdEnabled;

    public int scriptCount;

    public ScriptTypes[] scriptTypes;

    public int externalsCount;

    public Externals[] externals;

    public int refTypeCount;

    public SerializedType[] refTypes;

    public String userInformation;

    public SerializedType[] types;

    public long[] typeDependencies;

    public ObjectReader[] objects;

    public SerializedFile(File f, FileHeader header) throws IOException {
        this.version = header.version;
        this.dataOffset = header.dataOffset;
        DataReader reader = DataReader.getReader(f, header.bigEndian);
        for (int i = 0; i < 12; i++)
            reader.readInt();
        if (this.version >= 7L)
            this.unityVersion = reader.readStringToNull();
        if (this.version >= 8L)
            this.mTargetPlatform = Platform.byOrdinal(reader.readInt());
        if (this.version >= 13L)
            this.enableTypeTree = reader.readBoolean();
        int type_count = reader.readInt();
        this.types = SerializedType.getTypes(this, type_count, reader);
        this.bigIdEnabled = 0;
        if (7L <= this.version && this.version < 14L)
            this.bigIdEnabled = reader.readInt();
        int object_count = reader.readInt();
        this.objects = new ObjectReader[object_count];
        int j;
        for (j = 0; j < object_count; j++)
            this.objects[j] = new ObjectReader(this, reader);
        if (this.version >= 11L) {
            this.scriptCount = reader.readInt();
            this.scriptTypes = new ScriptTypes[this.scriptCount];
            for (j = 0; j < this.scriptCount; j++) {
                ScriptTypes st = new ScriptTypes();
                st.localSerializedFileIndex = reader.readInt();
                if (this.version < 14L) {
                    st.localIdentifierInFile = reader.readInt();
                } else {
                    reader.alignStream();
                    st.localIdentifierInFile = reader.readLong();
                }
                this.scriptTypes[j] = st;
            }
        }
        this.externalsCount = reader.readInt();
        this.externals = new Externals[this.externalsCount];
        for (j = 0; j < this.externalsCount; j++) {
            Externals ex = new Externals();
            if (this.version >= 6L)
                ex.tempEmpty = reader.readStringToNull();
            if (this.version >= 5L) {
                ex.guid = reader.readByte(16);
                ex.type = reader.readInt();
            }
            ex.path = reader.readStringToNull();
            this.externals[j] = ex;
        }
        if (this.version >= 20L) {
            this.refTypeCount = reader.readInt();
            this.refTypes = SerializedType.getTypes(this, this.refTypeCount, reader);
        }
        if (this.version >= 5L)
            this.userInformation = reader.readStringToNull();
        for (ObjectReader obj : this.objects) {
            if (obj.type == ClassIDType.AssetBundle)
                throw new RuntimeException("Reading unimplemented bundles");
        }
    }

    public static class ScriptTypes {
        int localSerializedFileIndex;

        long localIdentifierInFile;
    }

    public static class Externals {
        String tempEmpty;

        byte[] guid;

        int type;

        String path;
    }
}

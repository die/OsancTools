package com.github.waifu.assets.resextractor;

import java.io.IOException;

public class SerializedType {
    public int classId;

    public boolean isStrippedType;

    public short scriptTypeIndex;

    public byte[] scriptId = new byte[16];

    public byte[] oldTypeHash = new byte[16];

    public static SerializedType[] getTypes(SerializedFile serializedFile, int typeCount, DataReader reader) throws IOException {
        SerializedType[] types = new SerializedType[typeCount];
        for (int i = 0; i < typeCount; i++) {
            SerializedType st = new SerializedType();
            st.classId = reader.readInt();
            if (serializedFile.version >= 16L)
                st.isStrippedType = reader.readBoolean();
            if (serializedFile.version >= 17L)
                st.scriptTypeIndex = reader.readShort();
            if (serializedFile.version >= 13L) {
                if ((serializedFile.version < 16L && st.classId < 0) || (serializedFile.version >= 16L && st.classId == 114))
                    reader.read(st.scriptId);
                reader.read(st.oldTypeHash);
            }
            if (serializedFile.enableTypeTree) {
                if (serializedFile.version >= 21L) {
                    int type_dep_size = reader.readInt();
                    serializedFile.typeDependencies = new long[type_dep_size];
                    for (int j = 0; j < type_dep_size; j++)
                        serializedFile.typeDependencies[j] = Integer.toUnsignedLong(reader.readInt());
                }
                throw new IOException("enable_type_tree");
            }
            types[i] = st;
        }
        return types;
    }
}

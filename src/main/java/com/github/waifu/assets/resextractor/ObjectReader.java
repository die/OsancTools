package com.github.waifu.assets.resextractor;

public class ObjectReader {
    public long version;

    public long dataOffset;

    public DataReader reader;

    public long pathId;

    public long byteStartOffset;

    public int bstTupple;

    public long byteStart;

    public long byteHeaderOffset;

    public long byteBaseOffset;

    public long byteSizeOffset;

    public long byteSize;

    public int typeId;

    public SerializedType serializedType;

    public int classId;

    public ClassIDType type;

    public int isDestroyed;

    public byte stripped;

    public short scriptTypeIndex;

    public ObjectReader(SerializedFile serializedFile, DataReader reader) {
        this.version = serializedFile.version;
        this.reader = reader;
        this.dataOffset = serializedFile.dataOffset;
        if (serializedFile.bigIdEnabled != 0) {
            this.pathId = reader.readLong();
        } else if (this.version < 14L) {
            this.pathId = reader.readInt();
        } else {
            reader.alignStream();
            this.pathId = reader.readLong();
        }
        if (this.version >= 22L) {
            this.byteStartOffset = reader.getPosition();
            this.bstTupple = 8;
            this.byteStart = reader.readLong();
        } else {
            this.byteStartOffset = reader.getPosition();
            this.bstTupple = 4;
            this.byteStart = reader.readInt();
        }
        this.byteStart += this.dataOffset;
        this.byteHeaderOffset = this.dataOffset;
        this.byteBaseOffset = 0L;
        this.byteSizeOffset = reader.getPosition();
        this.byteSize = Integer.toUnsignedLong(reader.readInt());
        this.typeId = reader.readInt();
        if (this.version < 16L) {
            short s = reader.readShort();
        } else {
            SerializedType typ = serializedFile.types[this.typeId];
            this.serializedType = typ;
            this.classId = typ.classId;
        }
        this.type = ClassIDType.byOrdinal(this.classId);
        if (this.version < 11L) {
            this.isDestroyed = reader.readUnsignedShort();
        } else if (this.version < 17L) {
            this.scriptTypeIndex = reader.readShort();
        }
        if (this.version == 15L || this.version == 16L)
            this.stripped = reader.readByte();
    }
}

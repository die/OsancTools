package com.github.waifu.assets.resextractor;

public class Texture2D {
    DataReader reader;

    private String name;

    String path;

    boolean mDownscaleFallback;

    boolean mIsAlphaChannelOptional;

    boolean mIsReadable;

    boolean mIsPreProcessed;

    boolean mIgnoreMasterTextureLimit;

    boolean mStreamingMipmaps;

    int mForcedFallbackFormat;

    private int mWidth;

    private int mHeight;

    int mCompleteImageSize;

    int mMipsStripped;

    int mMipCount;

    int mStreamingMipmapsPriority;

    int mImageCount;

    int mTextureDimension;

    int mFilterMode;

    int mAniso;

    int mWrapMode;

    int mWrapV;

    int mWrapW;

    int mLightmapFormat;

    int mColorSpace;

    int imageDataSize;

    float mMipBias;

    long offset;

    long size;

    byte[] mPlatformBlob;

    private byte[] imageData;

    TextureFormat mTextureFormat;

    public Texture2D(ObjectReader o) {
        this.reader = o.reader;
        this.reader.setPosition((int)o.byteStart);
        this.name = this.reader.readAlignedString();
        this.mForcedFallbackFormat = this.reader.readInt();
        this.mDownscaleFallback = this.reader.readBoolean();
        this.mIsAlphaChannelOptional = this.reader.readBoolean();
        this.reader.alignStream();
        this.mWidth = this.reader.readInt();
        this.mHeight = this.reader.readInt();
        this.mCompleteImageSize = this.reader.readInt();
        this.mMipsStripped = this.reader.readInt();
        this.mTextureFormat = TextureFormat.byOrdinal(this.reader.readInt());
        this.mMipCount = this.reader.readInt();
        this.mIsReadable = this.reader.readBoolean();
        this.mIsPreProcessed = this.reader.readBoolean();
        this.mIgnoreMasterTextureLimit = this.reader.readBoolean();
        this.mStreamingMipmaps = this.reader.readBoolean();
        this.reader.alignStream();
        this.mStreamingMipmapsPriority = this.reader.readInt();
        this.mImageCount = this.reader.readInt();
        this.mTextureDimension = this.reader.readInt();
        gLTextureSettings();
        this.mLightmapFormat = this.reader.readInt();
        this.mColorSpace = this.reader.readInt();
        this.mPlatformBlob = this.reader.readByteArrayInt();
        this.reader.alignStream();
        this.imageDataSize = this.reader.readInt();
        if (this.imageDataSize != 0)
            this.imageData = this.reader.readByte(this.imageDataSize);
        streamingInfo();
    }

    private void gLTextureSettings() {
        this.mFilterMode = this.reader.readInt();
        this.mAniso = this.reader.readInt();
        this.mMipBias = this.reader.readFloat();
        this.mWrapMode = this.reader.readInt();
        this.mWrapV = this.reader.readInt();
        this.mWrapW = this.reader.readInt();
    }

    private void streamingInfo() {
        this.offset = this.reader.readLong();
        this.size = this.reader.readUnsignedInt();
        this.path = this.reader.readAlignedString();
    }

    public String getName() {
        return this.name;
    }

    public int getMWidth() {
        return this.mWidth;
    }

    public int getMHeight() {
        return this.mHeight;
    }

    public byte[] getImageData() {
        return this.imageData;
    }
}

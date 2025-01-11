package com.github.waifu.assets.resextractor;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class SpriteAtlas {
    DataReader reader;

    String name;

    int packedSpritesSize;

    PPTR[] mPackedSprites;

    String[] mPackedSpriteNamesToIndex;

    int mrenderDataMapSize;

    RenderDataMap[] mRenderDataMap;

    public SpriteAtlas(ObjectReader o) {
        this.reader = o.reader;
        this.reader.setPosition((int)o.byteStart);
        this.name = this.reader.readAlignedString();
        this.packedSpritesSize = this.reader.readInt();
        this.mPackedSprites = new PPTR[this.packedSpritesSize];
        int i;
        for (i = 0; i < this.packedSpritesSize; i++)
            this.mPackedSprites[i] = new PPTR();
        this.mPackedSpriteNamesToIndex = this.reader.readStringArray();
        this.mrenderDataMapSize = this.reader.readInt();
        this.mRenderDataMap = new RenderDataMap[this.mrenderDataMapSize];
        for (i = 0; i < this.mrenderDataMapSize; i++)
            this.mRenderDataMap[i] = new RenderDataMap();
    }

    public class RenderDataMap {
        byte[] first;

        long second;

        SpriteAtlas.PPTR texture;

        SpriteAtlas.PPTR alphaTexture;

        Rectangle2D textureRect;

        Vec2f textureRectOffset;

        Vec2f atlasRectOffset;

        Vec4f uvTransform;

        float downscaleMultiplier;

        long settingsRaw;

        long packed;

        long packingMode;

        long packingRotation;

        long meshType;

        int secondaryTexturesSize;

        SpriteAtlas.SecondarySpriteTexture[] secondaryTextures;

        public RenderDataMap() {
            this.first = SpriteAtlas.this.reader.readByte(16);
            this.second = SpriteAtlas.this.reader.readLong();
            spriteAtlasData();
        }

        private void spriteAtlasData() {
            this.texture = new SpriteAtlas.PPTR();
            this.alphaTexture = new SpriteAtlas.PPTR();
            this.textureRect = new Rectangle();
            this.textureRect.setRect(SpriteAtlas.this.reader.readFloat(), SpriteAtlas.this.reader.readFloat(), SpriteAtlas.this.reader.readFloat(), SpriteAtlas.this.reader.readFloat());
            this.textureRectOffset = new Vec2f(SpriteAtlas.this.reader.readFloat(), SpriteAtlas.this.reader.readFloat());
            this.atlasRectOffset = new Vec2f(SpriteAtlas.this.reader.readFloat(), SpriteAtlas.this.reader.readFloat());
            this.uvTransform = new Vec4f(SpriteAtlas.this.reader.readFloat(), SpriteAtlas.this.reader.readFloat(), SpriteAtlas.this.reader.readFloat(), SpriteAtlas.this.reader.readFloat());
            this.downscaleMultiplier = SpriteAtlas.this.reader.readFloat();
            spriteSettings();
            this.secondaryTexturesSize = SpriteAtlas.this.reader.readInt();
            this.secondaryTextures = new SpriteAtlas.SecondarySpriteTexture[this.secondaryTexturesSize];
            for (int i = 0; i < this.secondaryTexturesSize; i++)
                this.secondaryTextures[i] = new SpriteAtlas.SecondarySpriteTexture();
            SpriteAtlas.this.reader.alignStream();
        }

        private void spriteSettings() {
            this.settingsRaw = SpriteAtlas.this.reader.readUnsignedInt();
            this.packed = this.settingsRaw & 0x1L;
        }
    }

    public class SecondarySpriteTexture {
        SpriteAtlas.PPTR texture;

        String name;

        public SecondarySpriteTexture() {
            this.texture = new SpriteAtlas.PPTR();
            this.name = SpriteAtlas.this.reader.readStringToNull();
        }
    }

    public class PPTR {
        int fileId = SpriteAtlas.this.reader.readInt();

        long pathId = SpriteAtlas.this.reader.readLong();
    }
}

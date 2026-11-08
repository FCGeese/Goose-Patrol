package io.github.EngOnePrototype.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Defines the texture atlas assets used by the game.
 * 
 * Each enum constant represents a texture atlas file and provides
 * an AssetDescriptor that can be used by the AssetService to load it.
 */
public enum AtlasAsset implements Asset<TextureAtlas> {

    // Texture atlas containing the game's object graphics.
    OBJECTS("objects.atlas");

    /** Descriptor containing the file path and type of this texture atlas. */
    private final AssetDescriptor<TextureAtlas> descriptor;

    /**
     * Creates an asset descriptor for the specified texture atlas.
     * The atlas file is expected to be located in the graphics directory of 'assets'.
     * 
     * @param atlasName The filename of the texture atlas.
     */
    AtlasAsset(String atlasName) {
        this.descriptor = new AssetDescriptor<>("graphics/" + atlasName, TextureAtlas.class);
    }

    /**
     * Returns the asset descriptor associated with this texture atlas.
     * 
     * @return The descriptor used to load this texture atlas.
     */
    @Override
    public AssetDescriptor<TextureAtlas> getDescriptor() {
        return descriptor;
    }
}
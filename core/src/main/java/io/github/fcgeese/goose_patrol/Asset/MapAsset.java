package io.github.fcgeese.goose_patrol.Asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Defines the available Tiled maps used throughout the game.
 * Stores their asset descriptors and loading parameters.
 */
public enum MapAsset implements Asset<TiledMap> {
    /** Main game map. */
    MAIN("main.tmx");

    /** Stores the map's file path, type and loading parameters. */
    private final AssetDescriptor<TiledMap> descriptor;

    /**
     * Creates an asset descriptor for the specified Tiled map.
     *
     * @param mapName The filename of the Tiled map.
     */
    MapAsset(String mapName) {
        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        parameters.projectFilePath = "maps/GoosePatrol Practice Map.tiled-project";
        this.descriptor = new AssetDescriptor<>("maps/" + mapName, TiledMap.class, parameters);
    }

    /**
     * Returns the asset descriptor for this map.
     *
     * @return The map's asset descriptor.
     */
    @Override
    public AssetDescriptor<TiledMap> getDescriptor() {
        return this.descriptor;
    }
}

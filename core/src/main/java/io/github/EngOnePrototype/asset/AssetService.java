package io.github.EngOnePrototype.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.Gdx;

/**
 * Provides centralised asset loading and management using LibGDX's AssetManager.
 * Supports loading, queuing, retrieving and disposing of game assets.
 */
public class AssetService implements Disposable {
    private final AssetManager assetManager;

    /**
     * Initialises the AssetManager and registers the loader for Tiled maps.
     * 
     * @param fileHandleResolver Determines how asset file paths are resolved.
     */
    public AssetService(FileHandleResolver fileHandleResolver) {
        this.assetManager = new AssetManager(fileHandleResolver);
        this.assetManager.setLoader(TiledMap.class, new TmxMapLoader(fileHandleResolver));
    }

    /**
     * Loads an asset and waits for loading to complete before returning it.
     * 
     * @param asset The asset to load.
     * @return The loaded asset.
     */
    public <T> T load(Asset<T> asset) {
        this.assetManager.load(asset.getDescriptor());
        this.assetManager.finishLoading();
        return this.assetManager.get(asset.getDescriptor());
    }

    /**
     * Adds an asset to the loading queue without waiting for completion.
     * (Allows the game to continue running while assets are being loaded.)
     * 
     * @param asset The asset to queue for loading.
     */
    public <T> void queue(Asset<T> asset) {
        this.assetManager.load(asset.getDescriptor());
    }

    /**
     * Retrieves an already-loaded asset.
     * 
     * @param asset The asset to retrieve.
     * @return The requested asset.
     */
    public <T> T get(Asset<T> asset) {
        return this.assetManager.get(asset.getDescriptor());
    }

    /**
     * Processes queued asset loading tasks.
     * 
     * @return True if all queued assets have finished loading, otherwise false.
     */
    public boolean update() {
        return this.assetManager.update();
    }

    /**
     * Prints diagnostic information about currently managed assets.
     */
    public void debugDiagnostics() {
        Gdx.app.debug("AssetService", this.assetManager.getDiagnostics());
    }

    /**
     * Releases all assets managed by the AssetManager.
     */
    @Override
    public void dispose() {
        this.assetManager.dispose();
    }
}

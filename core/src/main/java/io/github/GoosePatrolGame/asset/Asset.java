package io.github.GoosePatrolGame.asset;

import com.badlogic.gdx.assets.AssetDescriptor;

/**
 * Represents a generic game asset with an associated descriptor.
 *
 * @param <T> The type of asset represented by this interface. (e.g. TiledMap, etc.)
 */
public interface Asset<T> {

    /** 
     * Returns the descriptor containing information about the asset.
     * 
     * @return The asset's descriptor.
     */
    AssetDescriptor<T> getDescriptor();
}
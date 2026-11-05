package io.github.EngOnePrototype.tiled;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.GdxRuntimeException;

import io.github.EngOnePrototype.asset.AssetService;
import io.github.EngOnePrototype.asset.MapAsset;

import java.util.function.Consumer;

/**
 * Manages Tiled maps, including loading, switching and processing map objects.
 * Uses AssetService for asset management and consumers to communicate map changes
 * and loaded objects to other parts of the game.
 */
public class TiledService {
    private final AssetService assetService;

    private TiledMap currentMap;

    private Consumer<TiledMap> mapChangeConsumer;
    private Consumer<TiledMapTileMapObject> loadObjectConsumer;

    /**
     * Constructs a new TiledService using the provided asset service.
     * 
     * @param assetService The service responsible for loading and unloading map assets.
     */
    public TiledService(AssetService assetService) {
        this.assetService = assetService;
        this.mapChangeConsumer = null;
        this.loadObjectConsumer = null;
        this.currentMap = null;
    }
    
    /**
     * Loads a Tiled map using the provided map asset.
     * Stores the asset reference in the map's properties for later unloading.
     * 
     * @param mapAsset The map asset to load.
     * @return The loaded Tiled map.
     */
    public TiledMap loadMap(MapAsset mapAsset) {
        TiledMap tiledMap = this.assetService.load(mapAsset);
        tiledMap.getProperties().put("mapAsset", mapAsset);

        return tiledMap;
    }

    /**
     * Sets the currently active Tiled map.
     * Unloads the previously active map, processes the map's new objects and
     * notifies the registered map change consumer.
     * 
     * @param map The Tiled map to set as the current map.
     */
    public void setMap(TiledMap map) {
        // Unloads the previous map if one is currently assigned.
        if(this.currentMap != null) {
            this.assetService.unload(this.currentMap.getProperties().get("mapAsset", MapAsset.class));
        }

        this.currentMap = map;

        // Processes objects in the new map before notifying the rendering system.
        loadMapObjects(map);

        // Notifies the registered consumer that the active map has changed.
        if(this.mapChangeConsumer != null) {
            this.mapChangeConsumer.accept(map);
        }
    }

    /**
     * Searches the Tiled map for the designated object layer and processes its objects.
     * 
     * @param tiledMap The Tiled map containing the layers to search.
     */
    private void loadMapObjects(TiledMap tiledMap) {
        for (MapLayer layer : tiledMap.getLayers()) {
            // Only processes the layer named "Objects".
            if("Objects".equals(layer.getName())) {
                loadObjectLayer(layer);
            }
        }
    }

    /**
     * Processes the objects within a Tiled object layer.
     * Passes supported tile map objects to the registered load object consumer.
     * 
     * @param objectLayer The Tiled object layer containing the objects to process.
     * @throws GdxRuntimeException If an object is not a supported tile map object.
     */
    private void loadObjectLayer(MapLayer objectLayer) {
        // Stops processing if no object consumer has been registered.
        if(loadObjectConsumer == null) return;

        for (MapObject mapObject : objectLayer.getObjects()) {
            if(mapObject instanceof TiledMapTileMapObject tileMapObject) {
                loadObjectConsumer.accept(tileMapObject);
            } else {
                // Throws an exception if the object type is not supported.
                throw new GdxRuntimeException("Unsupported object: " + mapObject.getClass().getSimpleName());
            }
        }
    }

    /**
     * Sets the consumer responsible for receiving notifications when the active map changes.
     * 
     * @param mapChangeConsumer The consumer that receives the newly assigned Tiled map.
     */
    public void setMapChangeConsumer(Consumer<TiledMap> mapChangeConsumer) {
        this.mapChangeConsumer= mapChangeConsumer;
    }

    /**
     * Sets the consumer responsible for processing loaded Tiled tile map objects.
     * 
     * @param loadObjectConsumer The consumer that receives supported tile map objects.
     */
    public void setLoadObjectConsumer(Consumer<TiledMapTileMapObject> loadObjectConsumer) {
        this.loadObjectConsumer = loadObjectConsumer;
    }
}
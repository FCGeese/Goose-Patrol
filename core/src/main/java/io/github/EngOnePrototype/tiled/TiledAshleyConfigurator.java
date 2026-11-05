package io.github.EngOnePrototype.tiled;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;

import io.github.EngOnePrototype.Main;
import io.github.EngOnePrototype.asset.AssetService;
import io.github.EngOnePrototype.component.Graphic;
import io.github.EngOnePrototype.component.Transform;

/**
 * Configures Ashley entities from objects loaded from a Tiled map.
 * Creates entities and assigns their appropriate components.
 */
public class TiledAshleyConfigurator {
    private final Engine engine;
    private final AssetService assetService;

    /**
     * Constructs a new TiledAshleyConfigurator using the provided engine
     * and asset service.
     * 
     * @param engine The Ashley engine used to create and manage entities.
     * @param assetService The service responsible for managing game assets.
     */
    public TiledAshleyConfigurator(Engine engine, AssetService assetService) {
        this.engine = engine;
        this.assetService = assetService;
    }

    /**
     * Creates an Ashley entity from a Tiled map tile object.
     * Assigns the entity its appropriate components and adds it to the engine.
     * 
     * @param tileMapObject
     */
    public void onLoadObject(TiledMapTileMapObject tileMapObject) {
        Entity entity = this.engine.createEntity();
        TiledMapTile tile = tileMapObject.getTile();
        TextureRegion textureRegion = getTextureRegion(tile);
        int z = tile.getProperties().get("z", 1, Integer.class);

        // Assigns the entity a graphical component using its texture region.
        entity.add(new Graphic(Color.WHITE.cpy(), textureRegion));

        // Creates and assigns the entity's transformation component.
        addEntityTransform(
            tileMapObject.getX(), tileMapObject.getY(), z,
            textureRegion.getRegionWidth(), textureRegion.getRegionHeight(),
            tileMapObject.getScaleX(), tileMapObject.getScaleY(),
            entity);
        
        // Adds the fully configured entity to the Ashley engine.
        this.engine.addEntity(entity);
    }

    /**
     * Creates and assigns a transformation component to an entity.
     * Converts the position and dimensions from Tiled map coordinates
     * into the game's world units using the unit scale.
     * 
     * @param x The entity's horizontal position.
     * @param y The entity's vertical position.
     * @param z The entity's rendering layer.
     * @param w The entity's original width.
     * @param h The entity's original height.
     * @param scaleX The entity's horizontal scaling factor.
     * @param scaleY The entity's vertical scaling factor.
     * @param entity The Ashley entity receiving the transformation component.
     */
    private void addEntityTransform(
        float x, float y, int z,
        float w, float h,
        float scaleX, float scaleY,
        Entity entity
    ) {
        Vector2 position = new Vector2(x, y);
        Vector2 size = new Vector2(w, h);
        Vector2 scaling = new Vector2(scaleX, scaleY);

        // Converts the position and dimensions into the game's world units.
        position.scl(Main.UNIT_SCALE);
        size.scl(Main.UNIT_SCALE);

        entity.add(new Transform(position, z, size, scaling, 0f));
    }
    
    /**
     * Retrieves the texture region associated with a Tiled map tile.
     * 
     * @param tile The Tiled map tile containing the texture region.
     * @return The texture region associated with the specified tile.
     */
    private TextureRegion getTextureRegion(TiledMapTile tile) {
        return tile.getTextureRegion();
    }
}

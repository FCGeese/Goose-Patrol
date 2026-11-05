package io.github.EngOnePrototype.system;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.EngOnePrototype.Main;
import io.github.EngOnePrototype.component.Graphic;
import io.github.EngOnePrototype.component.Transform;

/**
 * Handles the rendering of the Tiled map and game entities to the screen.
 * Uses Ashley's SortedIteratingSystem to render entities in a sorted order 
 * based on their Transform component.
 */
public class RenderSystem extends SortedIteratingSystem implements Disposable {
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Batch batch;
    private final Viewport viewport;
    private final OrthographicCamera cam;
    
    /**
     * Constructs a new RenderSystem using the specified Batch, Viewport and Camera.
     * 
     * @param batch The batch used for rendering sprites and textures.
     * @param viewport The viewport used to manage the game's camera and screen dimensions.
     * @param cam The camera used to determine the viewing area of the game.
     */
    public RenderSystem(Batch batch, Viewport viewport, OrthographicCamera cam) {
        super(
            // Only processes entities containing both Transform and Graphic components.
            Family.all(Transform.class, Graphic.class).get(),

            // Sorts entities based on their Transform component, allowing for proper rendering order.
            Comparator.comparing(Transform.MAPPER::get)
        );

        this.batch = batch;
        this.viewport = viewport;
        this.cam = cam;

        // Creates the Tiled map renderer using the game's world unit scale and shared batch.
        this.mapRenderer = new OrthogonalTiledMapRenderer(null, Main.UNIT_SCALE, this.batch);
    }

    /**
     * Updates the rendering system for the current frame.
     * Applies the viewport, renders the Tiled map and processes the game entities.
     * 
     * @param deltaTime The time elapsed since the previous frame, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        // Applies the viewport settings to ensure the camera and rendering are correctly 
        // aligned with the screen dimensions.
        this.viewport.apply();

        // Resets the batch colour to white to avoid color tinting from previous frames.
        this.batch.setColor(Color.WHITE);

        // Updates the map renderer's viewing area to match the camera's current position and zoom level.
        this.mapRenderer.setView(this.cam);

        // Renders the Tiled map.
        this.mapRenderer.render();
        
        // Re-sorts entities to account for any changes in their Transform components.
        forceSort();

        // Begins batch rendering before processing the game entities.
        batch.begin();

        // Processes all entities belonging to this system's family.
        super.update(deltaTime);

        // Ends batch rendering after all entities have been processed.
        batch.end();
    }

    /** 
     * Renders an individual entity to the screen using its Transform and Graphic components.
     * 
     * @param entity The entity to be rendered.
     * @param deltaTime The time elapsed since the previous frame, in seconds.
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // Retrieves the entity's Transform and Graphic components using their respective ComponentMappers.
        Transform transform = Transform.MAPPER.get(entity);
        Graphic graphic = Graphic.MAPPER.get(entity);

        // Skips rendering if the entity does not have a valid TextureRegion.
        if(graphic.getRegion() == null) {
            return;
        }

        // Retrieves the entity's position, scaling, and dimensions.
        Vector2 position = transform.getPosition();
        Vector2 scaling = transform.getScaling();
        Vector2 size = transform.getSize();

        // Applies the entity's colour to the batch before drawing.
        this.batch.setColor(graphic.getColor());

        // Draws the entity's TextureRegion to the screen with applied Transform properties.
        this.batch.draw(
            graphic.getRegion(),                                // Texture of the entity.
            position.x - (1f - scaling.x) * size.x / 0.5f,      // X and Y coordinates of the bottom left corner of the entity, coordinates
            position.y - (1f - scaling.y) * size.y / 0.5f,      // account for scaling so sprites stay centered on their position when scaled.
            size.x * 0.5f, size.y * 0.5f,                       // Origin coordinates dictating the centre of the entity, used for rotation and scaling.
            size.x, size.y,                                     // Width and height of the entity.
            scaling.x, scaling.y,                               // Scaling factors for the entity.
            transform.getRotationDeg()                          // Rotation of the entity in degrees.
        );
    }
    
    /**
     * Assigns the Tiled map to the map renderer.
     * 
     * @param tiledMap The Tiled map to be rendered.
     */
    public void setMap(TiledMap tiledMap) {
        this.mapRenderer.setMap(tiledMap);
    }

    /**
     * Disposes any resources owned by the Tiled map renderer.
     */
    @Override
    public void dispose() {
        this.mapRenderer.dispose();
    }
}
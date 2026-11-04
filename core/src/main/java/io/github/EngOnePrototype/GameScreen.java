package io.github.EngOnePrototype;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.ashley.core.Engine;
import io.github.EngOnePrototype.asset.AssetService;
import io.github.EngOnePrototype.asset.MapAsset;

/**
 * Represents the main gameplay screen.
 * Handles map loading, rendering and screen-specific resources.
 */
public class GameScreen extends ScreenAdapter {
    // Responsible for drawing sprites and textures to the screen.
    private final SpriteBatch batch;
    private final AssetService assetService;
    // Responsible for rendering the Tiled map to the screen.
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Viewport viewport;
    private final OrthographicCamera cam;
    private final Engine engine;

    /**
     * Constructs a new GameScreen using resources provided by the main game.
     *
     * @param game The main game instance providing access to shared resources.
     */
    public GameScreen(Main game) {
        this.batch = game.getBatch();
        this.assetService = game.getAssetService();

        this.mapRenderer = new OrthogonalTiledMapRenderer(null, Main.UNIT_SCALE, this.batch);
        this.viewport = game.getViewport();
        this.cam = game.getCamera();
        this.engine = game.getEngine();

        this.engine.addSystem(new RenderSystem(this.batch, this.viewport, this.assetService));
        this.engine.addSystem(new MovementSystem());
        this.engine.addSystem(new DamageSystem(this.cam, this.viewport));
        this.engine.addSystem(new HealSystem(this.cam, this.viewport));
    }

    /**
     * Called when this screen becomes the current screen for the game.
     * Loads the main Tiled map and assigns it to the map renderer.
     */
    @Override
    public void show() {
        this.assetService.load(MapAsset.MAIN);
        this.mapRenderer.setMap(this.assetService.get(MapAsset.MAIN));
    }

    /**
    * Called when this screen is no longer the current screen for the game.
    * Removes all entities from the Ashley engine to clear the screen's game objects.
    */
    @Override
    public void hide() {
        this.engine.removeAllEntities();
    }

    /**
     * Renders the Tiled map and updates the Ashley engine.
     * 
     * @param delta The time elapsed since the previous frame, in seconds.
     */
    @Override
    public void render(float delta) {
        // Update engine and cap delta time to avoid large jumps in game logic
        this.engine.update(delta);
        delta = Math.min(delta, 1 / 30f);

        this.viewport.apply();
        this.batch.setColor(Color.WHITE);
        this.mapRenderer.setView(this.cam);
        this.mapRenderer.render();
    }

    /**
     * Releases resources owned by the gameplay screen.
     */
    @Override
    public void dispose() {
        this.mapRenderer.dispose();
    }
}
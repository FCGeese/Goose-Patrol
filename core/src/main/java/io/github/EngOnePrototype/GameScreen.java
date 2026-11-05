package io.github.EngOnePrototype;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;

import io.github.EngOnePrototype.asset.AssetService;
import io.github.EngOnePrototype.asset.MapAsset;
import io.github.EngOnePrototype.system.RenderSystem;

/**
 * Represents the main gameplay screen.
 * Handles map loading, rendering and screen-specific resources.
 */
public class GameScreen extends ScreenAdapter {
    private final SpriteBatch batch;
    private final AssetService assetService;
    private final Viewport viewport;
    // Ashley engine for managing game entities and systems.
    private final Engine engine;

    /**
     * Constructs a new GameScreen using resources provided by the main game.
     *
     * @param game The main game instance providing access to shared resources.
     */
    public GameScreen(Main game) {
        this.batch = game.getBatch();
        this.assetService = game.getAssetService();
        this.viewport = game.getViewport();
        this.engine = new Engine();

        this.engine.addSystem(new RenderSystem(this.batch, this.viewport));
    }

    /**
     * Called when this screen becomes the current screen for the game.
     * Loads the main Tiled map and assigns it to the map renderer.
     */
    @Override
    public void show() {
        this.engine.getSystem(RenderSystem.class).setMap(this.assetService.get(MapAsset.MAIN));
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
     * Updates the Ashley engine, allowing the systems to process the current frame.
     * 
     * @param delta The time elapsed since the previous frame, in seconds.
     */
    @Override
    public void render(float delta) {
        // Cap delta time to avoid large jumps in game logic and update engine
        delta = Math.min(delta, 1 / 30f);
        this.engine.update(delta);
    }

    /**
     * Disposes of any appropriate systems when the screen is no longer needed.
     */
    @Override
    public void dispose() {
        for (EntitySystem system : this.engine.getSystems()) {
            if(system instanceof Disposable disposableSystem) {
                disposableSystem.dispose();
            }
        }
    }
}
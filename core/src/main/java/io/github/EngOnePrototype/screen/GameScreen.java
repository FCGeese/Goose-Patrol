package io.github.EngOnePrototype.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;

import io.github.EngOnePrototype.Main;
import io.github.EngOnePrototype.asset.AssetService;
import io.github.EngOnePrototype.asset.MapAsset;
import io.github.EngOnePrototype.tiled.TiledAshleyConfigurator;
import io.github.EngOnePrototype.tiled.TiledService;
import io.github.EngOnePrototype.system.RenderSystem;

/**
 * Represents the main gameplay screen.
 * Handles map loading, rendering and screen-specific resources.
 */
public class GameScreen extends ScreenAdapter {
    private final SpriteBatch batch;
    private final AssetService assetService;
    private final Viewport viewport;
    private final OrthographicCamera cam;

    // Ashley engine for managing game entities and systems.
    private final Engine engine;

    
    private final TiledService tiledService;
    private final TiledAshleyConfigurator tiledAshleyConfigurator;


    /**
     * Constructs a new GameScreen using resources provided by the main game.
     *
     * @param game The main game instance providing access to shared resources.
     */
    public GameScreen(Main game) {
        this.batch = game.getBatch();
        this.assetService = game.getAssetService();
        this.viewport = game.getViewport();
        this.cam = game.getCamera();
        this.tiledService = new TiledService(this.assetService);
        this.engine = new Engine();
        this.tiledAshleyConfigurator = new TiledAshleyConfigurator(this.engine, this.assetService);

        // Registers the rendering system with the Ashley engine.
        this.engine.addSystem(new RenderSystem(this.batch, this.viewport, this.cam));
    }

    /**
     * Called when this screen becomes the current screen for the game.
     * Sets up map-related callbacks, loads the main Tiled map and assigns
     * it to the rendering system.
     */
    @Override
    public void show() {
        // Connects map changes to the rendering system.
        Consumer<TiledMap> renderConsumer = this.engine.getSystem(RenderSystem.class)::setMap;
        this.tiledService.setMapChangeConsumer(renderConsumer);

        // Connects loaded Tiled objects to the Ashley entity configurator.
        this.tiledService.setLoadObjectConsumer(this.tiledAshleyConfigurator::onLoadObject);
        
        TiledMap tiledMap = this.tiledService.loadMap(MapAsset.MAIN);
        this.tiledService.setMap(tiledMap);
    }

    /**
    * Called when this screen is no longer the current screen for the game.
    * Removes all entities from the Ashley engine to clear the screen's 
    * game objects.
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
        // Limits the max delta time to prevent large jumps in game logic.
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
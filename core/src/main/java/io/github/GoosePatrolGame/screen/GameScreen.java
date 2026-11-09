package io.github.GoosePatrolGame.screen;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;

import io.github.GoosePatrolGame.Main;
import io.github.GoosePatrolGame.asset.MapAsset;
import io.github.GoosePatrolGame.input.GameControllerState;
import io.github.GoosePatrolGame.input.KeyboardController;
import io.github.GoosePatrolGame.system.ControllerSystem;
import io.github.GoosePatrolGame.system.MoveSystem;
import io.github.GoosePatrolGame.system.RenderSystem;
import io.github.GoosePatrolGame.tiled.TiledAshleyConfigurator;
import io.github.GoosePatrolGame.tiled.TiledService;

/**
 * Represents the main gameplay screen.
 * Handles map loading, rendering and screen-specific resources.
 */
public class GameScreen extends ScreenAdapter {
    private final Main game;

    // Ashley engine for managing game entities and systems.
    private final Engine engine;

    private final TiledService tiledService;
    private final TiledAshleyConfigurator tiledAshleyConfigurator;
    private final KeyboardController keyboardController;

    /**
     * Constructs a new GameScreen using resources provided by the main game.
     *
     * @param game The main game instance providing access to shared resources.
     */
    public GameScreen(Main game) {
        this.game = game;
        this.engine = new Engine();
        this.tiledService = new TiledService(game.getAssetService());
        this.tiledAshleyConfigurator = new TiledAshleyConfigurator(this.engine, game.getAssetService());
        this.keyboardController = new KeyboardController(GameControllerState.class, engine);

        // Registers all necessary systems for this screen with the Ashley engine.
        this.engine.addSystem(new ControllerSystem());
        this.engine.addSystem(new MoveSystem());
        this.engine.addSystem(new RenderSystem(game.getBatch(), game.getViewport(), game.getCamera()));
    }

    /**
     * Called when this screen becomes the current screen for the game.
     * Sets up map-related callbacks, loads the main Tiled map and assigns
     * it to the rendering system.
     */
    @Override
    public void show() {
        game.setInputProcessors(keyboardController);
        keyboardController.setActiveState(GameControllerState.class);
        
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
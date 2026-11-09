package io.github.GoosePatrolGame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import io.github.GoosePatrolGame.Main;
import io.github.GoosePatrolGame.asset.AssetService;
import io.github.GoosePatrolGame.asset.AtlasAsset;

/**
 * Screen responsible for loading the game's required assets before gameplay begins.
 * 
 * Queues the texture atlas assets through the AssetService, monitors their
 * loading progress and transitions to the GameScreen once loading is complete.
 */
public class LoadingScreen extends ScreenAdapter {

    private final Main game;
    private final AssetService assetService;

    /**
     * Creates the loading screen with references to the main game and asset service.
     * 
     * @param game The main game instance used to manage screens.
     * @param assetService The service used to load game assets.
     */
    public LoadingScreen(Main game, AssetService assetService) {
        this.game = game;
        this.assetService = assetService;
    }

    /**
     * Called when the loading screen becomes the active screen.
     * 
     * Queues every texture atlas defined in AtlasAsset for loading.
     */
    @Override 
    public void show() {
        // Queue all defined texture atlas assets for asynchronous loading.
        for (AtlasAsset atlas : AtlasAsset.values()) {
            assetService.queue(atlas);
            
        }
    }

    /**
     * Updates the asset loading process and transitions to the game screen
     * once all queued assets have finished loading.
     * 
     * @param delta Time elapsed since the previous frame, in seconds.
     */
    @Override
    public void render(float delta) {
        if(this.assetService.update()) {
            Gdx.app.debug("LoadingScreen", "Finished asset loading");

            // Create all the screens required by the game.
            createScreens();

            // Remove and dispose of the loading screen once loading is complete.
            this.game.removeScreen(this);
            this.dispose();

            // Switch to the main gameplay screen.
            this.game.setScreen(GameScreen.class);
        }
    }

    /**
     * Creates and registers the screens that will be used after loading.
     * 
     * Currently, this creates the main gameplay screen.
     */
    private void createScreens() {
        this.game.addScreen(new GameScreen(this.game));
    }
}

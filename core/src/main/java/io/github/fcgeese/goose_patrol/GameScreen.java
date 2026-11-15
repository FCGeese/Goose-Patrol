package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.badlogic.gdx.graphics.Color;
import io.github.fcgeese.goose_patrol.Asset.AssetService;
import io.github.fcgeese.goose_patrol.Asset.MapAsset;

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
     * Renders the Tiled map using the current viewport and camera settings.
     *
     * @param delta The time elapsed since the previous frame, in seconds.
     */
    @Override
    public void render(float delta) {
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

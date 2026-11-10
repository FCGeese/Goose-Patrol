package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.EngOnePrototype.asset.AssetService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    // Keep 16:9 aspect ratio for the game world, so that it looks the same on all devices.
    public static final float WORLD_WIDTH = 32f; // Shown world width in tiled units
    public static final float WORLD_HEIGHT = 18f; // Shown world height in tiled units
    public static final float UNIT_SCALE = 1f / 16f; // 1 unit = 16 pixels
    /** Service for managing game assets (e.g., textures, sounds). */
    private io.github.EngOnePrototype.asset.AssetService assetService;
    /** Profiler for monitoring OpenGL calls. */
    private GLProfiler glProfiler;
    /** Logger for monitoring FPS & diagnostics for asset servers. */
    private FPSLogger fpsLogger;
    private SpriteBatch batch;
    private Texture image;
    /** Hashmap for storing the game's screens. */
    private final Map<Class<? extends Screen>, Screen> screenCache = new HashMap<>();

    private Player player;
    private List<Goose> geese = new ArrayList<>();;
    private List<Element> gooseColliders = new ArrayList<>();
    private List<Element> targets = new ArrayList<>();
    private TargetManager targetManager;

    private Camera playerCamera;

    @Override
    public void create() {
        // Set the log level to DEBUG for detailed logging during development.
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        this.batch = new SpriteBatch();
        this.assetService = new io.github.EngOnePrototype.asset.AssetService(new InternalFileHandleResolver());
        this.glProfiler = new GLProfiler(Gdx.graphics);
        this.glProfiler.enable();
        this.fpsLogger = new FPSLogger();

        // Add the GameScreen to the screen cache and set it as the current screen.
        addScreen(new io.github.EngOnePrototype.GameScreen(this));
        setScreen(io.github.EngOnePrototype.GameScreen.class);

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);

        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        player = new Player(new Vector2(100,100));

        targets.add(player);

        for(int x = 0; x<10; x++) {
            geese.add(new Goose(new Vector2(300+200, 1000*x)));
        }

        gooseColliders.addAll(geese);

        for (Goose g : geese){
            g.setColliderList(gooseColliders);
            g.setTargetList(targets);
        }

        playerCamera = new OrthographicCamera(
            player.getPositionVector().x-32,
            player.getPositionVector().y-64);
        playerCamera.viewportWidth = 600;
        playerCamera.viewportHeight = 600;
    }

    // Window size is changed by the user
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        super.resize(width, height);
    }
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);
        player.move();
        player.draw(batch);
        playerCamera.position.set(player.getPositionVector().x, player.getPositionVector().y, 0);
        playerCamera.update();
        batch.setProjectionMatrix(playerCamera.combined);

        for ( Goose g : geese){
            g.move();
            g.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }

    public void addScreen(Screen screen) {
        screenCache.put(screen.getClass(), screen);
    }

    public void setScreen(Class<? extends Screen> screenClass) {
        Screen screen = screenCache.get(screenClass);
        if(screen == null) {
            throw new GdxRuntimeException("No screen with class " + screenClass + " found in the screen cache");
        }
        super.setScreen(screen);
    }

    public AssetService getAssetService() {
        return assetService;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}

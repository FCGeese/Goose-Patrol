package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.fcgeese.goose_patrol.Asset.AssetService;
import io.github.fcgeese.goose_patrol.Elements.Element;
import io.github.fcgeese.goose_patrol.Elements.Goose;
import io.github.fcgeese.goose_patrol.Elements.Player;
import io.github.fcgeese.goose_patrol.Managers.TargetManager;

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


    /** SpriteBatch for rendering 2D graphics. */
    private SpriteBatch batch;
    /** Orthographic camera for rendering the game world. */
    private OrthographicCamera cam;
    /** Viewport for managing the game world's display. */
    private Viewport viewport;
    /** Service for managing game assets (e.g., textures, sounds). */
    private AssetService assetService;
    /** Profiler for monitoring OpenGL calls. */
    private GLProfiler glProfiler;
    /** Logger for monitoring FPS & diagnostics for asset servers. */
    private FPSLogger fpsLogger;


    /** Hashmap for storing the game's screens. */
    private final Map<Class<? extends Screen>, Screen> screenCache = new HashMap<>();

    //Player object
    private Player player;
    //List of every goose object
    private List<Goose> geese = new ArrayList<>();
    //List of everything geese can colide with
    private List<Element> gooseColliders = new ArrayList<>();
    //List of everything geese can target
    private List<Element> targets = new ArrayList<>();
    //Target manager for all geese
    private TargetManager gooseTargetManager;

    @Override
    public void create() {
        // Set the log level to DEBUG for detailed logging during development.
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        cam = new OrthographicCamera(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        viewport = new FitViewport(1000, 1000, cam);
        assetService = new AssetService(new InternalFileHandleResolver());
        glProfiler = new GLProfiler(Gdx.graphics);
        glProfiler.enable();
        fpsLogger = new FPSLogger();
        batch = new SpriteBatch();
        // Add the GameScreen to the screen cache and set it as the current screen.
        addScreen(new GameScreen(this));
        setScreen(GameScreen.class);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
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

    }

    // Window size is changed by the user
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        super.resize(width, height);
    }


    @Override
    public void render() {
        // Reset the GLProfiler at the start of each frame to get accurate profiling data for the current frame.
        glProfiler.reset();
        // Clear the screen with a black color before rendering the next frame.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        screen.render(Gdx.graphics.getDeltaTime());
        batch.begin();
        // Update the window title with the current FPS and OpenGL profiling data.
        Gdx.graphics.setTitle("Goose-Patrol | FPS: " + Gdx.graphics.getFramesPerSecond() +
            " | Draw Calls: " + glProfiler.getDrawCalls() +
            " | Texture Bindings: " + glProfiler.getTextureBindings() +
            " | Shader Switches: " + glProfiler.getShaderSwitches());
        // Log the current FPS to the console.
        fpsLogger.log();
        player.move();
        player.draw(batch);
        cam.position.set(player.getPositionVector().x, player.getPositionVector().y, 0);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        for (Goose g : geese){
            g.move();
            g.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
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

    public Viewport getViewport() {
        return viewport;
    }

    public OrthographicCamera getCamera() {
        return cam;
    }
}

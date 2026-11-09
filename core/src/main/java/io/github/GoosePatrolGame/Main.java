package io.github.GoosePatrolGame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.profiling.GLProfiler;

import io.github.GoosePatrolGame.asset.AssetService;
import io.github.GoosePatrolGame.screen.LoadingScreen;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

/*
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */

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
    /** Handles and distributes input events to multiple input processors. */
    private InputMultiplexer inputMultiplexer;

    boolean paused = false;

    /** Hashmap for storing the game's screens. */
    private final Map<Class<? extends Screen>, Screen> screenCache = new HashMap<>();

    @Override
    public void create() {
        // Set the log level to DEBUG for detailed logging during development.
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        // Create a new InputMultiplexer and set it as LibGDX's active input processor.
        this.inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        this.batch = new SpriteBatch();
        this.cam = new OrthographicCamera(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, this.cam);
        this.assetService = new AssetService(new InternalFileHandleResolver());
        this.glProfiler = new GLProfiler(Gdx.graphics);
        this.glProfiler.enable();
        this.fpsLogger = new FPSLogger();

        // Add the LoadingScreen to the screen cache and set it as the current screen.
        addScreen(new LoadingScreen(this, assetService));
        setScreen(LoadingScreen.class);
        
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

        super.render();

        // Update the window title with the current FPS and OpenGL profiling data.
        Gdx.graphics.setTitle("GoosePatrolGame | FPS: " + Gdx.graphics.getFramesPerSecond() +
                " | Draw Calls: " + glProfiler.getDrawCalls() +
                " | Texture Bindings: " + glProfiler.getTextureBindings() +
                " | Shader Switches: " + glProfiler.getShaderSwitches());

        // Log the current FPS to the console.
        fpsLogger.log();
    }

    public void addScreen(Screen screen) {
        screenCache.put(screen.getClass(), screen);
    }

    public void removeScreen(Screen screen) {
        screenCache.remove(screen.getClass());
    }

    public void setScreen(Class<? extends Screen> screenClass) {
        Screen screen = screenCache.get(screenClass);
        if(screen == null) {
            throw new GdxRuntimeException("No screen with class " + screenClass + " found in the screen cache");
        }
        super.setScreen(screen);
    }

    @Override
    public void dispose() {
        screenCache.values().forEach(Screen::dispose);
        screenCache.clear();

        this.batch.dispose();
        this.assetService.debugDiagnostics();
        this.assetService.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetService getAssetService() {
        return assetService;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public OrthographicCamera getCamera() {
        return cam;
    }

    public void setInputProcessors(InputProcessor ... processors) {
        inputMultiplexer.clear();
        if(processors == null) return;

        for (InputProcessor processor : processors) {
            inputMultiplexer.addProcessor(processor);
        }
    }
}
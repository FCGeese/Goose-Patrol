package io.github.EngOnePrototype;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.ashley.core.Engine;

import io.github.EngOnePrototype.Elements.*;
import io.github.EngOnePrototype.UI.HealthBar;
import io.github.EngOnePrototype.UI.MiniMap;
import io.github.EngOnePrototype.Utils.Coord;
import io.github.EngOnePrototype.Utils.Hitbox;
import io.github.EngOnePrototype.Utils.Rect;
import io.github.EngOnePrototype.asset.AssetService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;


/*
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */

public class Main extends Game implements InputProcessor {

    // Keep 16:9 aspect ratio for the game world, so that it looks the same on all devices.
    public static final float WORLD_WIDTH = 32f; // Shown world width in tiled units
    public static final float WORLD_HEIGHT = 18f; // Shown world height in tiled units
    public static final float UNIT_SCALE = 1f / 16f; // 1 unit = 16 pixels
    
    ShapeRenderer shape;
    Player player;
    ArrayList<Goose> geese = new ArrayList<>();
    ArrayList<Hitbox> buildings = new ArrayList<>();
    ArrayList<Food> placedFood = new ArrayList<>();
    TextureRegion Map;
    HealthBar health;
    MiniMap miniMap;

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
    /** Ashely engine for managing game entities and systems. */
    private Engine engine;

    boolean paused = false;

    /** Hashmap for storing the game's screens. */
    private final Map<Class<? extends Screen>, Screen> screenCache = new HashMap<>();

    @Override
    public void create() {
        // Set the log level to DEBUG for detailed logging during development.
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        this.batch = new SpriteBatch();
        this.cam = new OrthographicCamera(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, this.cam);
        this.assetService = new AssetService(new InternalFileHandleResolver());
        this.glProfiler = new GLProfiler(Gdx.graphics);
        this.glProfiler.enable();
        this.fpsLogger = new FPSLogger();
        this.engine = new Engine();

        // Add the GameScreen to the screen cache and set it as the current screen.
        addScreen(new GameScreen(this));
        setScreen(GameScreen.class);
        
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
        
        shape = new ShapeRenderer();
        
        Map = new TextureRegion(new Texture(Gdx.files.internal("mapImage.png")));
        player = new Player(new Coord(Settings.playerStartX, Settings.playerStartY),
                Settings.playerSpeed);
        health = new HealthBar();
        miniMap = new MiniMap();

        for (int i = 0; i < Settings.numberOfGeese; i++) {
            geese.add(new Goose(new Coord(i * 100, i * 100),
                    Settings.gooseSpeed, Settings.gooseAccuracy));
        }

        buildings.add(new Hitbox(new Rect(new Coord(291, 741), 200, 182)));
        buildings.add(new Hitbox(new Rect(new Coord(811, 263), 85, 75)));
        buildings.add(new Hitbox(new Rect(new Coord(973, 454), 148, 272)));

        Gdx.input.setInputProcessor(this);
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
        Gdx.graphics.setTitle("EngOnePrototype | FPS: " + Gdx.graphics.getFramesPerSecond() +
                " | Draw Calls: " + glProfiler.getDrawCalls() +
                " | Texture Bindings: " + glProfiler.getTextureBindings() +
                " | Shader Switches: " + glProfiler.getShaderSwitches());

        // Log the current FPS to the console.
        fpsLogger.log();
    /*
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(0, 0, 1, 1, true);

        batch.begin();
        batch.draw(Map, 0, 0);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);

        player.render(batch, buildings);

        cam.position.set(player.getX(), player.getY(), 0);
        cam.viewportWidth = 300;
        cam.viewportHeight = 300;
        cam.update();
        batch.begin();
        batch.setProjectionMatrix(cam.combined);

        for (Goose goose : geese) {
            Element nearestFood = player;
            if (!placedFood.isEmpty()) {
                for (Food food : placedFood) {
                    food.draw(batch);
                    if (goose.getPosition().distanceFrom(food.getPosition()) < goose.getPosition()
                            .distanceFrom(nearestFood.getPosition())) {
                        nearestFood = food;
                    }
                }
                goose.render(batch, geese, buildings, nearestFood);
            } else {
                goose.render(batch, geese, buildings, player);
            }
        }
        placedFood.removeIf(food -> food.getHealth() < 0);
        health.draw(shape, player);
        ArrayList<Goose> mapGeese = new ArrayList<Goose>(geese);
        miniMap.draw(batch, shape, player, mapGeese);
        batch.end();
        shape.end();
        */
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            paused = !paused;
            if (paused) {
                player.setSpeed(0);
                for (Goose goose : geese) {
                    goose.setSpeed(0);
                }
            } else {
                player.setSpeed(Settings.playerSpeed);
                for (Goose goose : geese) {
                    goose.setSpeed(Settings.gooseSpeed);
                }
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {

            Vector3 mouseVector = cam.unproject(new Vector3(screenX, screenY, 0));

            float direction = player.getPosition().findDirectionTo(
                    new Coord(
                            (int) mouseVector.x,
                            (int) mouseVector.y));

            Coord foodPos = new Coord(
                    player.getPosition().getX() + (int) (Math.cos(Math.toRadians(direction)) * 50),
                    player.getPosition().getY() + (int) (Math.sin(Math.toRadians(direction)) * 50));

            placedFood.add(new Food(foodPos));
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
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

    @Override
    public void dispose() {
        screenCache.values().forEach(Screen::dispose);
        screenCache.clear();

        this.batch.dispose();
        this.assetService.debugDiagnostics();
        this.assetService.dispose();

        shape.dispose();
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

    public Engine getEngine() {
        return engine;
    }
}
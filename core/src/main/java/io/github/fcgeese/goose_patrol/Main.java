package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.fcgeese.goose_patrol.Asset.AssetService;
import io.github.fcgeese.goose_patrol.Elements.*;
import io.github.fcgeese.goose_patrol.Events.SpeedUpEvent;
import io.github.fcgeese.goose_patrol.Events.WinEvent;
import io.github.fcgeese.goose_patrol.Managers.TargetManager;
import io.github.fcgeese.goose_patrol.UI.HealthBar;
import io.github.fcgeese.goose_patrol.UI.WinCountdown;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game implements InputProcessor {

    // Keep 16:9 aspect ratio for the game world, so that it looks the same on all devices.
    public static final float WORLD_WIDTH = 32f; // Shown world width in tiled units
    public static final float WORLD_HEIGHT = 18f; // Shown world height in tiled units
    public static final float UNIT_SCALE = 1f / 16f; // 1 unit = 16 pixels
    public boolean isPuased = false;

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
    //Large lists of all elements to allow for single render loop
    private List<Element> allElements = new ArrayList<>();
    //Large lists of all elements to allow for single movement loop
    private List<MovingElement> allMovers = new ArrayList<>();
    //List of every goose object
    private List<Goose> geese = new ArrayList<>();
    //List of everything collidable for moving elemetns
    private List<Element> gooseColliders = new ArrayList<>();
    private List<Element> playerColliders = new ArrayList<>();
    //List of everything geese can target
    private List<Element> targets = new ArrayList<>();

    //Time remaining in game
    public float timeRemaining = 300f;
    //UI elements
    public HealthBar health;
    public WinCountdown winBar;
    public ShapeRenderer shape;
    //Events
    public SpeedUpEvent gooseSpeedEvent;
    public WinEvent win;
    //items
    public BreadItem bread;
    public CoffeeItem coffee;

    @Override
    public void create() {
        // Gdx treats main as its primary input handler.
        Gdx.input.setInputProcessor(this);

        // UI
        health = new HealthBar();
        winBar = new WinCountdown();
        shape = new ShapeRenderer();

        // Events
        gooseSpeedEvent = new SpeedUpEvent();

        // Set the log level to DEBUG for detailed logging during development.
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam);
        viewport.update(
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight(),
            true);
        assetService = new AssetService(new InternalFileHandleResolver());
        glProfiler = new GLProfiler(Gdx.graphics);
        glProfiler.enable();
        fpsLogger = new FPSLogger();
        batch = new SpriteBatch();

        // Add the GameScreen to the screen cache and set it as the current screen.
        addScreen(new GameScreen(this));
        setScreen(GameScreen.class);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);

        // Generates player and assigns it a healthbar listener.
        player = new Player(new Vector2(400 * UNIT_SCALE, 100 * UNIT_SCALE));
        player.addHealthListener(health);
        targets.add(player);

        for (int x = 12; x < 18; x++) {
            geese.add(new Goose(new Vector2(8,x)));
        }

        TiledMap map = new TmxMapLoader().load("maps/main.tmx");
        MapLayer mapHitbox = map.getLayers().get("MapHitboxes");
        ArrayList<Element> mapHitboxes = new ArrayList<>();
        for (MapObject o: mapHitbox.getObjects()){
            Rectangle terrainHitbox = ((RectangleMapObject)o ).getRectangle();
            Element collider = new Element();
            collider.setHitbox(new Rectangle(
                terrainHitbox.x*UNIT_SCALE,
                terrainHitbox.y * UNIT_SCALE,
                terrainHitbox.width * UNIT_SCALE,
                terrainHitbox.height * UNIT_SCALE)
            );
            Gdx.app.log("HitboxDebug", "Raw Tiled width: " + terrainHitbox.width + " -> Scaled width: " + (terrainHitbox.width * UNIT_SCALE));
            mapHitboxes.add(collider);
        }
        gooseColliders.addAll(geese);
        gooseColliders.addAll(mapHitboxes);
        for (Goose g : geese) {
            g.setColliderList(gooseColliders);
            g.setTargetList(targets);
        }
        player.setColliderList(mapHitboxes);
        allMovers.addAll(geese);
        allMovers.add(player);
        allElements.addAll(allMovers);
        allElements.addAll(mapHitboxes);

    }

    // Window size is changed by the user
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        super.resize(width, height);
    }

    @Override
    public void render() {
        if(!isPuased) {
            float delta = Gdx.graphics.getDeltaTime();

            // Reset the GLProfiler at the start of each frame to get accurate profiling data for the current frame.
             glProfiler.reset();

            // Clear the screen with a black color before rendering the next frame.
            Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // ### UPDATES ###

            // Update game time
            timeRemaining = Math.max(timeRemaining - delta, 0);

            if (timeRemaining == 0) {
                win.onStart();
            }

            // Update goose speed
            // Increase goose speed by 1% every second for the final 50 seconds.
            // Note: gooseSpeed ends up being ~1.974f after 50 seconds, is initialised as 1f.
            if (timeRemaining <= 50) {
                gooseSpeedEvent.changeEffect();
            }
            // Increase goose speed statically when game is halfway finished.
            else if (timeRemaining <= 150) {
                //Ensure we only change gooseSpeed once.
                if (Settings.gooseSpeed == 1f) {
                    Gdx.app.log("Time", "Time remaining: " + timeRemaining);
                    Gdx.app.log("Event", "Halfway through game. Goose speed increased.");
                    gooseSpeedEvent.onStart();
                }
            }

            // Update all movement logic
            for (MovingElement e : allMovers) {
                e.move();
            }

            // ### RENDERING ###

            // Resets camera
            cam.position.set(
                player.getPositionVector().x + Settings.playerWidth / 2,
                player.getPositionVector().y + Settings.playerHeight / 2,
                0
            );
            cam.update();

            // Update projection matrix and render background map
            batch.setProjectionMatrix(cam.combined);
            screen.render(delta);

            // Render game objects
            batch.begin();

            for (Element e : allMovers) {
                e.draw(batch);
            }
            batch.end();

            // Draws UI elements
            shape.begin(ShapeRenderer.ShapeType.Filled);
            winBar.draw(shape, timeRemaining);
            health.draw(shape);
            shape.end();

            // ### DEBUGGING ###

            // Update the window title with the current FPS and OpenGL profiling data.
            Gdx.graphics.setTitle("Goose-Patrol | FPS: " + Gdx.graphics.getFramesPerSecond() +
                " | Draw Calls: " + glProfiler.getDrawCalls() +
                " | Texture Bindings: " + glProfiler.getTextureBindings() +
                " | Shader Switches: " + glProfiler.getShaderSwitches());

            // Log the current FPS to the console.
            fpsLogger.log();


        }
    }

    @Override
    public void dispose() {
        batch.dispose(); shape.dispose();
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


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE){
            isPuased = !isPuased;
            return true;
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
}

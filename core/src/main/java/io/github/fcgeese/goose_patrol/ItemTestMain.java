package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TEMPORARY test harness for the item system - not part of the real game.
 * To use it, point your launcher at this class instead of Main:
 *     new Lwjgl3Application(new ItemTestMain(), config);
 *
 * What you get:
 *   - a blue square "player" you move with WASD
 *   - a row of white squares standing in for geese (bread passes straight
 *     through them for now). They are plain Elements, so this test does NOT
 *     need Goose.png or the Goose class.
 *   - the ItemManager running exactly as it would in the real game
 *   - console logs for the things you can't see on screen
 */
public class ItemTestMain extends ApplicationAdapter {

    private static final float PLAYER_SPEED = 150f; // pixels per second, before any coffee boost

    private SpriteBatch batch;
    private ItemManager itemManager;
    private Element player;
    private Texture playerTexture;
    private Texture gooseStandInTexture;
    private final List<Element> geese = new ArrayList<>();

    // Used to log only when something changes
    private Item lastHeld = null;
    private float lastMultiplier = 1f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        itemManager = new ItemManager(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Stand-in player: Element has no abstract methods, so a plain subclass works
        playerTexture = makeSquare(Color.BLUE, 20);
        player = new Element() { };
        player.setTexture(new TextureRegion(playerTexture));
        player.setPositionVector(new Vector2(50, 50));
        player.setHitbox(new Rectangle(50, 50, 20, 20));

        // Five stationary white squares in a row, just for visual reference
        gooseStandInTexture = makeSquare(Color.WHITE, 16);
        for (int i = 0; i < 5; i++) {
            Element goose = new Element() { };
            goose.setTexture(new TextureRegion(gooseStandInTexture));
            goose.setPositionVector(new Vector2(150 + i * 80, 300));
            goose.setHitbox(new Rectangle(150 + i * 80, 300, 16, 16));
            geese.add(goose);
        }
        Gdx.app.log("ItemTest", "Started with " + geese.size() + " stand-in geese");
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Coffee boost shows up here: the manager's multiplier scales the player's speed
        movePlayer(delta, PLAYER_SPEED * itemManager.GetSpeedMultiplier());

        // Mouse position in world coordinates (no camera, so just flip the y axis)
        Vector2 aim = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        itemManager.update(delta, player, aim);


        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        for (Element goose : geese) {
            goose.draw(batch);
        }
        itemManager.draw(batch);
        player.draw(batch);
        batch.end();
    }

    /** WASD movement; keeps the hitbox glued to the position. */
    private void movePlayer(float delta, float speed) {
        Vector2 move = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) move.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) move.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) move.x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) move.x -= 1;
        if (!move.isZero()) {
            move.nor().scl(speed * delta);
            player.getPositionVector().add(move);
            player.getHitbox().setPosition(player.getPositionVector());
        }
    }



    private static Texture makeSquare(Color color, int size) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void dispose() {
        batch.dispose();
        itemManager.dispose();
        playerTexture.dispose();
        gooseStandInTexture.dispose();
    }
}

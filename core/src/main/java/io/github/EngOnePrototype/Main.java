package io.github.EngOnePrototype;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.EngOnePrototype.Elements.*;
import io.github.EngOnePrototype.Utils.Coord;
import io.github.EngOnePrototype.Utils.Hitbox;
import io.github.EngOnePrototype.Utils.Rect;

/*
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */

public class Main extends ApplicationAdapter implements InputProcessor {
    static final int WORLD_WIDTH = 1920;
    static final int WORLD_HEIGHT = 1080;
    Camera cam;
    ShapeRenderer shape;
    SpriteBatch batch;
    Player player;
    ArrayList<Goose> geese = new ArrayList<>();
    ArrayList<Hitbox> buildings = new ArrayList<>();
    TextureRegion Map;

    boolean paused = false;

    @Override
    public void create() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
        cam = new OrthographicCamera(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        shape = new ShapeRenderer();
        batch = new SpriteBatch();
        Map = new TextureRegion(new Texture(Gdx.files.internal("mapImage.png")));
        player = new Player(new Coord(Settings.playerStartX, Settings.playerStartY),
                Settings.playerSpeed);

        for (int i = 0; i < Settings.numberOfGeese; i++) {
            geese.add(new Goose(new Coord(i * 100, i * 100),
                    Settings.gooseSpeed, Settings.gooseAccuracy));
        }

        buildings.add(new Hitbox(new Rect(new Coord(291, 741), 200, 182)));
        buildings.add(new Hitbox(new Rect(new Coord(811, 263), 85, 75)));
        buildings.add(new Hitbox(new Rect(new Coord(973, 454), 148, 272)));

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // ScreenUtils.clear(Color.OLIVE);
        batch.begin();
        batch.draw(Map, 0, 0);
        batch.end();
        player.render(batch, buildings);
        cam.position.set(player.getX(), player.getY(), 0);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        for (Goose goose : geese) {
            goose.render(batch, geese, buildings, player);
        }
    }

    @Override
    public void dispose() {
        shape.dispose();
        batch.dispose();
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
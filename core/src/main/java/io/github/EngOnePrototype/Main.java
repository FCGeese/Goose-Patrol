package io.github.EngOnePrototype;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.EngOnePrototype.Mobs.*;
import io.github.EngOnePrototype.Utils.Coord;

/*
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */

public class Main extends ApplicationAdapter implements InputProcessor {
    ShapeRenderer shape;
    SpriteBatch batch;
    Texture gooseTexture;
    Player player;
    TextureRegion gooseTextureRegion;
    ArrayList<Goose> geese = new ArrayList<>();

    boolean paused = false;

    @Override
    public void create() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
        shape = new ShapeRenderer();
        batch = new SpriteBatch();
        gooseTexture = new Texture(Gdx.files.internal("16x16SideGoose3.png"));
        gooseTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        gooseTextureRegion = new TextureRegion(gooseTexture);

        player = new Player(Settings.playerStartX, Settings.playerStartY, Settings.playerSpeed);

        for (int i = 0; i < Settings.numberOfGeese; i++) {
            geese.add(new Goose(i * 500, i * 100, Settings.gooseSpeed, Settings.gooseAccuracy));
        }

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //ScreenUtils.clear(Color.OLIVE);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        // player
        shape.setColor(Color.RED);
        if (Settings.movement == "WASD+Mouse") {
            player.move(player.update());
            player.draw(shape);
        } else if (Settings.movement == "TANK") {
            player.tankUpdate(Settings.tankTurnSpeed);
            player.tankDraw(shape);
        }
        shape.end();

        batch.begin();
        // geese
        shape.setColor(Color.LIGHT_GRAY);
        for (Goose goose : geese) {
            // kill state
            if (goose.getPosition().checkCollision(player.getPosition(), player.getSize() + goose.getSize())) {
                dispose();
            }
            // pursuit
            Coord nextGooseFrame = goose.update(player);
            boolean collision = false;
            // Flock collision
            for (Goose goose2 : geese) {
                if (goose != goose2) {
                    if (nextGooseFrame.checkCollision(
                        goose2.getPosition(), player.getSize()+goose.getSize())
                    ) {

                        // undo move if collision takes place
                        collision = true;
                    }
                }
            }
            if (!collision){
                goose.move(nextGooseFrame);
            }

            // goose.draw(shape); 
            if (player.getX()>goose.getX()){
                if (!(goose.getFlipped())){
                    gooseTextureRegion.flip(true,false);

                }
                goose.setFlipped(true);

            }
            else if (goose.getX()>player.getX()){
                if (goose.getFlipped()){
                    gooseTextureRegion.flip(true,false);
                }
                goose.setFlipped(false);
            }
            batch.draw(gooseTextureRegion,
                goose.getPosition().getX(),
                goose.getPosition().getY());
        }
        batch.end();

    }

    @Override
    public void dispose() {
        shape.dispose();
        batch.dispose();
        gooseTexture.dispose();
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
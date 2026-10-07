package io.github.EngOnePrototype;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.EngOnePrototype.Mobs.*;

/*
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */

public class Main extends ApplicationAdapter {
    ShapeRenderer shape;
    Player player;
    ArrayList<Goose> geese = new ArrayList<>();

    @Override
    public void create() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
        shape = new ShapeRenderer();

        player = new Player(Settings.playerStartX, Settings.playerStartY, Settings.playerSpeed);

        for (int i = 0; i < Settings.numberOfGeese; i++) {
            geese.add( new Goose(i * 500, i * 100, Settings.gooseSpeed, Settings.gooseAccuracy) );
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        //player
        shape.setColor(Color.RED);
        player.update();
        player.draw(shape);

        //geese
        shape.setColor(Color.LIGHT_GRAY);
        for (Goose goose : geese) {
            //kill state
            if (goose.checkCollision(player)) {
                dispose();
            }
            //pursuit
            goose.update(player);
            //Flock collision
            for (Goose goose2 : geese) {
                if (goose != goose2) {
                    if (goose.checkCollision(goose2, 5)) {
                        //undo move if collision takes place
                        goose.setSpeed(-goose.getSpeed());
                        goose.update();
                        goose.setSpeed(-goose.getSpeed());
                    }
                }
            }
            goose.draw(shape);
        }
        shape.end();

    }

    @Override
    public void dispose() {
        shape.dispose();
    }
}
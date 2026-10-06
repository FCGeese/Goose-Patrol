package io.github.EngOnePrototype;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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
        shape = new ShapeRenderer();
        player = new Player(Settings.playerStartX, Settings.playerStartY, Settings.playerSpeed);
        for (int i = 0; i < 5; i++) {
            geese.add(new Goose(i * 30, i * 100, 2, (int) (Math.random() * 360)));
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.RED);
        player.update();
        player.draw(shape);
        shape.setColor(Color.LIGHT_GRAY);
        for (Goose goose : geese) {
            if (goose.checkCollision(player)) {
                dispose();
            }

            goose.update(player);
            for (Goose goose2 : geese) {
                if (goose != goose2) {
                    if (goose.checkCollision(goose2, 5)) {
                        System.out.println("COLLIDES");
                        goose.direction = (goose.direction - 180) % 360;
                        goose.update();
                        goose.direction = (goose.direction - 180) % 360;
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
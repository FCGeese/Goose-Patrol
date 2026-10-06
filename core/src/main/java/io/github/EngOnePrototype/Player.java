package io.github.EngOnePrototype;

import com.badlogic.gdx.Gdx;

public class Player extends Entity {

    public Player(int x, int y, int speed) {
        super(x, y, speed, 0, 10);
    }

    public void update() {
        super.direction = findDirection(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        super.update();
    }

}

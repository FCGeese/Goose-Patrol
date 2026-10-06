package io.github.EngOnePrototype;

import com.badlogic.gdx.Gdx;

public class Player extends Entity {

    public Player(int x, int y, int speed) {
        super(x, y, speed, 0, 10);
    }

    public void update() {
        // key watchers
        if (Gdx.input.isKeyPressed(51)) {
            if (Gdx.input.isKeyPressed(59)) {
                speed = Settings.playerSprintSpeed;
            }
            speed = Settings.playerSpeed;
        } else {
            if (Gdx.input.isKeyPressed(47)) {
                speed = -Settings.playerSpeed;
            } else {
                speed = 0;
            }

        }
        super.direction = findDirection(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        super.update();
    }

}

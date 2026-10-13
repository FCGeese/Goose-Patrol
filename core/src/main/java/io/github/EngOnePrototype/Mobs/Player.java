package io.github.EngOnePrototype.Mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.EngOnePrototype.Settings;
import io.github.EngOnePrototype.Utils.Coord;

public class Player extends Entity {
    int defaultSpeed;

    public Player(int x, int y, int defaultSpeed) {
        super(x, y, 0, 1, 10);
        this.defaultSpeed = defaultSpeed;
    }

    public Player(Coord playerStart, int defaultSpeed) {
        super(playerStart);
        this.defaultSpeed = defaultSpeed;
    }

    public void setDefaultSpeed(int defaultSpeed) {
        this.defaultSpeed = defaultSpeed;
    }

    @Override
    public Coord update() {
        int newX = position.getX();
        int newY = position.getY();
        // key watchers
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            speed = 2 * speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (Gdx.input.isKeyPressed(Input.Keys.A) ^ (Gdx.input.isKeyPressed(Input.Keys.D))) {
                // stops the '45 degree rush'
                speed = (int) Math.pow(speed, 2);
                speed = speed / 2;
                speed = (int) Math.sqrt(speed);
            }
            newY = position.getY() + speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (Gdx.input.isKeyPressed(Input.Keys.A) ^ (Gdx.input.isKeyPressed(Input.Keys.D))) {
                speed = (int) Math.pow(speed, 2);
                speed = speed / 2;
                speed = (int) Math.sqrt(speed);
            }
            newY = position.getY() - speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newX = position.getX() - speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newX = position.getX() + speed;
        }
        speed = defaultSpeed;
        return new Coord(newX, newY);
    }

    @Override
    public void draw(ShapeRenderer shape) {
        Coord mouseCoord = new Coord(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        direction = position.findDirectionTo(mouseCoord);
        shape.circle(position.getX(), position.getY(), size);
        Coord reticle = new Coord(
                position.getX() + (int) (Math.cos(Math.toRadians(direction)) * 50),
                position.getY() + (int) (Math.sin(Math.toRadians(direction)) * 50));

        shape.line(position.getX(), position.getY(),
                reticle.getX(), reticle.getY());
    }

    public void tankDraw(ShapeRenderer shape) {
        shape.circle(position.getX(), position.getY(), size);
        Coord reticle = new Coord(
                position.getX() + (int) (Math.cos(Math.toRadians(direction)) * 50),
                position.getY() + (int) (Math.sin(Math.toRadians(direction)) * 50));
        shape.line(position.getX(), position.getY(),
                reticle.getX(), reticle.getY());
    }

    public Coord tankUpdate(int turnSpeed) {
        Coord nextPosition = new Coord(position.getX(), position.getY());
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            speed = 2 * speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            nextPosition.set(super.update());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            speed = -speed;
            nextPosition.set(super.update());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction = ((direction + turnSpeed) % 360);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction = ((direction - turnSpeed) % 360);
        }
        speed = defaultSpeed;
        return nextPosition;
    }

    public void render(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        // player
        shape.setColor(Color.RED);
        if (Settings.movement == "WASD+Mouse") {
            position.set(update());
            draw(shape);
        } else if (Settings.movement == "TANK") {
            position.set(tankUpdate(Settings.tankTurnSpeed));
            tankDraw(shape);
        }
        shape.end();
    }
}

/*
 * OLD MOUSE CHASING MOVEMENT useful for joystick control?
 * public void update() {
 * direction = findDirection(Gdx.input.getX(), Gdx.graphics.getHeight() -
 * Gdx.input.getY());
 * // key
 * //strafe
 * char strafeDirection=' ';
 * if (Gdx.input.isKeyPressed(29) ^ Gdx.input.isKeyPressed(32)){
 * if(Gdx.input.isKeyPressed(29)){
 * strafeDirection = 'l';
 * }
 * else{
 * strafeDirection = 'r';
 * }
 * speed = Settings.playerSpeed;
 * super.update(strafeDirection);
 * speed = 0;
 * }
 * 
 * //forward/reverse
 * if (Gdx.input.isKeyPressed(51)) {
 * if (Gdx.input.isKeyPressed(59)) {
 * speed = Settings.playerSprintSpeed;
 * }
 * else{
 * speed = Settings.playerSpeed;
 * }
 * } else {
 * if (Gdx.input.isKeyPressed(47)) {
 * speed = -Settings.playerSpeed;
 * } else {
 * speed = 0;
 * }
 * 
 * }
 * 
 * super.update();
 * }
 */
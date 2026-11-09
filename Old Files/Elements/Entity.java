package io.github.GoosePatrolGame.Elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.GoosePatrolGame.Utils.Coord;

public abstract class Entity extends Element {
    int speed = 0;
    float direction = 0;

    public Entity(TextureRegion texture, Coord position) {
        super(texture, position);
    }

    public Entity(TextureRegion texture, Coord position, int speed) {
        super(texture, position);
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Coord update() {
        int xSpeed = (int) (Math.cos(Math.toRadians(direction)) * speed);
        int ySpeed = (int) (Math.sin(Math.toRadians(direction)) * speed);
        return new Coord(position.getX() + xSpeed, position.getY() + ySpeed);
    }
}

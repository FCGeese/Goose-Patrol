package io.github.EngOnePrototype.Elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.EngOnePrototype.Utils.Coord;

public abstract class Entity extends Element {
    int speed = 0;
    int size = 5; /// REMOVE ONCE HITBOXS SET
    float direction = 0;

    public Entity(TextureRegion texture, Coord position) {
        super(texture, position);
    }

    public Entity(TextureRegion texture, Coord position, int speed) {
        super(texture, position);
        this.speed = speed;
    }

    public int getSize() {
        return size;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public Coord getPosition() {
        return position;
    }

    public Coord update() {
        int xSpeed = (int) (Math.cos(Math.toRadians(direction)) * speed);
        int ySpeed = (int) (Math.sin(Math.toRadians(direction)) * speed);
        return new Coord(position.getX() + xSpeed, position.getY() + ySpeed);
    }

    public Coord move(Coord newPosition) {
        hitbox.move(newPosition.delta(position));
        position.set(newPosition);
        return newPosition;
    }
}

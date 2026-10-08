package io.github.EngOnePrototype.Mobs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Entity {
    int x;
    int y;
    int speed;
    float direction = 0;
    int size = 5;

    public Entity(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public Entity(int x, int y, int speed, int direction) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = direction;
    }

    public Entity(int x, int y, int speed, int direction, int size) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = direction;
        this.size = size;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update() {
        int xSpeed = (int) (Math.cos(Math.toRadians(direction)) * speed);
        int ySpeed = (int) (Math.sin(Math.toRadians(direction)) * speed);
        x = x + xSpeed;
        y = y + ySpeed;
    }

    public boolean checkCollision(Entity e) {
        if (distanceFrom(e.x, e.y) <= e.size + size) {
            return true;
        }
        return false;
    }

    public void draw(ShapeRenderer shape) {
        shape.circle(x, y, size);
    }

    public void draw(SpriteBatch batch) {

    }

    protected float findDirection(int targetX, int targetY) {
        int deltaX = targetX - x;
        int deltaY = targetY - y;
        return (float) (Math.toDegrees(Math.atan2(deltaY, deltaX)) + 360) % 360;
    }

    public boolean checkCollision(Entity e, int minDist) {
        if (distanceFrom(e.x, e.y) <= size + minDist) {
            return true;
        }
        return false;
    }

    private float distanceFrom(int otherX, int otherY) {
        return (float) Math.hypot((x - otherX), (y - otherY));
    }
}

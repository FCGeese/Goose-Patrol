package io.github.EngOnePrototype;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Entity {
    int x;
    int y;
    int speed;
    float direction;
    int size = 5;

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

    public void update() {
        int xSpeed = (int) (Math.cos(Math.toRadians(direction)) * speed);
        int ySpeed = (int) (Math.sin(Math.toRadians(direction)) * speed);
        x = x + xSpeed;
        y = y + ySpeed;
    }

    public void draw(ShapeRenderer shape) {
        shape.circle(x, y, size);
    }

    protected float findDirection(int targetX, int targetY) {
        int deltaX = targetX - x;
        int deltaY = targetY - y;
        return (float) (Math.toDegrees(Math.atan2(deltaY, deltaX)) + 360) % 360;
    }

    protected boolean checkCollision(Entity e, int minDist) {
        if (distanceFrom(e.x, e.y) <= size + minDist) {
            return true;
        }
        return false;
    }

    protected boolean checkCollision(Entity e) {
        if (distanceFrom(e.x, e.y) <= e.size + size) {
            return true;
        }
        return false;
    }

    private float distanceFrom(int otherX, int otherY) {
        return (float) Math.hypot((x - otherX), (y - otherY));
    }
}

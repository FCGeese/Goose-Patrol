package io.github.EngOnePrototype.Mobs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.EngOnePrototype.Utils.Coord;

public class Entity {
    Coord position;
    int speed;
    float direction = 0;
    int size = 5;

    public Entity(int x, int y, int speed) {
        position = new Coord(x, y);
        this.speed = speed;
    }
    public Entity(Coord position){
        this.position = position;
    }

    public Entity(int x, int y, int speed, int direction) {
        position = new Coord(x, y);
        this.speed = speed;
        this.direction = direction;
    }

    public Entity(int x, int y, int speed, int direction, int size) {
        position = new Coord(x, y);
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
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }
    public int getSize() {
        return size;
    }

    public Coord getPosition() {
        return position;
    }

    public Coord update() {
        int xSpeed = (int) (Math.cos(Math.toRadians(direction)) * speed);
        int ySpeed = (int) (Math.sin(Math.toRadians(direction)) * speed);
        return new Coord(position.getX()+xSpeed, position.getY()+ySpeed);
    }

    public void move(Coord newPosition){
        position.set(newPosition);
    }

    public void draw(ShapeRenderer shape) {
        shape.circle(position.getX(), position.getY(), size);
    }

    public void draw(SpriteBatch batch) {

    }

}

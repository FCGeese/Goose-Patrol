package io.github.GoosePatrolGame.Utils;

public class Coord {
    private int x;
    private int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void set(Coord newCoord) {
        this.x = newCoord.getX();
        this.y = newCoord.getY();
    }

    public float distanceFrom(Coord other) {
        return (float) Math.hypot((this.x - other.getX()), (this.y - other.getY()));
    }

    public boolean checkCollision(Coord other, int collisionRadius) {
        if (distanceFrom(other) <= collisionRadius) {
            return true;
        }
        return false;
    }

    public float findDirectionTo(Coord other) {
        int deltaX = other.getX() - x;
        int deltaY = other.getY() - y;
        return (float) (Math.toDegrees(Math.atan2(deltaY, deltaX)) + 360) % 360;
    }

    public Coord add(Coord other) {
        return new Coord(x + other.getX(), y + other.getY());
    }

    public Coord delta(Coord other) {
        return new Coord(x - other.getX(), y - other.getY());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

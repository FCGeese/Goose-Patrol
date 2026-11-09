package io.github.GoosePatrolGame.Utils;

/**
 * Rect
 */
public class Rect {
    Coord position;
    int width;
    int height;
    Coord[] corners;

    public Rect(Coord position, int width, int height) {
        this.position = position;
        this.height = height;
        this.width = width;
        corners = new Coord[] { position,
                new Coord(position.getX(), position.getY() + height),
                new Coord(position.getX() + width, position.getY()),
                new Coord(position.getX() + width, position.getY() + height) };
    }

    public boolean isOverlapped(Rect other) {
        return !(((this.position.getX()) > (other.position.getX() + other.width)) ||
                ((this.position.getY()) > (other.position.getY() + other.height)) ||
                ((this.position.getX() + this.width) < (other.position.getX())) ||
                ((this.position.getY() + this.height) < (other.position.getY())));

    }

    /*
     * MORE READABLE? LESS READABLE?
     * public boolean isOverlapped(Rect other) {
     * return !((corners[0].getX() > other.corners[3].getX()) || // left of this >
     * right of other
     * (corners[3].getX() < other.corners[0].getX()) || // right of this corner <
     * left of other corner
     * (corners[0].getY() > other.corners[3].getY()) || // bottom of this > top of
     * other
     * (corners[3].getY() < other.corners[0].getY())); // top of this corner < top
     * of other corner
     * }
     */
    @Override
    public String toString() {
        return position.toString() + " Width:" + width + " Height:" + height;
    }
}

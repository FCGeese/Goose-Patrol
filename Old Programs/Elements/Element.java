package io.github.EngOnePrototype.Elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.EngOnePrototype.Utils.Coord;
import io.github.EngOnePrototype.Utils.Hitbox;

/**
 * Element
 */
public abstract class Element {
    TextureRegion texture;
    Coord position;
    Hitbox hitbox;
    private int health;

    public Element(TextureRegion texture, Coord position, Hitbox hitbox) {
        this.texture = texture;
        this.position = position;
        this.hitbox = hitbox;
    }

    public Element(TextureRegion texture, Coord position) {
        this.texture = texture;
        this.position = position;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public Coord getPosition() {
        return position;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public Coord setPosition(Coord newPosition) {
        hitbox.setPosition(newPosition.delta(position));
        position.set(newPosition);
        return newPosition;
    }
    public boolean isCollide(Element other) {
        return this.hitbox.isCollision(other.hitbox);
    }

    public void draw(SpriteBatch batch) {
        //batch.begin();
        batch.draw(texture, position.getX(), position.getY());
        //batch.end();
    }
}

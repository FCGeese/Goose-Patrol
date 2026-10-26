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

    public Element(TextureRegion texture, Coord position, Hitbox hitbox) {
        this.texture = texture;
        this.position = position;
        this.hitbox = hitbox;
    }

    public Element(TextureRegion texture, Coord position) {
        this.texture = texture;
        this.position = position;
    }

    public boolean isCollide(Element other) {
        return this.hitbox.isCollision(other.hitbox);
    }

    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, position.getX(), position.getY());
    }
}

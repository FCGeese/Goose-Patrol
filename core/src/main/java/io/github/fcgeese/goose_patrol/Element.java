package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Element {
    private Vector2 position;
    private TextureRegion texture;
    private int health;
    private Rectangle hitbox;

    public int getHealth() {
        return health;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    /**
     * Collision checker for two elements
     * @param other element to check collision with
     * @return true if the hitboxes of this and other elements overlap
     *          false otherwise
     */
    public boolean collidesWith(Element other){
        return hitbox.overlaps(other.getHitbox());
    }

    /**
     * Top level method for rendering Element on screen
     * @param batch open SpriteBatch (batch.begin must be called before this method and batch.end() after!!)
     */
    public void draw(SpriteBatch batch){
        batch.draw(texture, position.x, position.y);
    }


}

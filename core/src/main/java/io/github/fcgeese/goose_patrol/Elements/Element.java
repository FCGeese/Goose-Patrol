package io.github.fcgeese.goose_patrol.Elements;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Element {
    private Vector2 positionVector;
    private TextureRegion texture;
    private Rectangle hitbox;

    public Vector2 getPositionVector() {
        return new Vector2(positionVector);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setPositionVector(Vector2 positionVector) {
        this.positionVector = positionVector;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
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
        batch.draw(
            texture,
            positionVector.x,
            positionVector.y,
            hitbox.width,
            hitbox.height
        );
    }
}

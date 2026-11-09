package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public abstract class Element {
    private Vector2 positionVector;
    private TextureRegion texture;
    public ArrayList<HealthListener> healthListeners = new ArrayList<>();
    private float health;
    private Rectangle hitbox;
    private State state;

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public Vector2 getPositionVector() {
        return positionVector;
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

    public void addHealthListener(HealthListener h){
        healthListeners.add(h);
    }

    public void changeHealth(float delta) {
        health += delta;
        if (getHealth() >= 0) {
            this.setState(State.ZERO_HEALTH);
            for (HealthListener h : healthListeners) {
                h.onDeath();
            }
        }
        else{
            if (delta<0){this.setState(State.HIT);}
            for (HealthListener h : healthListeners) {
                h.onChange();
            }
        }
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
        batch.draw(texture, positionVector.x, positionVector.y);
    }

    protected void setState(State state) {
        this.state = state;
    }

    protected State getState() {
        return state;
    }

    protected enum State {
        ZERO_HEALTH,
        HIT,
        IDLE,
        MOVING
    }

}

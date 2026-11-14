package io.github.fcgeese.goose_patrol.Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.fcgeese.goose_patrol.Managers.HealthListener;

import java.util.ArrayList;

public class MovingElement extends Element{
    private Vector2 displacementVector = new Vector2();
    public Animation<TextureRegion> movingAnimation;
    private TextureRegion[][] texturePack;
    private float stateTime = 0;
    public ArrayList<HealthListener> healthListeners = new ArrayList<>();
    private float health = 100;
    private State state = State.IDLE;

    public void setDisplacementVector(Vector2 displacementVector) {
        this.displacementVector = displacementVector;
    }

    public void setDisplacementVector(float x, float y){
        this.displacementVector = new Vector2(x, y);
    }

    public void setTexturePack(TextureRegion[][] texturePack) {
        this.texturePack = texturePack;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    protected void setState(State state) {
        this.state = state;
    }

    protected State getState() {
        return state;
    }

    public TextureRegion[][] getTexturePack() {
        return texturePack;
    }

    public Vector2 getDisplacementVector() {
        return new Vector2(displacementVector);
    }

    public float getHealth() {
        return health;
    }

    public void addHealthListener(HealthListener h){
        healthListeners.add(h);
    }

    public void changeHealth(float delta) {
        health = Math.max(health + delta, 0);
        if (getHealth() == 0) {
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
     * Draws the animation frame for a moving object overriding its static super method
     * @param batch open SpriteBatch (batch.begin must be called before this method and batch.end() after!!)
     */
    @Override
    public void draw(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();

        boolean looping = getState() == State.MOVING || getState() == State.IDLE;

        setTexture(movingAnimation.getKeyFrame(stateTime, looping));
        super.draw(batch);
    }

    protected void setMovingAnimation(Animation<TextureRegion> animation) {
        if (movingAnimation != animation) {
            movingAnimation = animation;
            stateTime = 0f;
        }
    }

    /**
     * Grabs a new post-update hitbox
     * @return Rectangle Next hitbox
     */
    public Rectangle nxtHitbox(){
        return new Rectangle(getPositionVector().x+getDisplacementVector().x,
                             getPositionVector().y+getDisplacementVector().y,
                              getHitbox().width,
                              getHitbox().height);
    }

    /**
     * Updates the elements position using its displacement vector
     */
    public void update(){
        setPositionVector(getPositionVector().add(getDisplacementVector()));
        getHitbox().setPosition(getPositionVector());
    }

    public void move() {

    }

    protected enum State {
        ZERO_HEALTH,
        HIT,
        IDLE,
        MOVING
    }
}

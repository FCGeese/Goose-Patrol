package io.github.fcgeese.goose_patrol;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class MovingElement extends Element{
    private Vector2 displacementVector;
    public Animation<TextureRegion> movingAnimation;
    private TextureRegion[][] texturePack;
    private float stateTime = 0;
    public ArrayList<HealthListener> healthListeners = new ArrayList<>();
    private float health;
    private State state;

    public Vector2 getDisplacementVector() {
        return displacementVector;
    }

    public void setDisplacementVector(Vector2 displacementVector) {
        this.displacementVector = displacementVector;
    }

    public void setDisplacementVector(float x, float y){
        this.displacementVector = new Vector2(x, y);
    }

    public void setTexturePack(TextureRegion[][] texturePack) {
        this.texturePack = texturePack;
    }

    public TextureRegion[][] getTexturePack() {
        return texturePack;
    }

    /**
     * Updates the elements position using its displacement vector
     */
    public void update(){
        getPositionVector().add(getDisplacementVector());
        getHitbox().setPosition(getPositionVector());
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
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

    @Override
    public void draw(SpriteBatch batch){
        stateTime += Gdx.graphics.getDeltaTime();
        setTexture(movingAnimation.getKeyFrame(stateTime, getState().equals(State.MOVING)));
        super.draw(batch);

    }

    public Rectangle nxtHitbox(){
        return new Rectangle(getPositionVector().x+getDisplacementVector().x,
                             getPositionVector().y+getDisplacementVector().y,
                              getHitbox().width,
                              getHitbox().height);
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

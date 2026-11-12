package io.github.fcgeese.goose_patrol.Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.fcgeese.goose_patrol.Managers.CollisionManager;
import io.github.fcgeese.goose_patrol.Settings;

public class Player extends MovingElement {
    private final CollisionManager colliders = new CollisionManager();

    public Player(Vector2 position) {
        setPositionVector(position);
        setDisplacementVector(0, 0);
        setDisplacementVector(getDisplacementVector().nor());
        setTexturePack(Settings.playerTexturePack.split(64,128));
        movingAnimation = new Animation<>(0.3f, getTexturePack()[0]);
        setHitbox(new Rectangle(
            getPositionVector().x,
            getPositionVector().y,
            Settings.playerWidth,
            Settings.playerHeight
        ));
        setState(State.IDLE);
    }

    /**
     * Handles player movement
     *
     */
    public void move() {
        //polls key input and adjusts displacement vector
        //each collision dimension handled independently to allow smooth sliding along walls
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {

            if (colliders.canMove(this, 0, 1)) {
                setDisplacementVector(getDisplacementVector().add(0, 1));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (colliders.canMove(this, -1, 0)) {
                setDisplacementVector(getDisplacementVector().add(-1, 0));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (colliders.canMove(this, 0, -1)) {
                setDisplacementVector(getDisplacementVector().add(0, -1));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (colliders.canMove(this, 1, 0)) {
                setDisplacementVector(getDisplacementVector().add(1, 0));
            }
        }
        //normalises and scales to player speed
        setDisplacementVector(getDisplacementVector().nor().scl(Settings.playerSpeed));
        //doubles speed if sprinting, could be changed to one time dash?
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            setDisplacementVector(getDisplacementVector().scl(2));
        }
        if (getDisplacementVector().isZero()){
            setState(State.IDLE);
        }
        else{
            setState(State.MOVING);
        }
        //updates position vector
        update();
        setDisplacementVector(0,0);
    }
}

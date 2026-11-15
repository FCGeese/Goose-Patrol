package io.github.fcgeese.goose_patrol.Elements;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.fcgeese.goose_patrol.Managers.CollisionManager;
import io.github.fcgeese.goose_patrol.Settings;

public class Player extends MovingElement {

    // Keeps track of the direction the player is facing
    private enum Direction {
        DOWN, RIGHT, UP, LEFT
    }
    private Direction facingDirection = Direction.DOWN;

    private final CollisionManager colliders = new CollisionManager();

    // Animations for each direction (in which the player is facing)
    private final Animation<TextureRegion> idleDown;
    private final Animation<TextureRegion> idleRight;
    private final Animation<TextureRegion> idleUp;
    private final Animation<TextureRegion> idleLeft;

    private final Animation<TextureRegion> walkDown;
    private final Animation<TextureRegion> walkRight;
    private final Animation<TextureRegion> walkUp;
    private final Animation<TextureRegion> walkLeft;

    private final Animation<TextureRegion> runDown;
    private final Animation<TextureRegion> runRight;
    private final Animation<TextureRegion> runUp;
    private final Animation<TextureRegion> runLeft;

    private float speedMult = 1;

    public Player(Vector2 position) {

        // Define animations for player in each direction
        setTexturePack(Settings.playerTexturePack.split(14,19));

        // Player not moving
        this.idleDown = new Animation<>(0.2f, getTexturePack()[0]);
        this.idleRight = new Animation<>(0.2f, getTexturePack()[1]);
        this.idleUp = new Animation<>(0.2f, getTexturePack()[2]);
        this.idleLeft = new Animation<>(0.2f, getTexturePack()[3]);

        // Player walking
        this.walkDown = new Animation<>(0.2f, getTexturePack()[4]);
        this.walkRight = new Animation<>(0.2f, getTexturePack()[5]);
        this.walkUp = new Animation<>(0.2f, getTexturePack()[6]);
        this.walkLeft = new Animation<>(0.2f, getTexturePack()[7]);

        // Player running (same as walking, just faster frames)
        this.runDown = new Animation<>(0.1f, getTexturePack()[4]);
        this.runRight = new Animation<>(0.1f, getTexturePack()[5]);
        this.runUp = new Animation<>(0.1f, getTexturePack()[6]);
        this.runLeft = new Animation<>(0.1f, getTexturePack()[7]);

        setPositionVector(position);
        setDisplacementVector(0, 0);
        setDisplacementVector(getDisplacementVector().nor());
        setHitbox(new Rectangle(
            getPositionVector().x,
            getPositionVector().y,
            Settings.playerWidth,
            Settings.playerHeight
        ));
        setState(State.IDLE);
    }

    public void setSpeedMult(float speedMult) {
        this.speedMult = speedMult;
    }

    public void setColliderList(List<Element> collidables){
        colliders.addCollidableList(collidables);
    }

    /**
     * Handles player movement
     *
     */
    @Override
    public void move(float deltaT) {

        // Multiplies move speed by 1.75 if sprinting
        float dstToMove;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            dstToMove = Settings.playerSpeed * speedMult * 1.75f;
        }
        else {
            dstToMove = Settings.playerSpeed * speedMult;
        }

        // Convert movement speed to a distance travelled per frame
        dstToMove *= deltaT;

        // Polls key input and adjusts displacement vector
        // Each collision dimension handled independently to allow smooth sliding along walls
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {

            if (colliders.canMove(this, 0, dstToMove)) {
                setDisplacementVector(getDisplacementVector().add(0, dstToMove));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (colliders.canMove(this, -dstToMove, 0)) {
                setDisplacementVector(getDisplacementVector().add(-dstToMove, 0));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (colliders.canMove(this, 0, -dstToMove)) {
                setDisplacementVector(getDisplacementVector().add(0, -dstToMove));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (colliders.canMove(this, dstToMove, 0)) {
                setDisplacementVector(getDisplacementVector().add(dstToMove, 0));
            }
        }
        // Determine player's facing direction
        Vector2 direction = getDisplacementVector();

        if (!direction.isZero()) {
            if (Math.abs(direction.x) > Math.abs(direction.y)) {
                facingDirection = direction.x > 0 ? Direction.RIGHT : Direction.LEFT;
            } else {
                facingDirection = direction.y  > 0 ? Direction.UP : Direction.DOWN;
            }

            setState(State.MOVING);

        } else {
            setState(State.IDLE);
        }

        updateAnimation();

        // Normalises and scales to player speed
        setDisplacementVector(getDisplacementVector().nor().scl(dstToMove));
        // Doubles speed if sprinting, could be changed to one time dash?
        if (getDisplacementVector().isZero()){
            setState(State.IDLE);
        }
        else{
            setState(State.MOVING);
        }

        // Updates position vector
        update();
        setDisplacementVector(0,0);
    }

    /**
     * Updates the player's animation based on their movement and facing direction.
     */
    private void updateAnimation() {

        switch (facingDirection) {

            case DOWN:
                setMovingAnimation(getState() == State.IDLE ? idleDown :
                    (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? runDown : walkDown));
                break;

            case RIGHT:
                setMovingAnimation(getState() == State.IDLE ? idleRight :
                    (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? runRight : walkRight));
                break;

            case UP:
                setMovingAnimation(getState() == State.IDLE ? idleUp :
                    (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? runUp : walkUp));
                break;

            case LEFT:
                setMovingAnimation(getState() == State.IDLE ? idleLeft :
                    (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? runLeft : walkLeft));
                break;
        }
    }
}

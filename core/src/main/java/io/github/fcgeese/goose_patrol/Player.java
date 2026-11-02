package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends MovingElement {
    public Player(Vector2 position) {
        setPositionVector(position);
        setDisplacementVector(0, 0);
        setDisplacementVector(getDisplacementVector().nor());
        setTexture(new TextureRegion(Settings.playerTexture));
        setHitbox(new Rectangle(
            getPositionVector().x,
            getPositionVector().y,
            Settings.playerWidth,
            Settings.playerHeight
        ));
    }

    /**
     * Handles player movement
     * @param collisionManager Handles player collisions
     */
    public void move(CollisionManager collisionManager) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (collisionManager.canMove(this, 0, 1)) {
                setDisplacementVector(getDisplacementVector().add(0, 1));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (collisionManager.canMove(this, -1, 0)) {
                setDisplacementVector(getDisplacementVector().add(-1, 0));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (collisionManager.canMove(this, 0, -1)) {
                setDisplacementVector(getDisplacementVector().add(0, -1));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (collisionManager.canMove(this, 1, 0)) {
                setDisplacementVector(getDisplacementVector().add(1, 0));
            }
        }
        setDisplacementVector(getDisplacementVector().nor().scl(Settings.playerSpeed));

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            setDisplacementVector(getDisplacementVector().scl(2));
        }
        update();
        setDisplacementVector(0,0);
        }
    }


package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Goose extends MovingElement {
    private final float accuracy = Settings.gooseAccuracy;
    private Element target;
    private float attackDuration = Settings.gooseAttackDuration;
    private float attackCoolDown = Settings.gooseAttackCoolDown;
    private boolean attacking = false;
    private float lockOnDuration = Settings.gooseLockOnDuration;

    public Goose(Vector2 position) {
        setPositionVector(position);
        setDisplacementVector(1,1);
        getDisplacementVector().nor();
        setTexturePack(Settings.gooseTexturePack.split(64,64));
        movingAnimation = new Animation<TextureRegion>(0.3f, getTexturePack()[0]);
        setHitbox(new Rectangle(
            getPositionVector().x,
            getPositionVector().y,
            Settings.gooseWidth,
            Settings.gooseHeight
        ));
    }

    /**
     * goose wanders randomly by changing its displacements vectors angle in place
     *
     */
    public void wander(){
        if (Math.random()>0.85){
            getDisplacementVector().rotateRad(accuracy);
            getDisplacementVector().nor().scl(Settings.gooseSpeed);
        }
        else if (Math.random()<0.15){
            getDisplacementVector().rotateRad( - accuracy);
            getDisplacementVector().nor().scl(Settings.gooseSpeed);
        }
    }

    /**
     * turns the goose towards their target, affects displacmentVector in place
     */
    public void pursue() {
        //finds the angle from the goose to the target in rads as a float
        float targetAngle = target.getPositionVector().cpy().sub(getPositionVector()).angleRad();
        //finds the absolute difference between the current direction and the target direction
        double angleDifference = Math.abs((getDisplacementVector().angleRad()%Math.PI)- targetAngle);

        //sorts the differences into regions to avoid wraparound errors
        if (getDisplacementVector().angleRad() < targetAngle) {
            if (angleDifference < Math.PI) {
                getDisplacementVector().rotateRad(accuracy);
            } else {
                getDisplacementVector().rotateRad(-accuracy);
            }
        } else {
            if (angleDifference > Math.PI) {
                getDisplacementVector().rotateRad(accuracy);
            } else {
                getDisplacementVector().rotateRad(-accuracy);
            }
        }
        //normalises the vector and scales it to speed
        getDisplacementVector().nor().scl(Settings.gooseSpeed);
    }


    /**
     * redirects and moves the goose forward
     * @param targetManager
     * @param collisionManager
     */
    public void move(TargetManager targetManager, CollisionManager collisionManager){
        //sets target, as nearest in view targetable element
        target = targetManager.getTarget(this);
        //current displacment
        Vector2 currentDisplacement = getDisplacementVector().cpy();
        if (target != null){
            //attempts to chase, otherwise wanders
            //sets displacement
            pursue();
            if (!collisionManager.canMove(this, getDisplacementVector())) {
                getDisplacementVector().set(currentDisplacement);
                wander();
            }
        }
        else {
            wander();
        }

        update();
    }
}

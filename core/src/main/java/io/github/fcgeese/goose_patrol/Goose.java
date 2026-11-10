package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class Goose extends MovingElement {
    private final float accuracy = Settings.gooseAccuracy;
    private Element target;
    private final CollisionManager collider = new CollisionManager();
    private final TargetManager targeter = new TargetManager();
    private float stunTime = 2;

    /*private float attackDuration = Settings.gooseAttackDuration;
    private float attackCoolDown = Settings.gooseAttackCoolDown;
    private boolean attacking = false;
    private float lockOnDuration = Settings.gooseLockOnDuration;*/

    public Goose(Vector2 position) {
        setPositionVector(position);
        setDisplacementVector(1,1);
        getDisplacementVector().nor();
        setTexturePack(Settings.gooseTexturePack.split(64,64));
        movingAnimation = new Animation<>(0.3f, getTexturePack()[0]);
        setHitbox(new Rectangle(
            getPositionVector().x,
            getPositionVector().y,
            Settings.gooseWidth,
            Settings.gooseHeight
        ));
        setState(State.MOVING);
    }

    public void setColliderList(List<Element> colliderList) {
        this.collider.addCollidableList(colliderList);
    }

    public void setTargetList(List<Element> targetList){
        targetList.removeIf(t -> t==this);
        this.targeter.setTargetList(targetList);
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

     */
    public void move(){
        //sets target, as nearest in view targetable element
            target = targeter.getTarget(this);

            if (collidesWith(target)) {
                target.changeHealth(-4);  //MAGIC DAMAGE NUMBER FOR TEST USE ONLY
            }

            if (target != null) {
                //attempts to chase, otherwise wanders
                //sets displacement
                pursue();
                System.out.println(collider.canMove(this));

                //Runs away if satitated w/bread or "fleeing"
                if (getState() == State.ZERO_HEALTH) {
                    setDisplacementVector(getDisplacementVector().scl(-1));
                    if (getPositionVector().dst(target.getPositionVector())>200){   //MAGIC NUMBER FOR FLEE DIST TEST ONLY
                        setHealth(100);
                        setState(State.MOVING);
                    }
                }
                if (getState() == State.HIT){
                    if (stunTime <= 0){
                        getDisplacementVector().scl(Settings.gooseSpeed);
                        setState(State.MOVING);
                        stunTime = 3;
                    }
                    stunTime -= Gdx.graphics.getDeltaTime();
                    getDisplacementVector().scl(0.1f);
                }

                if (!collider.canMove(this)) {
                    setDisplacementVector(getDisplacementVector().rotateRad(accuracy));
                    getDisplacementVector().scl(-0.5f);
                }

            } else {
                wander();
            }
            update();
    }
}

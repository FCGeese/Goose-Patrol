package io.github.fcgeese.goose_patrol.Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.fcgeese.goose_patrol.Managers.CollisionManager;
import io.github.fcgeese.goose_patrol.Settings;
import io.github.fcgeese.goose_patrol.Managers.TargetManager;

import java.util.List;

public class Goose extends MovingElement {
    private final float accuracy = Settings.gooseAccuracy;
    private Element target = null;
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
        setTexturePack(Settings.gooseTexturePack.split(32,32));
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
     * Returns a random rotation direction (weighted towards not turning).
     * @return float random direction of goose rotation in radians.
     */
    public float wander(){
        double rand = Math.random();
        if (rand>0.85){
            return accuracy;
        }
        else if (rand<0.15){
            return -accuracy;
        }
        else{
            return 0;
        }
    }

    /**
     * Returns a rotation of magnitude "accuracy" towards the player, or the direct turn if
     * closer.
     * @return float direction of goose rotation in radians, scaled to goose accuracy.
     */
    public float pursue() {
        //finds the angle from the goose to the target in rads as a float
        float targetAngle = target.getPositionVector().cpy().sub(getPositionVector()).angleRad();
        //finds the absolute difference between the current direction and the target direction
        double angleDifference = Math.abs((getDisplacementVector().angleRad()%Math.PI)- targetAngle);
        //if goose is with its accuracy to the target returns the exact "lock on" rotation
        if (angleDifference<accuracy){
            return (float) angleDifference;
        }
        //sorts the differences into regions to avoid wraparound errors
        if (getDisplacementVector().angleRad() < targetAngle) {
            if (angleDifference < Math.PI) {
                return accuracy;
            } else {
                return -accuracy;
            }
        } else {
            if (angleDifference > Math.PI) {
                return accuracy;
            } else {
                return -accuracy;
            }
        }
    }

    /**
     * Grabs the next goose displacement vector depending on current goose state
     * @return Vector2 Next Displacement Vector
     */
    public Vector2 getNewDispVect(){

        if (getDisplacementVector().isZero() &&
            getState() != State.HIT &&
            getState() != State.IDLE){

            return new Vector2(Vector2.One.rotateRad((float) (Math.random()*Math.PI*2)));

        }

        switch(getState()){
            case HIT -> {
                return Vector2.Zero;
            }
            case MOVING -> {
                if (target == null){
                    return new Vector2(getDisplacementVector().rotateRad(wander()));
                }
                else{
                    return new Vector2(getDisplacementVector().rotateRad(pursue()));
                }
            }
            case ZERO_HEALTH -> {
                return new Vector2(getDisplacementVector().rotateRad(-pursue()));
            }
        }
        return Vector2.Zero;
    }


    private void attack(){
        if (this.getHitbox().overlaps(target.getHitbox()) &&
            target instanceof MovingElement){
            ((MovingElement) target).changeHealth(-Settings.gooseDamage);
        }
    }

    /**
     * gets state, target, and moves in appropriate direction
     */
    @Override
    public void move(){
        float delta = Gdx.graphics.getDeltaTime();

        react(); //polls heath and stun time, sets to appropriate state
        target = targeter.getTarget(this,collider);
        if (target !=null)
            if(targeter.isInRange(this, target)){
                attack();
            }
        Vector2 nextDisplacement = getNewDispVect();
        setDisplacementVector(Vector2.Zero);

        if (collider.canMove(this, nextDisplacement.x, nextDisplacement.y)){
            setDisplacementVector(getDisplacementVector().add(nextDisplacement ));
        }
        else{
            setDisplacementVector(getDisplacementVector().add(nextDisplacement.scl(-1)));
        }
        setDisplacementVector(getDisplacementVector().nor().scl(Settings.gooseSpeed * delta));
        update();
    }

    /**
     * polls heath and stun time, sets to appropriate state
     */
    public void react(){
        if (getState() == State.HIT){
            stunTime = Math.max(0, stunTime-Gdx.graphics.getDeltaTime());
            if (stunTime == 0){
                setState(State.MOVING);
            }
        }
        else if(getHealth() <= 0){
            setState(State.ZERO_HEALTH);
        }
        else {
            setState(State.MOVING);
        }

    }
}

/*
public void move(){
    //sets target, as nearest in view targetable element
    target = targeter.getTarget(this);

    if (collidesWith(target)) {
        if (target instanceof MovingElement){
            ((MovingElement) target).changeHealth(-4); //MAGIC DAMAGE NUMBER FOR TEST USE ONLY
        }
    }

    if (target != null) {
        //attempts to chase, otherwise wanders
        //sets displacement
        pursue();
        //Runs away if satitated w/bread or "fleeing"
        if (getState() == MovingElement.State.ZERO_HEALTH) {
            setDisplacementVector(getDisplacementVector().scl(-1));
            if (getPositionVector().dst(target.getPositionVector())>200){   //MAGIC NUMBER FOR FLEE DIST TEST ONLY
                setHealth(100);
                setState(MovingElement.State.MOVING);
            }
        }
        if (getState() == MovingElement.State.HIT){
            if (stunTime <= 0){
                getDisplacementVector().scl(Settings.gooseSpeed);
                setState(MovingElement.State.MOVING);
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
}*/

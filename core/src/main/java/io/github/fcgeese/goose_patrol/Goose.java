package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.Gdx;
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
        setTexture(new TextureRegion(Settings.gooseTexture));
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

        float targetAngle = target.getPositionVector().cpy().sub(getPositionVector()).angleRad();
        double angleDifference = Math.abs((getDisplacementVector().angleRad()%Math.PI)- targetAngle);
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
        getDisplacementVector().nor().scl(Settings.gooseSpeed);
    }


    /**
     * redirects and moves the goose forward
     * @param targetManager
     * @param collisionManager
     */
    public void move(TargetManager targetManager, CollisionManager collisionManager){
        target = targetManager.getTarget(this);
        if (target != null){
            if (collisionManager.canMove(this, getDisplacementVector())) {
                pursue();
            }
            else{
                wander();
            }
        }
        else {
            wander();
        }
        update();
    }
}






   /*
       public void attack(){
        if (attackDuration>0){
            attackDuration -= Gdx.graphics.getDeltaTime();
            //draw line tot next vector(big displacement
            /*ShapeRenderer shape = new ShapeRenderer();
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.line(getPositionVector(),getDisplacementVector());
            shape.end();
            shape.dispose();

        }
            else{
                getDisplacementVector().nor();
                attacking=false;
                attackCoolDown = Settings.gooseAttackCoolDown;
        }
    }

    public void move(TargetManager targetManager, CollisionManager collisionManager){
        target = (targetManager.getTarget(this));
        if ( target instanceof Player &&
            targetManager.isInRange(this, target) &&
            attackCoolDown < 0 &&
            !attacking
        ){
            pursue();
            lockOnDuration -= Gdx.graphics.getDeltaTime();
            if (lockOnDuration < 0) {
                lockOnDuration = Settings.gooseLockOnDuration;
                attacking = true;
                setDisplacementVector(getDisplacementVector().scl(15));
            }
        }
        else if (!targetManager.isInRange(this, target)){
            lockOnDuration = Settings.gooseLockOnDuration;
            setDisplacementVector(getDisplacementVector().nor());
            if (target!=null){
                pursue();
            }
            else{
                wander();
            }
        }

        if (collisionManager.canMove(this)){
            if(attacking){
                attack();
            }
            else{
                update();
            }

        }
    }*/


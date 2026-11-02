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
        setDisplacementVector(getDisplacementVector().nor());
        setTexture(new TextureRegion(Settings.gooseTexture));
        setHitbox(new Rectangle(
            getPositionVector().x,
            getPositionVector().y,
            Settings.gooseWidth,
            Settings.gooseHeight
        ));
        this.setSpeed(Settings.gooseSpeed);
    }

    /**
     * goose wanders randomly
     * @return Vector2, from MovingElements update, next Vector2 position of goose
     */
    public void wander(){
        if (Math.random()>0.85){
            setDirection(getDirection()+accuracy);
        }
        else if (Math.random()<0.15){
            setDirection(getDirection() - accuracy);
        }
    }

    /**
     * turns the goose towards their target while moving the goose forwards
     * @return Vector2 the reutrn value of MovingElements update() -> the next Vector2 position of the goose
     */
    public void pursue() {
        float targetDirection = getPositionVector().angleRad(target.getPositionVector());
        if (getDirection() < targetDirection) {
            if (targetDirection < Math.PI) {
                setDirection(getDirection() + accuracy);
            } else {
                setDirection(getDirection() - accuracy);
            }
        } else {
            if (targetDirection > Math.PI) {
                setDirection(getDirection() + accuracy);
            } else {
                setDirection(getDirection() - accuracy);
            }
        }
        setDirection( (float) ((float) ((getDirection() + Math.PI)) % Math.PI));
    }

    /**
     * draws the attack effect for the duration of the goose attack
     */
    public void attack(){
        getPositionVector().angleDeg(target.getPositionVector());
        if (attackDuration>0){
            attackDuration = attackDuration - Gdx.graphics.getDeltaTime();
            //draw line tot next vector(big displacement
            ShapeRenderer shape = new ShapeRenderer();
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.line(getPositionVector(),getDisplacementVector());
            shape.end();
            shape.dispose();
        }
        else{
            setDisplacementVector(getDisplacementVector().nor());
            attacking=false;
            attackCoolDown = Settings.gooseAttackCoolDown;
        }
    }

    public void move(targetManager targetManager, collisionManager collisionManager){
        target = (targetManager.getTarget(this));

        if ( target instanceof Player &&
            targetManager.isInRange(this, target) &&
            attackCoolDown < 0 &&
            !attacking
        ){
            pursue();
            lockOnDuration = -Gdx.graphics.getDeltaTime();
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
    }
}

package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Goose extends MovingElement {
    float accuracy = Settings.gooseAccuracy;
    Element target;

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

    public void setTarget(Element target) {
        this.target = target;

    }

    public Element getTarget() {
        return target;
    }

    /**
     * goose wanders randomly
     * @return Vector2, from MovingElements update, next Vector2 position of goose
     */
    public Vector2 wander(){
        if (Math.random()>0.95){
            setDirection(getDirection()+accuracy);
        }
        else if (Math.random()<0.05){
            setDirection(getDirection() - accuracy);
        }
        return update();
    }

    /**
     * turns the goose towards their target while moving the goose forwards
     * @return Vector2 the reutrn value of MovingElements update() -> the next Vector2 position of the goose
     */
    public Vector2 pursue() {
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
        setDirection ( (float) ((float) ((getDirection() + Math.PI)) % Math.PI));
        return update();
    }

    /**
     * Handles Goose movement logic for each tick, conditional on collisions and pursuit
     * @param collidables a list of every element the goose should check collisions with
     */
    public void movementLogic(ArrayList<Element> collidables){

        collidables.removeIf(element -> element.getPositionVector().dst(getPositionVector())>40);

        Vector2 newVector;
        if (target != null){
            if (collidesWith(target)){
                target.setHealth(target.getHealth()-1);
            }
            newVector = pursue();
        }
        else{
            newVector = wander();
        }
        boolean canMove = true;
        for (Element element : collidables){
            if (element != this){
                if (collidesWith(element)){
                    canMove = false;
                }
            }
        }
        if (canMove){setPositionVector(newVector);}
    }
}


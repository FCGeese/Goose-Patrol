package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.math.Vector2;

public class MovingElement extends Element{
    private Vector2 displacementVector;

    public float getDirection(){
        return getPositionVector().angleRad(displacementVector);
    }
    public float getSpeed(){
        return getPositionVector().dst(displacementVector);
    }

    public void setDirection(float rotation){
        displacementVector.rotateAroundRad(getPositionVector(), rotation);
    }

    public void setSpeed(float speed){
        displacementVector.setLength(speed);
    }

    /**
     * Updates the elements position using its direction and speed
     * @return Vector2, the elements position after the update pass
     */
    public Vector2 update(){
       return new Vector2(
           getVector().x + (float) Math.cos(direction*speed), //x
              getVector().y + (float) Math.sin(direction*speed)  //y
       );
    }


}

package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.math.Vector2;

public class MovingElement extends Element{
    private Vector2 displacementVector;

    public float getDirection(){
        return displacementVector.angleRad();
    }
    public float getSpeed(){
        return displacementVector.len();
    }

    public void setDirection(float rotation){
        displacementVector.setAngleRad(rotation);
    }

    public void setSpeed(float speed){
        displacementVector.scl(speed);
    }

    /**
     * Updates the elements position using its direction and speed
     * @return Vector2, the elements position after the update pass
     */
    public Vector2 update(){
       return new Vector2(
            getPositionVector().x + (float) Math.cos(getDirection()*getSpeed()), //x
            getPositionVector().y + (float) Math.sin(getDirection()*getSpeed())  //y
       );
    }


}

package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.math.Vector2;

public class MovingElement extends Element{
    float speed;
    //Direction in Rads
    double direction;

    public float getSpeed() {
        return speed;
    }

    public double getDirection() {
        return direction;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    /**
     * Updates the elements position using its direction and speed
     * @return Vector2, the elements position after the update pass
     */

    public Vector2 update(){
       return new Vector2(
           (float) Math.cos(direction*speed), //x
           (float) Math.sin(direction*speed)  //y
       );
    }

}

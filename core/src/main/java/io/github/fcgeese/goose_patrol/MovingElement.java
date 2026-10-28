package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.math.Vector2;

public class MovingElement extends Element{
    float speed = 0;
    float direction = 0;


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

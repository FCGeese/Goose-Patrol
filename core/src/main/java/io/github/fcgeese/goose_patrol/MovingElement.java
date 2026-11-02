package io.github.fcgeese.goose_patrol;
import com.badlogic.gdx.math.Vector2;

public class MovingElement extends Element{
    private Vector2 displacementVector;

    public Vector2 getDisplacementVector() {
        return displacementVector;
    }
    public void setDisplacementVector(Vector2 displacementVector) {
        this.displacementVector = displacementVector;
    }
    public void setDisplacementVector(float x, float y){
        this.displacementVector = new Vector2(x, y);
    }

    /**
     * Updates the elements position using its direction and speed
     */
    public void update(){
        getPositionVector().add(getDisplacementVector());
        getHitbox().setPosition(getPositionVector());
    }



}

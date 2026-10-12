package io.github.EngOnePrototype.Mobs;

import io.github.EngOnePrototype.Utils.Coord;

public class Goose extends Entity {
    int accuracy = 4;
    boolean flipped;

    public Goose(int x, int y, int speed, int accuracy) {
        super(x, y, speed);
        this.accuracy = accuracy;
        flipped = false;
    }


    public Coord update(Entity e) {
        //aims goose 'accuracy' deg toward player every frame
        direction = turnToward(e.getPosition());

        //adds some random wobble/goosier movement
        if (Math.random() > 0.7) {
            direction += 8;
        }
        if (Math.random() < 0.3) {
            direction -= 8;
        }
        return super.update();
    }

    public float turnToward(Coord target) {
        //finds angle to target
        float targetDirection = position.findDirectionTo(target);
        //finds absolute difference in current direction and target direction 
        float angleDifference = Math.abs((direction % 360) - targetDirection);
        //wraps toward target
        if (direction < targetDirection) {
            if (angleDifference < 180) {
                direction += accuracy;
            } else {
                direction -= accuracy;
            }
        } else {
            if (angleDifference > 180) {
                direction += accuracy;
            } else {
                direction -= accuracy;
            }
        }
        //wraps back to 0-360 plain
        return (direction + 360) % 360;
    }

    public float turnAway(Entity e) {
        //invertion of turn toward method above
        return -(direction - turnToward(e.getPosition()));
    }


    public void setFlipped(boolean b) {
        flipped = b;
        return;
    }
    public boolean getFlipped(){
        return flipped;
    }

}

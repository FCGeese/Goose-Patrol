package io.github.EngOnePrototype;

public class Goose extends Entity {

    public Goose(int x, int y, int speed, int direction) {
        super(x, y, speed, direction);
    }

    public void update(Entity e) {
        direction = turnToward(e);
        if (Math.random() > 0.8) {
            direction += 8;
        }
        if (Math.random() < 0.2) {
            direction -= 8;
        }
        super.update();
    }

    public float turnToward(Entity e) {
        System.out.println(direction);
        float targetDirection = findDirection(e.x, e.y);
        float angleDifference = Math.abs((direction % 360) - targetDirection);
        if (direction < targetDirection) {
            if (angleDifference < 180) {
                direction += 4;
            } else {
                direction -= 4;
            }
        } else {
            if (angleDifference > 180) {
                direction += 4;
            } else {
                direction -= 4;
            }
        }
        return (direction + 360) % 360;
    }

    public float turnAway(Entity e) {
        return -(direction - turnToward(e));
    }

}

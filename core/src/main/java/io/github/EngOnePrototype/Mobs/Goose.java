package io.github.EngOnePrototype.Mobs;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.EngOnePrototype.Utils.Coord;

public class Goose extends Entity {
    int accuracy = 4;

    public Goose(int x, int y, int speed, int accuracy) {
        super(x, y, speed);
        this.accuracy = accuracy;
    }

    public Coord update(Entity e) {
        // aims goose 'accuracy' deg toward player every frame
        direction = turnToward(e.getPosition());

        // adds some random wobble/goosier movement
        if (Math.random() > 0.7) {
            direction += 9;
        }
        if (Math.random() < 0.3) {
            direction -= 9;
        }
        return super.update();
    }

    public float turnToward(Coord target) {
        // finds angle to target
        float targetDirection = position.findDirectionTo(target);
        // finds absolute difference in current direction and target direction
        float angleDifference = Math.abs((direction % 360) - targetDirection);
        // wraps toward target
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
        // wraps back to 0-360 plain
        return (direction + 360) % 360;
    }

    public float turnAway(Entity e) {
        // invertion of turn toward method above
        return -(direction - turnToward(e.getPosition()));
    }

    public void render(SpriteBatch batch, TextureRegion texture, ArrayList<Goose> geese, Player player) {
        if (this.getPosition().checkCollision(player.getPosition(), player.getSize() + getSize())) {
            System.exit(0);
        }
        // pursuit
        Coord nextGooseFrame = update(player);
        boolean collision = false;
        // Flock collision
        for (Goose goose2 : geese) {
            if (this != goose2) {
                if (nextGooseFrame.checkCollision(
                        goose2.getPosition(), player.getSize() + getSize())) {

                    // undo move if collision takes place
                    collision = true;
                }
            }
        }
        if (!collision) {
            move(nextGooseFrame);
        }
        draw(batch, texture);
    }

    public void draw(SpriteBatch batch, TextureRegion texture) {
        batch.begin();
        if (direction < 90 || direction > 270) {
            if (!texture.isFlipX()) {
                texture.flip(true, false);
            }
            batch.draw(texture, getX(), getY());

        }
        if (direction > 90 && direction < 270) {
            if (texture.isFlipX()) {
                texture.flip(true, false);
            }
            batch.draw(texture, getX(), getY());
        }
        batch.end();
    }

}

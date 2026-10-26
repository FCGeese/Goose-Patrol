package io.github.EngOnePrototype.Elements;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.EngOnePrototype.Utils.Coord;
import io.github.EngOnePrototype.Utils.Hitbox;
import io.github.EngOnePrototype.Utils.Rect;

public class Goose extends Entity {
    int accuracy = 4;

    public Goose(Coord position, int defaultSpeed, int accuracy) {
        super(new TextureRegion(new Texture(Gdx.files.internal("16x16SideGoose3.png"))),
                position, defaultSpeed);

        this.accuracy = accuracy;
        hitbox = new Hitbox(new Rect(position, 16, 16));
    }

    public Goose(Coord position, int defaultSpeed) {
        super(new TextureRegion(new Texture(Gdx.files.internal("16x16SideGoose3.png"))),
                position, defaultSpeed);

        hitbox = new Hitbox(new Rect(position, 16, 16));
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

    public void render(SpriteBatch batch, ArrayList<Goose> geese, ArrayList<Hitbox> buildings, Player player) {
        if (this.isCollide(player)) {
            player.setHealth(player.getHealth() - 1);
        }
        // saves old position
        Coord oldPos = new Coord(position.getX(), position.getY());
        // projects to next position
        move(update(player));

        boolean collision = false;

        // checks collision with all other geese
        for (Goose other : geese) {
            if (this != other) {
                if (isCollide(other)) {
                    // undo move if collision takes place
                    collision = true;
                }
            }
        }
        for (Hitbox building : buildings) {
            if (hitbox.isCollision(building)) {
                collision = true;
            }
        }

        // backtracks before drawing if collision inbound.
        if (collision) {
            move(oldPos);
        }
        draw(batch);
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

    @Override
    public void draw(SpriteBatch batch) {
        batch.begin();
        if (direction < 90 || direction > 270) {
            if (!texture.isFlipX()) {
                texture.flip(true, false);
            }
            batch.draw(texture, position.getX(), position.getY());

        }
        if (direction > 90 && direction < 270) {
            if (texture.isFlipX()) {
                texture.flip(true, false);
            }
            batch.draw(texture, position.getX(), position.getY());
        }
        batch.end();
    }

}

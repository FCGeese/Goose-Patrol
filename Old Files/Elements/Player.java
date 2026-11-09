package io.github.GoosePatrolGame.Elements;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.GoosePatrolGame.Settings;
import io.github.GoosePatrolGame.Utils.Coord;
import io.github.GoosePatrolGame.Utils.Hitbox;
import io.github.GoosePatrolGame.Utils.Rect;

public class Player extends Entity {
    public Player(Coord position, int defaultSpeed) {
        super(new TextureRegion(new Texture(Gdx.files.internal("objects/player/32x32player.png"))),
                position, defaultSpeed);
        setHealth(100);
        hitbox = new Hitbox(new Rect(position, 32, 32));
    }


    @Override
    public Coord update() {
        int newX = position.getX();
        int newY = position.getY();
        // key watchers
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            speed = 2 * speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (Gdx.input.isKeyPressed(Input.Keys.A) ^ (Gdx.input.isKeyPressed(Input.Keys.D))) {
                // stops the '45 degree rush'
                speed = (int) Math.ceil((Math.pow(Math.sqrt(speed), 2) / 2));
            }
            newY = (position.getY() + speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (Gdx.input.isKeyPressed(Input.Keys.A) ^ (Gdx.input.isKeyPressed(Input.Keys.D))) {
                speed = (int) Math.ceil((Math.pow(Math.sqrt(speed), 2) / 2));
            }
            newY =  (position.getY() - speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newX =  (position.getX() - speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newX =  (position.getX() + speed);
        }
        speed = Settings.playerSpeed;
        return new Coord(newX, newY);
    }

    public void render(SpriteBatch batch, ArrayList<Hitbox> buildings) {

            if (getHealth() <= 0 ) {
            System.exit(0);
        }
        batch.begin();
        Coord oldPosition = new Coord(getX(), getY());
        setPosition(update());
        boolean collision = false;

        for (Hitbox building : buildings) {
            if (hitbox.isCollision(building)) {
                collision = true;
            }
        }

        //TO_DO fix magic numbers
        if (getPosition().getX()<0 ||
            getPosition().getX()+32>1920||
            getPosition().getY()<0||
            getPosition().getY()+32>1080){
                collision = true;
        }
        if (collision) {
            setPosition(oldPosition);
        }

        batch.draw(texture, getX(), getY());
        batch.end();
    }
}

/*
 * old tank controls, maybe useful for a car or kart implementation?
 * 
 * public void tankDraw(ShapeRenderer shape) {
 * shape.circle(position.getX(), position.getY(), size);
 * Coord reticle = new Coord(
 * position.getX() + (int) (Math.cos(Math.toRadians(direction)) * 50),
 * position.getY() + (int) (Math.sin(Math.toRadians(direction)) * 50));
 * shape.line(position.getX(), position.getY(),
 * reticle.getX(), reticle.getY());
 * }
 * 
 * public void tankUpdate(int turnSpeed) {
 * if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
 * speed = 2 * speed;
 * }
 * if (Gdx.input.isKeyPressed(Input.Keys.W)) {
 * super.move(super.update());
 * }
 * if (Gdx.input.isKeyPressed(Input.Keys.S)) {
 * speed = -speed;
 * super.move(super.update());
 * }
 * if (Gdx.input.isKeyPressed(Input.Keys.A)) {
 * direction = ((direction + turnSpeed) % 360);
 * }
 * if (Gdx.input.isKeyPressed(Input.Keys.D)) {
 * direction = ((direction - turnSpeed) % 360);
 * }
 * speed = defaultSpeed;
 * }
 * 
 */

/*
 * old draw w/ reticle method
 * public void draw(ShapeRenderer shape) {
 * Coord mouseCoord = new Coord(Gdx.input.getX(), Gdx.graphics.getHeight() -
 * Gdx.input.getY());
 * direction = position.findDirectionTo(mouseCoord);
 * shape.circle(position.getX(), position.getY(), size);
 * Coord reticle = new Coord(
 * position.getX() + (int) (Math.cos(Math.toRadians(direction)) * 50),
 * position.getY() + (int) (Math.sin(Math.toRadians(direction)) * 50));
 * 
 * shape.line(position.getX(), position.getY(),
 * reticle.getX(), reticle.getY());
 * }
 */
/*
 * OLD MOUSE CHASING MOVEMENT useful for joystick control?
 * public void update() {
 * direction = findDirection(Gdx.input.getX(), Gdx.graphics.getHeight() -
 * Gdx.input.getY());
 * // key
 * //strafe
 * char strafeDirection=' ';
 * if (Gdx.input.isKeyPressed(29) ^ Gdx.input.isKeyPressed(32)){
 * if(Gdx.input.isKeyPressed(29)){
 * strafeDirection = 'l';
 * }
 * else{
 * strafeDirection = 'r';
 * }
 * speed = Settings.playerSpeed;
 * super.update(strafeDirection);
 * speed = 0;
 * }
 * 
 * //forward/reverse
 * if (Gdx.input.isKeyPressed(51)) {
 * if (Gdx.input.isKeyPressed(59)) {
 * speed = Settings.playerSprintSpeed;
 * }
 * else{
 * speed = Settings.playerSpeed;
 * }
 * } else {
 * if (Gdx.input.isKeyPressed(47)) {
 * speed = -Settings.playerSpeed;
 * } else {
 * speed = 0;
 * }
 * 
 * }
 * 
 * super.update();
 * }
 */
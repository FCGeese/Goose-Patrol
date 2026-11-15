package io.github.fcgeese.goose_patrol.Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.fcgeese.goose_patrol.Main;


public abstract class Item extends Element {


    // The 3 states an item can be in held in an enumerable
    public enum State{
        onGround,
        inInventory,
        used
    }
    private State state = State.onGround;

    // Constructor to set the item's texture, position and hitbox
    protected Item(TextureRegion texture){
        setTexture(texture);
        setPositionVector(new Vector2(0,0));
        setHitbox(new Rectangle(
            0,0, 
            texture.getRegionWidth() * Main.UNIT_SCALE, 
            texture.getRegionHeight() * Main.UNIT_SCALE
        ));
    }

    // Method to put item at specific point in the world
    // Marks item as on ground
    public void spawnAt(float x, float y){
        setPositionVector(new Vector2(x, y));
        getHitbox().setPosition(x,y);
        state = State.onGround;
    }


    //Upon collision puts item in inventory
    public boolean tryPickup(Element player){
        if ((state == State.onGround) && collidesWith(player)){
            state = State.inInventory;
            return true;
        }
        return false;
    }

    //Overriding methods

    // Unique trigger for each item
    // Unique effect for each item once used
    protected abstract int getTriggerButton();
    protected abstract void onUse(Vector2 userCentre, Vector2 aimTarget);


    // Checks eligibility for item usage
    // Makes sure item is in inventory to trigger
    // Correct button pressed for trigger
    public boolean tryUse (Element user, Vector2 aimTarget){
        if (state == State.inInventory && Gdx.input.isButtonJustPressed(getTriggerButton())){
            Vector2 userCentre = user.getHitbox().getCenter(new Vector2());
            onUse(userCentre, aimTarget);
            return true;
        }
        return false;
    }

    // Consumes item - Changes state
    protected void consume(){
        state = State.used;
    }


    public float getSpeedMultiplier(){
        return 1f;
    }

    // Only draws if item is "on ground"
    @Override
    public void draw(SpriteBatch batch){
        if (state == State.onGround){
            super.draw(batch);
        }
    }

    public State getState() {
        return state;
    }

    public boolean isOnGround(){
        return state == State.onGround;
    }

    public boolean isInInventory(){
        return state == State.inInventory;
    }

    public boolean isUsed(){
        return state == State.used;
    }

    public boolean itemEffectEnded(){
        return state == State.used;
    }

    public void update(float delta){}

}

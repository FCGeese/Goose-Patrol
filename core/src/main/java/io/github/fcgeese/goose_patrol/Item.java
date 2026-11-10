package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.List;


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
        setHitbox(new Rectangle(0,0, texture.getRegionWidth(), texture.getRegionHeight()));
    }

    // Method to put item at specific point in the world
    // Marks item as on ground
    public void SpawnAt(float x, float y){
        getPositionVector().set(x,y);
        getHitbox().setPosition(x,y);
        state = State.onGround;
    }


    //Upon collision puts item in inventory
    public boolean TryPickup(Element player){
        if ((state == State.onGround) && collidesWith(player)){
            state = State.inInventory;
            return true;
        }
        return false;
    }



    //Overriding methods

    // Unique trigger for each item
    // Unique effect for each item once used
    protected abstract int GetTriggerButton();
    protected abstract void OnUse(Vector2 userCentre, Vector2 aimTarget);


    // Checks eligibility for item usage
    // Makes sure item is in inventory to trigger
    // Correct button pressed for trigger
    public boolean tryUse (Element user, Vector2 aimTarget){
        if (state == State.inInventory && Gdx.input.isButtonJustPressed(GetTriggerButton())){
            Vector2 userCentre = user.getHitbox().getCenter(new Vector2());
            OnUse(userCentre, aimTarget);
            return true;
        }
        return false;
    }

    // Consumes item - Changes state
    protected void consume(){
        state = State.used;
    }


    public float GetSpeedMultiplier(){
        return 1f;
    }

    // Only draws if item is "on ground"
    @Override
    public void draw(SpriteBatch batch){
        if (state == State.onGround){
            super.draw(batch);
        }
    }

    public State GetState() {
        return state;
    }

    public boolean IsOnGround(){
        return state == State.onGround;
    }

    public boolean IsInInventory(){
        return state == State.inInventory;
    }

    public boolean IsUsed(){
        return state == State.used;
    }

    public boolean ItemEffectEnded(){
        return state == State.used;
    }

    public void update(float delta){}

}

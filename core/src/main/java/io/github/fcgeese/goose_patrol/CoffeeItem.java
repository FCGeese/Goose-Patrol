package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


public class CoffeeItem extends Item {
    public static float speedMultiplier = 1.15f;
    public static float boostDuration = 5f;
    private float boostTimeLeft = 0f;


    //Constructor that takes coffee sprite/texture
    public CoffeeItem(TextureRegion texture) {
        super(texture);
    }


    //Registers Right Click to trigger coffee
    @Override
    protected int GetTriggerButton() {
        return Input.Buttons.RIGHT;
    }

    //Consumes item
    //Starts speed boost timer
    @Override
    protected void OnUse(Vector2 userCentre, Vector2 aimTarget) {
        boostTimeLeft = boostDuration;
        consume();
    }

    //Return boosted speed if effect still active
    @Override
    public float GetSpeedMultiplier() {
        if (boostTimeLeft > 0f) {
            return speedMultiplier;
        } else {
            return 1f;
        }
    }
    //Constantly updates boost timer
    @Override
    public void update(float delta) {
        if (boostTimeLeft > 0f) {
            boostTimeLeft = Math.max(0f, boostTimeLeft - delta);
        }
    }

    @Override
    public boolean ItemEffectEnded() {
        return super.ItemEffectEnded() && boostTimeLeft <= 0f;
    }

    public boolean IsBoostActive(){
        return boostTimeLeft > 0f;
    }
}

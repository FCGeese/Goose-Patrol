package io.github.fcgeese.goose_patrol.Elements;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.github.fcgeese.goose_patrol.Settings;

public class CoffeeItem extends Item {
    public static float speedMultiplier = Settings.coffeeSpeedMultiplier;
    public static float boostDuration = Settings.coffeeBoostDuration;
    private float boostTimeLeft = 0f;

    //Constructor that takes coffee sprite/texture
    public CoffeeItem(TextureRegion texture) {
        super(texture);
    }

    //Registers Right Click to trigger coffee
    @Override
    protected int getTriggerButton() {
        return Input.Buttons.RIGHT;
    }

    /**
     * Consumes item
     * Starts speed boost timer
     * @param userCentre
     * @param aimTarget
     */
    @Override
    protected void onUse(Vector2 userCentre, Vector2 aimTarget) {
        boostTimeLeft = boostDuration;
        consume();
    }

    //Return boosted speed if effect still active
    @Override
    public float getSpeedMultiplier() {
        if (boostTimeLeft > 0f) {
            return speedMultiplier;
        } else {
            return 1f;
        }
    }
    //Constantly updates boost timers
    @Override
    public void update(float delta) {
        if (boostTimeLeft > 0f) {
            boostTimeLeft = Math.max(0f, boostTimeLeft - delta);
        }
    }

    @Override
    public boolean itemEffectEnded() {
        return super.itemEffectEnded() && boostTimeLeft <= 0f;
    }

    public boolean isBoostActive(){
        return boostTimeLeft > 0f;
    }
}

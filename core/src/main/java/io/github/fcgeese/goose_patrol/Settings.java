package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Settings {
    private static final float UNIT_SCALE = Main.UNIT_SCALE;

    // Goose settings
    public static float gooseAccuracy = 0.035f;
    public static float gooseWidth = 1f; // tile(s) wide
    public static float gooseHeight = 1f; // tile(s) tall
    public static TextureRegion gooseTexturePack =
       new TextureRegion( new Texture( Gdx.files.internal("Goose_Run_Pack.png") ) ) ;
    public static float gooseSpeed = 4;
    public static float gooseAttackCoolDown = 1f;
    public static float gooseAttackDuration = 5f;
    public static float gooseLockOnDuration = 500f;
    public static float gooseAttackRange = 50 * UNIT_SCALE;
    public static float gooseDamage = 1;
    public static float gooseAttackRange = 50f;
    public static float gooseDamage=1;

    public static TextureRegion coffeeTexture =
        new TextureRegion(new Texture(
            Gdx.files.internal(
                "assets_raw/Asset Packs/Free_pixel_food_16x16/Free_pixel_food_16x16/Icons/coffee_foam.png")));

    public static TextureRegion breadTexture =
        new TextureRegion(new Texture(
            Gdx.files.internal(
                "assets_raw/objects/bread/bread.png")));

    // Player settings
    public static TextureRegion playerTexturePack =
        new TextureRegion( new Texture( Gdx.files.internal("Player_Run_Pack.png") ) ) ;

    public static float playerWidth = 1f; // tile(s) wide
    public static float playerHeight = 2f; // tile(s) tall
    public static float playerSpeed = 4;
}

package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Settings {
    public static final float UNIT_SCALE = Main.UNIT_SCALE;

    // Goose settings
    public static float gooseAccuracy = 0.015f;
    public static float gooseWidth = 1f; // tile(s) wide
    public static float gooseHeight = 1f; // tile(s) tall
    public static TextureRegion gooseTexturePack =
       new TextureRegion( new Texture( Gdx.files.internal("geese/Goose_Run_Pack.png") ) ) ;
    public static float gooseSpeed = 4;
    public static float gooseAttackRange = 50 * UNIT_SCALE;
    public static float gooseDamage = 1;

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
        new TextureRegion( new Texture( Gdx.files.internal("player/PlayerTexturePack.png") ) ) ;

    public static float playerWidth = 0.875f; // tile(s) wide
    public static float playerHeight = 1.1875f; // tile(s) tall
    public static float playerSpeed = 4;
}

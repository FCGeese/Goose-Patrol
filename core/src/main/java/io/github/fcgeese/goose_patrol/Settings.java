package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Settings {
    public static final float UNIT_SCALE = Main.UNIT_SCALE;

    // Entity settings

    // Goose settings
    public static float gooseAccuracy = 0.015f;
    public static float gooseWidth = 1f; // tile(s) wide
    public static float gooseHeight = 1f; // tile(s) tall
    public static TextureRegion gooseTexturePack =
       new TextureRegion( new Texture( Gdx.files.internal("geese/GooseTexturePack.png") ) ) ;
    public static float gooseSpeed = 4; // DEFAULT: 4
    public static float gooseAttackRange = 50 * UNIT_SCALE;
    public static float gooseDamage = 1;

    // Player settings
    public static TextureRegion playerTexturePack =
        new TextureRegion( new Texture( Gdx.files.internal("player/PlayerTexturePack.png") ) ) ;

    public static float playerWidth = 0.875f; // tile(s) wide
    public static float playerHeight = 1.1875f; // tile(s) tall
    public static float playerSpeed = 4; // DEFAULT: 4

    public static TextureRegion controlsTexture =
        new TextureRegion( new Texture( Gdx.files.internal("assets_raw/CONTROL1.png") ) ) ;

    // Item settings

    // Bread settings
    public static TextureRegion breadTexture =
        new TextureRegion(new Texture(
            Gdx.files.internal(
                "assets_raw/objects/bread/bread.png")));
    public static int breadMaxShots = 5;

    // Coffee settings
    public static TextureRegion coffeeTexture =
        new TextureRegion(new Texture(
            Gdx.files.internal(
                "assets_raw/objects/coffee/coffee.png")));
    public static float coffeeSpeedMultiplier = 1.5f;
    public static float coffeeBoostDuration = 5f;

}

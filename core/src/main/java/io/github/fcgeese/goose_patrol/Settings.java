package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Settings {

    public static float gooseAccuracy = 0.035f;
    public static float gooseWidth = 64;
    public static float gooseHeight = 64;
    public static TextureRegion gooseTexturePack =
       new TextureRegion( new Texture( Gdx.files.internal("Goose_Run_Pack.png") ) ) ;
    public static float gooseSpeed = 1;
    public static float gooseAttackCoolDown = 1f;
    public static float gooseAttackDuration = 5f;
    public static float gooseLockOnDuration = 500f;
    public static float gooseAttackRange = 50f;



    public static TextureRegion playerTexturePack =
        new TextureRegion( new Texture( Gdx.files.internal("Player_Run_Pack.png") ) ) ;

    public static float playerWidth = 64;
    public static float playerHeight = 128;
    public static float playerSpeed = 1;
}

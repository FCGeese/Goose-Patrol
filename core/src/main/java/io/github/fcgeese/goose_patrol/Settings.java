package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Settings {
    public static float gooseAccuracy = 0.4f;
    public static float gooseWidth = 16;
    public static float gooseHeight = 16;
    public static Texture gooseTexture = new Texture(Gdx.files.internal("Goose.png"));
    public static float gooseSpeed = 0.5f;
    public static float gooseAttackCoolDown = 1f;
    public static float gooseAttackDuration = 5f;
    public static float gooseLockOnDuration = 5f;


    public static Texture playerTexture = new Texture(Gdx.files.internal("Player.png"));

    public static float playerWidth = 16;
    public static float playerHeight = 32;
    public static float playerSpeed = 2;
}

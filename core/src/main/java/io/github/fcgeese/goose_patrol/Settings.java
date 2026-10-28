package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Settings {
    public static float gooseAccuracy = 4;
    public static float gooseWidth = 16;
    public static float gooseHeight = 16;
    public static Texture gooseTexture = new Texture(Gdx.files.internal("Goose.png"));
    public static float gooseSpeed = 4;
}

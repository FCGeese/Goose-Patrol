package io.github.fcgeese.goose_patrol.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.fcgeese.goose_patrol.Elements.Element;
import io.github.fcgeese.goose_patrol.Settings;

public class Controls extends Element {

        public Controls(){
            setTexture(Settings.controlsTexture);
            setPositionVector(Vector2.Zero);
            setHitbox(new Rectangle(getPositionVector().x,getPositionVector().y, 3 ,3));
        }
}

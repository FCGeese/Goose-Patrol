package io.github.GoosePatrolGame.Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.GoosePatrolGame.Utils.Coord;
import io.github.GoosePatrolGame.Utils.Hitbox;
import io.github.GoosePatrolGame.Utils.Rect;

public class Food extends Element{

    public Food(Coord position) {
        super(new TextureRegion(new Texture(Gdx.files.internal("objects/breadcrumbs/8x8foodImage.png"))),
            position, new Hitbox(new Rect(position, 8, 8)));
        setHealth(500);
    }
}

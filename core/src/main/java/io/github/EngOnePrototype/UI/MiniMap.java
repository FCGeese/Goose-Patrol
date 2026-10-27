package io.github.EngOnePrototype.UI;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.EngOnePrototype.Elements.Goose;
import io.github.EngOnePrototype.Elements.Player;
import io.github.EngOnePrototype.Utils.Coord;

public class MiniMap {
    Coord screenPos;
    TextureRegion miniMap;

    public MiniMap() {
        screenPos = new Coord(0, 0);
        miniMap = new TextureRegion(new Texture(Gdx.files.internal("mapImage.png"))); // https://gist.github.com/metaphore/ce78cafbd1ebdde02903
    }

    public void draw(SpriteBatch batch, ShapeRenderer shape, Player player, ArrayList<Goose> geese) {
        geese.removeIf(goose -> ((goose.getPosition().getX() > 1900) ||
                (goose.getPosition().getY() > 1060)));
        shape.setColor(Color.BLUE);
        shape.rect(0, 0, 140, 105);
        shape.end();//lets the map gte drawn ontop of shapes
        batch.end();
        batch.begin();
        batch.draw(miniMap,player.getX()-150 + 3,player.getY()-150 + 3,60,60);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.RED);
        shape.circle((player.getPosition().getX() / 15) + 5,
                (player.getPosition().getY() / 11) + 5,
                2);

        for (Goose goose : geese) {
            System.out.println(goose.getPosition().getY());
            shape.setColor(Color.GRAY);
            shape.circle((goose.getPosition().getX() / 15) + 5, (goose.getPosition().getY() / 11) + 5, 1);
        }
    }
}

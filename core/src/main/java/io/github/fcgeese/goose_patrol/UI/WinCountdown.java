package io.github.fcgeese.goose_patrol.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WinCountdown {
    Vector2 screenPos = new Vector2(300, 400);
    Rectangle baseBar = new Rectangle(
        screenPos.x,
        screenPos.y,
        170,
        40
    );
    float timeLeft = 300;

    Rectangle winBar = new Rectangle(
        screenPos.x + 10,
        screenPos.y + 10,
        timeLeft / 2 ,
        20
    );

    public void draw(ShapeRenderer shape, float timeLeft) {

        shape.setColor(Color.BLUE);
        shape.rect(
            baseBar.x,
            baseBar.y,
            baseBar.width,
            baseBar.height
        );
        shape.setColor(Color.GOLD);
        shape.rect(
            winBar.x,
            winBar.y,
            timeLeft/2,
            winBar.height
        );
    }
}

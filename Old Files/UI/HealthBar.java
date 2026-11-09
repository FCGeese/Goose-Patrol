package io.github.GoosePatrolGame.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.GoosePatrolGame.Elements.Player;
import io.github.GoosePatrolGame.Utils.Coord;

public class HealthBar {
    Coord screenPos;

    public HealthBar() {
        screenPos = new Coord(30, 400);
    }

    public void draw(ShapeRenderer shape, Player player) {
        shape.setColor(Color.BLACK);
        shape.rect(screenPos.getX(), screenPos.getY(), 170, 40);
        shape.setColor(Color.RED);
        shape.rect(screenPos.getX() + 10, screenPos.getY() + 10, (float) (player.getHealth() * 1.5), 20);
    }
}

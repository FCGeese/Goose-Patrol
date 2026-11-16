package io.github.fcgeese.goose_patrol.UI;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.fcgeese.goose_patrol.Managers.HealthListener;
import io.github.fcgeese.goose_patrol.Settings;

public class HealthBar implements HealthListener {
    Vector2 screenPos = new Vector2(30, 400);
    Rectangle baseBar = new Rectangle(
        screenPos.x,
        screenPos.y,
        170,
        40
    );
    float playerHealth = 100;
    Rectangle healthBar = new Rectangle(
        screenPos.x + 10,
        screenPos.y + 10,
        (float) (playerHealth * 1.5),
        20
    );
    public void draw(ShapeRenderer shape) {
        shape.setColor(Color.BLUE);
        shape.rect(
            baseBar.x,
            baseBar.y,
            baseBar.width,
            baseBar.height
        );
        shape.setColor(Color.RED);
        shape.rect(
            healthBar.x,
            healthBar.y,
            healthBar.width,
            healthBar.height
        );
    }

    @Override
    public void onDeath() {

    }

    @Override
    public void onChange() {
        playerHealth -= Settings.gooseDamage;
        healthBar = new Rectangle(
            screenPos.x + 10,
            screenPos.y + 10,
            (float) (playerHealth * 1.5),
            20
        );
    }
}

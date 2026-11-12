package io.github.fcgeese.goose_patrol.Events;

import com.badlogic.gdx.Gdx;
import io.github.fcgeese.goose_patrol.Elements.Element;
import io.github.fcgeese.goose_patrol.Managers.EventListener;
import io.github.fcgeese.goose_patrol.Settings;

import java.util.ArrayList;
import java.util.List;

public class SpeedUpEvent implements EventListener {
    float secondTimer = 0f;
    @Override
    public void onStart() {
        //Ensure we only change gooseSpeed once.
        Settings.gooseSpeed = 1.2f;
    }

    @Override
    public void changeEffect() {
        //Increase every tick, used so we know when a second has passed.
        secondTimer += Gdx.graphics.getDeltaTime();
        //Need to check if a second has passed because otherwise speed gets increased every tick.
        if(secondTimer >= 1f) {
            secondTimer = 0f;
            Settings.gooseSpeed = 1.01f*Settings.gooseSpeed;
            Gdx.app.log("Event", "Final " + " Goose speed: " + Settings.gooseSpeed);
        }
    }

    @Override
    public void onEnd() {
        Settings.gooseSpeed = 1f;
    }
}

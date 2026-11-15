package io.github.fcgeese.goose_patrol.Events;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.fcgeese.goose_patrol.Elements.Element;
import io.github.fcgeese.goose_patrol.Managers.EventListener;

public class EggEvent implements EventListener {
    private Element spawnTrigger = new Element();
    private boolean triggered = false;

    public EggEvent(Vector2 triggerPos){
        this.spawnTrigger.setPositionVector(triggerPos);
        this.spawnTrigger.setHitbox(new Rectangle(
            triggerPos.x,
            triggerPos.y,
            0.1f,
            0.1f
            ));
    }

    public boolean isTriggered() {
        return triggered;
    }

    public Element getSpawnTrigger() {
        return spawnTrigger;
    }

    @Override
    public void onStart() {
        triggered = true;
    }

    @Override
    public void changeEffect() {

    }

    @Override
    public void onEnd() {

    }
}

package io.github.fcgeese.goose_patrol.Events;

import io.github.fcgeese.goose_patrol.Managers.EventListener;

public class WinEvent implements EventListener {

    @Override
    public void onStart() {
        System.out.println("WINNER");
    }

    @Override
    public void changeEffect() {

    }

    @Override
    public void onEnd() {

    }
}

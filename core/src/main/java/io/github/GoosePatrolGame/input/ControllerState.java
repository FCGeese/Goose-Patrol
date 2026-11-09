package io.github.GoosePatrolGame.input;

public interface ControllerState {
    void keyDown(Command command);

    default void keyUp(Command command) {
    }
}

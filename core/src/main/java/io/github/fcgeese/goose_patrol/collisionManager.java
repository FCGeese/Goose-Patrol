package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.math.Vector2;

public interface collisionManager {
    boolean canMove(MovingElement e);
    boolean canMove(MovingElement e, Vector2 v);
    boolean canMove(MovingElement e, int x, int y);
}

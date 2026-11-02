package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class CollisionManager {
    ArrayList<Element> collidables;

    public void addCollidable(Element e) {
        collidables.add(e);
    }

    public boolean canMove(MovingElement e) {
        if (collidables == null){return true;}
        for (Element collidable : collidables) {
            if (e.collidesWith(collidable)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks
     * @param e
     * @param v
     * @return
     */
    public boolean canMove(MovingElement e, Vector2 v){
        if (collidables == null){return true;}
        Vector2 oldPos = e.getPositionVector();
        e.setPositionVector(v);
        for(Element collidable : collidables){
            if (e.collidesWith(collidable)){
                e.setPositionVector(oldPos);
                return false;
            }
        }
        e.setPositionVector(oldPos);
        return true;
    }
    public boolean canMove(MovingElement e, int x, int y){
        Vector2 v = new Vector2(x,y);
        return canMove(e, v);
    }
}

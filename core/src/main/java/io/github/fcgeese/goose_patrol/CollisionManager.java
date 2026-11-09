package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {
    List<Element> collidables = new ArrayList<>();

    public void addCollidable(Element e) {
        collidables.add(e);
    }
    public void addCollidableList(List<Element> collidables){
        for (Element e : collidables){
            addCollidable(e);
        }
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
            if (e.collidesWith(collidable) && (e != collidable)){
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

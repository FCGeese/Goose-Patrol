package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.math.Rectangle;
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

    public boolean canMove(MovingElement e){
        Rectangle nextHitbox = new Rectangle(e.nxtHitbox());
        for(Element collidable : collidables){
            if (collidable != e) {
                if (nextHitbox.overlaps(collidable.getHitbox())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canMove(MovingElement e, float x, float y){
        Rectangle nextHitbox = new Rectangle(
            e.getHitbox().x + x,
            e.getHitbox().y + y,
            e.getHitbox().width,
            e.getHitbox().height
        );
        for(Element collidable : collidables){
            if (nextHitbox.overlaps(collidable.getHitbox())){
                return false;
            }
        }
        return true;
    }

}

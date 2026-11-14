package io.github.fcgeese.goose_patrol.Managers;

import com.badlogic.gdx.math.Rectangle;
import io.github.fcgeese.goose_patrol.Elements.Element;
import io.github.fcgeese.goose_patrol.Elements.MovingElement;

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

    public List<Element> getCollidables() {
        return collidables;
    }

    public boolean canMove(MovingElement e, float x, float y){
        Rectangle nextHitbox = new Rectangle(
            e.getHitbox().x + x,
            e.getHitbox().y + y,
            e.getHitbox().width,
            e.getHitbox().height
        );
        for(Element collidable : collidables){
            if (collidable != e) {
                if (nextHitbox.overlaps(collidable.getHitbox())) {
                    return false;
                }
            }
        }
        return true;
    }

    public Element hitObj(Element e){
        for (Element c : collidables){
            if ( c!=e && e.getHitbox().overlaps(c.getHitbox())){
                return e;
            }
        }
        return null;
    }
}

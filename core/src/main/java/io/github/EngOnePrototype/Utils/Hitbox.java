package io.github.EngOnePrototype.Utils;

import java.util.ArrayList;

/**
 * Hitbox
 */
public class Hitbox {
    ArrayList<Rect> boxList;

    public Hitbox(ArrayList<Rect> boxList) {
        this.boxList = boxList;
    }

    public Hitbox(Rect singleBox) {
        boxList = new ArrayList<>();
        boxList.add(singleBox);
    }

    public void addBox(Rect rect) {
        boxList.add(rect);
    }

    public void setPosition(Coord deltaPos) {
        for (Rect rectangle : boxList) {
            rectangle.position = rectangle.position.add(deltaPos);
        }
    }

    public boolean isCollision(Hitbox other) {
        boolean flag = false;
        for (Rect thisRectangle : boxList) {
            for (Rect otherRectangle : other.boxList) {
                if (thisRectangle.isOverlapped(otherRectangle)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

}

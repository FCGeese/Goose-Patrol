package io.github.fcgeese.goose_patrol.Managers;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import io.github.fcgeese.goose_patrol.Elements.Element;
import io.github.fcgeese.goose_patrol.Elements.Goose;
import io.github.fcgeese.goose_patrol.Settings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TargetManager {
    ArrayList<Element> possibleTargets = new ArrayList<>();

    public void addTarget(Element e){
        possibleTargets.add(e);
    }

    public void setTargetList(List<Element> targetList){
        for (Element e : targetList){
            addTarget(e);
        }
    }

    public boolean isInRange(Goose goose, Element target){
        return (goose.getPositionVector().dst(target.getPositionVector())<= Settings.gooseAttackRange);
    }
    public boolean canSee(Goose goose, Element nextTarget, CollisionManager colliders) {
        List<Element> notSeeThrough = colliders.getCollidables()
            .stream()
            .filter(e -> (!(e instanceof Goose)))
            .toList();

        for (Element e : notSeeThrough) {
            if (Intersector.intersectSegmentRectangle(
                goose.getPositionVector(),
                nextTarget.getPositionVector(),
                e.getHitbox()
            )
            ) {
                return false;
            }
        }
        return true;
    }

    public Element getTarget(Goose goose, CollisionManager colliders){

        Element nextTarget= possibleTargets.stream()
            .min(Comparator.comparingDouble(target ->
                goose.getPositionVector().dst(target.getPositionVector())))
            .orElse(null);
        if( canSee(goose, nextTarget, colliders) ){
            return nextTarget;
        }
        return null;
    }

}

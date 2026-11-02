package io.github.fcgeese.goose_patrol;

import java.util.ArrayList;
import java.util.Comparator;

public class targetManager {
    ArrayList<Element> possibleTargets = new ArrayList<>();

    public void addTarget(Element e){
        possibleTargets.add(e);
    }

    boolean isInRange(Goose goose, Element target){
        if (target == null){return false;}
        return (goose.getPositionVector().dst(target.getPositionVector())<= Settings.gooseAttackRange);
    }

    Element getTarget(Goose goose){
        if (possibleTargets == null){return null;}
        return possibleTargets.stream()
            .min(Comparator.comparingDouble(possibleTargets ->
                goose.getPositionVector().dst(possibleTargets.getPositionVector())))
            .orElse(null);
    }
}

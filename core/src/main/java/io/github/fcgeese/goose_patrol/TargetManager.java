package io.github.fcgeese.goose_patrol;

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

    public Element getTarget(Goose goose){
        return possibleTargets.stream()
            .min(Comparator.comparingDouble(target ->
                goose.getPositionVector().dst(target.getPositionVector())))
            .orElse(null);
    }

}

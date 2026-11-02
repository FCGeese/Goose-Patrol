package io.github.fcgeese.goose_patrol;

public interface targetManager {
    boolean isInRange(Goose goose, Element target);

    Element getTarget(Goose goose);
}

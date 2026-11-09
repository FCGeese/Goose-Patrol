import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import io.github.GoosePatrolGame.Settings;
import io.github.GoosePatrolGame.Mobs.Goose;
import io.github.GoosePatrolGame.Utils.Coord;

@DisplayName("Entity Tests")

class EntityTests {
    private Goose goose;

    @BeforeEach
    void setup() {
        goose = new Goose(new Coord(100, 100), Settings.gooseSpeed);
    }

    @Test
    @DisplayName("Movement Test")
    void testMovement() {
        assertEquals(new Coord(50, 50), goose.move(new Coord(50, 50)));
    }
}

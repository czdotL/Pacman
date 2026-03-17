package hu.pacman;

import hu.pacman.model.Ghost;
import hu.pacman.model.Map;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

class GhostTest {

    @Test
    void testGhostCreation() {
        Ghost g = new Ghost(5, 10, Color.RED);
        assertEquals(5, g.getX());
        assertEquals(10, g.getY());
        assertEquals(Color.RED, g.getColor());
    }

    @Test
    void testGhostMoveAttempts() {
        Map map = new Map();
        Ghost g = new Ghost(1, 1, Color.BLUE);
        
        g.moveRandom(map);
        
        assertTrue(g.getX() >= 0);
        assertTrue(g.getY() >= 0);
    }
}
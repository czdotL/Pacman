package hu.pacman;

import hu.pacman.model.Map;
import hu.pacman.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;
    private Map map;

    @BeforeEach
    void setUp() {
        player = new Player(1, 1);
        map = new Map();
        
        int[][] testData = {
            {1, 1, 1, 1},
            {1, 0, 0, 1},
            {1, 1, 1, 1}
        };
        map.setMapData(testData);
    }

    @Test
    void testMoveValid() {
        player.setDirection(1, 0); 
        boolean result = player.move(map);
        
        assertTrue(result);
        assertEquals(2, player.getX()); 
        assertEquals(1, player.getY());
    }

    @Test
    void testMoveIntoWall() {
        player.setDirection(0, 1); 
        boolean result = player.move(map);
        
        assertFalse(result); 
        assertEquals(1, player.getX()); 
        assertEquals(1, player.getY());
    }

    @Test
    void testViewAngleUpdate() {
        
        assertEquals(30, player.getViewAngle());

        player.setDirection(0, -1); 
        
        int[][] openUp = {{1, 0, 1}, {1, 0, 1}};
        map.setMapData(openUp);
        player.move(map);
        
        assertEquals(120, player.getViewAngle()); 
    }
    
    @Test
    void testLives() {
        assertEquals(3, player.getLives());
        player.decreaseLives();
        assertEquals(2, player.getLives());
    }
}
package hu.pacman;

import hu.pacman.model.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map();
    }

    @Test
    void testDefaultMapLoad() {
        assertNotNull(map.getMapData());
        assertTrue(map.getWidth() > 0);
        assertTrue(map.getHeight() > 0);
    }

    @Test
    void testOutOfBounds() {
        assertEquals(1, map.getTile(-1, 0));
        assertEquals(1, map.getTile(0, -1));
        assertEquals(1, map.getTile(999, 999));
    }

    @Test
    void testRemovePoint() {
        int[][] testData = {{2, 1}}; 
        map.setMapData(testData);

        assertTrue(map.hasPoints());   
        assertEquals(2, map.getTile(0, 0)); 

        map.removePoint(0, 0);            

        assertEquals(0, map.getTile(0, 0)); 
        assertFalse(map.hasPoints());     
    }
}
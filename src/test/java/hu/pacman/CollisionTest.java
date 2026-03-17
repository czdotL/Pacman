package hu.pacman;

import hu.pacman.model.Map;
import hu.pacman.model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CollisionTest {
    @Test
    void testWallCollision() {
        Map map = new Map();
        
        int[][] wallMap = {
            {1, 1, 1},
            {1, 0, 1}, 
            {1, 1, 1}
        };
        map.loadMap(wallMap);
        
        Player player = new Player(1, 1);
        
        player.setDirection(-1, 0); 
        boolean moved = player.move(map);
        
        assertFalse(moved, "Nem mozdulhat el, ha fal van ott");
        assertEquals(1, player.getX(), "A pozíciónak változatlannak kell lennie");
    }
}
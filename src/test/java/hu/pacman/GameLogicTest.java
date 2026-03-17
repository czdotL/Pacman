package hu.pacman;

import hu.pacman.model.Game;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {

    @Test
    void testSaveGameFileCreation() {

        Game game = new Game(null);
        game.startNewGame(); 
        
        boolean success = game.saveGameState();
        
        assertTrue(success, "A mentésnek sikeresnek kell lennie");
        
        File file = new File("saved_game.json");
        assertTrue(file.exists(), "A mentés fájlnak léteznie kell");
        
        file.delete();
    }
}
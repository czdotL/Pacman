package hu.pacman;

import hu.pacman.data.ScoreManager;
import hu.pacman.data.ScoreEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ScoreManagerTest {

    @BeforeEach
    void setUp() {
        ScoreManager.setFileName("test_scores_unit.json");
    }

    @AfterEach
    void tearDown() {
        new File("test_scores_unit.json").delete();
        ScoreManager.setFileName("scores.json"); 
    }

    @Test
    void testSaveAndLoad() {
        ScoreManager.saveScore("Tester", 1234);
        
        List<ScoreEntry> scores = ScoreManager.loadScores();
        assertFalse(scores.isEmpty());
        
        ScoreEntry entry = scores.get(0);
        assertEquals("Tester", entry.name);
        assertEquals(1234, entry.score);
    }
    
    @Test
    void testScoreEntryObject() {
        ScoreEntry se = new ScoreEntry("Name", 100);
        assertEquals("Name", se.name);
        assertEquals(100, se.score);
    }
}
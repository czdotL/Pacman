package hu.pacman.data;

/**
 * Egy dicsőséglista bejegyzést reprezentáló osztály.
 * Tárolja a játékos nevét és elért pontszámát.
 */
public class ScoreEntry {
    public String name;
    public int score;

    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
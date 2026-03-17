package hu.pacman.model;

/**
 * A játékost (Pac-Man) reprezentáló osztály.
 * Kezeli a pozíciót, mozgást és az életek számát.
 */
public class Player {
    private int x, y;
    private int dx, dy; // Jelenlegi mozgási irány
    private int nextDx, nextDy; // Következő kért irány (bufferelt bemenet)
    private int lives = 3;
    private int startX, startY;
    private int viewAngle = 30; // Száj nyitási szöge/iránya

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
    }

    // Irányváltás kérése
    public void setDirection(int dx, int dy) {
        this.nextDx = dx;
        this.nextDy = dy;
    }

    // Mozgás megkísérlése a térképen
    public boolean move(Map map) {
        // Először a kért (új) irányba próbálunk fordulni
        if (tryMove(nextDx, nextDy, map)) {
            updateViewAngle(nextDx, nextDy); 
            dx = nextDx;
            dy = nextDy;
            return true;
        }

        // Ha nem lehet fordulni, folytatjuk a régi irányt
        if (tryMove(dx, dy, map)) {
            updateViewAngle(dx, dy); 
            return true;
        }

        return false;
    }

    // Segédfüggvény: ütközésvizsgálat falakkal
    private boolean tryMove(int tDx, int tDy, Map map) {
        if (tDx == 0 && tDy == 0) return false;
        int nextX = x + tDx;
        int nextY = y + tDy;
        
        if (map.getTile(nextX, nextY) != 1) { // 1 = Fal
            x = nextX;
            y = nextY;
            return true;
        }
        return false;
    }

    // A száj irányának beállítása mozgás alapján
    private void updateViewAngle(int dx, int dy) {
        if (dx == 1)  viewAngle = 30;  // Jobbra
        if (dx == -1) viewAngle = 210; // Balra
        if (dy == -1) viewAngle = 120; // Fel
        if (dy == 1)  viewAngle = 300; // Le
    }
    
    // Játékos visszahelyezése a kezdőpontra (halál esetén)
    public void reset() {
        this.x = startX;
        this.y = startY;
        this.dx = 0; this.dy = 0;
        this.nextDx = 0; this.nextDy = 0;
        this.viewAngle = 30; 
    }

    // Getterek és Setterek
    public int getX() { return x; }
    public int getY() { return y; }
    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }
    public void decreaseLives() { this.lives--; }
    public int getViewAngle() { return viewAngle; }
}
package hu.pacman.model;

import java.awt.Color;

public class Ghost {
    private int x, y;
    private int dx, dy;
    private Color color;

    public Ghost(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        // Kezdő irány, hogy ne álljon egy helyben
        this.dx = 1; 
        this.dy = 0;
    }

    // mozgás
    public void moveSmart(Map map, int playerX, int playerY) {
        
        // 1. Megnézzük, merre van a játékos
        int diffX = playerX - x;
        int diffY = playerY - y;

        // 2. Eldöntjük az ideális irányt
        int bestDx = 0;
        int bestDy = 0;

        // Ha vízszintesen messzebb van, akkor arra próbálunk menni először
        if (Math.abs(diffX) > Math.abs(diffY)) {
            // Ha diffX pozitív, jobbra (1), ha negatív, balra (-1)
            bestDx = (diffX > 0) ? 1 : -1; 
        } else {
            // Különben függőlegesen próbálunk menni
            bestDy = (diffY > 0) ? 1 : -1;
        }

        // 3. Megpróbálunk a "legjobb" irányba lépni
        // Ha a legjobb irány nem fal, akkor arra megyünk
        if (map.getTile(x + bestDx, y + bestDy) != 1) {
            dx = bestDx;
            dy = bestDy;
            
            x += dx;
            y += dy;
        } 
        // 4. Ha a legjobb irány FAL, akkor:
        // Vagy megpróbáljuk a másik tengelyt, vagy véletlenszerűen lépünk.
        // Itt most az egyszerűség kedvéért meghívjuk a régi véletlen mozgást,
        // így ha elakad, kiszabadítja magát.
        else {
            moveRandom(map);
        }
    }

    // Ez a régi metódus
    public void moveRandom(Map map) {
        // Ha eddig álltunk, vagy falnak mentünk, vagy csak véletlenül (10%) irányt váltunk
        int nextX = x + dx;
        int nextY = y + dy;

        if (map.getTile(nextX, nextY) == 1 || Math.random() < 0.1) {
             changeDirection();
        }
        
        // Újra ellenőrizzük, hogy az új irány jó-e
        nextX = x + dx;
        nextY = y + dy;

        if (map.getTile(nextX, nextY) != 1) {
            x = nextX;
            y = nextY;
        } else {
            // Ha még mindig fal, akkor újra sorsolunk (hogy ne ragadjon be)
            changeDirection();
        }
    }

    private void changeDirection() {
        int dir = (int)(Math.random() * 4);
        switch(dir) {
            case 0 -> { dx=1; dy=0; }
            case 1 -> { dx=-1; dy=0; }
            case 2 -> { dx=0; dy=1; }
            case 3 -> { dx=0; dy=-1; }
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Color getColor() { return color; }
}
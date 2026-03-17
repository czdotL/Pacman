package hu.pacman.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A játéktérképet reprezentáló osztály.
 * Tárolja a falakat (1), pontokat (2) és üres mezőket (0).
 */

public class Map {
    private int[][] mapData; // A térkép rácsszerkezete
    private int width;
    private int height;
    private List<Point> points; // A még felvehető pontok listája

    public Map() {
        initDefaultMap();
    }


    // Alapértelmezett pálya inicializálása
    public void initDefaultMap() {
        
        int[][] defaultMap = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,1,2,1,2,1,1,2,1,1,1,2,1},
            {1,2,1,1,1,2,1,1,2,1,2,1,1,2,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,2,1,1,1,2,1,2,1,1,1,2,1},
            {1,2,2,2,2,2,1,2,2,1,2,2,1,2,2,2,2,2,1},
            {1,1,1,1,1,2,1,1,0,1,0,1,1,2,1,1,1,1,1},
            {0,0,0,0,1,2,1,0,0,0,0,0,1,2,1,0,0,0,0},
            {1,1,1,1,1,2,1,0,1,0,1,0,1,2,1,1,1,1,1},
            {1,2,2,2,2,2,0,0,1,0,1,0,0,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,1,1,1,1,1,1,2,1,1,1,2,1},
            {1,2,2,2,1,2,2,2,2,1,2,2,2,2,1,2,2,2,1},
            {1,1,1,2,1,2,1,1,2,1,2,1,1,2,1,2,1,1,1},
            {1,2,2,2,2,2,1,2,2,2,2,2,1,2,2,2,2,2,1},
            {1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        loadMap(defaultMap);
    }

    // Térkép betöltése és pontok listázása
    public void loadMap(int[][] data) {
        this.height = data.length;
        this.width = data[0].length;
        this.mapData = new int[height][width];
        this.points = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.mapData[y][x] = data[y][x];
                if (data[y][x] == 2) {
                    points.add(new Point(x, y));
                }
            }
        }
    }

    // Új térképadat beállítása (pl. betöltésnél)
    public void setMapData(int[][] newData) {
        loadMap(newData);
    }

    // Adott koordináta típusának lekérdezése (falütközéshez)
    public int getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return 1; // Pályán kívül falnak tekintjük
        }
        return mapData[y][x];
    }

    // Pont eltávolítása a térképről (ha a játékos megette)
    public void removePoint(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height && mapData[y][x] == 2) {
            mapData[y][x] = 0;
            points.removeIf(p -> p.x == x && p.y == y);
        }
    }

    //Getterek
    public int[][] getMapData() { return mapData; }
    public boolean hasPoints() { return !points.isEmpty(); }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
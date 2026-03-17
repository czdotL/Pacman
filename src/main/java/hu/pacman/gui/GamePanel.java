package hu.pacman.gui;

import hu.pacman.model.Game;
import hu.pacman.model.Ghost;
import hu.pacman.model.Map;

import javax.swing.JPanel;
import java.awt.*;

/**
 * A játék grafikus megjelenítéséért felelős panel.
 * Kirajzolja a pályát, a játékost, a szellemeket és a HUD-ot.
 */

public class GamePanel extends JPanel {
    private Game game;
    private final int TILE_SIZE = 30; 

    public GamePanel(Game game) {
        this.game = game;
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (game.getMap() == null) return;

        Graphics2D g2d = (Graphics2D) g;
        // Élsimítás bekapcsolása a szebb körökért
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Map map = game.getMap();
        int[][] data = map.getMapData();

        // 1. Pálya elemek kirajzolása (Fal, Pont)
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[0].length; x++) {
                int posX = x * TILE_SIZE;
                int posY = y * TILE_SIZE;

                if (data[y][x] == 1) { //Fal
                    g2d.setColor(new Color(25, 25, 112)); 
                    g2d.fillRect(posX, posY, TILE_SIZE, TILE_SIZE);
                    g2d.setColor(new Color(65, 105, 225));
                    g2d.drawRect(posX, posY, TILE_SIZE, TILE_SIZE);
                } else if (data[y][x] == 2) { // Pont
                    g2d.setColor(new Color(255, 184, 174)); 
                    int dotSize = 8;
                    g2d.fillOval(posX + (TILE_SIZE - dotSize)/2, posY + (TILE_SIZE - dotSize)/2, dotSize, dotSize);
                }
            }
        }

        // 2. Játékos
        g2d.setColor(Color.YELLOW);
        
        g2d.fillArc(game.getPlayer().getX() * TILE_SIZE + 2, 
                    game.getPlayer().getY() * TILE_SIZE + 2, 
                    TILE_SIZE - 4, TILE_SIZE - 4, 
                    game.getPlayer().getViewAngle(), 300); 

        // 3. Szellemek kirajzolása
        for (Ghost ghost : game.getGhosts()) {
            g2d.setColor(ghost.getColor());
            g2d.fillOval(ghost.getX() * TILE_SIZE + 2, 
                         ghost.getY() * TILE_SIZE + 2, 
                         TILE_SIZE - 4, TILE_SIZE - 4);
        }

        // 4. Hud, pontszám és életek
        int mapHeight = data.length * TILE_SIZE;
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, mapHeight, getWidth(), 50); 

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 20));
        g2d.drawString("SCORE: " + game.getScore(), 20, mapHeight + 32);
        
        // Életek rajzolása
        g2d.drawString("LIVES:", 200, mapHeight + 32);
        g2d.setColor(Color.YELLOW);
        for(int i=0; i<game.getLives(); i++) {
            g2d.fillOval(280 + (i * 25), mapHeight + 15, 20, 20);
        }
    }
}
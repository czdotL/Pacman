package hu.pacman.model;

import hu.pacman.gui.GamePanel;
import hu.pacman.data.ScoreManager;
import hu.pacman.gui.MainMenu;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A központi játékvezérlő osztály (Controller).
 * Kezeli a játékciklust (Timer), bemeneteket, ütközéseket és a játékállapotot.
 */

public class Game implements ActionListener, KeyListener {
    private Timer timer;
    private Player player;
    private Map map;
    private ArrayList<Ghost> ghosts;
    private GamePanel panel;
    private MainMenu mainMenu;
    private java.util.HashMap<String, Integer> metaData; // Pontszám, életek tárolása
    private int tickCounter = 0; // Időzítés szinkronizálása

    private static final String SAVE_FILE = "saved_game.json";

    public Game(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        this.panel = new GamePanel(this);
        this.metaData = new HashMap<>();
        this.timer = new Timer(18, this); 
    }
    // Új játék indítása alaphelyzetből
    public void startNewGame() {
        map = new Map();
        player = new Player(1, 1);
        ghosts = new ArrayList<>();
        ghosts.add(new Ghost(5, 5, Color.RED));
        ghosts.add(new Ghost(6, 5, Color.PINK));
        
        metaData.put("score", 0);
        metaData.put("lives", 3);
        
        tickCounter = 0;
        initGameLoop();
    }

    private void initGameLoop() {
        panel.setFocusable(true);
        panel.addKeyListener(this);
        panel.requestFocusInWindow();
        timer.start();
    }

    // Játékállás mentése JSON-be (szerializáció)
    public boolean saveGameState() {
        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append("  \"score\": ").append(metaData.get("score")).append(",\n");
            sb.append("  \"lives\": ").append(metaData.get("lives")).append(",\n");
            sb.append("  \"playerX\": ").append(player.getX()).append(",\n");
            sb.append("  \"playerY\": ").append(player.getY()).append(",\n");
            
            sb.append("  \"map\": [");
            int[][] data = map.getMapData();
            for(int i=0; i<data.length; i++) {
                sb.append("[");
                for(int j=0; j<data[i].length; j++) {
                    sb.append(data[i][j]);
                    if(j < data[i].length -1) sb.append(",");
                }
                sb.append("]");
                if(i < data.length -1) sb.append(",");
            }
            sb.append("]\n");
            sb.append("}");
            writer.write(sb.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mentett állás betöltése és parse-olása
    public boolean loadSavedGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return false;

        try {
            String content = new String(Files.readAllBytes(Paths.get(SAVE_FILE)));
            
            int score = Integer.parseInt(extractValue(content, "score"));
            int lives = Integer.parseInt(extractValue(content, "lives"));
            int pX = Integer.parseInt(extractValue(content, "playerX"));
            int pY = Integer.parseInt(extractValue(content, "playerY"));

            map = new Map();
            int[][] loadedMap = parseMapData(content, map.getHeight(), map.getWidth());
            map.setMapData(loadedMap);

            player = new Player(pX, pY);
            metaData.put("score", score);
            metaData.put("lives", lives);
            
            ghosts = new ArrayList<>();
            ghosts.add(new Ghost(5, 5, Color.RED));
            ghosts.add(new Ghost(6, 5, Color.PINK));

            initGameLoop();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Segédfüggvény JSON parse-oláshoz
    private String extractValue(String json, String key) {
        int start = json.indexOf("\"" + key + "\":") + key.length() + 3;
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("\n", start);
        return json.substring(start, end).trim();
    }

    private int[][] parseMapData(String json, int h, int w) {
        int[][] data = new int[h][w];
        int start = json.indexOf("\"map\": [") + 8;
        int end = json.lastIndexOf("]");
        String arrayStr = json.substring(start, end);
        
        String[] rows = arrayStr.split("\\],");
        for(int i=0; i<rows.length && i<h; i++) {
            String row = rows[i].replace("[", "").replace("]", "");
            String[] cols = row.split(",");
            for(int j=0; j<cols.length && j<w; j++) {
                data[i][j] = Integer.parseInt(cols[j].trim());
            }
        }
        return data;
    }

    // A Timer eseménykezelője (Game Loop)
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        panel.repaint(); // Képernyő frissítése
    }

    // Játéklogika frissítése
    private void update() {
        if (!timer.isRunning()) return;
        tickCounter++;

        // Pac-Man mozgása minden 5. ciklusban (sebesség szabályozás) 
        if (tickCounter % 5 == 0) {
            if (player.move(map)) {
                if (map.getTile(player.getX(), player.getY()) == 2) {
                    int currentScore = metaData.get("score");
                    metaData.put("score", currentScore + 10);
                    map.removePoint(player.getX(), player.getY());
                    if (!map.hasPoints()) {
                        panel.paintImmediately(panel.getBounds());
                        gameOver("YOU WIN!");
                        return;
                    }
                }
            }
        }
        // Szellemek mozgása 
        if (tickCounter % 8 == 0) { // Lassítás
            for (Ghost ghost : ghosts) {
                
                // Átadjuk a térképet ÉS a játékos koordinátáit
                ghost.moveSmart(map, player.getX(), player.getY());
                
                checkCollision(ghost);
                if (!timer.isRunning()) return; 
            }
        }
        // Folyamatos ütközésvizsgálat
        for (Ghost ghost : ghosts) {
            checkCollision(ghost);
            if (!timer.isRunning()) return;
        }
    }

    // Ütközés detektálása a játékos és egy szellem között
    private void checkCollision(Ghost ghost) {
        if (!timer.isRunning()) return;
        if (ghost.getX() == player.getX() && ghost.getY() == player.getY()) {
            int lives = metaData.get("lives");
            lives--;
            metaData.put("lives", lives);
            player.reset();
            
            if (lives <= 0) {
                panel.paintImmediately(panel.getBounds());
                gameOver("GAME OVER");
            }
        }
    }

    // Szünet menü megjelenítése
    private void showPauseMenu() {
        timer.stop(); 
        
        Window window = SwingUtilities.getWindowAncestor(panel);
        JDialog dialog = new JDialog(window, "Paused", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);

        JPanel content = new JPanel();
        content.setBackground(Color.BLACK);
        content.setBorder(new CompoundBorder(
            new LineBorder(new Color(25, 25, 112), 4),
            new EmptyBorder(20, 50, 20, 50)            
        ));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("PAUSED");
        title.setFont(new Font("Monospaced", Font.BOLD, 40));
        title.setForeground(Color.YELLOW);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(30));

        JButton resumeBtn = new JButton("RESUME");
        JButton saveBtn = new JButton("SAVE GAME");
        JButton exitBtn = new JButton("EXIT TO MENU");

        styleButton(resumeBtn);
        styleButton(saveBtn);
        styleButton(exitBtn);

        resumeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        resumeBtn.addActionListener(e -> {
            dialog.dispose();
            timer.start(); 
        });

        saveBtn.addActionListener(e -> {
            if(saveGameState()) {
                saveBtn.setText("GAME SAVED!"); 
                saveBtn.setForeground(Color.GREEN);
                saveBtn.setEnabled(false); 
            } else {
                saveBtn.setText("ERROR!");
                saveBtn.setForeground(Color.RED);
            }
        });

        exitBtn.addActionListener(e -> {
            dialog.dispose();
            mainMenu.showMenu(); 
        });

        content.add(resumeBtn);
        content.add(Box.createVerticalStrut(15));
        content.add(saveBtn);
        content.add(Box.createVerticalStrut(15));
        content.add(exitBtn);

        dialog.add(content);
        dialog.pack();
        dialog.setLocationRelativeTo(panel);
        dialog.setVisible(true);
    }

    // Játék vége képernyő (név megadása és mentés)
    private void gameOver(String message) {
        timer.stop();
        Window window = SwingUtilities.getWindowAncestor(panel);
        JDialog dialog = new JDialog(window, "Result", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);

        JPanel content = new JPanel();
        content.setBackground(Color.BLACK);
        content.setBorder(new CompoundBorder(
            new LineBorder(new Color(25, 25, 112), 4),
            new EmptyBorder(20, 40, 20, 40)
        ));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(message);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        titleLabel.setForeground(message.equals("YOU WIN!") ? Color.GREEN : Color.RED);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel scoreLabel = new JLabel("FINAL SCORE: " + metaData.get("score"));
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setBorder(new EmptyBorder(10, 0, 20, 0));

        JLabel namePrompt = new JLabel("ENTER YOUR NAME:");
        namePrompt.setFont(new Font("Monospaced", Font.PLAIN, 14));
        namePrompt.setForeground(Color.WHITE);
        namePrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = new JTextField(10);
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.setFont(new Font("Monospaced", Font.BOLD, 16));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setBackground(new Color(20, 20, 20));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setBorder(new LineBorder(Color.WHITE, 1));

        JButton saveBtn = new JButton("SAVE & MENU");
        styleButton(saveBtn);
        saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        saveBtn.addActionListener(e -> {
            String name = nameField.getText();
            if (name != null && !name.trim().isEmpty()) {
                new ScoreManager().saveScore(name, metaData.get("score"));
            }
            dialog.dispose();
        });

        content.add(titleLabel);
        content.add(scoreLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(namePrompt);
        content.add(Box.createVerticalStrut(5));
        content.add(nameField);
        content.add(Box.createVerticalStrut(20));
        content.add(saveBtn);

        dialog.add(content);
        dialog.pack();
        dialog.setLocationRelativeTo(panel);
        
        dialog.setVisible(true);
        mainMenu.showMenu();
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Monospaced", Font.BOLD, 18));
        btn.setBackground(new Color(0, 0, 139));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
                new LineBorder(Color.WHITE, 2),
                new EmptyBorder(5, 20, 5, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    // Billentyűzet kezelés (Irányítás)
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) player.setDirection(0, -1);
        if (key == KeyEvent.VK_DOWN) player.setDirection(0, 1);
        if (key == KeyEvent.VK_LEFT) player.setDirection(-1, 0);
        if (key == KeyEvent.VK_RIGHT) player.setDirection(1, 0);
        
        if (key == KeyEvent.VK_ESCAPE) {
             showPauseMenu();
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    public Player getPlayer() { return player; }
    public Map getMap() { return map; }
    public ArrayList<Ghost> getGhosts() { return ghosts; }
    public int getScore() { return metaData.getOrDefault("score", 0); }
    public int getLives() { return metaData.getOrDefault("lives", 0); }
    public GamePanel getPanel() { return panel; }
}
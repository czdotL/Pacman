package hu.pacman.gui;

import hu.pacman.model.Game;
import javax.swing.*;
import java.awt.*;

/**
 * A főmenüt megvalósító osztály.
 * CardLayout-ot használ a menü és a játék közötti váltáshoz.
 */

public class MainMenu {
    private JFrame frame;
    private JPanel menuPanel;
    private CardLayout cardLayout; // Nézetváltó elrendezés
    private JPanel mainContainer;
    private Game game;

    public MainMenu() {
        // Ablak inicializálása
        frame = new JFrame("Pac-Man");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        createMenu();

        mainContainer.add(menuPanel, "MENU");
        frame.add(mainContainer);
        frame.setLocationRelativeTo(null); // Középre igazítás
        frame.setVisible(true);
    }

    // Menü UI felépítése
    private void createMenu() {
        menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(Color.BLACK);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel title = new JLabel("PAC-MAN", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 60));
        title.setForeground(Color.YELLOW);
        menuPanel.add(title, gbc);
        
        menuPanel.add(Box.createVerticalStrut(40), gbc);

        addStyledButton("New Game", e -> startNewGame(), gbc);
        // ÚJ GOMB:
        addStyledButton("Load Game", e -> loadGame(), gbc);
        addStyledButton("High Scores", e -> showHighScores(), gbc);
        addStyledButton("Exit", e -> System.exit(0), gbc);
    }

    // gombok hozzáadása
    private void addStyledButton(String text, java.awt.event.ActionListener al, GridBagConstraints gbc) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(new Color(0, 0, 139));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 40, 10, 40)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(al);
        menuPanel.add(btn, gbc);
    }
    // Új játék indítása
    private void startNewGame() {
        game = new Game(this); 
        game.startNewGame();
        switchToGame();
    }

    // Játék betöltése
    private void loadGame() {
        game = new Game(this);
        boolean success = game.loadSavedGame(); 
        if (success) {
            switchToGame();
        } else {
            JOptionPane.showMessageDialog(frame, "No saved game found!");
        }
    }

    // Nézet váltása a játékpanelre
    private void switchToGame() {
        mainContainer.add(game.getPanel(), "GAME");
        cardLayout.show(mainContainer, "GAME");
        game.getPanel().requestFocusInWindow();
    }

    // Dicsőséglista megjelenítése
    private void showHighScores() {
        HighScoreTable scoreTable = new HighScoreTable(() -> showMenu());
        mainContainer.add(scoreTable, "SCORES");
        cardLayout.show(mainContainer, "SCORES");
    }

    //Menü
    public void showMenu() {
        cardLayout.show(mainContainer, "MENU");
    }
}
package hu.pacman;

import hu.pacman.gui.MainMenu;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}
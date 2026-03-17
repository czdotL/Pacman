package hu.pacman.gui;

import hu.pacman.data.ScoreEntry;
import hu.pacman.data.ScoreManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * A legjobb eredményeket megjelenítő panel.
 */

public class HighScoreTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public HighScoreTable(Runnable onBackAction) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK); 

        // Cím és táblázat inicializálása
        JLabel title = new JLabel("HIGH SCORES", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 40));
        title.setForeground(Color.YELLOW);
        title.setBorder(new EmptyBorder(20, 0, 20, 0)); 
        add(title, BorderLayout.NORTH);

        String[] columns = {"RANK", "NAME", "SCORE"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        table = new JTable(tableModel);
        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Monospaced", Font.BOLD, 18));
        table.setRowHeight(35); 
        table.setGridColor(new Color(25, 25, 112)); 
        table.setFillsViewportHeight(true);
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 0, 139)); 
        header.setForeground(Color.YELLOW);
        header.setFont(new Font("Monospaced", Font.BOLD, 22));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        refreshScores(); // Adatok betöltése

        // Görgetősáv és Vissza gomb hozzáadása
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.BLACK); 
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(25, 25, 112), 2));
        
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.BLACK);
        tableContainer.setBorder(new EmptyBorder(0, 50, 0, 50)); 
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(tableContainer, BorderLayout.CENTER);

        JButton backButton = new JButton("BACK TO MENU");
        styleButton(backButton);
        backButton.addActionListener(e -> onBackAction.run());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Monospaced", Font.BOLD, 20));
        btn.setBackground(new Color(0, 0, 139)); 
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Pontszámok betöltése, rendezése és megjelenítése
    public void refreshScores() {
        tableModel.setRowCount(0); // Tábla törlése
        List<ScoreEntry> scores = ScoreManager.loadScores();
        
        // Rendezés csökkenő sorrendbe
        scores.sort((s1, s2) -> Integer.compare(s2.score, s1.score));
        
        // Toplista feltöltése (max 10 elem)
        int currentRank = 1;
        
        for (int i = 0; i < scores.size(); i++) {
            ScoreEntry s = scores.get(i);

            // Rangszámítás logika
            if (i > 0 && s.score == scores.get(i-1).score) {
            } else {
                currentRank = i + 1; 
            }

            tableModel.addRow(new Object[]{currentRank, s.name.toUpperCase(), s.score});
            
            if (i >= 9) break; 
        }
    }
}
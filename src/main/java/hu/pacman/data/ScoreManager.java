package hu.pacman.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A pontszámok kezeléséért felelős osztály.
 * JSON formátumú fájlba menti és onnan olvassa vissza az adatokat.
 */
public class ScoreManager {
    private static String fileName = "scores.json";

    public static void setFileName(String name) {
        fileName = name;
    }

    // Új pontszám hozzáadása és mentése
    public static void saveScore(String name, int score) {
        List<ScoreEntry> scores = loadScores();
        scores.add(new ScoreEntry(name, score));
        writeScores(scores);
    }

    // Lista kiírása fájlba JSON formátumban (manuális string építés)
    private static void writeScores(List<ScoreEntry> scores) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < scores.size(); i++) {
            ScoreEntry s = scores.get(i);
            sb.append(String.format("  {\"name\": \"%s\", \"score\": %d}", s.name, s.score));
            if (i < scores.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Pontszámok beolvasása és manuális parse-olása
    public static List<ScoreEntry> loadScores() {
        List<ScoreEntry> list = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return list;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Érvényes adatsor keresése
                if (line.contains("name") && line.contains("score")) {
                    try {
                        // Név kinyerése string műveletekkel
                        int nameStart = line.indexOf("\"name\": \"") + 9;
                        int nameEnd = line.indexOf("\"", nameStart);
                        String name = line.substring(nameStart, nameEnd);
                        
                        // Pontszám kinyerése és konvertálása
                        int scoreStart = line.indexOf("\"score\": ") + 9;
                        String scorePart = line.substring(scoreStart).replaceAll("[^0-9]", "");
                        int score = Integer.parseInt(scorePart);
                        
                        list.add(new ScoreEntry(name, score));
                    } catch (Exception parseEx) {
                        System.err.println("Hiba a sor feldolgozásakor: " + line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
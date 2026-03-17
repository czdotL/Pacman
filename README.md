# 🎮 Pac-Man Java Swing

A classic Pac-Man style game implemented in **Java 17** using the **Swing** graphical user interface. This project features a modern menu system, persistent game states, and a competitive leaderboard, all built with zero external dependencies for JSON processing.

---

## 🎮 Features

- **Classic Gameplay:** Navigate a maze, collect pellets, and avoid ghosts.
- **Save & Load System:** Save your current progress (positions, lives, score) to a JSON file and resume later.
- **Leaderboard:** Tracks and displays the top 10 high scores.
- **Modern UI:** Button-based Main Menu and Pause Menu.
- **Animated Graphics:** Utilizes `Graphics2D` for antialiasing and dynamic shape drawing (e.g., Pac-Man's animated mouth).
- **Zero External Dependencies:** Uses custom string manipulation algorithms for JSON processing.

---

## 🕹️ Controls

| Key | Action |
| :--- | :--- |
| **Arrow Keys** | Move Pac-Man (Up, Down, Left, Right) |
| **ESC** | Pause Game / Display Pause Menu |

---

## 📜 Game Rules

| Rule | Description |
| :--- | :--- |
| **Lives** | The player starts with **3 lives** |
| **Scoring** | Each collected pellet is worth **10 points** |
| **Ghosts** | Red and Pink ghosts move toward the player. Sometimes they move randomly |
| **Collision** | Contact with a ghost results in losing a life and returning to the start position |
| **Win Condition** | Collect all pellets on the map |
| **Loss Condition** | Lose all lives (Game Over) |

---

## 🏗️ Architecture

The software follows the **Model-View-Separation** principle, organized into three main packages for maintainability:

| Package | Description |
| :--- | :--- |
| `hu.pacman.model` | Contains game logic, rules, and state (no GUI code) |
| `hu.pacman.gui` | UI elements including the main window, menus, and game panel |
| `hu.pacman.data` | Data persistence layer for file I/O |

### Key Classes

| Class | Description |
| :--- | :--- |
| `Game` | Central controller managing the game loop, keyboard events, and state updates |
| `Map` | Represents the level using a 2D integer array (`0`: empty, `1`: wall, `2`: pellet) |
| `Player` | Stores Pac-Man's data and implements collision detection (`tryMove`) |
| `Ghost` | Determines player position to attempt movement; moves randomly if a wall is hit |
| `ScoreManager` | Handles static methods for `scores.json` |

---

## ⚙️ Technical Details

### Technology Stack
- **Language:** Java 17
- **UI Library:** Java Swing
- **Rendering:** `Graphics2D`

### Game Loop
- Uses a `javax.swing.Timer` running at **20ms intervals** (~50 FPS)
- **Player Movement:** Occurs every **5th cycle**
- **Ghost Movement:** Occurs every **8th cycle** (slower than the player for balanced gameplay)

### Data Structures
| Structure | Purpose |
| :--- | :--- |
| `int[][]` | 2D Array for the map grid (fast collision checks) |
| `ArrayList<Ghost>` | Dynamic storage for ghosts |
| `HashMap<String, Integer>` | Flexible storage for game metadata (score, lives) |

### File Handling
- **Format:** JSON
- **Implementation:** Custom parsing (no external libraries)
- **Files:**
  - `saved_game.json`: Stores map state and entity coordinates
  - `scores.json`: Appends player name and score, sorted in descending order

---

## 🧪 Testing

The project utilizes **JUnit 5** to verify critical business logic:

| Test Class | Purpose |
| :--- | :--- |
| `PlayerTest` | Ensures walls block movement and pellet collection increases score |
| `CollisionTest` | Validates life loss detection during player-ghost contact |
| `ScoreManagerTest` | Verifies correct file I/O and handles corrupted files without crashing |

---

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 17 or higher

### Running the Game

1. Clone the repository
2. Compile the Java source files
3. Run the main class located in the `hu.pacman.gui` package

```bash
java hu.pacman.gui.Main

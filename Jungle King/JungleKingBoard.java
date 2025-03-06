import javax.swing.*;
import java.awt.*;

public class JungleKingBoard extends JFrame {
    private static final int ROWS = 7;
    private static final int COLS = 9;
    private JButton[][] grid;
    private ImageIcon landIcon;
    private ImageIcon lakeIcon;

    public JungleKingBoard() {
        setTitle("Jungle King Game Board");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(ROWS, COLS));

        landIcon = scaleImage("Assets/Board/land.png", 100, 100);
        lakeIcon = scaleImage("Assets/Board/lake.png", 100, 100); 

        grid = new JButton[ROWS][COLS];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = new JButton();
                grid[row][col].setIcon(getTileIcon(row, col));
                grid[row][col].setBorderPainted(false);
                grid[row][col].setFocusPainted(false);
                grid[row][col].setContentAreaFilled(false);
                add(grid[row][col]);
            }
        }
    }

    private ImageIcon getTileIcon(int row, int col) {
        if (isLake(row, col)) {
            return lakeIcon;
        }
        return landIcon;
    }

    private boolean isLake(int row, int col) {
        return ((row == 1 || row == 2 || row == 4 || row == 5) && (col == 3 || col == 4 || col == 5));
    }

    private ImageIcon scaleImage(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JungleKingBoard board = new JungleKingBoard();
            board.setVisible(true);
        });
    }
}

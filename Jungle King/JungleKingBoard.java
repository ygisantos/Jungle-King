import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import pieces.*;

public class JungleKingBoard extends JFrame {
    private static final int ROWS = 7;
    private static final int COLS = 9;
    private JButton[][] grid;
    private ImageIcon landIcon;
    private ImageIcon lakeIcon;
    private ImageIcon trapIcon;
    private ImageIcon denIcon;
    private Piece[][] pieces;
    private GameState gameState;
    private Piece selectedPiece;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Piece bluePieceSelected;
    private Piece redPieceSelected;

    public JungleKingBoard() {
        setTitle("Jungle King Game Board");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(ROWS, COLS));

        landIcon = scaleImage("Assets/Board/land.png", 100, 100);
        lakeIcon = scaleImage("Assets/Board/lake.png", 100, 100);
        trapIcon = scaleImage("Assets/Board/trap.png", 100, 100);
        denIcon = scaleImage("Assets/Board/den.png", 100, 100);

        grid = new JButton[ROWS][COLS];
        pieces = new Piece[ROWS][COLS];
        initializeBoard();
        initializePieces();
        gameState = new GameState();
        addPieceSelectionListeners();

        // Add keyboard listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        setFocusable(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = new JButton();
                grid[row][col].setIcon(getTileIcon(row, col));
                grid[row][col].setBorderPainted(true); // Changed to true
                grid[row][col].setFocusPainted(false);
                grid[row][col].setContentAreaFilled(false);
                add(grid[row][col]);
            }
        }
    }

    private void initializePieces() {
        // Blue team (top)
        pieces[0][0] = new Lion(true);
        pieces[0][2] = new Elephant(true);
        pieces[1][1] = new Cat(true);
        pieces[2][2] = new Wolf(true);
        pieces[4][2] = new Leopard(true);
        pieces[5][1] = new Dog(true);
        pieces[6][0] = new Tiger(true);
        pieces[6][2] = new Rat(true);
        
        // Red team (bottom)
        pieces[0][8] = new Tiger(false);
        pieces[1][7] = new Dog(false);
        pieces[2][6] = new Leopard(false);
        pieces[0][6] = new Rat(false);
        pieces[6][6] = new Elephant(false);
        pieces[6][8] = new Lion(false);
        pieces[5][7] = new Cat(false);
        pieces[4][6] = new Wolf(false);

        updateBoardDisplay();
    }

    private void updateBoardDisplay() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                ImageIcon baseIcon = getTileIcon(row, col);
                if (pieces[row][col] != null) {
                    grid[row][col].setIcon(overlayIcons(baseIcon, pieces[row][col].getIcon()));
                } else {
                    grid[row][col].setIcon(baseIcon);
                }
            }
        }
    }

    private ImageIcon overlayIcons(ImageIcon backgroundIcon, ImageIcon foregroundIcon) {
        Image background = backgroundIcon.getImage();
        Image foreground = foregroundIcon.getImage();
        
        BufferedImage combined = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        
        g.drawImage(background, 0, 0, 100, 100, null);
        g.drawImage(foreground, 0, 0, 100, 100, null);
        g.dispose();
        
        return new ImageIcon(combined);
    }

    private ImageIcon getTileIcon(int row, int col) {
        if (isLake(row, col)) return lakeIcon;
        if (isTrap(row, col)) return trapIcon;
        if (isDen(row, col)) return denIcon;
        return landIcon;
    }

    private boolean isLake(int row, int col) {
        return ((row >= 1 && row <= 2 || row >= 4 && row <= 5) && 
                (col >= 3 && col <= 5));
    }

    private boolean isTrap(int row, int col) {
        // Top traps
        if (row == 2 && (col == 0 || col == 8)) return true;
        if (row == 3 && (col == 1 || col == 7)) return true;
        if (row == 4 && (col == 0 || col == 8)) return true;
        return false;
    }

    private boolean isDen(int row, int col) {
        return (row == 3 && (col == 0 || col == 8)); // Player dens
    }

    private boolean isOwnDen(int row, int col, boolean isBlueTeam) {
        // Blue team's den is on the left (col 0), Red team's den is on the right (col 8)
        return isDen(row, col) && (isBlueTeam ? (col == 0) : (col == 8));
    }

    private ImageIcon scaleImage(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    private void addPieceSelectionListeners() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                final int r = row;
                final int c = col;
                grid[row][col].addActionListener(e -> handleTileClick(r, c));
            }
        }
    }

    private void handleKeyPress(KeyEvent e) {
        if (selectedPiece == null || !gameState.isGameStarted()) return;

        int newRow = selectedRow;
        int newCol = selectedCol;

        switch (e.getKeyChar()) {
            case 'w': case 'W': newRow--; break;
            case 's': case 'S': newRow++; break;
            case 'a': case 'A': newCol--; break;
            case 'd': case 'D': newCol++; break;
            default: return;
        }

        if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS) {
            if (isValidMove(selectedRow, selectedCol, newRow, newCol)) {
                movePiece(selectedRow, selectedCol, newRow, newCol);
                gameState.toggleTurn();
                clearSelection();
            }
        }
    }

    private void handleTileClick(int row, int col) {
        if (!gameState.isGameStarted()) {
            handleInitialPieceSelection(row, col);
            return;
        }

        // Clear previous selection if clicking on a different piece of same team
        if (pieces[row][col] != null && 
            pieces[row][col].isBlueTeam() == gameState.isBlueTeamTurn()) {
            clearSelection();
            selectedPiece = pieces[row][col];
            selectedRow = row;
            selectedCol = col;
            highlightSelectedPiece(row, col);
            highlightValidMoves(row, col);
            requestFocus(); // Get keyboard focus after selecting piece
            return;
        }

        // If a piece is selected and the move is valid, make the move
        if (selectedPiece != null && isValidMove(selectedRow, selectedCol, row, col)) {
            movePiece(selectedRow, selectedCol, row, col);
            gameState.toggleTurn();
            clearSelection();
        }
    }

    private void highlightSelectedPiece(int row, int col) {
        // Make border thicker and more visible
        grid[row][col].setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createLineBorder(Color.GREEN, 3)
        ));
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = pieces[fromRow][fromCol];
        
        // Can't move to own den
        if (isOwnDen(toRow, toCol, piece.isBlueTeam())) {
            return false;
        }

        // Check if destination has a piece that can be captured
        if (pieces[toRow][toCol] != null) {
            if (pieces[toRow][toCol].isBlueTeam() == piece.isBlueTeam()) {
                return false;
            }
            // Check trap capture
            if (isTrap(toRow, toCol)) {
                return true;
            }
            // Normal capture check
            if (!piece.canCapture(pieces[toRow][toCol])) {
                return false;
            }
        }

        // Check piece-specific movement rules
        boolean isLakeMove = isLake(toRow, toCol);
        return piece.canMoveToTile(fromRow, fromCol, toRow, toCol, isLakeMove);
    }

    private void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        pieces[toRow][toCol] = pieces[fromRow][fromCol];
        pieces[fromRow][fromCol] = null;
        updateBoardDisplay();

        // Check win condition
        if (isDen(toRow, toCol)) {
            gameState.setGameEnded(true);
            JOptionPane.showMessageDialog(this, 
                (gameState.isBlueTeamTurn() ? "Blue" : "Red") + " team wins!");
        }
    }

    private void handleInitialPieceSelection(int row, int col) {
        if (pieces[row][col] == null) return;
        
        if (!gameState.isGameStarted()) {
            if (pieces[row][col].isBlueTeam() && bluePieceSelected == null) {
                bluePieceSelected = pieces[row][col];
            } else if (!pieces[row][col].isBlueTeam() && redPieceSelected == null) {
                redPieceSelected = pieces[row][col];
            }
            
            if (bluePieceSelected != null && redPieceSelected != null) {
                gameState.startGame(bluePieceSelected, redPieceSelected);
                JOptionPane.showMessageDialog(this, 
                    (gameState.isBlueTeamTurn() ? "Blue" : "Red") + " team goes first!");
            }
        }
    }

    private void highlightValidMoves(int row, int col) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (isValidMove(row, col, r, c)) {
                    // Make valid move highlights more visible
                    grid[r][c].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 1),
                        BorderFactory.createLineBorder(Color.YELLOW, 2)
                    ));
                }
            }
        }
    }

    private void clearSelection() {
        selectedPiece = null;
        selectedRow = -1;
        selectedCol = -1;
        
        // Clear highlights with null border
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c].setBorder(null);
            }
        }
        requestFocus(); // Maintain keyboard focus after clearing
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JungleKingBoard board = new JungleKingBoard();
            board.setVisible(true);
        });
    }
}
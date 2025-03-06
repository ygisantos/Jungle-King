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
    private ImageIcon hiddenIcon;
    private Piece[][] pieces;
    private Piece[][] originalPieces; // Add this field
    private GameState gameState;
    private Piece selectedPiece;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Piece bluePieceSelected;
    private Piece redPieceSelected;
    private JLabel turnLabel;

    public JungleKingBoard() {
        setTitle("Jungle King Game Board");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create turn indicator panel
        JPanel topPanel = new JPanel();
        turnLabel = new JLabel("Waiting for initial piece selection...");
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(turnLabel);
        add(topPanel, BorderLayout.NORTH);

        // Create game board panel
        JPanel boardPanel = new JPanel(new GridLayout(ROWS, COLS));
        add(boardPanel, BorderLayout.CENTER);

        landIcon = scaleImage("Assets/Board/land.png", 100, 100);
        lakeIcon = scaleImage("Assets/Board/lake.png", 100, 100);
        trapIcon = scaleImage("Assets/Board/trap.png", 100, 100);
        denIcon = scaleImage("Assets/Board/den.png", 100, 100);
        hiddenIcon = scaleImage("Assets/Board/hidden.png", 100, 100);

        grid = new JButton[ROWS][COLS];
        pieces = new Piece[ROWS][COLS];
        initializeBoard(boardPanel);
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

    private void initializeBoard(JPanel boardPanel) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = new JButton();
                grid[row][col].setIcon(getTileIcon(row, col));
                grid[row][col].setBorderPainted(true); // Changed to true
                grid[row][col].setFocusPainted(false);
                grid[row][col].setContentAreaFilled(false);
                boardPanel.add(grid[row][col]);
            }
        }
    }

    private void initializePieces() {
        // Blue team (top)
        pieces = new Piece[ROWS][COLS];
        originalPieces = new Piece[ROWS][COLS];

        // Initialize original positions
        originalPieces[0][0] = new Lion(true);
        originalPieces[0][2] = new Elephant(true);
        originalPieces[1][1] = new Cat(true);
        originalPieces[2][2] = new Wolf(true);
        originalPieces[4][2] = new Leopard(true);
        originalPieces[5][1] = new Dog(true);
        originalPieces[6][0] = new Tiger(true);
        originalPieces[6][2] = new Rat(true);
        
        // Red team (bottom)
        originalPieces[0][8] = new Tiger(false);
        originalPieces[1][7] = new Dog(false);
        originalPieces[2][6] = new Leopard(false);
        originalPieces[0][6] = new Rat(false);
        originalPieces[6][6] = new Elephant(false);
        originalPieces[6][8] = new Lion(false);
        originalPieces[5][7] = new Cat(false);
        originalPieces[4][6] = new Wolf(false);

        // Create randomized list of pieces
        java.util.List<Piece> allPieces = new java.util.ArrayList<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (originalPieces[row][col] != null) {
                    allPieces.add(originalPieces[row][col]);
                }
            }
        }

        // Randomize and place pieces
        java.util.Collections.shuffle(allPieces);
        int pieceIndex = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (!isLake(row, col) && !isDen(row, col) && pieceIndex < allPieces.size()) {
                    pieces[row][col] = allPieces.get(pieceIndex++);
                }
            }
        }

        updateBoardDisplayWithHiddenPieces();
    }

    private void updateBoardDisplayWithHiddenPieces() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                ImageIcon baseIcon = getTileIcon(row, col);
                if (pieces[row][col] != null) {
                    grid[row][col].setIcon(overlayIcons(baseIcon, hiddenIcon));
                } else {
                    grid[row][col].setIcon(baseIcon);
                }
            }
        }
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

        // Special case for Lion and Tiger
        if (piece instanceof Lion || piece instanceof Tiger) {
            if (isLakeJumpMove(fromRow, fromCol, toRow, toCol)) {
                return true;
            }
            if (isLake(toRow, toCol)) {
                return false; // Lions and Tigers cannot enter lakes
            }
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

    private boolean isLakeJumpMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Only allow vertical or horizontal jumps
        if (fromRow != toRow && fromCol != toCol) return false;
        
        // Check if destination has a piece that cannot be captured
        if (pieces[toRow][toCol] != null) {
            if (pieces[toRow][toCol].isBlueTeam() == pieces[fromRow][fromCol].isBlueTeam()) {
                return false; // Can't jump to space with own piece
            }
            // Check if piece can be captured (similar to normal capture rules)
            if (!pieces[fromRow][fromCol].canCapture(pieces[toRow][toCol])) {
                return false;
            }
        }
        
        // Check if jump is across lake
        if (fromRow == toRow) { // Horizontal jump
            // Only allow jumps from edge to edge of lake (columns 2 to 6)
            if ((fromCol == 2 && toCol == 6) || (fromCol == 6 && toCol == 2)) {
                // Check for rat in the path
                for (int col = 3; col <= 5; col++) {
                    if (pieces[fromRow][col] instanceof Rat) return false;
                }
                return isLake(fromRow, 4); // Verify it's jumping over lake
            }
        } else { // Vertical jump
            // Only allow jumps from edge to edge of lake (across rows)
            if (((fromRow == 0 && toRow == 3) || (fromRow == 3 && toRow == 0) ||
                 (fromRow == 3 && toRow == 6) || (fromRow == 6 && toRow == 3)) &&
                (fromCol >= 3 && fromCol <= 5)) { // Must be in lake columns
                // Check for rat in the path
                int minRow = Math.min(fromRow, toRow);
                int maxRow = Math.max(fromRow, toRow);
                for (int row = minRow + 1; row < maxRow; row++) {
                    if (pieces[row][fromCol] instanceof Rat) return false;
                }
                return isLake((fromRow + toRow)/2, fromCol); // Verify it's jumping over lake
            }
        }
        return false;
    }

    private void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        pieces[toRow][toCol] = pieces[fromRow][fromCol];
        pieces[fromRow][fromCol] = null;
        updateBoardDisplay();
        
        // Update turn indicator
        String currentTurn = gameState.isBlueTeamTurn() ? "Red" : "Blue";
        turnLabel.setText(currentTurn + "'s Turn");

        // Check win condition
        if (isDen(toRow, toCol)) {
            gameState.setGameEnded(true);
            turnLabel.setText((gameState.isBlueTeamTurn() ? "Blue" : "Red") + " team wins!");
            JOptionPane.showMessageDialog(this, 
                (gameState.isBlueTeamTurn() ? "Blue" : "Red") + " team wins!");
        }
    }

    private void handleInitialPieceSelection(int row, int col) {
        if (pieces[row][col] == null) return;
        
        if (!gameState.isGameStarted()) {
            if (pieces[row][col].isBlueTeam() && bluePieceSelected == null) {
                bluePieceSelected = pieces[row][col];
                // Hide the selected piece
                grid[row][col].setIcon(overlayIcons(getTileIcon(row, col), hiddenIcon));
            } else if (!pieces[row][col].isBlueTeam() && redPieceSelected == null) {
                redPieceSelected = pieces[row][col];
                // Hide the selected piece
                grid[row][col].setIcon(overlayIcons(getTileIcon(row, col), hiddenIcon));
            }
            
            if (bluePieceSelected != null && redPieceSelected != null) {
                // Reveal selected pieces
                determineFirstTurn();
            }
        }
    }

    private void determineFirstTurn() {
        // Show selected pieces to players
        String blueAnimal = bluePieceSelected.getClass().getSimpleName();
        String redAnimal = redPieceSelected.getClass().getSimpleName();
        
        // Determine who goes first based on piece rank
        boolean blueGoesFirst = bluePieceSelected.getRank() > redPieceSelected.getRank();
        
        JOptionPane.showMessageDialog(this, 
            "Blue selected: " + blueAnimal + "\n" +
            "Red selected: " + redAnimal + "\n\n" +
            (blueGoesFirst ? "Blue" : "Red") + " team goes first!");
        
        gameState.startGame(bluePieceSelected, redPieceSelected, blueGoesFirst);
        
        // Restore original piece positions
        pieces = new Piece[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                pieces[row][col] = originalPieces[row][col];
            }
        }
        
        turnLabel.setText((gameState.isBlueTeamTurn() ? "Blue" : "Red") + "'s Turn");
        updateBoardDisplay(); // Show pieces in their original positions
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
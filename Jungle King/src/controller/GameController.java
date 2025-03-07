package src.controller;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.scene.input.KeyEvent;
import java.awt.Color;
import src.model.GameModel;
import src.model.pieces.Lion;
import src.model.pieces.Piece;
import src.model.pieces.Rat;
import src.model.pieces.Tiger;
import src.view.GameView;

public class GameController extends JFrame{
    
    public Piece selectedPiece;
    public int selectedRow = -1;
    public int selectedCol = -1;
    public Piece bluePieceSelected;
    public Piece redPieceSelected;
    
    private GameView gameView;
    
    public void setGameView(GameView view) {
        this.gameView = view;
        
        bluePieceSelected = null;
        redPieceSelected = null;
        selectedPiece = null;
        selectedRow = -1;
        selectedCol = -1;
    }
    
    public GameController() {
        setFocusTraversalKeysEnabled(false);
    }
    
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        
        GameModel.pieces[toRow][toCol] = GameModel.pieces[fromRow][fromCol];
        GameModel.pieces[fromRow][fromCol] = null;
        gameView.updateBoardDisplay();
        
        
        String currentTurn = GameModel.isBlueTeamTurn ? "Red" : "Blue";
        GameModel.turnLabel.setText(currentTurn + "'s Turn");

        
        if (isDen(toRow, toCol)) {
            GameModel.setGameEnded(true);
            GameModel.turnLabel.setText((GameModel.isBlueTeamTurn() ? "Blue" : "Red") + " team wins!");
            JOptionPane.showMessageDialog(this, 
                (GameModel.isBlueTeamTurn() ? "Blue" : "Red") + " team wins!");
        }
    }

    public void highlightValidMoves(int row, int col) {
        for (int r = 0; r < GameModel.ROWS; r++) {
            for (int c = 0; c < GameModel.COLS; c++) {
                if (isValidMove(row, col, r, c)) {
                    
                    GameModel.grid[r][c].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 1),
                        BorderFactory.createLineBorder(Color.YELLOW, 2)
                    ));
                }
            }
        }
    }

    private void determineFirstTurn() {
        
        String blueAnimal = bluePieceSelected.getClass().getSimpleName();
        String redAnimal = redPieceSelected.getClass().getSimpleName();
        
        boolean blueGoesFirst = bluePieceSelected.getRank() > redPieceSelected.getRank();
        
        JOptionPane.showMessageDialog(this, 
            "Blue selected: " + blueAnimal + "\n" +
            "Red selected: " + redAnimal + "\n\n" +
            (blueGoesFirst ? "Blue" : "Green") + " team goes first!");
        
        GameModel.startGame(bluePieceSelected, redPieceSelected, blueGoesFirst);
        
        
        GameModel.pieces = new Piece[GameModel.ROWS][GameModel.COLS];
        for (int row = 0; row < GameModel.ROWS; row++) {
            for (int col = 0; col < GameModel.COLS; col++) {
                GameModel.pieces[row][col] = GameModel.originalPieces[row][col];
            }
        }
        
        GameModel.turnLabel.setText((GameModel.isBlueTeamTurn() ? "Blue" : "Red") + "'s Turn");
        gameView.updateBoardDisplay();
    }

    private void handleInitialPieceSelection(int row, int col) {
        if (GameModel.pieces[row][col] == null) return;
        
        if (!GameModel.isGameStarted()) {
            if (GameModel.pieces[row][col].isBlueTeam() && bluePieceSelected == null) {
                bluePieceSelected = GameModel.pieces[row][col];
                GameModel.grid[row][col].setIcon(gameView.overlayIcons(gameView.getTileIcon(row, col), GameModel.hiddenIcon));
            } else if (!GameModel.pieces[row][col].isBlueTeam() && redPieceSelected == null) {
                redPieceSelected = GameModel.pieces[row][col];
                GameModel.grid[row][col].setIcon(gameView.overlayIcons(gameView.getTileIcon(row, col), GameModel.hiddenIcon));
            }
            
            if (bluePieceSelected != null && redPieceSelected != null) {
                determineFirstTurn();
            }
        }
    }

    private boolean isLakeJumpMove(int fromRow, int fromCol, int toRow, int toCol) {
        
        if (fromRow != toRow && fromCol != toCol) return false;
        
        
        if (GameModel.pieces[toRow][toCol] != null) {
            if (GameModel.pieces[toRow][toCol].isBlueTeam() == GameModel.pieces[fromRow][fromCol].isBlueTeam()) {
                return false; 
            }
            
            if (!GameModel.pieces[fromRow][fromCol].canCapture(GameModel.pieces[toRow][toCol])) {
                return false;
            }
        }
        
        
        if (fromRow == toRow) { 
            
            if ((fromCol == 2 && toCol == 6) || (fromCol == 6 && toCol == 2)) {
                
                for (int col = 3; col <= 5; col++) {
                    if (GameModel.pieces[fromRow][col] instanceof Rat) return false;
                }
                return isLake(fromRow, 4); 
            }
        } else { 
            
            if (((fromRow == 0 && toRow == 3) || (fromRow == 3 && toRow == 0) ||
                 (fromRow == 3 && toRow == 6) || (fromRow == 6 && toRow == 3)) &&
                (fromCol >= 3 && fromCol <= 5)) { 
                
                int minRow = Math.min(fromRow, toRow);
                int maxRow = Math.max(fromRow, toRow);
                for (int row = minRow + 1; row < maxRow; row++) {
                    if (GameModel.pieces[row][fromCol] instanceof Rat) return false;
                }
                return isLake((fromRow + toRow)/2, fromCol); 
            }
        }
        return false;
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = GameModel.pieces[fromRow][fromCol];
        
        
        if (isOwnDen(toRow, toCol, piece.isBlueTeam())) {
            return false;
        }

        
        if (piece instanceof Lion || piece instanceof Tiger) {
            if (isLakeJumpMove(fromRow, fromCol, toRow, toCol)) {
                return true;
            }
            if (isLake(toRow, toCol)) {
                return false; 
            }
        }

        
        if (GameModel.pieces[toRow][toCol] != null) {
            if (GameModel.pieces[toRow][toCol].isBlueTeam() == piece.isBlueTeam()) {
                return false;
            }
            
            if (isTrap(toRow, toCol)) {
                return true;
            }
            
            if (!piece.canCapture(GameModel.pieces[toRow][toCol])) {
                return false;
            }
        }

        
        boolean isLakeMove = isLake(toRow, toCol);
        return piece.canMoveToTile(fromRow, fromCol, toRow, toCol, isLakeMove);
    }

    private void handleTileClick(int row, int col) {
        if (!GameModel.isGameStarted()) {
            handleInitialPieceSelection(row, col);
            return;
        }

        
        if (GameModel.pieces[row][col] != null && 
            GameModel.pieces[row][col].isBlueTeam() == GameModel.isBlueTeamTurn()) {
            gameView.clearSelection();
            selectedPiece = GameModel.pieces[row][col];
            selectedRow = row;
            selectedCol = col;
            gameView.highlightSelectedPiece(row, col);
            highlightValidMoves(row, col);
            requestFocus(); 
            return;
        }

        
        if (selectedPiece != null && isValidMove(selectedRow, selectedCol, row, col)) {
            movePiece(selectedRow, selectedCol, row, col);
            GameModel.toggleTurn();
            gameView.clearSelection();
        }
    }

    public boolean isLake(int row, int col) {
        return ((row >= 1 && row <= 2 || row >= 4 && row <= 5) && 
                (col >= 3 && col <= 5));
    }

    public boolean isTrap(int row, int col) {
        
        if (row == 2 && (col == 0 || col == 8)) return true;
        if (row == 3 && (col == 1 || col == 7)) return true;
        if (row == 4 && (col == 0 || col == 8)) return true;
        return false;
    }

    public boolean isDen(int row, int col) {
        return (row == 3 && (col == 0 || col == 8)); 
    }

    public boolean isOwnDen(int row, int col, boolean isBlueTeam) {
        
        return isDen(row, col) && (isBlueTeam ? (col == 0) : (col == 8));
    }

    public void addPieceSelectionListeners() {
        for (int row = 0; row < GameModel.ROWS; row++) {
            for (int col = 0; col < GameModel.COLS; col++) {
                final int r = row;
                final int c = col;
                GameModel.grid[row][col].addActionListener(e -> handleTileClick(r, c));
            }
        }
    }

    public void handleKeyPress(java.awt.event.KeyEvent e) {
        if (selectedPiece == null || !GameModel.isGameStarted()) return;

        int newRow = selectedRow;
        int newCol = selectedCol;

        switch (e.getKeyChar()) {
            case 'w': case 'W': newRow--; break;
            case 's': case 'S': newRow++; break;
            case 'a': case 'A': newCol--; break;
            case 'd': case 'D': newCol++; break;
            default: return;
        }

        if (newRow >= 0 && newRow < GameModel.ROWS && newCol >= 0 && newCol < GameModel.COLS) {
            if (isValidMove(selectedRow, selectedCol, newRow, newCol)) {
                movePiece(selectedRow, selectedCol, newRow, newCol);
                GameModel.toggleTurn();
                gameView.clearSelection();
            }
        }
    }

    
}

package src.model;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import src.model.pieces.Piece;
import src.view.GameView;

public class GameModel {
    public static boolean isGameStarted;
    public static boolean isGameEnded;
    public static boolean isBlueTeamTurn;
    
    public static Piece[][] pieces;
    public static Piece[][] originalPieces;

    public static final int ROWS = 7;
    public static final int COLS = 9;
    public static JButton[][] grid;
    public static ImageIcon landIcon;
    public static ImageIcon lakeIcon;
    public static ImageIcon trapIcon;
    public static ImageIcon denIcon;
    public static ImageIcon hiddenIcon;
    public static JLabel turnLabel;
    public static GameView view;

    public GameModel() {
        isGameStarted = false;
        isGameEnded = false;
        isBlueTeamTurn = true;
    }

    public static void startGame(Piece bluePiece, Piece redPiece, boolean blueTeamFirst) {
        isGameStarted = true;
        isBlueTeamTurn = blueTeamFirst;
    }

    public static boolean isGameStarted() {
        return isGameStarted;
    }

    public static boolean isGameEnded() {
        return isGameEnded;
    }

    public static void setGameEnded(boolean ended) {
        isGameEnded = ended;
    }

    public static boolean isBlueTeamTurn() {
        return isBlueTeamTurn;
    }

    public static void toggleTurn() {
        isBlueTeamTurn = !isBlueTeamTurn;
    }
}

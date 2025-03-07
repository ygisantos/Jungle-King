package src.model;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import src.controller.GameController;
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
    public static JButton restartBtn;
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

    public static void Restart(JFrame jFrame) {
        SwingUtilities.invokeLater(() -> {
            jFrame.dispose(); 
            GameModel.isGameStarted = false;
            GameModel.isGameEnded = false;;
            
            GameController controller = new GameController();
            GameView view = new GameView(controller);
            controller.setGameView(view); 
            GameModel.view = view;
            view.setVisible(true);
        });
    }

    public static void Start() {
        SwingUtilities.invokeLater(() -> {
            GameController controller = new GameController();
            view = new GameView(controller);
            controller.setGameView(view); 
            view.setVisible(true);
        });
    }
}

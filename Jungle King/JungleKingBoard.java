import javax.swing.*;
import src.model.GameModel;
import src.view.GameView;
import src.controller.GameController;

public class JungleKingBoard extends JFrame {
    public static GameView view;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameController controller = new GameController();
            view = new GameView(controller);
            controller.setGameView(view); // Add this line
            GameModel.view = view;
            view.setVisible(true);
        });
    }
}
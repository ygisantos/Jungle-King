import pieces.Piece;

public class GameState {
    private boolean isGameStarted;
    private boolean isGameEnded;
    private boolean isBlueTeamTurn;

    public GameState() {
        isGameStarted = false;
        isGameEnded = false;
        isBlueTeamTurn = true;
    }

    public void startGame(Piece bluePiece, Piece redPiece, boolean blueTeamFirst) {
        isGameStarted = true;
        isBlueTeamTurn = blueTeamFirst;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }

    public void setGameEnded(boolean ended) {
        isGameEnded = ended;
    }

    public boolean isBlueTeamTurn() {
        return isBlueTeamTurn;
    }

    public void toggleTurn() {
        isBlueTeamTurn = !isBlueTeamTurn;
    }
}

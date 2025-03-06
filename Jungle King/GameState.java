import pieces.Piece;

public class GameState {
    private boolean isBlueTeamTurn;
    private Piece selectedPiece;
    private boolean gameStarted;
    private boolean gameEnded;
    private Piece bluePieceSelected; // For initial piece selection
    private Piece redPieceSelected;  // For initial piece selection

    public GameState() {
        this.gameStarted = false;
        this.gameEnded = false;
    }

    public void startGame(Piece bluePiece, Piece redPiece) {
        this.bluePieceSelected = bluePiece;
        this.redPieceSelected = redPiece;
        this.isBlueTeamTurn = bluePiece.getRank() > redPiece.getRank();
        this.gameStarted = true;
    }

    // Getters and setters
    public boolean isBlueTeamTurn() { return isBlueTeamTurn; }
    public void toggleTurn() { isBlueTeamTurn = !isBlueTeamTurn; }
    public boolean isGameStarted() { return gameStarted; }
    public boolean isGameEnded() { return gameEnded; }
    public void setGameEnded(boolean ended) { this.gameEnded = ended; }
}

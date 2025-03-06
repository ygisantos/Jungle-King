package src.model.pieces;

public class Rat extends Piece {
    public Rat(boolean isBlueTeam) {
        super("Rat", 1, isBlueTeam, isBlueTeam ? "Jungle King/Assets/Pieces/blue_rat.png" : "Jungle King/Assets/Pieces/red_rat.png");
    }
    
    @Override
    public boolean canMoveToTile(int fromRow, int fromCol, int toRow, int toCol, boolean isLake) {
        // Rats can move anywhere including lakes
        return Math.abs(fromRow - toRow) + Math.abs(fromCol - toCol) == 1;
    }
}

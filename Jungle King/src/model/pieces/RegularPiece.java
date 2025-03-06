package src.model.pieces;

public class RegularPiece extends Piece {
    public RegularPiece(String name, int rank, boolean isBlueTeam, String imagePath) {
        super(name, rank, isBlueTeam, imagePath);
    }
    
    @Override
    public boolean canMoveToTile(int fromRow, int fromCol, int toRow, int toCol, boolean isLake) {
        // Regular pieces can only move one square orthogonally and can't enter lakes
        return !isLake && Math.abs(fromRow - toRow) + Math.abs(fromCol - toCol) == 1;
    }
}

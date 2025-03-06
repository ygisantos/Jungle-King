package src.model.pieces;

public abstract class LionTiger extends Piece {
    public LionTiger(String name, int rank, boolean isBlueTeam, String imagePath) {
        super(name, rank, isBlueTeam, imagePath);
    }
    
    @Override
    public boolean canMoveToTile(int fromRow, int fromCol, int toRow, int toCol, boolean isLake) {
        // Regular move
        if (Math.abs(fromRow - toRow) + Math.abs(fromCol - toCol) == 1) return true;
        
        // Lake jump move (implement lake jump logic in game controller)
        if (isLake) {
            return (fromRow == toRow && Math.abs(fromCol - toCol) == 3) || 
                   (fromCol == toCol && Math.abs(fromRow - toRow) == 2);
        }
        return false;
    }
}

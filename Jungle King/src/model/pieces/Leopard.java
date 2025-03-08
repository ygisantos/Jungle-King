package src.model.pieces;

public class Leopard extends RegularPiece {
    public Leopard(boolean isBlueTeam) {
        super("Leopard", 4, isBlueTeam, isBlueTeam ? "Assets/Pieces/blue_leopard.png" : "Assets/Pieces/red_leopard.png");
    }
}

package src.model.pieces;

public class Leopard extends RegularPiece {
    public Leopard(boolean isBlueTeam) {
        super("Leopard", 4, isBlueTeam, isBlueTeam ? "Jungle King/Assets/Pieces/blue_leopard.png" : "Jungle King/Assets/Pieces/red_leopard.png");
    }
}

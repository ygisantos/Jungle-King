package src.model.pieces;

public class Wolf extends RegularPiece {
    public Wolf(boolean isBlueTeam) {
        super("Wolf", 3, isBlueTeam, isBlueTeam ? "Jungle King/Assets/Pieces/blue_wolf.png" : "Jungle King/Assets/Pieces/red_wolf.png");
    }
}

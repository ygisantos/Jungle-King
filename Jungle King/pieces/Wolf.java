package pieces;

public class Wolf extends RegularPiece {
    public Wolf(boolean isBlueTeam) {
        super("Wolf", 3, isBlueTeam, isBlueTeam ? "Assets/Pieces/blue_wolf.png" : "Assets/Pieces/red_wolf.png");
    }
}

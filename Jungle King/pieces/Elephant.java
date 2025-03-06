package pieces;

public class Elephant extends RegularPiece {
    public Elephant(boolean isBlueTeam) {
        super("Elephant", 7, isBlueTeam, isBlueTeam ? "Assets/Pieces/blue_elephant.png" : "Assets/Pieces/red_elephant.png");
    }
}

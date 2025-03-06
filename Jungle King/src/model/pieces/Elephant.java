package src.model.pieces;

public class Elephant extends RegularPiece {
    public Elephant(boolean isBlueTeam) {
        super("Elephant", 7, isBlueTeam, isBlueTeam ? "Jungle King/Assets/Pieces/blue_elephant.png" : "Jungle King/Assets/Pieces/red_elephant.png");
    }
}

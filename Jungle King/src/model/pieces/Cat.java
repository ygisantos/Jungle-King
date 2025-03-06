package src.model.pieces;

public class Cat extends RegularPiece {
    public Cat(boolean isBlueTeam) {
        super("Cat", 1, isBlueTeam, isBlueTeam ? "Jungle King/Assets/Pieces/blue_cat.png" : "Jungle King/Assets/Pieces/red_cat.png");
    }
}

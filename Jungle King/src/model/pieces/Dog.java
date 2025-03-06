package src.model.pieces;

public class Dog extends RegularPiece {
    public Dog(boolean isBlueTeam) {
        super("Dog", 2, isBlueTeam, isBlueTeam ? "Jungle King/Assets/Pieces/blue_dog.png" : "Jungle King/Assets/Pieces/red_dog.png");
    }
}

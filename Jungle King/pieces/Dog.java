package pieces;

public class Dog extends RegularPiece {
    public Dog(boolean isBlueTeam) {
        super("Dog", 2, isBlueTeam, isBlueTeam ? "Assets/Pieces/blue_dog.png" : "Assets/Pieces/red_dog.png");
    }
}

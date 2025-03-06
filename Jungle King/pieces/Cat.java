package pieces;

public class Cat extends RegularPiece {
    public Cat(boolean isBlueTeam) {
        super("Cat", 1, isBlueTeam, isBlueTeam ? "Assets/Pieces/blue_cat.png" : "Assets/Pieces/red_cat.png");
    }
}

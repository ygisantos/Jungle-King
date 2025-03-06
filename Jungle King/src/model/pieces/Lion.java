package src.model.pieces;

public class Lion extends LionTiger {
    public Lion(boolean isBlueTeam) {
        super("Lion", 6, isBlueTeam, isBlueTeam ? "Jungle King/Assets/Pieces/blue_lion.png" : "Jungle King/Assets/Pieces/red_lion.png");
    }
}

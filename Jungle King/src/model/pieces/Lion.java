package src.model.pieces;

public class Lion extends LionTiger {
    public Lion(boolean isBlueTeam) {
        super("Lion", 6, isBlueTeam, isBlueTeam ? "Assets/Pieces/blue_lion.png" : "Assets/Pieces/red_lion.png");
    }
}

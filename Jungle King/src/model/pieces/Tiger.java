package src.model.pieces;

public class Tiger extends LionTiger {
    public Tiger(boolean isBlueTeam) {
        super("Tiger", 5, isBlueTeam, isBlueTeam ? "Jungle King/Assets/Pieces/blue_tiger.png" : "Jungle King/Assets/Pieces/red_tiger.png");
    }
}

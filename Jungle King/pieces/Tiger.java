package pieces;

public class Tiger extends LionTiger {
    public Tiger(boolean isBlueTeam) {
        super("Tiger", 5, isBlueTeam, isBlueTeam ? "Assets/Pieces/blue_tiger.png" : "Assets/Pieces/red_tiger.png");
    }
}

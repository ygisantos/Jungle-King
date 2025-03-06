package src.model.pieces;

import javax.swing.ImageIcon;

public abstract class Piece {
    protected String name;
    protected int rank;
    protected boolean isBlueTeam;
    protected ImageIcon icon;
    protected boolean isInTrap = false;
    
    public Piece(String name, int rank, boolean isBlueTeam, String imagePath) {
        this.name = name;
        this.rank = rank;
        this.isBlueTeam = isBlueTeam;
        this.icon = new ImageIcon(imagePath);
    }
    
    public boolean canCapture(Piece other) {
        if (other == null) return false;
        
        // Special case: Rat can capture Elephant
        if (this instanceof Rat && other instanceof Elephant) return true;
        
        // If opponent is in trap, any piece can capture it
        if (other.isInTrap) return true;
        
        return this.rank >= other.rank;
    }
    
    public abstract boolean canMoveToTile(int fromRow, int fromCol, int toRow, int toCol, boolean isLake);
    
    public void setInTrap(boolean inTrap) {
        this.isInTrap = inTrap;
    }
    
    // Getters
    public String getName() { return name; }
    public int getRank() { return rank; }
    public boolean isBlueTeam() { return isBlueTeam; }
    public ImageIcon getIcon() { return icon; }
}

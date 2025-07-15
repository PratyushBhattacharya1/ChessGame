package chess.game;

public abstract class PiecesActs {

    Position position;
    PieceName title;
    Color color;

    public Color getColor() {
        return this.color;
    } 

    public boolean isWhite() {
        return this.color == Color.W;
    }

    public boolean isBlack() {
        return this.color == Color.B;
    }

    @Override 
    public String toString() {
        return "" + this.color + this.title;
    }
    
}
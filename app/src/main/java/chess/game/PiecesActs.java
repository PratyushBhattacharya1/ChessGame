package chess.game;

public abstract class PiecesActs {

    Position position;
    Color color;

    public PiecesActs(Position position, Color color) {
        this.position = new Position(position);
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    } 

    public boolean isWhite() {
        return this.color == Color.W;
    }

    public boolean isBlack() {
        return this.color == Color.B;
    }

    public void move(Position p) {
        this.position.setPosition(p);
    }

    @Override 
    public String toString() {
        return "" + this.color + this.getClass();
    }
    
}
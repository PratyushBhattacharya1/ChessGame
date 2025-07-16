package chess.game;

public abstract class PiecesActs {

    Position position;
    Color color;

    public PiecesActs(Position position, Color color) {
        this.position = new Position(position);
        this.color = color;
    }

    public Position getPosition() {
        return this.position;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isWhite() {
        return this.color == Color.White;
    }

    public boolean isBlack() {
        return this.color == Color.Black;
    }

    public void move(Position p) {
        this.position.setPosition(p);
    }
}
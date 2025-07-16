package chess.game;

public interface Piece {

    public Position getPosition();

    public Color getColor();

    public boolean isWhite();

    public boolean isBlack();

    public boolean isValidMove(Position targetPosition, Piece board[][], int turnCount);
    
    public void move(Position p);
}
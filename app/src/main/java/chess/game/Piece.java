package chess.game;

public interface Piece {

    public Color getColor();

    public boolean isWhite();

    public boolean isBlack();

    boolean isValidMove(Position targetPosition, Piece board[][], int turnCount);
    
}
package chess.game;

import java.util.List;

public interface Piece {

    public Position getPosition();

    public Color getColor();

    public boolean isWhite();

    public boolean isBlack();

    public boolean isPseudoLegalMove(Position targetPosition, Piece board[][], int turnCount);
    
    public void move(Position p);

    public List<Position> generatePseudoLegalMoves(Piece board[][]);
}
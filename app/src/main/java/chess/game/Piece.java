package chess.game;

import java.util.Set;

public interface Piece {

    public Position getPosition();

    public Color getColor();

    public boolean isWhite();

    public boolean isBlack();

    public boolean isPseudoLegalMove(Position targetPosition, MoveContext moveContext);
    
    public void move(Position p, MoveContext moveContext);

    public Set<Position> generatePseudoLegalMoves(MoveContext moveContext);
}
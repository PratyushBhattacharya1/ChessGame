package chess.game;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a {@code Rook} chess piece.
 * <p>
 * The {@code Rook} is a sliding piece that moves any number of vacant squares in a horizontal or vertical direction.
 * This class extends {@link SlidingPieces} and implements the {@link Piece} interface.
 * </p>
 *
 * <p>
 * The {@code Rook} tracks whether it has moved, which is important for castling logic.
 * </p>
 *
 * @author Pratyush
 */
public class Rook extends SlidingPieces {

    public static final int MAX_MOVES = 14;
    public static final Title TITLE = Title.R;

    private boolean hasMoved = false;

    public Rook(Position position, Color color) {
        super(position, color);
    }

    @Override
    public boolean isPseudoLegalMove(Position targetPosition, MoveContext mContext) {
        return super.isValidRookMove(targetPosition, mContext.getBoard());
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void move(Position p, MoveContext mContext) {
        super.move(p, mContext);
        hasMoved = true;
    }

    @Override
    public Set<Position> generatePseudoLegalMoves(MoveContext mContext) {
        Set<Position> moves = new HashSet<>(MAX_MOVES);
        var board = mContext.getBoard();

        for (Direction dr : ORTHOGONALS) {
            this.processLine(dr.getRowDelta(), dr.getColDelta(), board, moves, (row, column, piece) -> {
                if (piece == null) moves.add(new Position(row, column));
                return moves;
            });
        }

        return moves;
    }
    
}

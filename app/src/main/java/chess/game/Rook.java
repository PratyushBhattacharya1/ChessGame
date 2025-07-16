package chess.game;

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
public class Rook extends SlidingPieces implements Piece {

    private boolean hasMoved = false;

    public Rook(Position position, Color color) {
        super(position, color);
    }

    @Override
    public boolean isValidMove(Position targetPosition, Piece[][] board, int turnCount) {
        return super.isValidRookMove(targetPosition, board);
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void move(Position p) {
        super.move(p);
        hasMoved = true;
    }
    
}

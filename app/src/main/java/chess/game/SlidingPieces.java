package chess.game;

import java.util.EnumSet;

/**
 * Abstract class representing chess pieces that move by sliding across the board,
 * such as rooks, bishops, and queens. Provides utility methods to validate
 * rook-like and bishop-like moves, ensuring that the path between the current
 * position and the target position is unobstructed.
 *
 * <p>Extends {@link PieceBehaviors} to inherit common piece behavior.</p>
 *
 * @author Pratyush
 */
public abstract class SlidingPieces extends PieceBehaviors {

    protected static final EnumSet<Direction> DIAGONALS = EnumSet.of(
        Direction.UP_LEFT, Direction.UP_RIGHT,
        Direction.DOWN_LEFT, Direction.DOWN_RIGHT
    );

    protected static final EnumSet<Direction> ORTHOGONALS = EnumSet.of(
        Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
    );

    /**
     * Constructs a new SlidingPieces object with the specified position and color.
     *
     * @param position the initial position of the sliding piece on the chessboard
     * @param color the color of the sliding piece (e.g., white or black)
     */
    public SlidingPieces(Position position, Color color) {
        super(position, color);
    }

    /**
     * Checks if the rook's move from its current position to the specified target position is valid.
     * <p>
     * A valid rook move must be either strictly horizontal or vertical, and all squares between
     * the current position and the target position must be empty (i.e., not occupied by any piece).
     * The method does not check for capturing logic or whether the target square contains a piece.
     *
     * @param targetPosition the position to which the rook is attempting to move
     * @param board the current state of the chess board, represented as a 2D array of {@code Piece} objects
     * @return {@code true} if the move is valid for a rook (clear path and correct direction), {@code false} otherwise
     */
    protected boolean isValidRookMove(Position targetPosition, Piece board[][]) {
        int r = this.position.getRow(), 
            c = this.position.getColumn(), 
            newR = targetPosition.getRow(), 
            newC = targetPosition.getColumn();

        if (!(r == newR ^ c == newC)) return false;
        
        return isPathClear(board, r, c, newR, newC);
    }

    private boolean isPathClear(Piece[][] board, int r, int c, int newR, int newC) {
        if (this.isPositionPieceSameColor(board, newR, newC)) return false;

        int rowDelta = Integer.signum(newR - r);
        int colDelta = Integer.signum(newC - c);

        if (this.processLine(rowDelta, colDelta, board, false, (row, column, piece) -> {
            if ( ((rowDelta > 0)? row >= newR : row <= newR) 
                && ((colDelta > 0)? column >= newC : column <= newC))
                // If we've passed the target square, stop processing (don't care if it's occupied)
                return false;
            return piece != null;
        })) return false;

        return true;
    }

    /**
     * Checks if the bishop can move from its current position to the specified target position
     * according to chess rules. The move is valid if the target position is on the same diagonal
     * and all squares between the current and target positions are unoccupied.
     *
     * @param targetPosition The position to which the bishop is attempting to move.
     * @param board The current state of the chess board, represented as a 2D array of {@code Piece} objects.
     * @return {@code true} if the move is a valid bishop move (diagonal and unobstructed), {@code false} otherwise.
     */
    protected boolean isValidBishopMove(Position targetPosition, Piece board[][]) {
        int r = this.position.getRow(), 
            c = this.position.getColumn(), 
            newR = targetPosition.getRow(), 
            newC = targetPosition.getColumn();

        // Absolute value difference between tiles must be equal to each other
        if (Math.abs(r - newR) != Math.abs(c - newC) || targetPosition.equals(this.position)) return false;

        return isPathClear(board, r, c, newR, newC);
    }
}

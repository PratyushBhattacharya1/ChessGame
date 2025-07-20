package chess.game;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a rook chess piece, which is a type of sliding piece.
 * The rook can move any number of squares along a rank or file, but cannot leap over other pieces.
 * Tracks whether the rook has moved, which is relevant for castling.
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Overrides pseudo-legal move generation for rook movement rules.</li>
 *   <li>Tracks if the rook has moved, used for castling logic.</li>
 *   <li>Generates all possible pseudo-legal moves along orthogonal directions.</li>
 * </ul>
 *
 * @see SlidingPieces
 * @see Position
 * @see MoveContext
 */
public class Rook extends SlidingPieces {

    /**
     * The maximum number of moves a rook can make on a standard chessboard.
     * Since a rook can move up to 7 squares in any of the four directions (horizontal and vertical),
     * the total possible moves from the center of the board is 14 (7 in each direction, two directions per axis).
     */
    public static final int MAX_MOVES = 14;
    // public static final Title TITLE = Title.R;

    /**
     * Indicates whether the rook has moved from its original position.
     * This flag is typically used to determine castling eligibility.
     */
    private boolean hasMoved = false;

    /**
     * Constructs a new Rook chess piece with the specified position and color.
     * Initializes the piece's title to {@code Title.R}.
     *
     * @param position the initial position of the rook on the chessboard
     * @param color the color of the rook (e.g., black or white)
     */
    public Rook(Position position, Color color) {
        super(position, color);
        this.TITLE = Title.R;
    }

    /**
     * Determines if the move to the specified target position is a pseudo-legal move for the rook.
     * A pseudo-legal move is one that follows the movement rules for a rook, but does not consider checks.
     *
     * @param targetPosition the position to which the rook is attempting to move
     * @param mContext the context of the move, including the current state of the board
     * @return {@code true} if the move is pseudo-legal for a rook, {@code false} otherwise
     */
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

package chess.game;
/**
 * Represents a {@code Queen} chess piece, which can move any number of squares along a rank, file, or diagonal.
 * Inherits movement logic from {@code SlidingPieces} for rook and bishop moves.
 *
 * <p>
 * The {@code Queen} can move to any square along the row, column, or diagonal unless blocked by another piece.
 * The move is invalid if the target square is occupied by a piece of the same color.
 * </p>
 *
 * @see SlidingPieces
 * @see Piece
 */
public class Queen extends SlidingPieces implements Piece {

    /**
     * Constructs a {@code Queen} chess piece with the specified position and color.
     *
     * @param position the initial position of the {@code Queen} on the chessboard
     * @param color the color of the {@code Queen} (e.g., white or black)
     */
    public Queen(Position position, Color color) {
        super(position, color);
    }

    /**
     * Determines if the Queen can move to the specified target position on the board.
     * A move is valid if:
     * <ul>
     *   <li>The target position is not occupied by a piece of the same color.</li>
     *   <li>The target position is not the current position.</li>
     *   <li>The move follows the rules of either a rook or a bishop (i.e., any number of squares along a rank, file, or diagonal, without jumping over other pieces).</li>
     * </ul>
     *
     * @param targetPosition The position to which the Queen is attempting to move.
     * @param board The current state of the chess board.
     * @param turnCount The current turn count in the game (may be used for special rules).
     * @return {@code true} if the move is valid for a Queen; {@code false} otherwise.
     */
    @Override
    public boolean isValidMove(Position targetPosition, Piece[][] board, int turnCount) {
        return super.isValidRookMove(targetPosition, board) || super.isValidBishopMove(targetPosition, board);
    }
    
    @Override
    public String toString() {
        return "" + (this.isWhite() ? "W" : "B") + (this.isWhite() ? "Q" : "Q");
    }
}

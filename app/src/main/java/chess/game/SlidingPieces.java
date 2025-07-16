package chess.game;

/**
 * Abstract class representing chess pieces that move by sliding across the board,
 * such as rooks, bishops, and queens. Provides utility methods to validate
 * rook-like and bishop-like moves, ensuring that the path between the current
 * position and the target position is unobstructed.
 *
 * <p>Extends {@link PiecesActs} to inherit common piece behavior.</p>
 *
 * @author Pratyush
 */
public abstract class SlidingPieces extends PiecesActs {

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
        // Get position details
        int r = this.position.getRow(), 
            c = this.position.getColumn(), 
            newR = targetPosition.getRow(), 
            newC = targetPosition.getColumn();

        // If either the column XOR row is unchanged
        if (!(r == newR ^ c == newC)) return false;
        
        // Target position cannot be the same color
        if (board[newR][newC] != null && board[newR][newC].getColor() == this.color) return false;

        int rowStep = 0;
        int colStep = 0;

        if (r == newR) {
            rowStep = 0;
            colStep = (newC > c) ? 1 : -1;
        } else if (c == newC) {
            rowStep = (newR > r) ? 1 : -1;
            colStep = 0;
        }
        int steps = (r == newR) ? Math.abs(newC - c) : Math.abs(newR - r);

        for (int i = 1; i < steps; i++) {
            if (board[r + i * rowStep][c + i * colStep] != null) return false;
        }

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
        // Get position details
        int r = this.position.getRow(), 
            c = this.position.getColumn(), 
            newR = targetPosition.getRow(), 
            newC = targetPosition.getColumn();

        // Absolute value difference between tiles must be equal to each other
        if (Math.abs(r - newR) != Math.abs(c - newC) || targetPosition.equals(this.position)) return false;

        int rowStep = (newR > r) ? 1 : -1;
        int colStep = (newC > c) ? 1 : -1;
        int steps = Math.abs(newR - r);
        for (int i = 1; i < steps; i++) {
            if (board[r + i * rowStep][c + i * colStep] != null) return false;
        }

        if (board[newR][newC] != null && board[newR][newC].getColor() == this.color) return false;
        
        return true;
    }
}

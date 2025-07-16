package chess.game;

/**
 * Represents a pawn chess piece.
 */
public class Pawn extends PiecesActs implements Piece {

    private static final int BLACK_ONE_SQUARE_UP = 2;
    private static final int WHITE_ONE_SQUARE_UP = 5;
    private static final int BLACK_TWO_SQUARES_UP = 3;
    private static final int WHITE_TWO_SQUARES_UP = 4;
    private static final int BLACK_PAWN_ROW = 1;
    private static final int WHITE_PAWN_ROW = 6;
    public static final int DEFAULT_EN_PASSANT_TURN_VALUE = 0;
    /**
     * The turn count when this pawn can perform an en passant capture.
     */
    private int enPassantTurn = DEFAULT_EN_PASSANT_TURN_VALUE;

    /**
     * Constructs a {@code Pawn} chess piece at the specified position and color.
     *
     * @param position the initial position of the pawn
     * @param color    the color of the pawn (either {@link Color#WHITE} or {@link Color#BLACK})
     */
    public Pawn(Position position, Color color) {
        super(position, color);
    }

    /**
     * Returns the turn count when this pawn can perform an en passant capture.
     *
     * @return the turn count for en passant
     */
    public int getEnPassantTurn() {
        return this.enPassantTurn;
    }

    /**
     * Sets the turn count when this pawn can perform an en passant capture.
     *
     * @param turnCount the turn count for en passant
     */
    public void setEnPassantTurn(int turnCount) {
        this.enPassantTurn = turnCount;
    }

    /**
     * Checks if the move to the target position is valid for this pawn.
     *
     * @param targetPosition the target position
     * @param board          the current game board
     * @param turnCount      the current turn count
     * @return {@code true} if the move is valid, {@code false} otherwise
     */
    public boolean isValidMove(Position targetPosition, Piece[][] board, int turnCount) {
        // Get position details
        int r = this.position.getRow(),
            c = this.position.getColumn(),
            newR = targetPosition.getRow(),
            newC = targetPosition.getColumn();

        // Handle initial two-square move
        if (this.isBlack() && r == BLACK_PAWN_ROW && newR == BLACK_TWO_SQUARES_UP && c == newC 
            && board[BLACK_ONE_SQUARE_UP][c] == null && board[BLACK_TWO_SQUARES_UP][c] == null) {
            return true;
        }
        if (this.isWhite() && r == WHITE_PAWN_ROW && newR == WHITE_TWO_SQUARES_UP && c == newC 
            && board[WHITE_ONE_SQUARE_UP][c] == null && board[WHITE_TWO_SQUARES_UP][c] == null) {
            return true;
        }


        // Handle single square move
        if (this.isBlack() && newR == r + 1 && c == newC && board[newR][c] == null) return true;
        if (this.isWhite() && newR == r - 1 && c == newC && board[newR][c] == null) return true;

        // Handle diagonal capture
        if (this.isBlack() && newR == r + 1 && Math.abs(newC - c) == 1 
            && board[newR][newC] != null && board[newR][newC].getColor() != this.color) 
                return true;
        if (this.isWhite() && newR == r - 1 
            && Math.abs(newC - c) == 1 && board[newR][newC] != null 
            && board[newR][newC].getColor() != this.color) 
                return true;


        // Handle enPassent
        // If very specific conditions for en passant for black have been met
        if (this.isBlack() && r == WHITE_TWO_SQUARES_UP && newR == WHITE_ONE_SQUARE_UP 
            && Math.abs(newC - c) == 1 && board[newR][newC] == null && board[r][newC] != null) {

            Piece piece = board[r][newC];
            // Verify the adjacent piece is a pawn
            if (piece instanceof Pawn) {
                Pawn pawn = (Pawn) piece;
                // Allow enPassent if black is performing en passant on the same turn as white
                if (pawn.getEnPassantTurn() == turnCount) return true;
            }
        } else if (this.isWhite() && r == BLACK_TWO_SQUARES_UP && newR == BLACK_ONE_SQUARE_UP 
            && Math.abs(newC - c) == 1 && board[newR][newC] == null && board[r][newC] != null) {

            Piece piece = board[r][newC];
            if (piece instanceof Pawn) {
                Pawn pawn = (Pawn) piece;
                // Same logic except allow white to do en passant only if the black pawn pushed two squares one turn ago
                if (pawn.getEnPassantTurn() - 1 == turnCount) return true;
            }
        } 

        // If none of the conditions are met, the move is invalid
        return false;
    }

    @Override
    public String toString() {
        return (this.isWhite() ? "W" : "B") + (this.isWhite() ? "P" : "P");
    }

    public boolean isPushingTwoSquares(Position targetPosition, Piece[][] board) {
        // Get position details
        int r = this.position.getRow(),
            c = this.position.getColumn(),
            newR = targetPosition.getRow(),
            newC = targetPosition.getColumn();


        // Handle initial two-square move
        if (this.isBlack() && r == BLACK_PAWN_ROW && newR == BLACK_TWO_SQUARES_UP && c == newC 
            && board[BLACK_ONE_SQUARE_UP][c] == null && board[BLACK_TWO_SQUARES_UP][c] == null) {
            return true;
        }
        if (this.isWhite() && r == WHITE_PAWN_ROW && newR == WHITE_TWO_SQUARES_UP && c == newC 
            && board[WHITE_ONE_SQUARE_UP][c] == null && board[WHITE_TWO_SQUARES_UP][c] == null) {
            return true;
        }


        return false;
    }
}

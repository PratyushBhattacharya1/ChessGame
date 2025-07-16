package chess.game;

/**
 * Represents a pawn chess piece.
 */
public class Pawn extends PiecesActs implements Piece {

    private static final int WHITE_PAWN_ROW = 6;
    /**
     * The turn count when this pawn can perform an en passant capture.
     */
    private int enPassentTurn = 0;

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
    public int getEnPassentTurn() {
        return this.enPassentTurn;
    }

    /**
     * Sets the turn count when this pawn can perform an en passant capture.
     *
     * @param turnCount the turn count for en passant
     */
    public void setEnPassentTurn(int turnCount) {
        this.enPassentTurn = turnCount;
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
        if (this.isWhite() && r == WHITE_PAWN_ROW && newR == 4 && c == newC && board[5][c] == null && board[6][c] == null) {
            this.setEnPassentTurn(turnCount);
            return true;
        }
        if (this.isBlack() && r == 1 && newR == 3 && c == newC && board[2][c] == null && board[3][c] == null) {
            this.setEnPassentTurn(turnCount);
            return true;
        }

        // Handle single square move
        if (this.isBlack() && newR == r + 1 && c == newC && board[newR][c] == null) return true;
        if (this.isWhite() && newR == r - 1 && c == newC && board[newR][c] == null) return true;

        // Handle diagonal capture
        if (this.isBlack() && newR == r + 1 && Math.abs(newC - c) == 1 
            && board[newR][newC] != null && board[newR][newC].getColor() != this.color) return true;
        if (this.isWhite() && newR == r - 1 
            && Math.abs(newC - c) == 1 && board[newR][newC] != null && board[newR][newC].getColor() != this.color) return true;


        // Handle enPassent
        // If very specific conditions for en passent for black have been met
        if (this.isBlack() && r == 5 && newR == WHITE_PAWN_ROW && Math.abs(newC - c) == 1 && board[newR][newC] == null && board[r][newC] != null) {
            Piece piece = board[r][newC];
            // Verify the adjacent piece is a pawn
            if (piece instanceof Pawn) {
                Pawn pawn = (Pawn) piece;
                // Allow enPassent if black is performing enPassent on the same turn as white
                if (pawn.getEnPassentTurn() == turnCount) return true;
            }
        } else if (this.isWhite() && r == 2 && newR == 1 && Math.abs(newC - c) == 1 && board[newR][newC] == null && board[r][newC] != null) {
            Piece piece = board[r][newC];
            if (piece instanceof Pawn) {
                Pawn pawn = (Pawn) piece;
                // Same logic except allow white to do enPassent only if the black pawn pushed two squares one turn ago
                if (pawn.getEnPassentTurn() - 1 == turnCount) return true;
            }
        } 

        // If none of the conditions are met, the move is invalid
        return false;
    }

    @Override
    public String toString() {
        return "" + (this.isWhite() ? "W" : "B") + (this.isWhite() ? "P" : "P");
    }

    public void enPassentMove(Position position, int turnColor) {
        this.position.setPosition(position);
    }
}

package chess.game;

import java.util.List;

/**
 * Represents a pawn chess piece.
 */
public class Pawn extends PieceBehaviors {

    private final int PAWN_ROW = (this.isWhite())? 6 : 1;
    private final int OPP_PAWN_ROW = (this.isWhite())? 1 : 6;
    
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
    public boolean isPseudoLegalMove(Position targetPosition, Piece[][] board, int turnCount) {
        // Get position details
        int r = this.position.getRow(),
            c = this.position.getColumn(),
            newR = targetPosition.getRow(),
            newC = targetPosition.getColumn();

        int dr = (this.isBlack())? 1 : -1;
        int oneUp = r + dr, twoUp = oneUp + dr;

        // Handle initial two-square move
        if (r == this.PAWN_ROW && newR == twoUp && c == newC && board[oneUp][c] == null 
            && board[twoUp][c] == null) 
                return true;


        // Handle single square move
        if (newR == oneUp && c == newC && board[newR][c] == null) return true;

        // Handle diagonal capture
        if (newR == oneUp && Math.abs(newC - c) == 1 && board[newR][newC] != null 
            && board[newR][newC].getColor() != this.color) 
                return true;


        int oppOnePawn = OPP_PAWN_ROW - dr, oppTwoPawn = oppOnePawn + dr;

        // Handle en passant if very specific conditions are met
        if (r == oppTwoPawn && newR == oppOnePawn && Math.abs(newC - c) == 1 
            && board[newR][newC] == null && board[r][newC] != null) {

            Piece piece = board[r][newC];
            // Verify the adjacent piece is a pawn
            if (piece.getColor() != this.color && piece instanceof Pawn) {
                // Allow en passant if same turn
                if (((Pawn)piece).getEnPassantTurn() - ((this.isBlack())? 0 : 1) == turnCount) return true;
            }
        }

        // If none of the conditions are met, the move is invalid
        return false;
    }

    @Override
    public String toString() {
        return (this.isWhite() ? "W" : "B") + (this.isWhite() ? "P" : "P");
    }

    @Override
    public List<Position> generatePseudoLegalMoves(Piece[][] board) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generatePseudoLegalMoves'");
    }

    // public boolean isPushingTwoSquares(Position targetPosition, Piece[][] board) {
    //     // Get position details
    //     int r = this.position.getRow(),
    //         c = this.position.getColumn(),
    //         newR = targetPosition.getRow(),
    //         newC = targetPosition.getColumn();


    //     // Handle initial two-square move
    //     if (this.isBlack() && r == BLACK_PAWN_ROW && newR == BLACK_TWO_SQUARES_UP && c == newC 
    //         && board[BLACK_ONE_SQUARE_UP][c] == null && board[BLACK_TWO_SQUARES_UP][c] == null) {
    //         return true;
    //     }
    //     if (this.isWhite() && r == WHITE_PAWN_ROW && newR == WHITE_TWO_SQUARES_UP && c == newC 
    //         && board[WHITE_ONE_SQUARE_UP][c] == null && board[WHITE_TWO_SQUARES_UP][c] == null) {
    //         return true;
    //     }


    //     return false;
    // }
}

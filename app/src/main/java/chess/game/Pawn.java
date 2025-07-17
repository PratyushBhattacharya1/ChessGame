package chess.game;

import java.util.ArrayList;
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
        this.title = Title.P;
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
    public boolean isPseudoLegalMove(Position targetPosition, MoveContext mContext) {
        // Get position details
        int r = this.position.getRow(),
            c = this.position.getColumn(),
            newR = targetPosition.getRow(),
            newC = targetPosition.getColumn();

        int dr = (this.isBlack())? 1 : -1;
        int oneUp = r + dr, twoUp = oneUp + dr;

        Piece[][] board = mContext.getBoard();
        int turnCount = mContext.getTurnCount();

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
            if (piece.getColor() != this.color && piece instanceof Pawn) {
                // Allow en passant if same turn
                if (((Pawn)piece).getEnPassantTurn() - ((this.isBlack())? 0 : 1) == turnCount) return true;
            }
        }

        // If none of the conditions are met, the move is invalid
        return false;
    }

    @Override
    public List<Position> generatePseudoLegalMoves(MoveContext mContext) {
        List<Position> moves = new ArrayList<>();

        int dr = (this.isBlack())? 1 : -1, c = this.position.getColumn();
        int upOne = this.position.getRow() + dr;

        Position oneUp = new Position(this.position.getRow() + dr, c);
        Position twoUp = new Position(upOne + dr, c);
        Position oneLeft = new Position(oneUp);
        Position oneRight = new Position(oneUp);
        if (this.isPseudoLegalMove(oneUp, mContext)) moves.add(oneUp);
        if (this.isPseudoLegalMove(twoUp, mContext)) moves.add(twoUp);
        if (this.isPseudoLegalMove(oneLeft, mContext)) moves.add(oneLeft);
        if (this.isPseudoLegalMove(oneRight, mContext)) moves.add(oneRight);

        return moves;
    }

    @Override
    public void move(Position p, MoveContext mContext) {
        super.move(p, mContext);
        this.enPassantTurn = mContext.getTurnCount();
    }
}

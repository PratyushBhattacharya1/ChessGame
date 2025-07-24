package chess.game;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a pawn chess piece.
 */
public class Pawn extends PieceBehaviors {

    public static final int MAX_MOVES = 4;
    // public static final Title TITLE = Title.P;

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
        this.TITLE = Title.P;
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
        if (this.isPushingTwoSquares(targetPosition, mContext)) return true;

        // Handle single square move
        if (newR == oneUp && c == newC && board[newR][c] == null) return true;

        // Handle diagonal capture
        if (newR == oneUp && Math.abs(newC - c) == 1 && board[newR][newC] != null 
            && board[newR][newC].getColor() != this.color) 
                return true;


        // int oppOnePawn = OPP_PAWN_ROW - dr, oppTwoPawn = oppOnePawn - dr;

        // // Handle en passant if very specific conditions are met
        // if (r == oppTwoPawn && newR == oppOnePawn && Math.abs(newC - c) == 1 
        //     && board[newR][newC] == null && board[r][newC] != null) {

        //     Piece piece = board[r][newC];
        //     if (piece.getColor() != this.color && piece instanceof Pawn) {
        //         // Allow en passant if same turn
        //         if (((Pawn)piece).getEnPassantTurn() == turnCount - ((this.isBlack())? 0 : 1)) return true;
        //     }
        // }

        return this.isEnPassant(targetPosition, mContext);
    }

    @Override
    public Set<Position> generatePseudoLegalMoves(MoveContext mContext) {
        Set<Position> moves = new HashSet<>(MAX_MOVES);

        int dr = (this.isBlack())? 1 : -1, c = this.position.getColumn();
        int upOne = this.position.getRow() + dr;

        addToList(mContext, moves, upOne, c);
        addToList(mContext, moves, upOne + dr, c);
        addToList(mContext, moves, upOne, c - 1);
        addToList(mContext, moves, upOne, c + 1);

        return moves;
    }

    private void addToList(MoveContext mContext, Set<Position> moves, int row, int column) {
        if (Position.isValidPosition(row, column)) {
            Position pos = new Position(row, column);
            if (this.isPseudoLegalMove(pos, mContext)) moves.add(pos);
        }
    }

    @Override
    public void move(Position p, MoveContext mContext) {
        if (this.isPushingTwoSquares(p, mContext)) this.enPassantTurn = mContext.getTurnCount();
        if (this.isEnPassant(p, mContext)) mContext.getBoard()[this.position.getRow()][p.getColumn()] = null;
        super.move(p, mContext);
        // System.out.println("Pawn moved to " + p + " with en passant turn: " + this.enPassantTurn);
    }

    private boolean isPushingTwoSquares(Position targetPosition, MoveContext mContext) {
        int r = this.position.getRow();
        int c = this.position.getColumn();
        int newR = targetPosition.getRow();
        int newC = targetPosition.getColumn();

        var board = mContext.getBoard();
        int dr = (this.isBlack())? 1 : -1;
        int oneUp = r + dr, twoUp = oneUp + dr;
        
        if (r == PAWN_ROW && newR == twoUp && c == newC && board[oneUp][c] == null 
            && board[twoUp][c] == null) 
                return true;

        return false;
    }

    private boolean isEnPassant(Position targetPosition, MoveContext mContext) {
        int r = this.position.getRow();
        int c = this.position.getColumn();
        int newR = targetPosition.getRow();
        int newC = targetPosition.getColumn();

        var board = mContext.getBoard();
        int dr = (this.isBlack())? 1 : -1;
        int oppOnePawn = OPP_PAWN_ROW - dr;

        if (r == oppOnePawn - dr && newR == oppOnePawn && Math.abs(newC - c) == 1 
            && board[newR][newC] == null && board[r][newC] != null) {
            Piece piece = board[r][newC];
            if (piece.getColor() != this.color && piece instanceof Pawn) {
                return ((Pawn)piece).getEnPassantTurn() == mContext.getTurnCount() - ((this.isBlack())? 0 : 1);
            }
        }
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        var pawn = new Pawn(this.getPosition(), this.getColor());
        pawn.setEnPassantTurn(this.getEnPassantTurn());
        return pawn;
    }
}

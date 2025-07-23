package chess.game;

import java.util.HashSet;
import java.util.Set;

public class King extends SlidingPieces {

    public static final int MAX_MOVES = 8;
    // public static final Title TITLE = Title.K;

    // private static final Position[] CASTLE_POSITION_WHITE = 
        // {new Position(7, 2), new Position(7, 6)};
        
    // private static final Position[] CASTLE_POSITION_BLACK = 
        // {new Position(0, 2), new Position(0, 6)};

    public final Position[] CASTLE_POSITION = (this.isWhite()) 
        ? new Position[]{new Position(7, 2), new Position(7, 6)} : 
        new Position[]{new Position(0, 2), new Position(0, 6)};

    private boolean hasMoved = false;

    public King(Position position, Color color) {
        super(position, color);
        this.TITLE = Title.K;
    }

    @Override
    public boolean isPseudoLegalMove(Position targetPosition, MoveContext mContext) {
        int r = this.position.getRow(),
            c = this.position.getColumn(),
            newR = targetPosition.getRow(),
            newC = targetPosition.getColumn();

        var board = mContext.getBoard();

        // if (this.canCastle(targetPosition, mContext)) return true;

        // Return false if invalid pseudo legal king move 
        if (Math.abs(r - newR) > 1 || Math.abs(c - newC) > 1 || (r == newR && c == newC)
            || this.isPositionPieceSameColor(board, newR, newC))
            return false;

        // Check all around the king for another king
        for (Direction dir : Direction.values()) {
            int dr = newR + dir.getRowDelta();
            int dc = newC + dir.getColDelta();

            if (Position.isValidPosition(dr, dc)) {
                Piece piece = board[dr][dc];
                if (piece != null && piece instanceof King && piece.getColor() != this.color) 
                    return false;
            }
        }

        // Check if the move would put the king in check
        King tempKing = new King(targetPosition, this.color);
        var mContext1 = new MoveContext(mContext.getTurnCount(), board);
        if (tempKing.isInCheck(mContext1)) return false;

        return true;
    }

    public boolean canCastle(Position targetPosition, MoveContext mContext) {
        if (this.hasMoved) return false;
        if (this.isInCheck(mContext)) return false;

        // Copy correct potential positions based on color
        // Position[] castlePositions = this.isWhite() ? CASTLE_POSITION_WHITE : CASTLE_POSITION_BLACK;
        int row = this.isWhite() ? 7 : 0;
        var board = mContext.getBoard();

        // Loop through respective color kingside/queenside castling positions until we hit target position
        for (int i = 0; i < CASTLE_POSITION.length; i++) {
            // Target position matches a castling position. The player wants to castle.
            if (targetPosition.equals(CASTLE_POSITION[i])) {
                // Rook column depends on which castling side we matched with
                int rookCol = (i == 0) ? 0 : 7;
                Piece rook = board[row][rookCol];
                // Check if rook piece exists and hasn't moved
                if (rook instanceof Rook && !((Rook)rook).hasMoved()) {

                    int kingCol = this.position.getColumn();
                    int dir = (rookCol == 0) ? -1 : 1;
                    // Check all squares between king and rook are empty
                    for (int c = kingCol + dir; c != rookCol; c += dir) {
                        if (board[row][c] != null) return false;
                    }
                    // Check king does not pass through or end up in check
                    for (int c = kingCol; c != CASTLE_POSITION[i].getColumn() + dir; c += dir) {
                        King tempKing = new King(new Position(row, c), this.color);
                        if (tempKing.isInCheck(mContext)) return false;
                    }
                    // Passed all checks and can castle
                    return true;
                }
            }
        }

        // Player does not want to castle
        return false;
    }

    public boolean isInCheck(MoveContext mContext) {

        int r = this.position.getRow();
        int c = this.position.getColumn();

        // Pawn direction to watch out for is fixed for color
        int pawnDir = this.isWhite() ? -1 : 1;
        int nextRow = r + pawnDir;

        var board = mContext.getBoard();

        // Check left and right columns
        for (int dc : new int[]{-1, 1}) {
            int nextCol = c + dc;
            if (Position.isValidRowOrColumn(nextCol)) {
                // Check if piece there is opposite colored pawn
                Piece piece = board[nextRow][nextCol];
                if (piece != null && piece instanceof Pawn && piece.getColor() != this.color) {
                    return true;
               }
            }
        }

        // All 8 possible horse positions
        int[][] horsePositions = Knight.generateHorsePositions(r, c);

        // Check every legal horse position for an oppsosite colroed knight
        for (int[] hp : horsePositions) {
            int newR = hp[0];
            int newC = hp[1];

            if (Position.isValidPosition(newR, newC)) {
                if (board[newR][newC] != null && board[newR][newC] instanceof Knight 
                    && board[newR][newC].getColor() != this.color) 
                        return true;
            }
        }

        // Check each individual line for either queens/rooks/bishops
        for (Direction dir : Direction.values()) {
            int dr = dir.getRowDelta();
            int dc = dir.getColDelta();

            if (this.processLine(dr, dc, board, Boolean.FALSE, (row, column, piece) -> {
                    if (piece != null && piece.getColor() != this.color) {
                        if (piece instanceof Queen) return true;
                        else if ((dr == 0 || dc == 0) && piece instanceof Rook) return true;
                        else if ((dr != 0 && dc != 0) && piece instanceof Bishop) return true;
                    }
                    return false;
            })) return true;
        }

        // Cleared all possible threats 
        return false;
    }

    @Override
    public void move(Position position, MoveContext mContext) {
        super.move(position, mContext);
        this.hasMoved = true;
    }

    @Override
    public Set<Position> generatePseudoLegalMoves(MoveContext mContext) {
        Set<Position> moves = new HashSet<>();
        // var board = mContext.getBoard();

        int r = this.position.getRow();
        int c = this.position.getColumn();
        // Check all around the king for another piece
        for (Direction dir : Direction.values()) {
            int newR = r + dir.getRowDelta();
            int newC = c + dir.getColDelta();

            if (Position.isValidPosition(newR, newC) && 
                this.isPseudoLegalMove(new Position(newR, newC), mContext)) {
                    moves.add(new Position(newR, newC));
            }
        }
        return moves;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        var King = new King(this.getPosition(), this.getColor());
        King.hasMoved = this.hasMoved;
        return King;
    }
}
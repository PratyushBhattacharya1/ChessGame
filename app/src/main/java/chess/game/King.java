package chess.game;

import java.util.Set;

public class King extends SlidingPieces {

    public static final int MAX_MOVES = 8;
    // public static final Title TITLE = Title.K;

    private static final Position[] CASTLE_POSITION_WHITE = 
        {new Position(7, 2), new Position(7, 6)};
        
    private static final Position[] CASTLE_POSITION_BLACK = 
        {new Position(0, 2), new Position(0, 6)};

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

        if (this.canCastle(targetPosition, mContext)) return true;

        // Return false if invalid pseudo legal king move 
        if (Math.abs(r - newR) > 1 || Math.abs(c - newC) > 1 || r == newR && c == newC
            || board[newR][newC] != null && board[newR][newC].getColor() == this.color) {
            return false;
        }

        return true;
    }

    private boolean canCastle(Position targetPosition, MoveContext mContext) {
        if (hasMoved) return false;

        // Copy correct potential positions based on color
        Position[] castlePositions = this.isWhite() ? CASTLE_POSITION_WHITE : CASTLE_POSITION_BLACK;
        int row = this.isWhite() ? 7 : 0;
        var board = mContext.getBoard();

        // Loop through respective color kingside/queenside castling positions until we hit target position
        for (int i = 0; i < castlePositions.length; i++) {
            // Target position matches a castling position. The player wants to castle.
            if (targetPosition.equals(castlePositions[i])) {
                // Rook column depends on which castling side we matched with
                int rookCol = (i == 0) ? 0 : 7;
                Piece rook = board[row][rookCol];
                // Check if similarly colored rook piece exists and hasn't moved
                if (rook != null && rook instanceof Rook && !((Rook)rook).hasMoved() 
                    && rook.getColor() == this.color) {

                    int kingCol = this.position.getColumn();
                    int dir = (rookCol == 0) ? -1 : 1;
                    // Check all squares between king and rook are empty
                    for (int c = kingCol + dir; c != rookCol; c += dir) {
                        if (board[row][c] != null) return false;
                    }
                    // Check king does not pass through or end up in check
                    for (int c = kingCol; c != castlePositions[i].getColumn() + dir; c += dir) {
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

        var board = mContext.getBoard();
        Piece lastMovedPiece = mContext.getLastMovedPiece();
        Position lastMovedPiecePosition = lastMovedPiece.getPosition();

        int r = this.position.getRow();
        int c = this.position.getColumn();
        int pieceRow = lastMovedPiecePosition.getRow();
        int pieceCol = lastMovedPiecePosition.getColumn();
       

        if (lastMovedPiece.getColor() != this.color) {
            if (lastMovedPiece instanceof Pawn) {
                int rowDelta = lastMovedPiece.getColor() == Color.White ? -1 : 1;
                int colDelta = Math.abs(pieceCol - c);

                if ((pieceRow - r == rowDelta) && (colDelta == 1)) {
                    return true; // Pawn can attack diagonally
                }
            } else if (lastMovedPiece instanceof Knight) {
                // Check if knight is in a position to attack the king
                if (Math.abs(pieceRow - r) == 2 && Math.abs(pieceCol - c) == 1 || 
                    Math.abs(pieceRow - r) == 1 && Math.abs(pieceCol - c) == 2) {
                    return true;
                }
            } else if (lastMovedPiece instanceof Queen || lastMovedPiece instanceof Rook 
                        && r == pieceRow ^ c == pieceCol) {
                int rowDelta = Integer.signum(pieceRow - r);
                int colDelta = Integer.signum(pieceCol - c);

                if (this.processLine(rowDelta, colDelta, board, false, (row, column, piece) ->
                        piece != null && piece.getColor() != this.color && (piece instanceof Queen || piece instanceof Rook)
                )) return true;
            } else if (lastMovedPiece instanceof Queen || lastMovedPiece instanceof Bishop 
                        && Math.abs(r - pieceRow) != Math.abs(c - pieceCol)) {
                int rowDelta = Integer.signum(pieceRow - r);
                int colDelta = Integer.signum(pieceCol - c);

                if (this.processLine(rowDelta, colDelta, board, false, (row, column, piece) ->
                        piece != null && piece.getColor() != this.color && (piece instanceof Queen || piece instanceof Bishop)
                )) return true;
            }
        } else {
            if (r == pieceRow ^ c == pieceCol) {
                int rowDelta = Integer.signum(pieceRow - r);
                int colDelta = Integer.signum(pieceCol - c);

                if (this.processLine(rowDelta, colDelta, board, false, (row, column, piece) ->
                        piece != null && piece.getColor() != this.color && (piece instanceof Queen || piece instanceof Rook)
                )) return true;
            } else if (r == pieceRow ^ c == pieceCol) {
                int rowDelta = Integer.signum(pieceRow - r);
                int colDelta = Integer.signum(pieceCol - c);

                if (this.processLine(rowDelta, colDelta, board, false, (row, column, piece) ->
                        piece != null && piece.getColor() != this.color && (piece instanceof Queen || piece instanceof Bishop)
                )) return true;
            }
        }
        

        // Check all around the king for another king
        for (Direction dir : Direction.values()) {
            int newR = r + dir.getRowDelta();
            int newC = c + dir.getColDelta();

            if (Position.isValidPosition(newR, newC)) {
                Piece piece = board[newR][newC];
                if (piece != null && piece instanceof King && piece.getColor() != this.color) 
                    return true;
            }
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generatePseudoLegalMoves'");
    }

}
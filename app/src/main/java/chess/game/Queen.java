package chess.game;

public class Queen extends SlidingPieces implements Piece {

    public Queen(Position position, Color color) {
        super(position, color);
    }

    @Override
    public boolean isValidMove(Position targetPosition, Piece[][] board, int turnCount) {
        int r = targetPosition.getRow(),
            c = targetPosition.getColumn();

        if (board[r][c] != null && board[r][c].getColor() == this.color) return false;

        return super.isValidRookMove(targetPosition, board) || super.isValidBishopMove(targetPosition, board);
    }
    
}

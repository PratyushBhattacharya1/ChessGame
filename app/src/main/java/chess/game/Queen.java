package chess.game;

public class Queen extends SlidingPieces implements Piece {

    @Override
    public boolean isValidMove(Position targetPosition, Piece[][] board) {
        int r = targetPosition.getRow(),
            c = targetPosition.getColumn();

        if (board[r][c] != null && board[r][c].getColor() == this.color) return false;

        return super.isValidRookMove(targetPosition, board) || super.isValidBishopMove(targetPosition, board);
    }
    
}

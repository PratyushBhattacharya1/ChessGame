package chess.game;

public class Rook extends SlidingPieces implements Piece {

    private boolean hasMoved = false;

    public Rook(Position position, Color color) {
        super(position, color);
    }

    @Override
    public boolean isValidMove(Position targetPosition, Piece[][] board, int turnCount) {
        int r = targetPosition.getRow(),
            c = targetPosition.getColumn();

        if (board[r][c] != null && board[r][c].getColor() == this.color) return false;

        return super.isValidRookMove(targetPosition, board);
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void move(Position p) {
        super.move(p);
        hasMoved = true;
    }
    
}

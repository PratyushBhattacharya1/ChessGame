package chess.game;

public class King extends SlidingPieces implements Piece {

    private boolean hasMoved = false;

    public King(Position position, Color color) {
        super(position, color);
    }

    @Override
    public boolean isValidMove(Position targetPosition, Piece[][] board, int turnCount) {
        // Get position details
        int r = this.position.getRow(),
            c = this.position.getColumn(),
            newR = targetPosition.getRow(),
            newC = targetPosition.getColumn();

        
        if (!(Math.abs(r - newR) == 1 || Math.abs(c - newC) == 1) || (r == newR && c == newC) || board[newR][newC] != null && board[newR][newC].getColor() == this.color) 
            return false;

        return true;
    }

    public boolean isInCheck(Piece[][] board) {




        return false;
    }

    public void move(Position position) {
        super.move(position);
        hasMoved = true;
    }

}
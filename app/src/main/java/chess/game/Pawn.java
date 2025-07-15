package chess.game;

public class Pawn extends PiecesActs implements Piece {

    @Override
    public boolean isValidMove(Position targetPosition, Piece[][] board) {
        int r = this.position.getRow(),
            c = this.position.getColumn(),
            newR = targetPosition.getRow(),
            newC = targetPosition.getColumn();

        if (this.isWhite() && r == 6 && newR == 4 && c == newC && board[5][c] == null && board[6][c] == null) return true;

        if (this.isBlack() && r == 1 && newR == 3 && c == newC && board[2][c] == null && board[3][c] == null) return true;

        if (newR == r + 1 && c == newC && board[newR][c] == null) return true;

        if (newR == r + 1 && Math.abs(newC - c) == 1 && board[newR][newC] != null && board[newR][newC].getColor() != this.color) return true;

        return true; // Placeholder for actual pawn movement logic
    }

    
    
}

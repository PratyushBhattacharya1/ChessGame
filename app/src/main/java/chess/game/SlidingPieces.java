package chess.game;

public abstract class SlidingPieces extends PiecesActs {

    protected boolean isValidRookMove(Position targetPosition, Piece board[][]) {

        int r = this.position.getRow(), 
            c = this.position.getColumn(), 
            newR = this.position.getRow(), 
            newC = this.position.getColumn();

        if ((r != newR && c != newC) || (r == newR && c == newC)) return false;

        if (r == newR) 
            if (newC > c) {
                for (int i = c; c < newC; i++) 
                    if (board[r][i] != null) return false;
                }
            else {
                for (int i = c; c > newC; i--)
                    if (board[r][i] != null) return false;
                }
        else 
            if (newR > r) {
                for (int i = r; r < newR; i++)
                    if (board[i][c] != null) return false;
            }
            else {
                for (int i = r; r > newR; i--) 
                    if (board[i][c] != null) return false;
            }
        
 
        return true;
    }

    protected boolean isValidBishopMove(Position targetPosition, Piece board[][]) {

        int r = this.position.getRow(), 
            c = this.position.getColumn(), 
            newR = this.position.getRow(), 
            newC = this.position.getColumn();

        if (Math.abs(r - newR) != Math.abs(c - newC)) return false;

        if (newR > r && newC > c) {
            for (int i = 1; i < newR - r; i++) {
                if (board[r + i][c + i] != null) return false;
            }
        } else if (newR > r && newC < c) {
            for (int i = 1; i < newR - r; i++) {
                if (board[r + i][c - i] != null) return false;
            }
        } else if (newR < r && newC > c) {
            for (int i = 1; i < r - newR; i++) {
                if (board[r - i][c + i] != null) return false;
            }
        } else if (newR < r && newC < c) {
            for (int i = 1; i < r - newR; i++) {
                if (board[r - i][c - i] != null) return false;
        }
    }

        return true;

    }
}

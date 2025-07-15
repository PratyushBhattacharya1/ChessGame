package chess.game;

public abstract class SlidingPieces extends PiecesActs {

    public SlidingPieces(Position position, Color color) {
        super(position, color);
    }

    protected boolean isValidRookMove(Position targetPosition, Piece board[][]) {
        int r = this.position.getRow(), 
            c = this.position.getColumn(), 
            newR = targetPosition.getRow(), 
            newC = targetPosition.getColumn();

        if ((r != newR && c != newC) || (r == newR && c == newC)) return false;

        if (r == newR) {
            int step = (newC > c) ? 1 : -1;
            for (int i = c + step; i != newC; i += step) {
                if (board[r][i] != null) return false;
            }
        } else {
            int step = (newR > r) ? 1 : -1;
            for (int i = r + step; i != newR; i += step) {
                if (board[i][c] != null) return false;
            }
        }
        return true;
    }

    protected boolean isValidBishopMove(Position targetPosition, Piece board[][]) {
        int r = this.position.getRow(), 
            c = this.position.getColumn(), 
            newR = targetPosition.getRow(), 
            newC = targetPosition.getColumn();

        if (Math.abs(r - newR) != Math.abs(c - newC)) return false;

        int rowStep = (newR > r) ? 1 : -1;
        int colStep = (newC > c) ? 1 : -1;
        int steps = Math.abs(newR - r);
        for (int i = 1; i < steps; i++) {
            if (board[r + i * rowStep][c + i * colStep] != null) return false;
        }
        return true;
    }
}

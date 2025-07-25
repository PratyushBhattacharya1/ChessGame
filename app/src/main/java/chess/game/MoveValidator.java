package chess.game;

public class MoveValidator {

    public static boolean validateMove(Piece piece, Position targetPosition, MoveContext mContext) {
        if (!piece.isPseudoLegalMove(targetPosition, mContext)) {
            return false;
        }

        if (piece instanceof King) {
            // King moves are always legal
            return true;
        }

        // Simulate the move to check for self-check
        var newBoard = simulateMove(mContext.getBoard(), piece, targetPosition);

        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                Piece p = newBoard[i][j];
                if (p != null && p.getColor() == piece.getColor() && p instanceof King
                    && ((King) p).isInCheck(new MoveContext(mContext.getTurnCount(), newBoard))) {
                    return false;
                }
            }
        }

        return true;
    }

    public static Piece[][] simulateMove(Piece[][] board, Piece piece, Position targetPosition) {
        // Clone the board
        Piece[][] newBoard = cloneBoard(board);

        // Move the piece
        int r = piece.getPosition().getRow();
        int c = piece.getPosition().getColumn();
        int newR = targetPosition.getRow();
        int newC = targetPosition.getColumn();
        newBoard[newR][newC] = newBoard[r][c];
        newBoard[r][c] = null;
        newBoard[newR][newC].getPosition().setPosition(targetPosition);
        return newBoard;
    }

    public static Piece[][] cloneBoard(Piece[][] board) {
        Piece[][] newBoard = new Piece[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != null) {
                    try {
                        newBoard[i][j] = (Piece) board[i][j].clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                } else {
                    newBoard[i][j] = null;
                }
            }
        }
        return newBoard;
    }
}
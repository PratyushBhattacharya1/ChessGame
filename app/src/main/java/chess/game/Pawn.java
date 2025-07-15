package chess.game;

public class Pawn extends PiecesActs implements Piece {

    // private boolean enPassant = false;
    private int enPassentTurn = 0;

    // public boolean isValidMove(Position targetPosition, Piece[][] board) {
        // if (targetPosition == null || board == null) throw new IllegalArgumentException("Target position and board cannot be null");

        // // Get position details
        // int r = this.position.getRow(),
        //     c = this.position.getColumn(),
        //     newR = targetPosition.getRow(),
        //     newC = targetPosition.getColumn();

        // // Handle initial two-square move
        // if (this.isWhite() && r == 6 && newR == 4 && c == newC && board[5][c] == null && board[6][c] == null) {
        //     enPassant = true;
        //     return true;
        // }
        // if (this.isBlack() && r == 1 && newR == 3 && c == newC && board[2][c] == null && board[3][c] == null) {
        //     enPassant = true;
        //     return true;
        // }

        // // Handle single square move
        // if (this.isBlack() && newR == r + 1 && c == newC && board[newR][c] == null) return true;
        // if (this.isWhite() && newR == r - 1 && c == newC && board[newR][c] == null) return true;

        // // Handle diagonal capture
        // if (this.isBlack() && newR == r + 1 && Math.abs(newC - c) == 1 && board[newR][newC] != null && board[newR][newC].getColor() != this.color) return true;
        // if (this.isWhite() && newR == r - 1 && Math.abs(newC - c) == 1 && board[newR][newC] != null && board[newR][newC].getColor() != this.color) return true;

    //     return false;
    // }

    // public boolean getEnPassent() {
    //     return this.enPassant;
    // }

    public int getEnPassentTurn() {
        return this.enPassentTurn;
    }

    public void setEnPassentTurn(int turnCount) {
        this.enPassentTurn = turnCount;
    }

    public boolean isValidMove(Position targetPosition, Piece[][] board, int turnCount) {
        if (targetPosition == null || board == null) throw new IllegalArgumentException("Target position and board cannot be null");
        // Get position details
        int r = this.position.getRow(),
            c = this.position.getColumn(),
            newR = targetPosition.getRow(),
            newC = targetPosition.getColumn();

        // Handle initial two-square move
        if (this.isWhite() && r == 6 && newR == 4 && c == newC && board[5][c] == null && board[6][c] == null) {
            this.setEnPassentTurn(turnCount);
            return true;
        }
        if (this.isBlack() && r == 1 && newR == 3 && c == newC && board[2][c] == null && board[3][c] == null) {
            // this.enPassant = true;
            this.setEnPassentTurn(turnCount);
            return true;
        }

        // Handle single square move
        if (this.isBlack() && newR == r + 1 && c == newC && board[newR][c] == null) return true;
        if (this.isWhite() && newR == r - 1 && c == newC && board[newR][c] == null) return true;

        // Handle diagonal capture
        if (this.isBlack() && newR == r + 1 && Math.abs(newC - c) == 1 && board[newR][newC] != null && board[newR][newC].getColor() != this.color) return true;
        if (this.isWhite() && newR == r - 1 && Math.abs(newC - c) == 1 && board[newR][newC] != null && board[newR][newC].getColor() != this.color) return true;


        // Handle enPassent
        if (this.isBlack() && r == 5 && newR == 6 && Math.abs(newC - c) == 1 && board[newR][newC] == null && board[r][newC] != null) {
            Piece piece = board[r][newC];
            if (piece.getClass().equals(this.getClass())) {
                Pawn pawn = (Pawn) piece;
                if (pawn.getEnPassentTurn() == turnCount) return true;
            }
        } else if (this.isWhite() && r == 2 && newR == 1 && Math.abs(newC - c) == 1 && board[newR][newC] == null && board[r][newC] != null) {
            Piece piece = board[r][newC];
            if (piece.getClass().equals(this.getClass())) {
                Pawn pawn = (Pawn) piece;
                if (pawn.getEnPassentTurn() - 1 == turnCount) return true;
            }
        } 

        if (newR == r ) return true;

        return false;
    }

    
    
}

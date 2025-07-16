package chess.game;

public class King extends SlidingPieces implements Piece {

    private static final Position[] CASTLE_POSITION_WHITE = {new Position(7, 2), new Position(7, 6)};
    private static final Position[] CASTLE_POSITION_BLACK = {new Position(0, 2), new Position(0, 6)};
    private static final int[][] DIRECTIONS = {
            {0, 1},     // right
            {0, -1},    // left
            {1, 0},     // down
            {-1, 0},    // up
            {-1, 1},    // up-right
            {-1, -1},   // up-left
            {1, 1},     // down-right
            {1, -1}     // down-left
        };

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

        // Check if the player wants to castle
        if (this.canCastle(targetPosition, board)) return true;

        // Check if new coordinate is possible for king. 
        if (!(Math.abs(r - newR) == 1 || Math.abs(c - newC) == 1) || (r == newR && c == newC) 
            || board[newR][newC] != null && board[newR][newC].getColor() == this.color)
            return false;

        // Position is viable
        return true;
    }

    private boolean canCastle(Position targetPosition, Piece[][] board) {
        // Cannot castle if king moved
        if (hasMoved) return false;

        // Copy correct potential positions based on color
        Position[] castlePositions = this.isWhite() ? CASTLE_POSITION_WHITE : CASTLE_POSITION_BLACK;
        int row = this.isWhite() ? 7 : 0;

        // Loop through respective color kingside/queenside castling positions until we hit target position
        for (int i = 0; i < castlePositions.length; i++) {
            // Target position matches a castling position. The player wants to castle.
            if (targetPosition.equals(castlePositions[i])) {
                // Rook column depends on which castling side we matched with
                int rookCol = (i == 0) ? 0 : 7;
                Piece rook = board[row][rookCol];
                // Check if similarly colored rook piece exists and hasn't moved
                if (rook != null && rook instanceof Rook && !((Rook)rook).hasMoved() && rook.getColor() == this.color) {
                    int kingCol = this.position.getColumn();
                    int dir = (rookCol == 0) ? -1 : 1;
                    // Check all squares between king and rook are empty
                    for (int c = kingCol + dir; c != rookCol; c += dir) {
                        if (board[row][c] != null) return false;
                    }
                    // Check king does not pass through or end up in check
                    for (int c = kingCol; c != castlePositions[i].getColumn() + dir; c += dir) {
                        King tempKing = new King(new Position(row, c), this.color);
                        if (tempKing.isInCheck(board)) return false;
                    }
                    // Passed all checks and can castle
                    return true;
                }
            }
        }

        // Player does not want to castle
        return false;
    }

    public boolean isInCheck(Piece[][] board) {

        // Get current row and column
        int r = this.position.getRow();
        int c = this.position.getColumn();

        // Pawn direction to watch out for is fixed for color
        int pawnDir = this.isWhite() ? -1 : 1;
        int nextRow = r + pawnDir;

        // Check left and right columns
        for (int dc : new int[]{-1, 1}) {
            int nextCol = c + dc;
            // Check if the column is out of bounds
            if (nextCol >= 0 && nextCol < 8) {
                // Check if piece there is opposite colored pawn
                Piece piece = board[nextRow][nextCol];
                if (piece != null && piece instanceof Pawn && piece.getColor() != this.color) {
                    return true;
               }
            }
        }

        // All 8 possible horse positions
        int[][] horsePositions = {
            {r + 2, c + 1},
            {r + 2, c - 1},
            {c + 2, r + 1},
            {c + 2, r - 1},
            {r - 2, c + 1},
            {r - 2, c - 1},
            {c - 2, r + 1},
            {c - 2, r - 1}
        };

        // Check every legal horse position for a knight
        for (int[] hp : horsePositions) {
            int newR = hp[0];
            int newC = hp[1];

            // If the position is legal
            if (newR >= 0 && newR < 8 && newC >= 0 && newC < 8) {
                // Check for an opposite color knight
                if (board[newR][newC] != null && board[newR][newC] instanceof Knight && board[newR][newC].getColor() != this.color) return true;
            }
        }

        // Check each individual line for either queens/rooks/bishops
        for (int[] dir : DIRECTIONS) {
            if (this.checkLine(dir[0], dir[1], board)) return true;
        }

        // Check all around the king for another king
        for (int[] dir : DIRECTIONS) {
            int newR = r + dir[0];
            int newC = c + dir[1];

            // If the position is legal
            if (newR >= 0 && newR < 8 && newC >= 0 && newC < 8) {
                // Check opposite color king
                Piece piece = board[newR][newC];
                if (piece != null && piece instanceof King && piece.getColor() != this.color) return true;
            }
        }

        // Cleared all possible threats 
        return false;
    }

    // Helper to check a direction for bishop/rook/queen threats
    private boolean checkLine(int dr, int dc, Piece[][] board) {
        // Get current row and column
        int r = this.position.getRow();
        int c = this.position.getColumn();

        // Don't accidently check current square
        r += dr;
        c += dc;

        // If the position is legal
        while (r >= 0 && r < 8 && c >= 0 && c < 8) {
            Piece piece = board[r][c];
            if (piece != null) {
                if (piece.getColor() != this.color) {
                    // Check if the piece is a queen
                    if (piece instanceof Queen) return true;
                    // Or check if the piece is rook/bishop depending on the direction being checked
                    else if ((dr == 0 || dc == 0) && piece instanceof Rook) return true;
                    else if ((dr != 0 && dc != 0) && piece instanceof Bishop) return true;
                }

                // If a piece was found but isn't threatening, then skip checking this direction. It means 
                // the king was blocked by another piece.
                break;
            }

            r += dr;
            c += dc;
        }

        // No threat in this direction
        return false;
    }

    public void move(Position position) {
        super.move(position);
        this.hasMoved = true;
    }

    @Override
    public String toString() {
        return "" + (this.isWhite() ? "W" : "B") + (this.isWhite() ? "K" : "K");
    }

}
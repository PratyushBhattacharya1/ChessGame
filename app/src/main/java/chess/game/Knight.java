package chess.game;

public class Knight extends PiecesActs implements Piece {

    public Knight(Position position, Color color) {
        super(position, color);
    }

    @Override
    public boolean isValidMove(Position targetPosition, Piece[][] board, int turnCount) {
        int r = this.position.getRow(),
            c = this.position.getColumn(),
            newR = targetPosition.getRow(),
            newC = targetPosition.getColumn();

        int rowdiff = Math.abs(r - newR),
            coldiff = Math.abs(c - newC);

        if (!((rowdiff == 2 && coldiff == 1) || (rowdiff == 1 && coldiff == 2))) return false;

        if (board[newR][newC] != null && board[newR][newC].getColor() == this.color) return false;

        return true;
    }
    
}

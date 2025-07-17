package chess.game;

import java.util.List;

/**
 * Represents a {@code Knight} chess piece.
 * Inherits common piece actions from {@link PieceBehaviors} and implements the {@link Piece} interface.
 * The {@code Knight} moves in an "L" shape: two squares in one direction and then one square perpendicular.
 */
public class Knight extends PieceBehaviors {

    /**
     * Constructs a {@code Knight} chess piece with the specified position and color.
     *
     * @param position the initial position of the {@code Knight} on the chessboard
     * @param color the color of the {@code Knight} (e.g., WHITE or BLACK)
     */
    public Knight(Position position, Color color) {
        super(position, color);
    }

    /**
     * Determines if the knight's move to the specified target position is valid.
     *
     * A move is considered valid if:
     * <ul>
     *   <li>The move follows the L-shaped pattern of a knight (two squares in one direction and one in the perpendicular direction).</li>
     *   <li>The target position is either empty or occupied by an opponent's piece (not a piece of the same color).</li>
     * </ul>
     *
     * @param targetPosition The position to which the knight is attempting to move.
     * @param board The current state of the chess board.
     * @param turnCount The current turn count in the game (unused in this method).
     * @return {@code true} if the move is valid for a knight; {@code false} otherwise.
     */
    @Override
    public boolean isPseudoLegalMove(Position targetPosition, Piece[][] board, int turnCount) {
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
    
    @Override
    public String toString() {
        return "" + (this.isWhite() ? "W" : "B") + (this.isWhite() ? "N" : "N");
    }

    @Override
    public List<Position> generatePseudoLegalMoves(Piece[][] board) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generatePseudoLegalMoves'");
    }

    // @Override
    // public void move(Position p) {
    //     super.move(p);
    // }
}

package chess.game;

import java.util.ArrayList;
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
        this.title = Title.N;
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
    public boolean isPseudoLegalMove(Position targetPosition, MoveContext mContext) {
        int r = this.position.getRow(),
            c = this.position.getColumn(),
            newR = targetPosition.getRow(),
            newC = targetPosition.getColumn();

        int rowdiff = Math.abs(r - newR),
            coldiff = Math.abs(c - newC);

        var board = mContext.getBoard();

        if (!((rowdiff == 2 && coldiff == 1) || (rowdiff == 1 && coldiff == 2))) return false;

        if (board[newR][newC] != null && board[newR][newC].getColor() == this.color) return false;

        return true;
    }
    
    @Override
    public String toString() {
        return "" + (this.isWhite() ? "W" : "B") + (this.isWhite() ? "N" : "N");
    }

    @Override
    public List<Position> generatePseudoLegalMoves(MoveContext mContext) {
        List<Position> moves = new ArrayList<>();

        for (int[] arr : generateHorsePositions(this.position.getRow(), this.position.getColumn())) {
            int row = arr[0];
            int column = arr[1];
            if (Position.isValidPosition(row, column) && mContext.getBoard()[row][column] == null) 
                moves.add(new Position(row, column)); 
        }

        return moves;
    }

    public static int[][] generateHorsePositions(int r, int c) {
        if (!Position.isValidPosition(r, c)) throw new IllegalArgumentException("Arguments out of bounds");

        return new int[][]{
            {r + 2, c + 1},
            {r + 2, c - 1},
            {c + 2, r + 1},
            {c + 2, r - 1},
            {r - 2, c + 1},
            {r - 2, c - 1},
            {c - 2, r + 1},
            {c - 2, r - 1}
        };
     }
}

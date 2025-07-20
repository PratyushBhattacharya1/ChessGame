package chess.game;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a {@code Bishop} chess piece.
 * <p>
 * The {@code Bishop} moves diagonally any number of squares, as long as its path is not blocked.
 * This class extends {@link SlidingPieces} to inherit common movement logic for sliding pieces.
 * </p>
 *
 * @author Pratyush
 */
public class Bishop extends SlidingPieces {

    public static final int MAX_MOVES = 13;
    // public static final Title TITLE = Title.B;

    /**
     * Constructs a Bishop chess piece with the specified position and color.
     *
     * @param position the initial position of the bishop on the chess board
     * @param color the color of the bishop (e.g., white or black)
     */
    public Bishop(Position position, Color color) {
        super(position, color);
        this.TITLE = Title.B;
    }

    /**
     * Determines if the bishop's move to the specified target position is valid.
     *
     * @param targetPosition The position to which the bishop is attempting to move.
     * @param board The current state of the chess board.
     * @param turnCount The current turn count in the game.
     * @return {@code true} if the move is a valid bishop move according to chess rules; false otherwise.
     */
    @Override
    public boolean isPseudoLegalMove(Position targetPosition, MoveContext mContext) {
        return super.isValidBishopMove(targetPosition, mContext.getBoard());
    }

    @Override
    public Set<Position> generatePseudoLegalMoves(MoveContext mContext) {
        Set<Position> moves = new HashSet<>(MAX_MOVES);
        var board = mContext.getBoard();

        for (Direction dr : DIAGONALS) {
            this.processLine(dr.getRowDelta(), dr.getColDelta(), board, moves, (row, column, piece) -> {
                if (piece == null || piece.getColor() != this.getColor()) moves.add(new Position(row, column));
                return moves;
            });
        }

        return moves;
    }
}

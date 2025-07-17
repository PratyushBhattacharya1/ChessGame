package chess.game;

import java.util.List;

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

    /**
     * Constructs a Bishop chess piece with the specified position and color.
     *
     * @param position the initial position of the bishop on the chess board
     * @param color the color of the bishop (e.g., white or black)
     */
    public Bishop(Position position, Color color) {
        super(position, color);
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
    public boolean isPseudoLegalMove(Position targetPosition, Piece[][] board, int turnCount) {
        return super.isValidBishopMove(targetPosition, board);
    }

    @Override
    public String toString() {
        return "" + (this.isWhite() ? "W" : "B") + (this.isWhite() ? "B" : "B");
    }

    @Override
    public List<Position> generatePseudoLegalMoves(Piece[][] board) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generatePseudoLegalMoves'");
    }
}

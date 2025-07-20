package chess.game;

import java.util.Set;

/**
 * Represents a chess piece on the board.
 * Provides methods to access piece properties and to perform and validate moves.
 */
public interface Piece {

    /**
     * Returns the current position of this piece on the chessboard.
     *
     * @return the {@link Position} object representing the piece's location
     */
    public Position getPosition();

    /**
     * Returns the color of this chess piece.
     *
     * @return the {@link Color} representing the color (e.g., WHITE or BLACK) of the piece
     */
    public Color getColor();

    /**
     * Determines if this piece is white.
     *
     * @return {@code true} if the piece is white; {@code false} otherwise.
     */
    public boolean isWhite();

    /**
     * Determines if this chess piece is black.
     *
     * @return {@code true} if the piece is black; {@code false} otherwise.
     */
    public boolean isBlack();

    /**
     * Determines if moving this piece to the specified target position is a pseudo-legal move.
     * <p>
     * A pseudo-legal move is a move that follows the movement rules for the piece,
     * but does not consider whether the move would leave the player's king in check.
     * </p>
     *
     * @param targetPosition the position to which the piece is to be moved
     * @param moveContext the context of the move, which may include information about the board state,
     *                    other pieces, and special move rules (such as castling or en passant)
     * @return {@code true} if the move is pseudo-legal for this piece; {@code false} otherwise
     */
    public boolean isPseudoLegalMove(Position targetPosition, MoveContext moveContext);
    
    /**
     * Moves the piece to the specified position.
     *
     * @param p the target position to move the piece to
     * @param moveContext the context of the move, containing additional information such as the game state
     */
    public void move(Position p, MoveContext moveContext);

    /**
     * Generates all pseudo-legal moves for this piece given the current move context.
     * Pseudo-legal moves are moves that are valid according to the movement rules of the piece,
     * but may not account for checks or other game-specific constraints.
     *
     * @param moveContext the context of the current move, including board state and other relevant information
     * @return a set of positions representing all possible pseudo-legal moves for this piece
     */
    public Set<Position> generatePseudoLegalMoves(MoveContext moveContext);


    /**
     * Creates and returns a copy of this object.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not support the {@code Cloneable} interface.
     */
    public Object clone() throws CloneNotSupportedException;

}
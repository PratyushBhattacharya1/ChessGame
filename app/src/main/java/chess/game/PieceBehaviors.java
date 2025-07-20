package chess.game;

import java.util.Set;

/**
 * Abstract base class for chess piece behaviors, implementing the Piece interface.
 * Provides common functionality and properties for all chess pieces, such as position,
 * color, and movement logic.
 *
 * <p>Defines enums for movement directions and piece titles, and utility methods for
 * movement, color checking, and board processing.</p>
 *
 * <ul>
 *   <li>{@link Direction} - Enum representing possible movement directions on the board.</li>
 *   <li>{@link Title} - Enum representing the type of chess piece (Rook, Knight, Bishop, Queen, King, Pawn).</li>
 * </ul>
 *
 * <p>Key Methods:</p>
 * <ul>
 *   <li>{@code getPosition()} - Returns the current position of the piece.</li>
 *   <li>{@code getColor()} - Returns the color of the piece.</li>
 *   <li>{@code isWhite()}, {@code isBlack()} - Checks the color of the piece.</li>
 *   <li>{@code move(Position, MoveContext)} - Moves the piece to a new position.</li>
 *   <li>{@code processLine(...)} - Processes a line in a given direction, applying a processor to each square.</li>
 *   <li>{@code isPositionPieceSameColor(...)} - Checks if a target position contains a piece of the same color.</li>
 * </ul>
 *
 * <p>Subclasses should implement specific movement and behavior logic for each piece type.</p>
 */
public abstract class PieceBehaviors implements Piece {

    /**
     * Enum representing the possible movement directions for chess pieces on a board.
     * Each direction is defined by its row and column delta values.
     * <ul>
     *   <li>{@link #UP} - Move up (decrease row index)</li>
     *   <li>{@link #DOWN} - Move down (increase row index)</li>
     *   <li>{@link #LEFT} - Move left (decrease column index)</li>
     *   <li>{@link #RIGHT} - Move right (increase column index)</li>
     *   <li>{@link #UP_LEFT} - Move diagonally up and left</li>
     *   <li>{@link #UP_RIGHT} - Move diagonally up and right</li>
     *   <li>{@link #DOWN_LEFT} - Move diagonally down and left</li>
     *   <li>{@link #DOWN_RIGHT} - Move diagonally down and right</li>
     * </ul>
     * Each direction provides methods to retrieve its row and column delta values.
     */
    public static enum Direction {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1),
        UP_LEFT(-1, -1),
        UP_RIGHT(-1, 1),
        DOWN_LEFT(1, -1),
        DOWN_RIGHT(1, 1);

        private final int dr; // Row delta
        private final int dc; // Column delta

        // private final int[] dir;

        Direction(int dr, int dc) {
            this.dr = dr;
            this.dc = dc;
            // this.dir = new int[] {dr, dc};
        }

        // public int[] getDirection() {return this.dir;}

        public int getRowDelta() {return this.dr;}
        public int getColDelta() {return this.dc;}
    }

    /**
     * Represents the different types of chess pieces using their standard single-letter notation.
     * <ul>
     *   <li>R - Rook</li>
     *   <li>N - Knight</li>
     *   <li>B - Bishop</li>
     *   <li>Q - Queen</li>
     *   <li>K - King</li>
     *   <li>P - Pawn</li>
     * </ul>
     */
    public static enum Title {
        R,
        N,
        B,
        Q,
        K,
        P
    }

    /**
     * The current position of the chess piece on the board.
     */
    Position position;
    /**
     * The color of the chess piece, indicating whether it belongs to the white or black side.
     */
    final Color color;
    /**
     * The maximum number of moves that a chess piece can make.
     * This value is used to limit the number of possible moves generated
     * for a piece during move calculation.
     */
    protected int MAX_MOVES;
    /**
     * The TITLE field represents the title or rank of the chess piece (e.g., King, Queen, Rook).
     * It is used to identify the type of the piece and may influence its behavior on the chessboard.
     */
    protected Title TITLE;

    /**
     * Constructs a new PieceBehaviors object with the specified position and color.
     *
     * @param position the initial position of the piece on the board
     * @param color the color of the piece (e.g., WHITE or BLACK)
     */
    public PieceBehaviors(Position position, Color color) {
        this.position = new Position(position);
        this.color = color;
    }

    /**
     * Returns the current position of this piece on the chessboard.
     *
     * @return the {@link Position} object representing the piece's location
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Returns the color of this piece.
     *
     * @return the {@link Color} representing the color of the piece
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Checks if this piece is white.
     *
     * @return {@code true} if the piece's color is white; {@code false} otherwise.
     */
    public boolean isWhite() {
        return this.color == Color.White;
    }

    /**
     * Checks if the piece is black.
     *
     * @return {@code true} if the piece's color is black; {@code false} otherwise.
     */
    public boolean isBlack() {
        return this.color == Color.Black;
    }

    /**
     * Moves the piece to the specified position.
     *
     * @param p the new position to move the piece to
     * @param moveContext the context of the move, containing additional information about the move
     */
    public void move(Position p, MoveContext moveContext) {
        this.position.setPosition(p);
    }

    /**
     * Processes a line on the chess board in a specified direction, starting from the current piece's position.
     * The method iterates over the board in the direction specified by (dr, dc), applying the given processor
     * to each position until it reaches the edge of the board or encounters a non-null piece.
     *
     * @param <T>           The type of the result produced by the processor.
     * @param dr            The row direction increment (e.g., -1, 0, 1).
     * @param dc            The column direction increment (e.g., -1, 0, 1).
     * @param board         The 2D array representing the chess board.
     * @param initialValue  The initial value to pass to the processor.
     * @param processor     The processor to apply at each position along the line.
     * @return              The result after processing the line, as determined by the processor.
     */
    public <T> T processLine(   int dr, 
                                int dc, 
                                Piece[][] board, 
                                T initialValue, 
                                LineProcessor<T> processor) {

        int r = this.position.getRow() + dr;
        int c = this.position.getColumn() + dc;

        T result = initialValue;

        while (Position.isValidPosition(r, c)) {
            Piece piece = board[r][c];
            result = processor.process(r, c, piece);
            if (piece != null) {
                break;
            }
            r += dr;
            c += dc;
        }
        return result;
    }

    /**
     * Returns a string representation of the piece, indicating its color ("W" for white, "B" for black)
     * followed by its title.
     *
     * @return a string representing the piece's color and title
     */
    @Override
    public String toString() {
        return "" + (this.isWhite()? "W" : "B") + TITLE;
    }

    /**
     * Checks if the piece at the specified position on the board is of the same color as this piece.
     *
     * @param board the 2D array representing the chess board
     * @param newR the row index of the position to check
     * @param newC the column index of the position to check
     * @return true if there is a piece at (newR, newC) and it is the same color as this piece; false otherwise
     */
    protected boolean isPositionPieceSameColor(Piece[][] board, int newR, int newC) {
        return board[newR][newC] != null && board[newR][newC].getColor() == this.color;
    }
    
    @Override
    public Set<Position> generatePseudoLegalMoves(MoveContext moveContext) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPseudoLegalMove(Position targetPosition, MoveContext moveContext) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
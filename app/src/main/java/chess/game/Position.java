package chess.game;

/**
 * Represents a position on a chessboard using row and column indices.
 * Provides utility methods for validation, conversion from chess notation,
 * and manipulation of positions.
 *
 * <p>Rows and columns are zero-indexed, with (0, 0) representing the top-left
 * corner of the board (typically "a8" in chess notation).</p>
 *
 * <ul>
 *   <li>Row indices range from 0 (top) to 7 (bottom).</li>
 *   <li>Column indices range from 0 (left, 'a') to 7 (right, 'h').</li>
 * </ul>
 *
 * <p>Includes methods for:</p>
 * <ul>
 *   <li>Validating positions</li>
 *   <li>Converting between chess notation and indices</li>
 *   <li>Comparing positions</li>
 *   <li>Setting and retrieving row and column values</li>
 * </ul>
 */
public class Position {
    private static final int MAX_STRING_LENGTH = 2;
    private int row, column;

    /**
     * Constructs a Position object with the specified row and column.
     * Throws an IllegalArgumentException if the given position is out of chessboard bounds.
     *
     * @param row the row index of the position (typically 0-7 for standard chessboard)
     * @param column the column index of the position (typically 0-7 for standard chessboard)
     * @throws IllegalArgumentException if the position is not valid on the chessboard
     */
    public Position(int row, int column) {
        if (!isValidPosition(row, column)) throw new IllegalArgumentException("Chess position is out of bounds.");

        this.row = row;
        this.column = column;
    }

    /**
     * Copy constructor for the {@code Position} class.
     * <p>
     * Creates a new {@code Position} object by copying the row and column values from the given {@code Position} instance.
     * Throws an {@code IllegalArgumentException} if the provided position is out of bounds.
     *
     * @param position the {@code Position} object to copy
     * @throws IllegalArgumentException if the position specified by {@code position} is not valid
     */
    public Position(Position position) { 
        this(position.getRow(), position.getColumn());
    }

    /**
     * Returns the row index of this position.
     *
     * @return the row number associated with this position
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the column index of this position.
     *
     * @return the column index as an integer
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Sets the current position to the specified new position.
     * Validates that the new position is within the allowed bounds.
     *
     * @param newPosition the new {@code Position} to set
     * @throws IllegalArgumentException if the new position is out of bounds
     */
    public void setPosition(Position newPosition) {
        this.setPosition(newPosition.getRow(), newPosition.getColumn());
    }

    /**
     * Sets the position of the object to the specified row and column.
     * 
     * @param row the row index to set the position to
     * @param column the column index to set the position to
     * @throws IllegalArgumentException if the specified position is out of bounds
     */
    public void setPosition(int row, int column) {
        if (!isValidPosition(row, column)) throw new IllegalArgumentException("New position is out of bounds.");

        this.row = row;
        this.column = column;
    }


    /**
     * Sets the row of this position to the specified value while keeping the current column unchanged.
     *
     * @param row the new row value to set
     */
    public void setRow(int row) {
        this.setPosition(row, this.column);
    }

    public void setColumn(int column) {
        this.setPosition(this.row, column);
    }


    /**
     * Checks if the given row and column represent a valid position on an 8x8 chessboard.
     *
     * @param row the row index to check (0-based)
     * @param column the column index to check (0-based)
     * @return {@code true} if both row and collumn are within the range [0, 7], false otherwise
     */
    public static boolean isValidPosition(int row, int column) {
        return (row < 8 && row >= 0 && column < 8 && column >= 0);
    }

    /**
     * Checks if the given value is a valid row or column index on a standard 8x8 chessboard.
     *
     * @param value the row or column index to check
     * @return {@code true} if the value is between 0 (inclusive) and 8 (exclusive), {@code false} otherwise
     */
    public static boolean isValidRowOrColumn(int value) {
        return (value < 8 && value >= 0);
    }

    /**
     * Converts a chessboard rank character ('1' to '8') to the corresponding row index (0 to 7)
     * used in a 2D array representation of the board, where row 0 is the top (rank '8')
     * and row 7 is the bottom (rank '1').
     *
     * @param rank the rank character ('1' to '8')
     * @return the corresponding row index (0 for '8', 7 for '1')
     */
    public static int rankToRow(char rank) {
        return Math.abs(rank - '0' - 8);
    }

    /**
     * Converts a chessboard file character ('a' through 'h') to its corresponding column index (0-7).
     *
     * @param file the file character ('a' to 'h') representing the column on a chessboard
     * @return the zero-based column index (0 for 'a', 7 for 'h')
     */
    public static int fileToColumn(char file) {
        return file - 'a';
    }

    /**
     * Converts a two-character chess position string (e.g., "e4") to a {@code Position} object.
     *
     * @param s the position string, where the first character is the file ('a'-'h') and the second is the rank ('1'-'8')
     * @return a {@code Position} object representing the specified location on the chessboard
     * @throws IllegalArgumentException if the input string is not exactly two characters long
     */
    public static Position stringToPosition(String s) {
        if (s.length() != MAX_STRING_LENGTH) throw new IllegalArgumentException("Input string isn't two characters.");
        
        int row = Position.rankToRow(s.charAt(1));
        int column = Position.fileToColumn(s.charAt(0));

        return new Position(row, column);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) return false;

        Position p = (Position) obj;

        int r = p.getRow();
        int c = p.getColumn();

        return this.row == r && this.column == c;
    }

    @Override
    public int hashCode() {
        return this.row * 31 + this.column;
    }

    @Override
    public String toString() {
        return "(" + this.row + ", " + this.column + ")";
    }
}


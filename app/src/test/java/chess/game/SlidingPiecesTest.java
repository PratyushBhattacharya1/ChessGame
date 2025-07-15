package chess.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class SlidingPiecesTest {

    // Concrete subclass for testing
    static class TestSlidingPiece extends SlidingPieces implements Piece {
        public TestSlidingPiece(Position position, Color color) {
            super(position, color);
        }

        @Override
        public boolean isValidMove(Position targetPosition, Piece[][] board, int turnCount) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'isValidMove'");
        }
    }

    TestSlidingPiece whiteRook;
    TestSlidingPiece blackBishop;
    Piece[][] board = new Piece[8][8];

    @BeforeEach
    public void setUp() {
        whiteRook = new TestSlidingPiece(new Position(3, 3), Color.W);
        blackBishop = new TestSlidingPiece(new Position(4, 4), Color.B);

        board[3][3] = whiteRook;
        board[4][4] = blackBishop;
    }

    @Test
    public void testValidMoveRookUnobstructed() {
        assertTrue(whiteRook.isValidRookMove(new Position(3, 7), board));
        assertTrue(whiteRook.isValidRookMove(new Position(3, 0), board));
        assertTrue(whiteRook.isValidRookMove(new Position(0, 3), board));
        assertTrue(whiteRook.isValidRookMove(new Position(7, 3), board));
    }

    @Test
    public void testInvalidRookMoveNotStraight() {
        assertFalse(whiteRook.isValidRookMove(new Position(4, 4), board));
        assertFalse(whiteRook.isValidRookMove(new Position(2, 2), board));
        assertFalse(whiteRook.isValidRookMove(new Position(0, 0), board));
        assertFalse(whiteRook.isValidRookMove(new Position(7, 7), board));
    }

    @Test
    public void testInvalidRookMoveObstructed() {
        board[2][4] = new DummyPiece(new Position(2, 4), Color.W); // Obstruction
        TestSlidingPiece whiteRook = new TestSlidingPiece(new Position(2, 2), Color.W);
        assertFalse(whiteRook.isValidRookMove(new Position(2, 6), board));
    }

    @Test
    public void testInvalidRookMoveSameSquare() {
        TestSlidingPiece whiteRook = new TestSlidingPiece(new Position(5, 5), Color.W);
        assertFalse(whiteRook.isValidRookMove(new Position(5, 5), board));
    }

    @Test
    public void testValidBishopMoveUnobstructed() {
        TestSlidingPiece blackBishop = new TestSlidingPiece(new Position(2, 0), Color.W);
        assertTrue(blackBishop.isValidBishopMove(new Position(5, 3), board));
    }

    @Test
    public void testInvalidBishopMoveNotDiagonal() {
        TestSlidingPiece blackBishop = new TestSlidingPiece(new Position(4, 4), Color.W);
        assertFalse(blackBishop.isValidBishopMove(new Position(4, 7), board));
    }

    @Test
    public void testInvalidBishopMoveObstructed() {
        board[3][3] = new DummyPiece(new Position(2, 4), Color.W); // Obstruction on the diagonal
        TestSlidingPiece blackBishop = new TestSlidingPiece(new Position(2, 2), Color.W);
        assertFalse(blackBishop.isValidBishopMove(new Position(4, 4), board));
    }

    @Test
    public void testValidBishopMoveLongDiagonalUnobstructed() {
        TestSlidingPiece blackBishop = new TestSlidingPiece(new Position(0, 0), Color.W);
        assertTrue(blackBishop.isValidBishopMove(new Position(7, 7), board));
    }

    @Test
    public void testInvalidBishopMoveSameSquare() {
        TestSlidingPiece blackBishop = new TestSlidingPiece(new Position(3, 3), Color.W);
        assertFalse(blackBishop.isValidBishopMove(new Position(3, 3), board));
    }
}
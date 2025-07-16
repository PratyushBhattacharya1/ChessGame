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
        blackBishop = new TestSlidingPiece(new Position(4, 5), Color.B);

        board[3][3] = whiteRook;
        board[4][5] = blackBishop;
    }

    @Test
    public void testValidMoveRookUnobstructed() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 3 ^ j == 3) assertTrue(whiteRook.isValidRookMove(new Position(i, j), board));
                else assertFalse(whiteRook.isValidRookMove(new Position(i, j), board));
            }
        }
    }

    @Test
    public void testInvalidRookMoveObstructed() {
        board[3][4] = new DummyPiece(new Position(3, 4), Color.W); // Obstruction
        board[3][2] = new DummyPiece(new Position(3, 2), Color.W); // Obstruction
        board[4][3] = new DummyPiece(new Position(4, 3), Color.W); // Obstruction
        board[2][3] = new DummyPiece(new Position(2, 3), Color.W); // Obstruction

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                assertFalse(whiteRook.isValidRookMove(new Position(i, j), board));
            }
        }
    }

    @Test
    public void testInvalidRookMoveSameSquare() {
        assertFalse(whiteRook.isValidRookMove(new Position(3, 3), board));
    }

    @Test
    public void testValidRookCapture() {
        board[3][4] = new DummyPiece(new Position(3, 4), Color.B); // Obstruction
        board[3][2] = new DummyPiece(new Position(3, 2), Color.B); // Obstruction
        board[4][3] = new DummyPiece(new Position(4, 3), Color.B); // Obstruction
        board[2][3] = new DummyPiece(new Position(2, 3), Color.B); // Obstruction

        assertTrue(whiteRook.isValidRookMove(new Position(3, 4), board));
        assertTrue(whiteRook.isValidRookMove(new Position(3, 2), board));
        assertTrue(whiteRook.isValidRookMove(new Position(4, 3), board));
        assertTrue(whiteRook.isValidRookMove(new Position(2, 3), board));
    }

    @Test
    public void testValidBishopMoveUnobstructed() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!(Math.abs(4 - i) != Math.abs(5 - j)) && !(i == 4 && j == 5)) assertTrue(blackBishop.isValidBishopMove(new Position(i, j), board));
                else assertFalse(blackBishop.isValidBishopMove(new Position(i, j), board));
            }
        }
    }

    @Test
    public void testInvalidBishopMoveObstructed() {
        board[3][6] = new DummyPiece(new Position(3, 6), Color.B); // Obstruction on the diagonal
        board[3][4] = new DummyPiece(new Position(3, 4), Color.B); // Obstruction on the diagonal
        board[5][6] = new DummyPiece(new Position(5, 6), Color.B); // Obstruction on the diagonal
        board[5][4] = new DummyPiece(new Position(5, 4), Color.B); // Obstruction on the diagonal

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                assertFalse(blackBishop.isValidBishopMove(new Position(i, j), board));
            }
        }
    }

    @Test
    public void testValidBishopMoveLongDiagonalUnobstructed() {
        board[3][3] = null;
        TestSlidingPiece blackBishop2 = new TestSlidingPiece(new Position(0, 0), Color.B);
        board[0][0] = blackBishop2;
        assertTrue(blackBishop2.isValidBishopMove(new Position(7, 7), board));
    }

    @Test
    public void testInvalidBishopMoveSameSquare() {
        assertFalse(blackBishop.isValidBishopMove(new Position(4, 5), board));
    }

    @Test
    public void testValidBishopCapture() {
        board[3][6] = new DummyPiece(new Position(3, 6), Color.W); // Enemy on the diagonal
        board[3][4] = new DummyPiece(new Position(3, 4), Color.W); // Enemy on the diagonal
        board[5][6] = new DummyPiece(new Position(5, 6), Color.W); // Enemy on the diagonal
        board[5][4] = new DummyPiece(new Position(5, 4), Color.W); // Enemy on the diagonal

        assertTrue(blackBishop.isValidBishopMove(new Position(3, 6), board));
        assertTrue(blackBishop.isValidBishopMove(new Position(3, 4), board));
        assertTrue(blackBishop.isValidBishopMove(new Position(5, 6), board));
        assertTrue(blackBishop.isValidBishopMove(new Position(5, 4), board));
    }
}
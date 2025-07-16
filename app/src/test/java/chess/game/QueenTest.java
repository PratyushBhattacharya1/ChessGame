package chess.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class QueenTest {

    private Queen whiteQueen;
    private Queen blackQueen;
    private Piece[][] board;

    @BeforeEach
    public void setUp() {
        // 8x8 chess board
        board = new Piece[8][8];
        whiteQueen = new Queen(new Position(3, 3), Color.White);
        blackQueen = new Queen(new Position(4, 4), Color.Black);
        board[3][3] = whiteQueen;
        board[4][4] = blackQueen;
    }

    @Test
    public void testValidRookMove() {
        // Move queen horizontally to (3, 7)
        assertTrue(whiteQueen.isValidMove(new Position(3, 7), board, 1));
        // Move queen vertically to (0, 3)
        assertTrue(whiteQueen.isValidMove(new Position(0, 3), board, 1));
        // Black queen: horizontally to (4, 0)
        assertTrue(blackQueen.isValidMove(new Position(4, 0), board, 1));
        // Black queen: vertically to (7, 4)
        assertTrue(blackQueen.isValidMove(new Position(7, 4), board, 1));
    }

    @Test
    public void testValidBishopMove() {
        // Move queen diagonally to (0, 0)
        assertTrue(whiteQueen.isValidMove(new Position(0, 0), board, 1));
        // Move queen diagonally to (2, 2)
        assertTrue(whiteQueen.isValidMove(new Position(2, 2), board, 1));
        // Black queen: diagonally to (7, 7)
        assertTrue(blackQueen.isValidMove(new Position(7, 7), board, 1));
        // Black queen: diagonally to (1, 1)
        assertTrue(blackQueen.isValidMove(new Position(5, 5), board, 1));
    }

    @Test
    public void testInvalidMoveNotRookOrBishop() {
        // Move queen to (2, 5) which is not a straight or diagonal move
        assertFalse(whiteQueen.isValidMove(new Position(2, 5), board, 1));
        // Black queen to (6, 5) which is not a straight or diagonal move
        assertFalse(blackQueen.isValidMove(new Position(6, 5), board, 1));
    }

    @Test
    public void testMoveToSamePosition() {
        // Move queen to its own position
        assertFalse(whiteQueen.isValidMove(new Position(3, 3), board, 1));
        // Black queen to its own position
        assertFalse(blackQueen.isValidMove(new Position(4, 4), board, 1));
    }

    @Test
    public void testMoveBlockedBySameColorPiece() {
        // Place a white piece at (3, 5)
        board[3][5] = new DummyPiece(new Position(3, 5), Color.White);
        assertFalse(whiteQueen.isValidMove(new Position(3, 5), board, 1));
        // Place a black piece at (4, 2)
        board[4][2] = new DummyPiece(new Position(4, 2), Color.Black);
        assertFalse(blackQueen.isValidMove(new Position(4, 2), board, 1));
    }

    @Test
    public void testMoveCapturesOpponentPiece() {
        // Place a black piece at (3, 5)
        board[3][5] = new DummyPiece(new Position(3, 5), Color.Black);
        assertTrue(whiteQueen.isValidMove(new Position(3, 5), board, 1));
        // Place a white piece at (4, 2)
        board[4][2] = new DummyPiece(new Position(4, 2), Color.White);
        assertTrue(blackQueen.isValidMove(new Position(4, 2), board, 1));
    }

    @Test
    public void testMoveBlockedByPieceInPath() {
        // Place a piece between queen and target
        board[3][5] = new DummyPiece(new Position(3, 5), Color.Black);
        // Queen tries to move to (3, 7) but path is blocked at (3, 5)
        assertFalse(whiteQueen.isValidMove(new Position(3, 7), board, 1));
        // Place a piece between black queen and target
        board[4][2] = new DummyPiece(new Position(4, 2), Color.White);
        // Black queen tries to move to (4, 0) but path is blocked at (4, 2)
        assertFalse(blackQueen.isValidMove(new Position(4, 0), board, 1));
    }
}
package chess.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

public class QueenTest {

    private Queen whiteQueen;
    private Queen blackQueen;
    private Piece[][] board;
    int turnCount = 1;

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
        assertTrue(whiteQueen.isPseudoLegalMove(new Position(3, 7), new MoveContext(turnCount, board)));
        // Move queen vertically to (0, 3)
        assertTrue(whiteQueen.isPseudoLegalMove(new Position(0, 3), new MoveContext(turnCount, board)));
        // Black queen: horizontally to (4, 0)
        assertTrue(blackQueen.isPseudoLegalMove(new Position(4, 0), new MoveContext(turnCount, board)));
        // Black queen: vertically to (7, 4)
        assertTrue(blackQueen.isPseudoLegalMove(new Position(7, 4), new MoveContext(turnCount, board)));
    }

    @Test
    public void testValidBishopMove() {
        // Move queen diagonally to (0, 0)
        assertTrue(whiteQueen.isPseudoLegalMove(new Position(0, 0), new MoveContext(turnCount, board)));
        // Move queen diagonally to (2, 2)
        assertTrue(whiteQueen.isPseudoLegalMove(new Position(2, 2), new MoveContext(turnCount, board)));
        // Black queen: diagonally to (7, 7)
        assertTrue(blackQueen.isPseudoLegalMove(new Position(7, 7), new MoveContext(turnCount, board)));
        // Black queen: diagonally to (1, 1)
        assertTrue(blackQueen.isPseudoLegalMove(new Position(5, 5), new MoveContext(turnCount, board)));
    }

    @Test
    public void testInvalidMoveNotRookOrBishop() {
        // Move queen to (2, 5) which is not a straight or diagonal move
        assertFalse(whiteQueen.isPseudoLegalMove(new Position(2, 5), new MoveContext(turnCount, board)));
        // Black queen to (6, 5) which is not a straight or diagonal move
        assertFalse(blackQueen.isPseudoLegalMove(new Position(6, 5), new MoveContext(turnCount, board)));
    }

    @Test
    public void testMoveToSamePosition() {
        // Move queen to its own position
        assertFalse(whiteQueen.isPseudoLegalMove(new Position(3, 3), new MoveContext(turnCount, board)));
        // Black queen to its own position
        assertFalse(blackQueen.isPseudoLegalMove(new Position(4, 4), new MoveContext(turnCount, board)));
    }

    @Test
    public void testMoveBlockedBySameColorPiece() {
        // Place a white piece at (3, 5)
        board[3][5] = new DummyPiece(new Position(3, 5), Color.White);
        assertFalse(whiteQueen.isPseudoLegalMove(new Position(3, 5), new MoveContext(turnCount, board)));
        // Place a black piece at (4, 2)
        board[4][2] = new DummyPiece(new Position(4, 2), Color.Black);
        assertFalse(blackQueen.isPseudoLegalMove(new Position(4, 2), new MoveContext(turnCount, board)));
    }

    @Test
    public void testMoveCapturesOpponentPiece() {
        // Place a black piece at (3, 5)
        board[3][5] = new DummyPiece(new Position(3, 5), Color.Black);
        assertTrue(whiteQueen.isPseudoLegalMove(new Position(3, 5), new MoveContext(turnCount, board)));
        // Place a white piece at (4, 2)
        board[4][2] = new DummyPiece(new Position(4, 2), Color.White);
        assertTrue(blackQueen.isPseudoLegalMove(new Position(4, 2), new MoveContext(turnCount, board)));
    }

    @Test
    public void testMoveBlockedByPieceInPath() {
        // Place a piece between queen and target
        board[3][5] = new DummyPiece(new Position(3, 5), Color.Black);
        // Queen tries to move to (3, 7) but path is blocked at (3, 5)
        assertFalse(whiteQueen.isPseudoLegalMove(new Position(3, 7), new MoveContext(turnCount, board)));
        // Place a piece between black queen and target
        board[4][2] = new DummyPiece(new Position(4, 2), Color.White);
        // Black queen tries to move to (4, 0) but path is blocked at (4, 2)
        assertFalse(blackQueen.isPseudoLegalMove(new Position(4, 0), new MoveContext(turnCount, board)));
    }

    @Test
    public void testgeneratePseudoLegalMoves() {
        MoveContext mContext = new MoveContext(turnCount, board);
        Set<Position> whiteMoves = whiteQueen.generatePseudoLegalMoves(mContext);
        Set<Position> blackMoves = blackQueen.generatePseudoLegalMoves(mContext);

        assertEquals(23, whiteMoves.size());
        assertEquals(23, blackMoves.size());
    }
}
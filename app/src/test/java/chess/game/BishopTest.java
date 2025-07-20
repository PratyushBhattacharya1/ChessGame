package chess.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

public class BishopTest {

    Position wBPos = new Position(2, 2);
    Position bBPos = new Position(4, 3);
    private Bishop wB;
    private Bishop bB;
    private Piece[][] board;
    private MoveContext context;
    private static final int BOARD_SIZE = 8;


    @BeforeEach
    public void setUp() {
        // Setup code for Bishop tests can be added here if needed
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        wB = new Bishop(wBPos, Color.White);
        bB = new Bishop(bBPos, Color.Black);
        context = new MoveContext(1, board);
    }

    @Test
    public void testValidMove() {
        // Test valid diagonal moves for white bishop
        assertTrue(wB.isPseudoLegalMove(new Position(wBPos.getRow() - 1, wBPos.getColumn() - 1), context));
        assertTrue(wB.isPseudoLegalMove(new Position(wBPos.getRow() + 1, wBPos.getColumn() + 1), context));
        // Test valid diagonal moves for black bishop
        assertTrue(bB.isPseudoLegalMove(new Position(bBPos.getRow() + 1, bBPos.getColumn() + 1), context));
        assertTrue(bB.isPseudoLegalMove(new Position(bBPos.getRow() - 1, bBPos.getColumn() - 1), context));
    }

    @Test
    public void testInvalidMove() {
        // Test invalid moves that are not diagonal
        assertFalse(wB.isPseudoLegalMove(new Position(wBPos.getRow(), wBPos.getColumn() + 1), context));
        assertFalse(bB.isPseudoLegalMove(new Position(bBPos.getRow() + 2, bBPos.getColumn()), context));
    }

    @Test
    public void testMoveBlockedByPieceInPath() {
        // Place a piece in the path of the white bishop
        Position blockerPos = new Position(wBPos.getRow() + 1, wBPos.getColumn() + 1);
        Piece blocker = new Rook(blockerPos, Color.White);
        board[blockerPos.getRow()][blockerPos.getColumn()] = blocker;
        assertFalse(wB.isPseudoLegalMove(blockerPos, context));                             // Blocked by own piece
        assertFalse(wB.isPseudoLegalMove(
            new Position(blockerPos.getRow() + 1, blockerPos.getColumn() + 1), context));   // Blocked by own piece
    }

    @Test
    public void generatePseudoLegalMoves() {
        Position blockerPos = new Position(BOARD_SIZE - 1, BOARD_SIZE - 1);
        Piece blocker = new DummyPiece(blockerPos, Color.White);
        board[blockerPos.getRow()][blockerPos.getColumn()] = blocker;

        Set<Position> moves = wB.generatePseudoLegalMoves(context);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                Position pos = new Position(i, j);
                if (i == blockerPos.getRow() && j == blockerPos.getColumn()) {
                    assertFalse(moves.contains(pos), "Blocked" + pos);
                }
                else if (Math.abs(j - wBPos.getColumn()) == Math.abs(i - wBPos.getRow()) 
                        && Math.abs(i - wBPos.getRow()) != 0) {
                    assertTrue(moves.contains(pos), "Expected move to be generated: " + pos);
                } else {
                    assertFalse(moves.contains(pos), "Empty square" + pos);
                }
            }
        }
    }
}

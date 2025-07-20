package chess.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

public class RookTest {

    private static final int BOARD_SIZE = 8;
    private static final int FIRST_ROW = 0;
    private static final int FIRST_COL = 0;
    private static final int LAST_ROW = BOARD_SIZE - 1;
    private static final int LAST_COL = BOARD_SIZE - 1;
    private static final int MID_ROW = 3;
    private static final int MID_COL = 3;
    private static final int HORIZONTAL_TARGET_COL = 5;
    private static final int VERTICAL_TARGET_ROW = 5;
    private static final int BLOCKER_COL = 3;
    private static final int BEYOND_BLOCKER_COL = 4;

    private Rook whiteRook;
    private Rook blackRook;
    private Piece[][] board;
    private MoveContext context;

    @BeforeEach
    public void setUp() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        whiteRook = new Rook(new Position(FIRST_ROW, FIRST_COL), Color.White);
        blackRook = new Rook(new Position(LAST_ROW, LAST_COL), Color.Black);
        board[FIRST_ROW][FIRST_COL] = whiteRook;
        board[LAST_ROW][LAST_COL] = blackRook;
        int turnCount = 1;
        // Initialize MoveContext with the board and color
        context = new MoveContext(turnCount, board);
    }

    @Test
    public void testHasMovedInitiallyFalse() {
        assertFalse(whiteRook.hasMoved());
    }

    @Test
    public void testMoveSetsHasMovedTrue() {
        Position newPos = new Position(FIRST_ROW, HORIZONTAL_TARGET_COL);
        whiteRook.move(newPos, context);
        assertTrue(whiteRook.hasMoved());
    }

    @Test
    public void testIsPseudoLegalMove_ValidHorizontal() {
        Position target = new Position(FIRST_ROW, HORIZONTAL_TARGET_COL);
        assertTrue(whiteRook.isPseudoLegalMove(target, context));
    }

    @Test
    public void testIsPseudoLegalMove_ValidVertical() {
        Position target = new Position(VERTICAL_TARGET_ROW, FIRST_COL);
        assertTrue(whiteRook.isPseudoLegalMove(target, context));
    }

    @Test
    public void testIsPseudoLegalMove_InvalidDiagonal() {
        Position target = new Position(MID_ROW, MID_COL);
        assertFalse(whiteRook.isPseudoLegalMove(target, context));
    }

    @Test
    public void testGeneratePseudoLegalMoves_EmptyBoard() {
        Set<Position> moves = whiteRook.generatePseudoLegalMoves(context);
        // From (0,0), rook can move to (0,1)-(0,7) and (1,0)-(7,0): 14 moves
        assertEquals(Rook.MAX_MOVES, moves.size());
        assertTrue(moves.contains(new Position(FIRST_ROW, LAST_COL)));
        assertTrue(moves.contains(new Position(LAST_ROW, FIRST_COL)));
    }

    @Test
    public void testGeneratePseudoLegalMoves_BlockedByOwnPiece() {
        // Place a white piece at (0,3)
        Piece blocker = new Rook(new Position(FIRST_ROW, BLOCKER_COL), Color.White);
        board[FIRST_ROW][BLOCKER_COL] = blocker;
        Set<Position> moves = whiteRook.generatePseudoLegalMoves(context);
        // Should not include (0,3) or any squares beyond
        assertFalse(moves.contains(new Position(FIRST_ROW, BLOCKER_COL)));
        assertFalse(moves.contains(new Position(FIRST_ROW, BEYOND_BLOCKER_COL)));
    }

    @Test
    public void testGeneratePseudoLegalMoves_CanCaptureOpponent() {
        // Place a black piece at (0,3)
        Piece opponent = new Rook(new Position(FIRST_ROW, BLOCKER_COL), Color.Black);
        board[FIRST_ROW][BLOCKER_COL] = opponent;
        Set<Position> moves = whiteRook.generatePseudoLegalMoves(context);
        // Should include (0,3) but not (0,4)
        assertTrue(moves.contains(new Position(FIRST_ROW, BLOCKER_COL)));
        assertFalse(moves.contains(new Position(FIRST_ROW, BEYOND_BLOCKER_COL)));
    }
}
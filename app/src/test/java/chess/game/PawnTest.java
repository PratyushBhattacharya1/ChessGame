package chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

public class PawnTest {
    static final Position defaultWhitePosition = new Position(6, 4);
    static final Position defaultBlackPosition = new Position(1, 5);
    Pawn whitePawn = new Pawn(defaultWhitePosition, Color.White);
    Pawn blackPawn = new Pawn(defaultBlackPosition, Color.Black);
    Piece[][] board = new Piece[8][8];
    int turnCount = 1;
    int oneUpWhite = 5;
    int twoUpWhite = 4;
    int oneUpBlack = 2;
    int twoUpBlack = 3;

    @BeforeEach
    public void setUp() {
        board[defaultWhitePosition.getRow()][defaultWhitePosition.getColumn()] = whitePawn;
        board[defaultBlackPosition.getRow()][defaultBlackPosition.getColumn()] = blackPawn;
    }

    @Test
    public void testValidMovesUnobstructed() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                Position p = new Position(i, j);
                MoveContext mContext = new MoveContext(turnCount, board);

                if ((i == twoUpWhite || i == oneUpWhite) && j == defaultWhitePosition.getColumn()) {
                    assertTrue(whitePawn.isPseudoLegalMove(p, mContext));
                } else if ((i == twoUpBlack || i == oneUpBlack) && j == defaultBlackPosition.getColumn()) {
                    assertTrue(blackPawn.isPseudoLegalMove(p, mContext));
                } else {
                    assertFalse(blackPawn.isPseudoLegalMove(p, mContext));
                    assertFalse(whitePawn.isPseudoLegalMove(p, mContext));
                }
            }
        }
    }

    @Test
    public void testValidMovesObstructed() {
        DummyPiece dummy = new DummyPiece(null, Color.White);
        board[oneUpWhite][defaultWhitePosition.getColumn()] = dummy;

        DummyPiece dummy2 = new DummyPiece(null, Color.Black);
        board[oneUpBlack][defaultBlackPosition.getColumn()] = dummy2;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                Position p = new Position(i, j);
                MoveContext mContext = new MoveContext(turnCount, board);
                assertFalse(whitePawn.isPseudoLegalMove(p, mContext));
                assertFalse(blackPawn.isPseudoLegalMove(p, mContext));
            }
        }

        board[oneUpWhite][defaultWhitePosition.getColumn()] = null;
        board[oneUpBlack][defaultBlackPosition.getColumn()] = null;
        board[twoUpWhite][defaultWhitePosition.getColumn()] = dummy;
        board[twoUpBlack][defaultBlackPosition.getColumn()] = dummy2;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                Position p = new Position(i, j);
                MoveContext mContext = new MoveContext(turnCount, board);

                if (i == oneUpWhite && j == defaultWhitePosition.getColumn()) {
                    assertTrue(whitePawn.isPseudoLegalMove(p, mContext));
                } else if (i == oneUpBlack && j == defaultBlackPosition.getColumn()) {
                    assertTrue(blackPawn.isPseudoLegalMove(p, mContext));
                } else {
                    assertFalse(whitePawn.isPseudoLegalMove(p, mContext));
                    assertFalse(blackPawn.isPseudoLegalMove(p, mContext));
                }
            }
        }


    }

    @Test
    public void testDiagonalCapture() {
        DummyPiece dummyRightWhite = new DummyPiece(null, Color.Black);
        board[oneUpWhite][defaultWhitePosition.getColumn() + 1] = dummyRightWhite;

        DummyPiece dummyLeftWhite = new DummyPiece(null, Color.Black);
        board[oneUpWhite][defaultWhitePosition.getColumn() - 1] = dummyLeftWhite;

        DummyPiece dummyRightBlack = new DummyPiece(null, Color.White);
        board[oneUpBlack][defaultBlackPosition.getColumn() + 1] = dummyRightBlack;

        DummyPiece dummyLeftBlack = new DummyPiece(null, Color.White);
        board[oneUpBlack][defaultBlackPosition.getColumn() - 1] = dummyLeftBlack;

        Position targetPositionLeftWhite = new Position(oneUpWhite, defaultWhitePosition.getColumn() + 1);
        Position targetPositionRightWhite = new Position(oneUpWhite, defaultWhitePosition.getColumn() - 1);
        Position targetPositionLeftBlack = new Position(oneUpBlack, defaultBlackPosition.getColumn() - 1);
        Position targetPositionRightBlack = new Position(oneUpBlack, defaultBlackPosition.getColumn() + 1);
        MoveContext mContext = new MoveContext(turnCount, board);

        assertTrue(whitePawn.isPseudoLegalMove(targetPositionLeftWhite, mContext));
        assertTrue(whitePawn.isPseudoLegalMove(targetPositionRightWhite, mContext));
        assertTrue(blackPawn.isPseudoLegalMove(targetPositionLeftBlack, mContext));
        assertTrue(blackPawn.isPseudoLegalMove(targetPositionRightBlack, mContext));
    }

    @Test
    public void testDiagonalBlocked() {
        DummyPiece dummyRightWhite = new DummyPiece(null, Color.White);
        board[oneUpWhite][defaultWhitePosition.getColumn() + 1] = dummyRightWhite;

        DummyPiece dummyLeftWhite = new DummyPiece(null, Color.White);
        board[oneUpWhite][defaultWhitePosition.getColumn() - 1] = dummyLeftWhite;

        DummyPiece dummyRightBlack = new DummyPiece(null, Color.Black);
        board[oneUpBlack][defaultBlackPosition.getColumn() + 1] = dummyRightBlack;

        DummyPiece dummyLeftBlack = new DummyPiece(null, Color.Black);
        board[oneUpBlack][defaultBlackPosition.getColumn() - 1] = dummyLeftBlack;

        Position targetPositionLeftWhite = new Position(oneUpWhite, defaultWhitePosition.getColumn() + 1);
        Position targetPositionRightWhite = new Position(oneUpWhite, defaultWhitePosition.getColumn() - 1);
        Position targetPositionLeftBlack = new Position(oneUpBlack, defaultBlackPosition.getColumn() - 1);
        Position targetPositionRightBlack = new Position(oneUpBlack, defaultBlackPosition.getColumn() + 1);
        MoveContext mContext = new MoveContext(turnCount, board);

        assertFalse(whitePawn.isPseudoLegalMove(targetPositionLeftWhite, mContext));
        assertFalse(whitePawn.isPseudoLegalMove(targetPositionRightWhite, mContext));
        assertFalse(blackPawn.isPseudoLegalMove(targetPositionLeftBlack, mContext));
        assertFalse(blackPawn.isPseudoLegalMove(targetPositionRightBlack, mContext));
    }

    @Test
    public void testEnPassant() {
        // Clear board
        board[defaultWhitePosition.getRow()][defaultWhitePosition.getColumn()] = null;
        board[defaultBlackPosition.getRow()][defaultBlackPosition.getColumn()] = null;

        Position blackPawnLeftPosition = new Position(twoUpBlack, defaultBlackPosition.getColumn() - 1);
        Position blackPawnRightPosition = new Position(twoUpBlack, defaultBlackPosition.getColumn() + 1);
        Pawn blackPawnLeft = new Pawn(blackPawnLeftPosition, Color.Black);
        Pawn blackPawnRight = new Pawn(blackPawnRightPosition, Color.Black);
        board[blackPawnLeftPosition.getRow()][blackPawnLeftPosition.getColumn()] = blackPawnLeft;
        board[blackPawnRightPosition.getRow()][blackPawnRightPosition.getColumn()] = blackPawnRight;
        blackPawnLeft.setEnPassantTurn(0);
        blackPawnRight.setEnPassantTurn(0);

        Position whitePawnLeftPosition = new Position(twoUpWhite, defaultWhitePosition.getColumn() - 1);
        Position whitePawnRightPosition = new Position(twoUpWhite, defaultWhitePosition.getColumn() + 1);
        Pawn whitePawnLeft = new Pawn(whitePawnLeftPosition, Color.White);
        Pawn whitePawnRight = new Pawn(whitePawnRightPosition, Color.White);
        board[whitePawnLeftPosition.getRow()][whitePawnLeftPosition.getColumn()] = whitePawnLeft;
        board[whitePawnRightPosition.getRow()][whitePawnRightPosition.getColumn()] = whitePawnRight;
        whitePawnLeft.setEnPassantTurn(turnCount);
        whitePawnRight.setEnPassantTurn(turnCount);

        Position whiteCapturerPosition = new Position(twoUpBlack, defaultBlackPosition.getColumn());
        Pawn whiteCapturer = new Pawn(whiteCapturerPosition, Color.White);
        board[whiteCapturerPosition.getRow()][whiteCapturerPosition.getColumn()] = whiteCapturer;

        Position blackCapturerPosition = new Position(twoUpWhite, defaultWhitePosition.getColumn());
        Pawn blackCapturer = new Pawn(blackCapturerPosition, Color.Black);
        board[blackCapturerPosition.getRow()][blackCapturerPosition.getColumn()] = blackCapturer;

        MoveContext mContext = new MoveContext(turnCount, board);

        assertTrue(whiteCapturer.isPseudoLegalMove(new Position(oneUpBlack, defaultBlackPosition.getColumn() - 1), mContext));
        assertTrue(whiteCapturer.isPseudoLegalMove(new Position(oneUpBlack, defaultBlackPosition.getColumn() + 1), mContext));
        assertTrue(blackCapturer.isPseudoLegalMove(new Position(oneUpWhite, defaultWhitePosition.getColumn() - 1), mContext));
        assertTrue(blackCapturer.isPseudoLegalMove(new Position(oneUpWhite, defaultWhitePosition.getColumn() + 1), mContext));

        board[blackPawnLeftPosition.getRow()][blackPawnLeftPosition.getColumn()] = 
            new Pawn(blackPawnLeftPosition, Color.White);
        blackPawnRight.setEnPassantTurn(turnCount - 99);
        assertFalse(whiteCapturer.isPseudoLegalMove(new Position(oneUpBlack, defaultBlackPosition.getColumn() - 1), mContext));
        assertFalse(whiteCapturer.isPseudoLegalMove(new Position(oneUpBlack, defaultBlackPosition.getColumn() + 1), mContext));
    }

    @Test
    public void testgeneratePseudoLegalMoves() {
        Set<Position> expectedWhiteMoves = new HashSet<>();
        expectedWhiteMoves.add(new Position(twoUpWhite, defaultWhitePosition.getColumn()));
        expectedWhiteMoves.add(new Position(oneUpWhite, defaultWhitePosition.getColumn()));

        Set<Position> expectedBlackMoves = new HashSet<>();
        expectedBlackMoves.add(new Position(oneUpBlack, defaultBlackPosition.getColumn()));
        expectedBlackMoves.add(new Position(twoUpBlack, defaultBlackPosition.getColumn()));

        MoveContext mContext = new MoveContext(turnCount, board);

        Set<Position> actualWhiteMoves = whitePawn.generatePseudoLegalMoves(mContext);
        Set<Position> actualBlackMoves = blackPawn.generatePseudoLegalMoves(mContext);

        // Two squares up
        assertEquals(expectedWhiteMoves, actualWhiteMoves);
        assertEquals(expectedBlackMoves, actualBlackMoves);

        board[twoUpWhite][defaultWhitePosition.getColumn()] = new DummyPiece(null, Color.White);
        board[twoUpBlack][defaultBlackPosition.getColumn()] = new DummyPiece(null, Color.Black);
        expectedWhiteMoves.remove(new Position(twoUpWhite, defaultWhitePosition.getColumn()));
        expectedBlackMoves.remove(new Position(twoUpBlack, defaultBlackPosition.getColumn()));

        actualWhiteMoves = whitePawn.generatePseudoLegalMoves(mContext);
        actualBlackMoves = blackPawn.generatePseudoLegalMoves(mContext);

        // One square up
        assertEquals(expectedWhiteMoves, actualWhiteMoves);
        assertEquals(expectedBlackMoves, actualBlackMoves);

        board[oneUpWhite][defaultWhitePosition.getColumn()] = new DummyPiece(null, Color.White);
        board[oneUpBlack][defaultBlackPosition.getColumn()] = new DummyPiece(null, Color.Black);
        expectedWhiteMoves.remove(new Position(oneUpWhite, defaultWhitePosition.getColumn()));
        expectedBlackMoves.remove(new Position(oneUpBlack, defaultBlackPosition.getColumn()));

        actualWhiteMoves = whitePawn.generatePseudoLegalMoves(mContext);
        actualBlackMoves = blackPawn.generatePseudoLegalMoves(mContext);

        // No squares up
        assertEquals(expectedWhiteMoves, actualWhiteMoves);
        assertEquals(expectedBlackMoves, actualBlackMoves);

        board[oneUpWhite][defaultWhitePosition.getColumn() - 1] = new DummyPiece(null, Color.Black);
        board[oneUpBlack][defaultBlackPosition.getColumn() - 1] = new DummyPiece(null, Color.White);
        board[oneUpWhite][defaultWhitePosition.getColumn() + 1] = new DummyPiece(null, Color.Black);
        board[oneUpBlack][defaultBlackPosition.getColumn() + 1] = new DummyPiece(null, Color.White);
        expectedWhiteMoves.add(new Position(oneUpWhite, defaultWhitePosition.getColumn() - 1));
        expectedBlackMoves.add(new Position(oneUpBlack, defaultBlackPosition.getColumn() - 1));
        expectedWhiteMoves.add(new Position(oneUpWhite, defaultWhitePosition.getColumn() + 1));
        expectedBlackMoves.add(new Position(oneUpBlack, defaultBlackPosition.getColumn() + 1));

        actualWhiteMoves = whitePawn.generatePseudoLegalMoves(mContext);
        actualBlackMoves = blackPawn.generatePseudoLegalMoves(mContext);

        // One square left diagonal
        assertEquals(expectedWhiteMoves, actualWhiteMoves);
        assertEquals(expectedBlackMoves, actualBlackMoves);
    }
}

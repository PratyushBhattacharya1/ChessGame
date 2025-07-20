package chess.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

public class KnightTest {

    Piece[][] board = new Piece[8][8];
    Position defaultKnightPosition = new Position(4, 4);
    Knight knight = new Knight(defaultKnightPosition, Color.White);
    int turnCount = 1;
    MoveContext mContext = new MoveContext(turnCount, board);

    @Test
    public void testValidKnightMoveEmptyTarget() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int rowdiff = Math.abs(defaultKnightPosition.getRow() - i),
                coldiff = Math.abs(defaultKnightPosition.getColumn() - j);

                if (!((rowdiff == 2 && coldiff == 1) || (rowdiff == 1 && coldiff == 2))) 
                    assertFalse(knight.isPseudoLegalMove(new Position(i, j), new MoveContext(turnCount, board)));
                else assertTrue(knight.isPseudoLegalMove(new Position(i, j), new MoveContext(turnCount, board)));
            }
        }
    }

    @Test
    public void testInvalidKnightMoveOwnPieceAtTarget() {
        // Place own piece at target
        Position ownPiecePosition = new Position(6, 5);
        board[6][5] = new DummyPiece(ownPiecePosition, Color.White);

        assertFalse(knight.isPseudoLegalMove(ownPiecePosition, new MoveContext(turnCount, board)));
    }

    @Test
    public void testValidKnightMoveOpponentPieceAtTarget() {
        // Place opponent's piece at target
        Position opponentPiecePosition = new Position(6, 5);
        board[6][5] = new DummyPiece(opponentPiecePosition, Color.Black);

        assertTrue(knight.isPseudoLegalMove(opponentPiecePosition, new MoveContext(turnCount, board)));
    }

    @Test
    public void testgeneratePseudoLegalMoves() {
        Set<Position> expected = new HashSet<>();
        expected.add(new Position(2, 3));
        expected.add(new Position(3, 2));
        expected.add(new Position(5, 6));
        expected.add(new Position(5, 2));
        expected.add(new Position(6, 3));
        expected.add(new Position(6, 5));
        expected.add(new Position(2, 5));
        expected.add(new Position(3, 6));

        assertEquals(expected, knight.generatePseudoLegalMoves(mContext));
        board[6][5] = new DummyPiece(null, Color.White);
        board[2][5] = new DummyPiece(null, Color.Black);

        expected.remove(new Position(6, 5));

        assertEquals(expected, knight.generatePseudoLegalMoves(mContext));

        Position rimKnightPosition = new Position(5, 0);
        var rimKnight = new Knight(rimKnightPosition, Color.White);
        board[rimKnightPosition.getRow()][rimKnightPosition.getColumn()] = rimKnight;

        expected.clear();
        expected.add(new Position(3, 1));
        expected.add(new Position(4, 2));
        expected.add(new Position(7, 1));
        expected.add(new Position(6, 2));

        assertEquals(expected, rimKnight.generatePseudoLegalMoves(mContext));

    }

    @Test
    public void testGenerateLegalHorsePositions() {
        // int[][] expected = {
        //     {2, 3},
        //     {3, 2},
        //     {5, 6},
        //     {5, 2},
        //     {6, 3},
        //     {6, 5},
        //     {2, 5},
        //     {3, 6}
        // };

        assertEquals(8, Knight.generateHorsePositions(
            defaultKnightPosition.getRow(), defaultKnightPosition.getColumn()).length);

        Position rimKnightPosition = new Position(5, 0);
        var rimKnight = new Knight(rimKnightPosition, Color.White);
        board[rimKnightPosition.getRow()][rimKnightPosition.getColumn()] = rimKnight;

        assertThrows(IllegalArgumentException.class, () -> Knight.generateHorsePositions(-1, -1));
    }
}
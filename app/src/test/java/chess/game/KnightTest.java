package chess.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KnightTest {

    Piece[][] board = new Piece[8][8];
    Position defaultKnightPosition = new Position(4, 4);
    Knight knight = new Knight(defaultKnightPosition, Color.White);
    int turnCount = 1;

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
}
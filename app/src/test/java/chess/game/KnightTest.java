package chess.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KnightTest {

    Piece[][] board = new Piece[8][8];
    Knight knight = new Knight(new Position(4, 4), Color.W);

    @Test
    public void testValidKnightMoveEmptyTarget() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int rowdiff = Math.abs(4 - i),
                coldiff = Math.abs(4 - j);

                if (!((rowdiff == 2 && coldiff == 1) || (rowdiff == 1 && coldiff == 2))) assertFalse(knight.isValidMove(new Position(i, j), board, 0));
                else assertTrue(knight.isValidMove(new Position(i, j), board, 0));  
            }
        }
    }

    @Test
    public void testInvalidKnightMoveOwnPieceAtTarget() {
        // Place own piece at target
        board[6][5] = new DummyPiece(new Position(6, 5), Color.W);

        assertFalse(knight.isValidMove(new Position(6, 5), board, 0));
    }

    @Test
    public void testValidKnightMoveOpponentPieceAtTarget() {
        // Place opponent's piece at target
        board[6][5] = new DummyPiece(new Position(6, 5), Color.B);

        assertTrue(knight.isValidMove(new Position(6, 5), board, 0));
    }
}
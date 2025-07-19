package chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;

public class KingTest {
    static final int BOARD_SIZE = 8;
    Position whiteKingPosition = new Position(6, 4);
    Position blackKingPosition = new Position(1, 4);
    private King whiteKing = new King(whiteKingPosition, Color.White);
    private King blackKing = new King(blackKingPosition, Color.Black);
    private Piece[][] board = new Piece[BOARD_SIZE][BOARD_SIZE];
    int turnCount = 1;
    MoveContext mContext = new MoveContext(turnCount, board);

    @BeforeEach
    public void setUp() {
        board[whiteKingPosition.getRow()][whiteKingPosition.getColumn()] = whiteKing;
        board[blackKingPosition.getRow()][blackKingPosition.getColumn()] = blackKing;
    }


    @Test
    public void testValidKingMoves() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                Position p = new Position(i, j);

                int rowdiffWhite = Math.abs(whiteKingPosition.getRow() - i);
                int coldiffWhite = Math.abs(whiteKingPosition.getColumn() - j);
                int rowdiffBlack = Math.abs(blackKingPosition.getRow() - i);
                int coldiffBlack = Math.abs(blackKingPosition.getColumn() - j);

                if (rowdiffWhite <= 1 && coldiffWhite <= 1 && !(i == whiteKingPosition.getRow() && j == whiteKingPosition.getColumn())) {
                    assertTrue(whiteKing.isPseudoLegalMove(p, mContext), "White King should be able to move to " + p);
                } else {
                    assertFalse(whiteKing.isPseudoLegalMove(p, mContext), "White King should not be able to move to " + p);
                }
                if (rowdiffBlack <= 1 && coldiffBlack <= 1 && !(i == blackKingPosition.getRow() && j == blackKingPosition.getColumn())) {
                    assertTrue(blackKing.isPseudoLegalMove(p, mContext), "Black King should be able to move to " + p);
                } else {
                    assertFalse(blackKing.isPseudoLegalMove(p, mContext), "Black King should not be able to move to " + p);
                }
            }
        }
    }
}

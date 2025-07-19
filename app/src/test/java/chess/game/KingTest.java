package chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

                if (rowdiffWhite <= 1 && coldiffWhite <= 1 
                    && !(i == whiteKingPosition.getRow() && j == whiteKingPosition.getColumn())) {
                    assertTrue(whiteKing.isPseudoLegalMove(p, mContext), 
                        "White King should be able to move to " + p);
                } else {
                    assertFalse(whiteKing.isPseudoLegalMove(p, mContext), 
                        "White King should not be able to move to " + p);
                }
                if (rowdiffBlack <= 1 && coldiffBlack <= 1 
                    && !(i == blackKingPosition.getRow() && j == blackKingPosition.getColumn())) {
                    assertTrue(blackKing.isPseudoLegalMove(p, mContext), 
                        "Black King should be able to move to " + p);
                } else {
                    assertFalse(blackKing.isPseudoLegalMove(p, mContext), 
                        "Black King should not be able to move to " + p);
                }
            }
        }
    }

    @Test
    public void testValidKingMovesObstructed() {
        // Place a piece in the way of the white king
        Position obstructedPositionRight = 
            new Position(whiteKingPosition.getRow(), whiteKingPosition.getColumn() + 1);
        board[obstructedPositionRight.getRow()][obstructedPositionRight.getColumn()] = 
            new DummyPiece(null, Color.White);

        Position obstructedPositionDownRight = 
            new Position(whiteKingPosition.getRow() - 1, whiteKingPosition.getColumn() + 1);
        board[obstructedPositionDownRight.getRow()][obstructedPositionDownRight.getColumn()] = 
            new DummyPiece(null, Color.White);

        Position obstructedPositionDownLeft = 
            new Position(whiteKingPosition.getRow() - 1, whiteKingPosition.getColumn() - 1);
        board[obstructedPositionDownLeft.getRow()][obstructedPositionDownLeft.getColumn()] = 
            new DummyPiece(null, Color.White);

        Position obstructedPositionUpLeft = 
            new Position(whiteKingPosition.getRow() + 1, whiteKingPosition.getColumn() - 1);
        board[obstructedPositionUpLeft.getRow()][obstructedPositionUpLeft.getColumn()] = 
            new DummyPiece(null, Color.White);

        Position obstructedPositionUpRight = 
            new Position(whiteKingPosition.getRow() + 1, whiteKingPosition.getColumn() + 1);
        board[obstructedPositionUpRight.getRow()][obstructedPositionUpRight.getColumn()] = 
            new DummyPiece(null, Color.White);

        Position obstructedPositionDown = 
            new Position(whiteKingPosition.getRow(), whiteKingPosition.getColumn() + 1);
        board[obstructedPositionDown.getRow()][obstructedPositionDown.getColumn()] = 
            new DummyPiece(null, Color.White);

        Position obstructedPositionUp = 
            new Position(whiteKingPosition.getRow() + 1, whiteKingPosition.getColumn());
        board[obstructedPositionUp.getRow()][obstructedPositionUp.getColumn()] = 
            new DummyPiece(null, Color.White);

        Position obstructedPositionLeft = 
            new Position(whiteKingPosition.getRow(), whiteKingPosition.getColumn() - 1);
        board[obstructedPositionLeft.getRow()][obstructedPositionLeft.getColumn()] = 
            new DummyPiece(null, Color.White);


        // Check that the king cannot move to obstructed position
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Position p = new Position(i, j);
                if (board[i][j] instanceof DummyPiece) {
                    assertFalse(whiteKing.isPseudoLegalMove(p, mContext), 
                        "White King should not be able to move to obstructed position " + p);
                }
            }   
        }
    }

    @Test
    public void testKingCannotMoveToOwnPosition() {
        assertFalse(whiteKing.isPseudoLegalMove(whiteKingPosition, mContext), 
            "White King should not be able to move to its own position");
        assertFalse(blackKing.isPseudoLegalMove(blackKingPosition, mContext), 
            "Black King should not be able to move to its own position");
    }

    @Test
    public void testIsInCheckRook() {
        board[blackKingPosition.getRow()][blackKingPosition.getColumn()] = null;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (i == whiteKingPosition.getRow() && j == whiteKingPosition.getColumn()) continue;
                board[i][j] = new Rook(new Position(i, j), Color.Black);

                if (i == whiteKingPosition.getRow() ^ j == whiteKingPosition.getColumn()) {
                    assertTrue(whiteKing.isInCheck(mContext), 
                        "White King should be in check from Rook at " + new Position(i, j));
                } else {
                    assertFalse(whiteKing.isInCheck(mContext), 
                        "White King should not be in check from Rook at " + new Position(i, j));
                }
                board[i][j] = null;
            }
        }
    }

    @Test
    public void testIsInCheckBishop() {
        board[blackKingPosition.getRow()][blackKingPosition.getColumn()] = null;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (i == whiteKingPosition.getRow() && j == whiteKingPosition.getColumn()) continue;
                board[i][j] = new Bishop(new Position(i, j), Color.Black);
                    
                if (Math.abs(i - whiteKingPosition.getRow()) == Math.abs(j - whiteKingPosition.getColumn())) {
                    assertTrue(whiteKing.isInCheck(mContext), 
                        "White King should be in check from Bishop at " + new Position(i, j));
                } else {
                    assertFalse(whiteKing.isInCheck(mContext), 
                        "White King should not be in check from Bishop at " + new Position(i, j));
                }
                board[i][j] = null;
            }
        }
    }

    @Test
    public void testInCheckQueen() {
        board[blackKingPosition.getRow()][blackKingPosition.getColumn()] = null;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (i == whiteKingPosition.getRow() && j == whiteKingPosition.getColumn()) continue;

                board[i][j] = new Queen(new Position(i, j), Color.Black);

                if ((i == whiteKingPosition.getRow() ^ j == whiteKingPosition.getColumn())
                    || (Math.abs(i - whiteKingPosition.getRow()) == Math.abs(j - whiteKingPosition.getColumn()))) {
                    assertTrue(whiteKing.isInCheck(mContext), 
                        "White King should be in check from Queen at " + new Position(i, j));
                } else {
                    assertFalse(whiteKing.isInCheck(mContext), 
                        "White King should not be in check from Queen at " + new Position(i, j));
                }

                board[i][j] = null;
            }
        }
    }

    @Test
    public void testInCheckKnight() {
        board[blackKingPosition.getRow()][blackKingPosition.getColumn()] = null;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (i == whiteKingPosition.getRow() && j == whiteKingPosition.getColumn()) continue;

                board[i][j] = new Knight(new Position(i, j), Color.Black);

                if ((Math.abs(i - whiteKingPosition.getRow()) == 2 
                    && Math.abs(j - whiteKingPosition.getColumn()) == 1)
                    || (Math.abs(i - whiteKingPosition.getRow()) == 1 
                    && Math.abs(j - whiteKingPosition.getColumn()) == 2)) {
                    assertTrue(whiteKing.isInCheck(mContext), 
                        "White King should be in check from Knight at " + new Position(i, j));
                } else {
                    assertFalse(whiteKing.isInCheck(mContext), 
                        "White King should not be in check from Knight at " + new Position(i, j));
                }

                board[i][j] = null;
            }
        }
    }

    @Test
    public void testInCheckPawn() {
        board[blackKingPosition.getRow()][blackKingPosition.getColumn()] = null;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (i == whiteKingPosition.getRow() && j == whiteKingPosition.getColumn()) continue;

                board[i][j] = new Pawn(new Position(i, j), Color.Black);

                if (i == whiteKingPosition.getRow() - 1 && (j == whiteKingPosition.getColumn() - 1
                    || j == whiteKingPosition.getColumn() + 1)) {
                    assertTrue(whiteKing.isInCheck(mContext), 
                        "White King should be in check from Pawn at " + new Position(i, j));
                } else {
                    assertFalse(whiteKing.isInCheck(mContext), 
                        "White King should not be in check from Pawn at " + new Position(i, j));
                }

                board[i][j] = null;
            }
        }
    }

    @Test
    public void testCanCastle() {
        // Reset the board
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        MoveContext mContext = new MoveContext(turnCount, board);
        Position whiteRookKingside = new Position(7, 7);
        Position whiteRookQueenside = new Position(7, 0);
        Position blackRookKingside = new Position(0, 7);
        Position blackRookQueenside = new Position(0, 0);
        board[whiteRookKingside.getRow()][whiteRookKingside.getColumn()] = 
            new Rook(whiteRookKingside, Color.White);
        board[whiteRookQueenside.getRow()][whiteRookQueenside.getColumn()] = 
            new Rook(whiteRookQueenside, Color.White);
        board[blackRookKingside.getRow()][blackRookKingside.getColumn()] =
            new Rook(blackRookKingside, Color.Black);
        board[blackRookQueenside.getRow()][blackRookQueenside.getColumn()] = 
            new Rook(blackRookQueenside, Color.Black);
        Position whiteKPosition = new Position(7, 4);
        Position blackKPosition = new Position(0, 4);
        whiteKing = new King(whiteKPosition, Color.White);
        blackKing = new King(blackKPosition, Color.Black);
        board[whiteKPosition.getRow()][whiteKPosition.getColumn()] = whiteKing;
        board[blackKPosition.getRow()][blackKPosition.getColumn()] = blackKing;

        // Test kingside castling
        Position kingsideTarget = new Position(7, 6);
        assertTrue(whiteKing.isPseudoLegalMove(kingsideTarget, mContext), 
            "White King should be able to castle kingside");
        
        // Test queenside castling
        Position queensideTarget = new Position(7, 2);
        assertTrue(whiteKing.isPseudoLegalMove(queensideTarget, mContext), 
            "White King should be able to castle queenside");

        // Test black king castling
        kingsideTarget = new Position(0, 6);
        assertTrue(blackKing.isPseudoLegalMove(kingsideTarget, mContext), 
            "Black King should be able to castle kingside");

        queensideTarget = new Position(0, 2);
        assertTrue(blackKing.isPseudoLegalMove(queensideTarget, mContext), 
            "Black King should be able to castle queenside");
    }
}

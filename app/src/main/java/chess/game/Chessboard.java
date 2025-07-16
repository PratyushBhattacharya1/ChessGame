package chess.game;

import java.util.ArrayList;
import java.util.Stack;

import org.checkerframework.checker.units.qual.t;

public class Chessboard {
    
    /*
     * Constants for board dimensions and piece positions
     */
    private static final int BOARD_DIMENSIONS = 8;
    private static final int QUEEN_COLUMN = 3;
    private static final int KING_COLUMN = 4;
    private static final int KINGSIDE_HORSES_COLUMN = 6;
    private static final int QUEENSIDE_HORSES_COLUMN = 1;
    private static final int KINGSIDE_BISHOPS_COLUMN = 5;
    private static final int QUEENSIDE_BISHOPS_COLUMN = 2;
    private static final int KINGSIDE_ROOKS_COLUMN = 7;
    private static final int QUEENSIDE_ROOKS_COLUMN = 0;
    private static final int WHITE_BACK_ROW = 7;
    private static final int BLACK_BACK_ROW = 0;
    private static final int BLACK_PAWN_ROW = 6;
    private static final int WHITE_PAWN_ROW = 1;
    
    /*
     * Instance variables
     */
    private Stack<Piece[][]> boardHistory;
    private King whiteKing;
    private King blackKing;
    private ArrayList<String> moveHistory;
    private GameState gameState;
    private int turnCount;
    private Color turnColor;

    /**
     * Initializes the chessboard with pieces in their starting positions.
     */
    public Chessboard() {
        this.boardHistory = new Stack<>();
        Piece[][] board = new Piece[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
        this.moveHistory = new ArrayList<>();
        this.gameState = GameState.unfinished;
        this.turnCount = 1;
        this.turnColor = Color.White;
        this.initializeBoard(board);
        this.boardHistory.push(board);
    }

    /**
     * Helper method to inialize the board with pieces in default position.
     */
    private void initializeBoard(Piece[][] board) {

        for (int i = 0; i < board.length; i++) {
            // Pawns
            board[WHITE_PAWN_ROW][i] = new Pawn(new Position(WHITE_PAWN_ROW, i), Color.Black);
            board[BLACK_PAWN_ROW][i] = new Pawn(new Position(BLACK_PAWN_ROW, i), Color.White);
        }

        // Rooks
        board[BLACK_BACK_ROW][QUEENSIDE_ROOKS_COLUMN] = 
            new Rook(new Position(BLACK_BACK_ROW, QUEENSIDE_ROOKS_COLUMN), Color.Black);
        board[BLACK_BACK_ROW][KINGSIDE_ROOKS_COLUMN] = 
            new Rook(new Position(BLACK_BACK_ROW, KINGSIDE_ROOKS_COLUMN), Color.Black);

        board[WHITE_BACK_ROW][QUEENSIDE_ROOKS_COLUMN] = 
            new Rook(new Position(WHITE_BACK_ROW, QUEENSIDE_ROOKS_COLUMN), Color.White);
        board[WHITE_BACK_ROW][KINGSIDE_ROOKS_COLUMN] = 
            new Rook(new Position(WHITE_BACK_ROW, KINGSIDE_ROOKS_COLUMN), Color.White);

        // Bishops
        board[BLACK_BACK_ROW][QUEENSIDE_BISHOPS_COLUMN] = 
            new Bishop(new Position(BLACK_BACK_ROW, QUEENSIDE_BISHOPS_COLUMN), Color.Black);
        board[BLACK_BACK_ROW][KINGSIDE_BISHOPS_COLUMN] = 
            new Bishop(new Position(BLACK_BACK_ROW, KINGSIDE_BISHOPS_COLUMN), Color.Black);

        board[WHITE_BACK_ROW][QUEENSIDE_BISHOPS_COLUMN] = 
            new Bishop(new Position(WHITE_BACK_ROW, QUEENSIDE_BISHOPS_COLUMN), Color.White);
        board[WHITE_BACK_ROW][KINGSIDE_BISHOPS_COLUMN] = 
            new Bishop(new Position(WHITE_BACK_ROW, KINGSIDE_BISHOPS_COLUMN), Color.White);

        // Knights
        board[BLACK_BACK_ROW][QUEENSIDE_HORSES_COLUMN] = 
            new Knight(new Position(BLACK_BACK_ROW, QUEENSIDE_HORSES_COLUMN), Color.Black);
        board[BLACK_BACK_ROW][KINGSIDE_HORSES_COLUMN] = 
            new Knight(new Position(BLACK_BACK_ROW, KINGSIDE_HORSES_COLUMN), Color.Black);

        board[WHITE_BACK_ROW][QUEENSIDE_HORSES_COLUMN] = 
            new Knight(new Position(WHITE_BACK_ROW, QUEENSIDE_HORSES_COLUMN), Color.White);
        board[WHITE_BACK_ROW][KINGSIDE_HORSES_COLUMN] = 
            new Knight(new Position(WHITE_BACK_ROW, KINGSIDE_HORSES_COLUMN), Color.White);

        // Queens
        board[BLACK_BACK_ROW][QUEEN_COLUMN] = 
            new Queen(new Position(BLACK_BACK_ROW, QUEEN_COLUMN), Color.Black);
        board[WHITE_BACK_ROW][QUEEN_COLUMN] = 
            new Queen(new Position(WHITE_BACK_ROW, QUEEN_COLUMN), Color.White);

        // Kings
        whiteKing = 
            new King(new Position(WHITE_BACK_ROW, KING_COLUMN), Color.White);
        blackKing = 
            new King(new Position(BLACK_BACK_ROW, KING_COLUMN), Color.Black);

        board[BLACK_BACK_ROW][KING_COLUMN] = this.blackKing;
        board[WHITE_BACK_ROW][KING_COLUMN] = this.whiteKing;
    }

    /*
     * Getter methods
     */
    public GameState getGameState() {return this.gameState;}
    
    public int getTurnCount() {return this.turnCount;}

    public boolean isWhiteTurn() {return this.turnColor == Color.White;}

    public boolean isBlackTurn() {return this.turnColor == Color.Black;}

    public ArrayList<String> getMoveHistory() {return this.moveHistory;}

    public Stack<Piece[][]> getBoardHistory() {return this.boardHistory;}

    public Piece[][] getBoard() {return this.boardHistory.peek();}

    /*
     * Setter methods
     */
    public void setGameState(GameState gameState) {this.gameState = gameState;}

    public void incrementTurnCount() {this.turnCount++;}

    public void addToMoveHistory(String move) {this.moveHistory.add(move);}

    public boolean selectedPiece(Position position) {
        Piece[][] board = this.getBoard();
        int r = position.getRow();
        int c = position.getColumn();

        Piece piece = board[r][c];

        // if (this.isWhiteTurn() && piece != null) {
        //     if (piece.isWhite() == this.isWhiteTurn) return true;
        // }

        return false;
    }

    public boolean isLegalMove(Position startingPosition, Position targetPosition) {
        Piece[][] board = this.getBoard();
        int r = startingPosition.getRow();
        int c = startingPosition.getColumn();

        Piece piece = board[r][c];

        if (piece == null) return false;
        else if (this.turnColor != piece.getColor()) return false;

        if (piece.isValidMove(targetPosition, board, this.turnCount)) return false;

        if (piece instanceof Pawn) piece.move(targetPosition, turnCount);
        piece.move(targetPosition);
        Piece[][] newBoard = this.movePiece(piece, targetPosition, board);


        if ((this.isWhiteTurn() && this.whiteKing.isInCheck(newBoard)) || (this.isBlackTurn() && this.blackKing.isInCheck(newBoard))) {
            piece.move(startingPosition);
            return false;
        }
    

        this.boardHistory.push(newBoard);

        return true;
    }

    public Piece[][] movePiece(Piece piece, Position targetPosition, Piece[][] board) {
        int r = piece.getPosition().getRow();
        int c = piece.getPosition().getColumn();
        int newR = targetPosition.getRow();
        int newC = targetPosition.getColumn();

        board[newR][newC] = piece;
        board[r][c] = null;
        return board;
    }


    /**
     * Print the current state of the board.
     */
    public void printBoard() {
        Piece[][] board = getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Piece piece = board[i][j];
                if (piece != null) System.out.print(piece + " ");
                else System.out.print(" . ");
            }
            System.out.println();
        }
    }

}

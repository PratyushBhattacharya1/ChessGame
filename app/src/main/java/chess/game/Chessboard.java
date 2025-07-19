package chess.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.checkerframework.checker.units.qual.g;

import chess.game.PieceBehaviors.Title;

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
    private Set<Piece> whitePieces;
    private Set<Piece> blackPieces;

    /**
     * Initializes the chessboard with pieces in their starting positions.
     */
    public Chessboard() {
        this.boardHistory = new Stack<>();
        Piece[][] board = new Piece[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
        this.moveHistory = new ArrayList<>();
        this.gameState = GameState.ongoing;
        this.turnCount = 1;
        this.turnColor = Color.White;
        this.blackPieces = new HashSet<>(16);
        this.whitePieces = new HashSet<>(16);
        this.initializeBoard(board);
        this.boardHistory.push(board);
    }

    /**
     * Helper method to inialize the board with pieces in default position.
     */
    private void initializeBoard(Piece[][] board) {

        for (int i = 0; i < board.length; i++) {
            // Pawns
            Pawn pW = new Pawn(new Position(WHITE_PAWN_ROW, i), Color.Black);
            Pawn pB = new Pawn(new Position(BLACK_PAWN_ROW, i), Color.White);

            board[WHITE_PAWN_ROW][i] = pW;
            blackPieces.add(pW);
            board[BLACK_PAWN_ROW][i] = pB;
            whitePieces.add(pB);

        }

        // Rooks
        // var rW1 = new Rook(new Position(WHITE_BACK_ROW, QUEENSIDE_ROOKS_COLUMN), Color.Black);
        // var rW2 = new Rook(new Position(WHITE_BACK_ROW, KINGSIDE_ROOKS_COLUMN), Color.Black);
        // var rB1 = new Rook(new Position(BLACK_BACK_ROW, QUEENSIDE_ROOKS_COLUMN), Color.White);
        // var rB2 = new Rook(new Position(BLACK_BACK_ROW, KINGSIDE_ROOKS_COLUMN), Color.White);
        // board[WHITE_BACK_ROW][QUEENSIDE_ROOKS_COLUMN] = rW1;
        // board[WHITE_BACK_ROW][KINGSIDE_ROOKS_COLUMN] = rW2;
        // board[BLACK_BACK_ROW][QUEENSIDE_ROOKS_COLUMN] = rB1;
        // board[BLACK_BACK_ROW][KINGSIDE_ROOKS_COLUMN] = rB2;
        // whitePieces.add(rW1);
        // whitePieces.add(rW2);
        // blackPieces.add(rB1);
        // blackPieces.add(rB2);

        Title[] titles = new Title[]{Title.R, Title.N, Title.B};

        for (int i = 0; i < titles.length; i++) {
            this.createPiece(titles[i], i, board);
            this.createPiece(titles[i], BOARD_DIMENSIONS - 1 - i, board);
        }

        this.createPiece(Title.Q, QUEEN_COLUMN, board);
        
        var positionWK = new Position(WHITE_BACK_ROW, KING_COLUMN);
        var kingW = PieceFactory.create(
            Title.K, 
            positionWK, 
            Color.White); 
        whitePieces.add(kingW);
        addToBoard(board, positionWK, kingW);
        whiteKing = (King) kingW;

        var positionBK = new Position(BLACK_BACK_ROW, KING_COLUMN);
        var kingB = PieceFactory.create(
            Title.K, 
            positionBK, 
            Color.Black); 
        whitePieces.add(kingB);
        addToBoard(board, positionBK, kingB);
        blackKing = (King) kingB;

        // for (int i = 0; i < 6; i++) {
        //     var pieceW = PieceFactory.create(
        //         null, 
        //         null, 
        //         Color.White); 
        //     var pieceB = PieceFactory.create(
        //         null, 
        //         new Position(BLACK_BACK_ROW, (i < 3)? i % 3 : BOARD_DIMENSIONS - 1 - i % 3),
        //         Color.Black); 
        // }
 
    }

    private void createPiece(Title title, int column, Piece[][] board) {
        var positionWQ = new Position(WHITE_BACK_ROW, column);
        var pieceW = PieceFactory.create(
            title, 
            positionWQ, turnColor); 
        whitePieces.add(pieceW);
        addToBoard(board, positionWQ, pieceW);
        
        var positionBQ = new Position(BLACK_BACK_ROW, column);
        var pieceB = PieceFactory.create(
            title, 
            positionBQ, 
            Color.Black);
        blackPieces.add(pieceB);
        addToBoard(board, positionBQ, pieceB);

        // var positionWK = new Position(WHITE_BACK_ROW, BOARD_DIMENSIONS - 1 - column);
        // var pieceWK = PieceFactory.create(
        //     title, 
        //     positionWK, turnColor); 
        // whitePieces.add(pieceWK);
        // board[WHITE_BACK_ROW][column] = pieceWK;
        
        // var positionBK = new Position(BLACK_BACK_ROW, column);
        // var pieceBK = PieceFactory.create(
        //     title, 
        //     positionBK, 
        //     Color.Black);
        // blackPieces.add(pieceBK);
        // board[BLACK_BACK_ROW][column] = pieceBK;


    }

    private void addToBoard(Piece[][] board, Position position, Piece piece) {
        board[position.getRow()][position.getColumn()] = piece;
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

    private void endTurn() {
        if (this.gameState != GameState.ongoing) return;

        this.turnCount += (this.isBlackTurn())? 1 : 0;
        this.turnColor = (this.isWhiteTurn())? Color.Black : Color.White;
    }

    public void addToMoveHistory(String move) {this.moveHistory.add(move);}


    public boolean tryMove(Position startingPosition, Position targetPosition) {
        if (this.gameState != GameState.ongoing) return false;

        Piece[][] board = this.getBoard();
        int r = startingPosition.getRow();
        int c = startingPosition.getColumn();

        Piece piece = board[r][c];

        if (piece == null) return false;
        else if (this.turnColor != piece.getColor()) return false;

        MoveContext mContext = new MoveContext(this.turnCount, board, piece);

        if (!piece.isPseudoLegalMove(targetPosition, mContext)) {
            return false;
        }

        Piece[][] newBoard = this.movePiece(piece, targetPosition, board);

        mContext.setLastMovedPiece(piece);

        // TODO: Fails if the king moves this turn because whiteKing isn't updated by the time it calls isInCheck()
        if ((this.isWhiteTurn() && this.whiteKing.isInCheck(mContext)) || (this.isBlackTurn() && this.blackKing.isInCheck(mContext))) {
            if (piece instanceof Pawn) {
                ((Pawn)piece).setEnPassantTurn(Pawn.DEFAULT_EN_PASSANT_TURN_VALUE);
            }
            return false;
        }

        if (piece instanceof King) {
            if (this.isWhiteTurn()) this.whiteKing = (King) piece;
            else this.blackKing = (King) piece;
        }

        piece.move(targetPosition, mContext);
        this.boardHistory.push(newBoard);
        this.endTurn();

        return true;
    }

    public Piece[][] movePiece(Piece piece, Position targetPosition, Piece[][] board) {
        int r = piece.getPosition().getRow();
        int c = piece.getPosition().getColumn();
        int newR = targetPosition.getRow();
        int newC = targetPosition.getColumn();

        var newBoard = board.clone();

        newBoard[newR][newC] = piece;
        newBoard[r][c] = null;
        return board;
    }

    /**
     * Print the current state of the board.
     */
    public void printBoard() {
        System.out.printf("--- %s's turn: %d---\n", this.turnColor, this.turnCount);

        //TODO: Make the letters/numbers clear on the printout
        Piece[][] board = getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                // if ((j == 0) || (i == 8)) {
                //     System.out.println("");
                // }
                Piece piece = board[i][j];
                if (piece != null) System.out.print(piece + " ");
                else System.out.print(" . ");
            }
            System.out.println();
        }
    }

}

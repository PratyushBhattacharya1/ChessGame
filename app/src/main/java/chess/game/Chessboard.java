package chess.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import chess.game.PieceBehaviors.Title;

public class Chessboard {
    
    /*
     * Constants for board dimensions and piece positions
     */
    private static final int BOARD_DIMENSIONS = 8;
    private static final int QUEEN_COLUMN = 3;
    private static final int KING_COLUMN = 4;
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
    private Position selectedPosition;

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
        // whitePieces.add(kingW);
        addToBoard(board, positionWK, kingW);
        whiteKing = (King) kingW;

        var positionBK = new Position(BLACK_BACK_ROW, KING_COLUMN);
        var kingB = PieceFactory.create(
            Title.K, 
            positionBK, 
            Color.Black); 
        // blackPieces.add(kingB);
        addToBoard(board, positionBK, kingB);
        blackKing = (King) kingB; 
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
        Piece piece = board[startingPosition.getRow()][startingPosition.getColumn()];
        var mContext = new MoveContext(this.turnCount, this.getBoard());

        if (piece == null || piece.getColor() != this.turnColor) {
            System.out.println("Invalid move: No piece at starting position or wrong color");
            return false;
        }

        if (!MoveValidator.validateMove(piece, targetPosition, mContext)) 
            return false;
    

        piece.move(targetPosition, mContext);
        board[startingPosition.getRow()][startingPosition.getColumn()] = null;
        board[targetPosition.getRow()][targetPosition.getColumn()] = piece;
        // Check for checkmate or stalemate
        if (this.isInCheckmate(mContext)) {
            this.gameState = (this.isWhiteTurn())? GameState.whiteWon : GameState.blackWon;
        } else if (this.isStalemate(mContext)) {
            this.gameState = GameState.draw;
        }

        this.boardHistory.push(board);
        this.endTurn();

        return true;
    }

    private boolean isStalemate(MoveContext mContext) {
        King kingToCheckMate = (this.isWhiteTurn())? this.blackKing : this.whiteKing;
        var allPiecesToCheck = (this.isWhiteTurn())? this.blackPieces : this.whitePieces;

        if (kingToCheckMate.isInCheck(mContext)) {
            return false; // Not a stalemate if the king is in check
        }

        for (Piece piece : allPiecesToCheck) {
            var possibleMoves = piece.generatePseudoLegalMoves(mContext);
            if (!possibleMoves.isEmpty()) {
                return false; // If any piece has a legal move, it's not stalemate
            }
        }
        return true; // If no pieces have legal moves, it's stalemate
    }

    private boolean isInCheckmate(MoveContext mContext) {
        King kingToCheckMate = (this.isWhiteTurn())? this.blackKing : this.whiteKing;
        var allPiecesToCheck = (this.isWhiteTurn())? this.blackPieces : this.whitePieces;

        if (!kingToCheckMate.isInCheck(mContext)) {
            return false;
        }

        if (!kingToCheckMate.generatePseudoLegalMoves(mContext).isEmpty()) {
            return false;
        }

        for (Piece piece : allPiecesToCheck) {
            // if (piece instanceof King) continue; // Skip the king itself
            var possibleMoves = piece.generatePseudoLegalMoves(mContext);
            for (Position position : possibleMoves) {
                MoveValidator.simulateMove(getBoard(), piece, position);
            }
        }
        return true; // No moves available to escape check
    }

    public Piece[][] movePiece(Piece piece, Position targetPosition, Piece[][] board) {
        int r = piece.getPosition().getRow();
        int c = piece.getPosition().getColumn();
        int newR = targetPosition.getRow();
        int newC = targetPosition.getColumn();

        // Clone board to avoid modifying the original
        Piece[][] newBoard = new Piece[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
        for (int i = 0; i < BOARD_DIMENSIONS; i++) {
            for (int j = 0; j < BOARD_DIMENSIONS; j++) {
                if (board[i][j] != null) {
                    try {
                        newBoard[i][j] = (Piece) board[i][j].clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                } else {
                    newBoard[i][j] = null;
                }
            }
        }

        // Remove target piece if captured
        var targetPiece = board[newR][newC];
        if (targetPiece != null) {
            if (this.isWhiteTurn()) {
                this.blackPieces.remove(targetPiece);
            } else {
                this.whitePieces.remove(targetPiece); 
            }
        }

        // Update piece position without modifying hasMoved/enPassant values
        if (this.isWhiteTurn() && piece.getColor() == Color.White) {
            this.whitePieces.remove(piece);
            piece.getPosition().setPosition(targetPosition);
            this.whitePieces.add(piece);
            if (piece instanceof King) {
                this.whiteKing = (King) piece;
            }
        } else if (this.isBlackTurn() && piece.getColor() == Color.Black) {
            this.blackPieces.remove(piece);
            piece.getPosition().setPosition(targetPosition);
            this.blackPieces.add(piece);
            if (piece instanceof King) {
                this.blackKing = (King) piece;
            }
        }

        newBoard[newR][newC] = piece;
        newBoard[r][c] = null;
        return newBoard;
    }

    /**
     * Print the current state of the board.
     */
    public void printBoard() {
        System.out.printf("--- %s's turn: %d---\n", this.turnColor, this.turnCount);

        Piece[][] board = getBoard();
        for (int row = 0; row <= board.length; row++) {
            for (int column = 0; column <= board[0].length; column++) {
                if (row == board.length && column > 0) {
                    System.out.print((char)('a' + column - 1) + "  ");
                    continue;
                } else if (column == 0 && row < board.length) {
                    System.out.print((char)('8' - row) + " ");
                    continue;
                } else if (row == board.length && column == 0) {
                    System.out.print("  ");
                    continue;
                }
                Piece piece = board[row][column - 1];
                if (piece != null) System.out.print(piece + " ");
                else System.out.print(" . ");
            }
            System.out.println();
        }
    }

    public boolean tryCastling(String move) throws CloneNotSupportedException {
        var king = (this.isWhiteTurn())? this.whiteKing : this.blackKing;
        var position = (move.equalsIgnoreCase("O-O")) ? 
            king.CASTLE_POSITION[1] : king.CASTLE_POSITION[0];
        var rookColumn = (move.equalsIgnoreCase("O-O")) ? 7 : 0;
        var rook = this.getBoard()[king.getPosition().getRow()][rookColumn];

        if(king.canCastle(position, new MoveContext(this.turnCount, this.getBoard()))) {
            var newRookCol = (rookColumn == 7) ? 5 : 3;
            var board = this.movePiece(king, position, this.getBoard());
            board = this.movePiece(rook, new Position(king.getPosition().getRow(), newRookCol), board);
            king.move(position, new MoveContext(this.turnCount, board));
            rook.move(new Position(king.getPosition().getRow(), newRookCol), new MoveContext(this.turnCount, board));
            var mContext = new MoveContext(this.turnCount, board);

            if (this.isInCheckmate(mContext)) {
                this.gameState = (this.isWhiteTurn())? GameState.whiteWon : GameState.blackWon;
                return true;
            } else if (this.isStalemate(mContext)) {
                this.gameState = GameState.draw;
                return true;
            }

            this.boardHistory.push(board);
            this.endTurn();

            return true;
        }
        return false;
    }

    public boolean handleSquareSelection(int row, int col) {
        var p = new Position(row, col);
        if (this.selectedPosition == null 
            || (this.getBoard()[row][col] != null && this.getBoard()[row][col].getColor() == this.turnColor
            || this.getBoard()[this.selectedPosition.getRow()][this.selectedPosition.getColumn()] == null)) {
            this.selectedPosition = p;
            System.out.println("Selected square: " + p);
            return false;
        } else {
            System.out.println("Moving from " + this.selectedPosition + " to " + p);
            if (!this.tryMove(this.selectedPosition, p)) {
                System.out.println("Invalid move");
                this.selectedPosition = null;
                return false;
            } else {
                System.out.println("Move successful");
                this.printBoard();
                this.selectedPosition = null;
                return true;
            }
        }
    }

    public Position getSelectedPosition() {
        return this.selectedPosition;
    }

    public Set<Position> getLegalMoves(int row, int col) {
        Piece[][] board = this.getBoard();
        Piece piece = board[row][col];
        if (piece == null || piece.getColor() != this.turnColor) {
            return new HashSet<>();
        }
        MoveContext mContext = new MoveContext(this.turnCount, board);
        var moves = piece.generatePseudoLegalMoves(mContext);
        var it = moves.iterator();
        while (it.hasNext()) {
            Position position = it.next();
            var newBoard = this.movePiece(piece, position, mContext.getBoard());
            var newContext = new MoveContext(this.turnCount, newBoard);
            if (this.isWhiteTurn() && this.whiteKing.isInCheck(newContext)) {
                it.remove();
            }
            else if (this.isBlackTurn() && this.blackKing.isInCheck(newContext)) {
                it.remove();
            }
            board = this.movePiece(piece, new Position(row, col), board);
        }
        return moves;
    }

    public boolean isInCheck() {
        var board = this.getBoard();
        var mContext = new MoveContext(this.turnCount, board);
        return this.isWhiteTurn() ? this.whiteKing.isInCheck(mContext) : this.blackKing.isInCheck(mContext);
    }

}

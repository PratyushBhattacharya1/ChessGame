package chess.game;

public class MoveContext {
    private int turnCount;
    private Piece[][] board;
    private Piece lastMovedPiece;
    private int turnsSinceLastPawnPush;

    public MoveContext(int turnCount, Piece[][] board, Piece lastMovedPiece) {
        this.turnCount = turnCount;
        this.board = board;
        this.lastMovedPiece = lastMovedPiece;
    }

    public MoveContext(int turnCount, Piece[][] board) {
        this.turnCount = turnCount;
        this.board = board;
        this.lastMovedPiece = null;
    }

    public void setLastMovedPiece(Piece lastMovedPiece) {
        this.lastMovedPiece = lastMovedPiece;
    }

    public int getTurnCount() { return turnCount; }
    public Piece[][] getBoard() { return board; }
    public Piece getLastMovedPiece() { return lastMovedPiece; }

    public void setBoard(Piece[][] newBoard) {
        this.board = newBoard;
    }
}
package chess.game;

public class MoveContext {
    private int turnCount;
    private Piece[][] board;
    private int turnsSinceLastPawnPush;

    public MoveContext(int turnCount, Piece[][] board) {
        this.turnCount = turnCount;
        this.board = board;
        // this.turnsSinceLastPawnPush = turnsSinceLastPawnPush;
    }

    public int getTurnCount() { return turnCount; }
    public Piece[][] getBoard() { return board; }
}
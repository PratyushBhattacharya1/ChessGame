package chess.game;

public abstract class PieceBehaviors implements Piece {

    protected static enum Direction {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1),
        UP_LEFT(-1, -1),
        UP_RIGHT(-1, 1),
        DOWN_LEFT(1, -1),
        DOWN_RIGHT(1, 1);

        private final int dr; // Row delta
        private final int dc; // Column delta

        // private final int[] dir;

        Direction(int dr, int dc) {
            this.dr = dr;
            this.dc = dc;
            // this.dir = new int[] {dr, dc};
        }

        // public int[] getDirection() {return this.dir;}

        public int getRowDelta() {return this.dr;}
        public int getColDelta() {return this.dc;}
    }

    protected static enum Title {
        R,
        N,
        B,
        Q,
        K,
        P
    }

    Position position;
    final Color color;
    Title title;

    public PieceBehaviors(Position position, Color color) {
        this.position = new Position(position);
        this.color = color;
    }

    public Position getPosition() {
        return this.position;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isWhite() {
        return this.color == Color.White;
    }

    public boolean isBlack() {
        return this.color == Color.Black;
    }

    public void move(Position p, MoveContext moveContext) {
        this.position.setPosition(p);
    }

    public <T> T processLine(   int dr, 
                                int dc, 
                                Piece[][] board, 
                                T initialValue, 
                                LineProcessor<T> processor) {

        int r = this.position.getRow() + dr;
        int c = this.position.getColumn() + dc;

        T result = initialValue;

        while (Position.isValidPosition(r, c)) {
            Piece piece = board[r][c];
            result = processor.process(r, c, piece);
            if (piece != null) {
                break;
            }
            r += dr;
            c += dc;
        }
        return result;
    }

    @Override
    public String toString() {
        return "" + (this.isWhite()? "W" : "B") + this.title;
    }
}
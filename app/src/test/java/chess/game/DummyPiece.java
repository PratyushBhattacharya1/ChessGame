package chess.game;

final class DummyPiece implements Piece {

        private final Position position;
        private final Color color;

        public DummyPiece(Position position, Color color) {
            this.position = position;
            this.color = color;
        }

        public Position getPosition() { return position; }

        @Override
        public Color getColor() { return color; }

        @Override
        public boolean isWhite() {
            return this.color == Color.White;
        }

        @Override
        public boolean isBlack() {
            return this.color == Color.Black;
        }
        
        @Override
        public boolean isValidMove(Position targetPosition, Piece[][] board, int turnCount) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'isValidMove'");
        }

        @Override
        public void move(Position p) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'move'");
        }
    }
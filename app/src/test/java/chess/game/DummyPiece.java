package chess.game;

import java.util.Set;

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
        public boolean isPseudoLegalMove(Position targetPosition, MoveContext moveContext) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'isPseudoLegalMove'");
        }

        @Override
        public void move(Position p, MoveContext moveContext) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'move'");
        }

        @Override
        public Set<Position> generatePseudoLegalMoves(MoveContext moveContext) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'generatePseudoLegalMoves'");
        }
    }
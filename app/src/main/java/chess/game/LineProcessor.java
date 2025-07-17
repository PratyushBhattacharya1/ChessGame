package chess.game;

@FunctionalInterface
interface LineProcessor<T> {
    T process(int r, int c, Piece piece);
}
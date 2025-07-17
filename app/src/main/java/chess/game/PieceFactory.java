package chess.game;

import java.util.Map;
import java.util.function.BiFunction;

import chess.game.PieceBehaviors.Title;

// In some util or factory class
public class PieceFactory {
  // Map each Title to a constructor reference
  private static final Map<Title, BiFunction<Position,Color,Piece>> pieceFactory =
      Map.of(
        Title.R, Rook::new,      // (pos, color) -> new Rook(pos, color)
        Title.N, Knight::new,
        Title.B, Bishop::new,
        Title.Q, Queen::new,
        Title.K, King::new,
        Title.P, Pawn::new
      );

  public static Piece create(Title title, Position pos, Color color) {
    return pieceFactory.get(title).apply(pos, color);
  }
}
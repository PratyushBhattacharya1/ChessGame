package chess.game;

public interface PromotionListener {
    void onPawnPromotion(Position position, Color color);
}
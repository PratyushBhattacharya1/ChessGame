package chess.game;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.checkerframework.checker.units.qual.C;

public class ChessSquareComponent extends JButton {
    private static final Font FONT = new java.awt.Font("Serif", java.awt.Font.BOLD, 36);
    public static final java.awt.Color CHESS_GRAY = new java.awt.Color(235, 236, 208);
    public static final java.awt.Color CHESS_GREEN = new java.awt.Color(115, 149, 82);
    private static final int DEFAULT_SQUARE_SIZE = 64;
    private int row;
    private int col;
    

    public ChessSquareComponent(int row, int col) {
        this.row = row;
        this.col = col;
        this.initButton();
    }

    private void initButton() {
        // Set preferred button size for uniformity
        this.setPreferredSize(new Dimension(DEFAULT_SQUARE_SIZE, DEFAULT_SQUARE_SIZE));

        // Set background color based on row and col for checkerboard effect
        if ((row + col) % 2 == 0) {
            this.setBackground(CHESS_GRAY);
        } else {
            this.setBackground(CHESS_GREEN);
        }

        this.setBorder(null);

        // Ensure text (chess piece symbols) is centered
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);

        // Font settings can be adjusted for visual enhancement
        this.setFont(FONT);
    }

    public void setPieceSymbol(String symbol, Color color) {
        this.setText(symbol);
        java.awt.Color c = (color == Color.White) ? java.awt.Color.white : java.awt.Color.gray;
        this.setForeground(c);
    }

    public void clearPieceSymbol() {
        this.setText("");
    }
}

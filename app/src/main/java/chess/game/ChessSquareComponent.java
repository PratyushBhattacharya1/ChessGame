package chess.game;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.SwingConstants;

public class ChessSquareComponent extends JButton {
    private static final java.awt.Color GRAY_TRANSPARENT = new java.awt.Color(128, 128, 128, 180);
    private static final Font FONT = new java.awt.Font("Serif", java.awt.Font.BOLD, 36);
    public static final java.awt.Color CHESS_GRAY = new java.awt.Color(235, 236, 208);
    public static final java.awt.Color CHESS_GREEN = new java.awt.Color(115, 149, 82);
    public static final int DEFAULT_SQUARE_SIZE = 80;
    private int row;
    private int col;
    private boolean highlightLegalMoves = false;
    private boolean kingInCheckHighLight = false;
    

    public ChessSquareComponent(int row, int col) {
        this.row = row;
        this.col = col;
        this.initButton();
    }

    public void setLegalMoveHighlight(boolean highlight) {
        this.highlightLegalMoves = highlight;
        this.repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (highlightLegalMoves) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(GRAY_TRANSPARENT); // semi-transparent gray
            int diameter = (getWidth() + getHeight()) / 6;
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;
            g2.fillOval(x, y, diameter, diameter);
            g2.dispose();
        }

        if (kingInCheckHighLight) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int glowSize = getWidth() - 10;
            int x = (getWidth() - glowSize) / 2;
            int y = (getHeight() - glowSize) / 2;
            for (int i = 0; i < 3; i++) { // Multiple circles for glow effect
                int alpha = 80 - i * 25;
                g2.setColor(new java.awt.Color(255, 0, 0, alpha));
                g2.fillOval(x - i * 4, y - i * 4, glowSize + i * 8, glowSize + i * 8);
            }
            g2.dispose();
        }
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

    public void setKingInCheckHighlight(boolean isInCheck) {
        this.kingInCheckHighLight = isInCheck;
    }
}

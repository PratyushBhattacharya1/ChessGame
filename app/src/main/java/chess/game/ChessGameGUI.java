package chess.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChessGameGUI extends JFrame {
    private static final String TITLE = "Chess Game";
    private static final int BOARD_DIMENSIONS = 8;
    private final ChessSquareComponent[][] squares = 
        new ChessSquareComponent[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
    private final Chessboard chessboard = new Chessboard();

    private final Map<Class<? extends Piece>, String> pieceUnicodeMap = new HashMap<>() {
        {
            put(Pawn.class, "\u265F");
            put(Rook.class, "\u265C");
            put(Knight.class, "\u265E");
            put(Bishop.class, "\u265D");
            put(Queen.class, "\u265B");
            put(King.class, "\u265A");
        }
    };

    public ChessGameGUI() {
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(BOARD_DIMENSIONS, BOARD_DIMENSIONS));
        this.initializeBoard();
        this.pack(); // Adjust window size to fit the chessboard
        this.setVisible(true);
        setDefaultLookAndFeelDecorated(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < this.squares.length; row++) {
            for (int col = 0; col < this.squares[row].length; col++) {
                final int finalRow = row;
                final int finalCol = col;
                ChessSquareComponent square = new ChessSquareComponent(row, col);
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        handleSquareClick(finalRow, finalCol);
                    }
                });
                add(square);
                this.squares[row][col] = square;
            }
        }
        refreshBoard();
    }

    private void refreshBoard() {
        var board = chessboard.getBoard();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    // If using Unicode symbols:
                    String symbol = pieceUnicodeMap.get(piece.getClass());
                    Color color = (piece.getColor() == Color.White) ? Color.White : Color.Black;
                    squares[row][col].setPieceSymbol(symbol, color);
                    // Or, if updating with icons or any other graphical representation
                } else {
                    squares[row][col].clearPieceSymbol(); // Ensure this method clears the square
                }
            }
        }
    }

    private void handleSquareClick(int row, int col) {
        this.clearHighlights();
        if (chessboard.handleSquareSelection(row, col)) {
            // this.clearHighlights();
            this.refreshBoard();
            this.checkGameState();
        } else if (chessboard.getSelectedPosition() != null) {
            // this.clearHighlights();
            System.out.println("Highlighting legal moves");
            this.highLightLegalMoves(chessboard.getSelectedPosition());
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                squares[row][col].setBackground((row + col) % 2 == 0 ? ChessSquareComponent.CHESS_GRAY : ChessSquareComponent.CHESS_GREEN);
            }
        }
    }

    public void highLightLegalMoves(Position position) {
        Set<Position> legalMoves = chessboard.getLegalMoves(position.getRow(), position.getColumn());
        squares[position.getRow()][position.getColumn()].setBackground(java.awt.Color.RED);
        for (Position pos : legalMoves) {
            squares[pos.getRow()][pos.getColumn()].setBackground(java.awt.Color.YELLOW);
        }
    }


    private void checkGameState() {
        var currentPlayer = (chessboard.isWhiteTurn())? "White" : "Black";
        boolean inCheck = chessboard.isInCheck();

        System.out.println("GameState: " + chessboard.getGameState());

        if (chessboard.getGameState() == GameState.whiteWon) {
            JOptionPane.showMessageDialog(this, "White has won the game!");
            return;
        } else if (chessboard.getGameState() == GameState.blackWon) {
            JOptionPane.showMessageDialog(this, "Black has won the game!");
            return;
        } else if (chessboard.getGameState() == GameState.draw) {
            JOptionPane.showMessageDialog(this, "The game is a draw!");
            return;
        }

        if (inCheck) {
            JOptionPane.showMessageDialog(this, currentPlayer + " is in check!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGameGUI::new);
    }
}
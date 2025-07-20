package chess.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class ChessGameGUI extends JFrame {
  private static final String TITLE = "Chess Game";
  private static final int BOARD_DIMENSIONS = 8;
  private final ChessSquareComponent[][] squares = 
    new ChessSquareComponent[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
  private final ChessGame game = new ChessGame();

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
  }

  private void initializeBoard() {
    for (int row = 0; row < this.squares.length; row++) {
        for (int col = 0; col < this.squares[row].length; col++) {
            final int finalRow = row;
            final int finalCol = col;
            ChessSquareComponent square = new ChessSquareComponent(row, col);
            square.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleSquareClick(finalRow, finalCol);
                }
            });
            // add(square);
            this.squares[row][col] = square;
        }
    }
    refreshBoard();
    }

  private void refreshBoard() {
      for (int row = 0; row < squares.length; row++) {
          for (int col = 0; col < squares[row].length; col++) {
            //   ChessSquareComponent square = squares[row][col];
            //   Piece piece = game.getPieceAt(row, col);
            //   square.setPiece(piece);
          }
      }
  }

   private void handleSquareClick(int row, int col) {
  }

  private void checkGameState() {
  }

  public static void main(String[] args) {
      SwingUtilities.invokeLater(ChessGameGUI::new);
  }
}
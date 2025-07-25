package chess.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChessGameGUI extends JFrame {
    private static final int NUM_COLORS = 2;
    private static final int NUM_PIECES = 6;
    private static final String SPRITESHEET_PATH = "/spritesheetTransparentXOffset.png";
    private static final java.awt.Color HIGHLIGHT_GREEN = new java.awt.Color(185, 202, 67);
    private static final java.awt.Color HIGHLIGHT_GRAY = new java.awt.Color(245, 246, 130);
    private static final String TITLE = "Chess Game";
    private static final int BOARD_DIMENSIONS = 8;
    private final ChessSquareComponent[][] squares = 
        new ChessSquareComponent[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
    private final Chessboard chessboard = new Chessboard();
    // private Position oldKingPos = new Position(0, 4);

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

    private Map<Class<? extends Piece>, ImageIcon[]> pieceIconMap = new HashMap<>();

    public ChessGameGUI() {
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(BOARD_DIMENSIONS, BOARD_DIMENSIONS));
        this.initializeBoard();
        this.pack(); // Adjust window size to fit the chessboard
        this.setVisible(true);
        // setDefaultLookAndFeelDecorated(true);
        chessboard.setPromotionListener((position, color) -> {
            String[] options = {"Queen", "Rook", "Bishop", "Knight"};
            String choice = (String) JOptionPane.showInputDialog(
                this, "Promote pawn to:", "Pawn Promotion",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            Piece promotedPiece;
            switch (choice) {
                case "Rook": promotedPiece = new Rook(position, color); break;
                case "Bishop": promotedPiece = new Bishop(position, color); break;
                case "Knight": promotedPiece = new Knight(position, color); break;
                default: promotedPiece = new Queen(position, color);
            }
            chessboard.promotePawn(position, promotedPiece);
            refreshBoard();
        });
    }

    private void initializeBoard() {
        for (int row = 0; row < this.squares.length; row++) {
            for (int col = 0; col < this.squares[row].length; col++) {
                int finalRow = row;
                int finalCol = col;
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
        this.loadSpriteSheet(); // Load piece icons from sprite sheet
        this.refreshBoard();
    }

    private void refreshBoard() {
        var board = chessboard.getBoard();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Piece piece = board[row][col];
                this.setVisual(row, col, piece);
            }
        }
    }

    private void setVisual(int row, int col, Piece piece) {
        ChessSquareComponent CSC = squares[row][col];
        if (piece != null) {
            ImageIcon[] icons = this.pieceIconMap.get(piece.getClass());
            if (icons == null) {
                // Revert back to unicode symbols if icons are unavailable
                String symbol = this.pieceUnicodeMap.get(piece.getClass());
                Color color = (piece.isWhite()) ? Color.White : Color.Black;
                CSC.setPieceSymbol(symbol, color);
            } else {
                // Set the icon based on the piece color
                ImageIcon icon = icons[piece.isWhite() ? 0 : 1];
                Image scaledImage = icon.getImage().getScaledInstance(
                    ChessSquareComponent.DEFAULT_SQUARE_SIZE, 
                    ChessSquareComponent.DEFAULT_SQUARE_SIZE, 
                    Image.SCALE_SMOOTH);
                CSC.setIcon(new ImageIcon(scaledImage));
            }
        } else {
            CSC.setIcon(null);
            CSC.clearPieceSymbol(); // Ensure this method clears the square
        }
    }

    private void loadSpriteSheet() {
        BufferedImage spriteSheet;
        try {
            spriteSheet = ImageIO.read(getClass().getResource(SPRITESHEET_PATH));
        } catch (IOException e) {
            System.out.println("Error loading sprite sheet: " + e.getMessage());
            e.printStackTrace();
            return;
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        int pieceWidth = spriteSheet.getWidth() / NUM_PIECES;
        int pieceHeight = spriteSheet.getHeight() / NUM_COLORS;

        @SuppressWarnings("unchecked")
        Class<? extends Piece>[] pieceClasses = new Class[]{
            King.class, Queen.class, Bishop.class, Knight.class, Rook.class, Pawn.class
        };

        for (int i = 0; i < NUM_PIECES; i++) {
            this.pieceIconMap.put(pieceClasses[i], new ImageIcon[]{
                // White Icon
                new ImageIcon(spriteSheet.getSubimage(i * pieceWidth, 0, pieceWidth, pieceHeight)),
                // Black Icon
                new ImageIcon(spriteSheet.getSubimage(i * pieceWidth, pieceHeight, pieceWidth, pieceHeight))
            });
        }
    }

    private void handleSquareClick(int row, int col) {
        this.clearHighlights();
        if (chessboard.handleSquareSelection(row, col)) {
            this.refreshBoard();
            this.checkGameState();
        } else if (chessboard.getSelectedPosition() != null) {
            this.highLightLegalMoves(chessboard.getSelectedPosition());
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                squares[row][col].setBackground((row + col) % 2 == 0 ? 
                    ChessSquareComponent.CHESS_GRAY : ChessSquareComponent.CHESS_GREEN);
                squares[row][col].setLegalMoveHighlight(false);
                squares[row][col].setKingInCheckHighlight(false);
            }
        }
    }

    public void highLightLegalMoves(Position position) {
        int row = position.getRow();
        int col = position.getColumn();

        Set<Position> legalMoves = chessboard.getLegalMoves(row, col);

        squares[row][col].setBackground((row + col) % 2 == 0 ? 
            HIGHLIGHT_GRAY : HIGHLIGHT_GREEN);

        for (Position pos : legalMoves) {
            // squares[pos.getRow()][pos.getColumn()].
            // setBackground(new java.awt.Color(255, 255, 0, 200)); 
            squares[pos.getRow()][pos.getColumn()].setLegalMoveHighlight(true);
        }
    }


    private void checkGameState() {
        var currentPlayer = (chessboard.isWhiteTurn())? "White" : "Black";
        var inCheckKing = chessboard.isInCheck();
        if (inCheckKing != null) {
            var oldKingPos = inCheckKing.getPosition();
            squares[oldKingPos.getRow()][oldKingPos.getColumn()]
                .setKingInCheckHighlight(true);
        }

        // } else {
        //     squares[oldKingPos.getRow()]
        //         [oldKingPos.getColumn()].setKingInCheckHighlight(false);
        // }

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

        if (inCheckKing != null) {
            JOptionPane.showMessageDialog(this, currentPlayer + " is in check!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGameGUI::new);
    }
}
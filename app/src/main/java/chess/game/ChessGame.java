package chess.game;

import java.util.Scanner;

public class ChessGame {
    
    /*
     * Static constants
     */
    private static final int MOVE_SIZE = 4;
    private static final int SECOND_PARTITION = 4;
    private static final int FIRST_PARTITION = 2;

    public static void main(String[] args) throws CloneNotSupportedException {
        Chessboard chessboard = new Chessboard();

        while(chessboard.getGameState() == GameState.ongoing) {
            System.out.println();
            chessboard.printBoard();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your move: ");
            String move = scanner.nextLine();

            if (move.equalsIgnoreCase("end") || move.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the game.");
                chessboard.setGameState(GameState.draw);
                continue;
            }

            if (move.length() != MOVE_SIZE) {
                System.out.println("Input isn't four characters");
                continue;
            }

            Position startingPosition;
            Position targetPosition;

            try {
                startingPosition = Position.stringToPosition(move.substring(0, FIRST_PARTITION));
                targetPosition = Position.stringToPosition(move.substring(FIRST_PARTITION, SECOND_PARTITION));
            } catch (IllegalArgumentException IllegalArgumentException) {
                System.out.println("Position was invalid");
                continue;
            }

            if (!chessboard.tryMove(startingPosition, targetPosition)) {
                System.out.println("Illegal move!");
                continue;
            }

            if (chessboard.getGameState() == GameState.whiteWon) {
                System.out.println("Congratulations! White wins!");
            } else if (chessboard.getGameState() == GameState.blackWon) {
                System.out.println("Congratulations! Black wins!");
            } else if (chessboard.getGameState() == GameState.draw) {
                System.out.println("It's a draw!");
            } else if (chessboard.getGameState() == GameState.stalemate) {
                System.out.println("Stalemate! No legal moves left.");
            }

            chessboard.addToMoveHistory(move);
        }
        System.out.println(chessboard.getMoveHistory());
    }
    
}

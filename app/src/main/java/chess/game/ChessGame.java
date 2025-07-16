package chess.game;

import java.util.Scanner;

public class ChessGame {
    public static void main(String[] args) {
        Chessboard chessboard = new Chessboard();

        while(chessboard.getGameState() == GameState.unfinished) {
            chessboard.printBoard();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your move: ");
            String move = scanner.nextLine();

            if (move.equalsIgnoreCase("end")) {
                System.out.println("Exiting the game.");
                chessboard.setGameState(GameState.draw);
                break;
            }

            if (move.length() != 4) {
                System.out.println("Input isn't four characters");
                break;
            }

            try {
                Position selectedPiece = Position.stringToPosition(move.substring(0, 2));
                Position targetPosition = Position.stringToPosition(move.substring(2, 4));
            } catch (IllegalArgumentException IllegalArgumentException) {
                System.out.println("Position was invalid");
                break;
            }

            



        }
    }
}

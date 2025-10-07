import java.util.*;

public class Assignment_5{
    static void printBoard(String[][] board) {
        for (String[] row : board) {
            System.out.println(String.join(" | ", row));
            System.out.println("-----");
        }
    }

    static int evaluate(String[][] board) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && !board[i][0].equals("_"))
                return board[i][0].equals("O") ? 10 : -10;
        }
        for (int i = 0; i < 3; i++) {
            if (board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i]) && !board[0][i].equals("_"))
                return board[0][i].equals("O") ? 10 : -10;
        }
        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]) && !board[0][0].equals("_"))
            return board[0][0].equals("O") ? 10 : -10;
        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]) && !board[0][2].equals("_"))
            return board[0][2].equals("O") ? 10 : -10;
        return 0;
    }

    static boolean movesLeft(String[][] board) {
        for (String[] row : board)
            for (String cell : row)
                if (cell.equals("_"))
                    return true;
        return false;
    }

    static int minimax(String[][] board, int depth, boolean isMax) {
        int score = evaluate(board);
        if (score == 10) return score - depth;
        if (score == -10) return score + depth;
        if (!movesLeft(board)) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].equals("_")) {
                        board[i][j] = "O";
                        best = Math.max(best, minimax(board, depth + 1, false));
                        board[i][j] = "_";
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].equals("_")) {
                        board[i][j] = "X";
                        best = Math.min(best, minimax(board, depth + 1, true));
                        board[i][j] = "_";
                    }
                }
            }
            return best;
        }
    }

    static int[] findBestMove(String[][] board) {
        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals("_")) {
                    board[i][j] = "O";
                    int moveVal = minimax(board, 0, false);
                    board[i][j] = "_";
                    if (moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove;
    }

    static void playGame() {
        Scanner sc = new Scanner(System.in);
        String[][] board = {{"_", "_", "_"}, {"_", "_", "_"}, {"_", "_", "_"}};
        System.out.println("Tic Tac Toe - You are X, AI is O");

        while (true) {
            printBoard(board);
            System.out.print("Enter your move (row col): ");
            int x = sc.nextInt();
            int y = sc.nextInt();
            if (!board[x][y].equals("_")) {
                System.out.println("Invalid move, try again.");
                continue;
            }
            board[x][y] = "X";
            if (evaluate(board) == -10) {
                printBoard(board);
                System.out.println("You win!");
                break;
            }
            if (!movesLeft(board)) {
                printBoard(board);
                System.out.println("It's a draw!");
                break;
            }
            int[] aiMove = findBestMove(board);
            board[aiMove[0]][aiMove[1]] = "O";
            if (evaluate(board) == 10) {
                printBoard(board);
                System.out.println("AI wins!");
                break;
            }
            if (!movesLeft(board)) {
                printBoard(board);
                System.out.println("It's a draw!");
                break;
            }
        }
        sc.close();
    }

    public static void main(String[] args) {
        playGame();
    }
}

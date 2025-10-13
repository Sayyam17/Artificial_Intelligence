import java.util.*;

public class Assignment_1 {

    private static final int[][] goalState = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
    };

    private static final int[][] moves = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    static class Node {
        int[][] state;
        List<int[][]> path;

        Node(int[][] state, List<int[][]> path) {
            this.state = state;
            this.path = path;
        }
    }

    private static int[] getBlankPos(int[][] state) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private static List<int[][]> generateNeighbors(int[][] state) {
        int[] blank = getBlankPos(state);
        int x = blank[0], y = blank[1];

        List<int[][]> neighbors = new ArrayList<>();
        for (int[] move : moves) {
            int nx = x + move[0];
            int ny = y + move[1];
            if (nx >= 0 && nx < 3 && ny >= 0 && ny < 3) {
                int[][] newState = deepCopy(state);
     
                int temp = newState[x][y];
                newState[x][y] = newState[nx][ny];
                newState[nx][ny] = temp;
                neighbors.add(newState);
            }
        }
        return neighbors;
    }

    private static String stateToString(int[][] state) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : state) {
            for (int val : row) {
                sb.append(val).append(",");
            }
        }
        return sb.toString();
    }

    // BFS 
    public static List<int[][]> bfs(int[][] startState) {
        Set<String> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();

        List<int[][]> initialPath = new ArrayList<>();
        queue.add(new Node(startState, initialPath));
        visited.add(stateToString(startState));

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            int[][] state = node.state;
            List<int[][]> path = node.path;

            if (Arrays.deepEquals(state, goalState)) {
                path.add(state);
                return path;
            }

            for (int[][] neighbor : generateNeighbors(state)) {
                String key = stateToString(neighbor);
                if (!visited.contains(key)) {
                    visited.add(key);
                    List<int[][]> newPath = new ArrayList<>(path);
                    newPath.add(state);
                    queue.add(new Node(neighbor, newPath));
                }
            }
        }
        return null;
    }

    //DFS 
    public static List<int[][]> dfs(int[][] startState, int depthLimit) {
        Set<String> visited = new HashSet<>();
        return dfsRecursive(startState, new ArrayList<>(), 0, depthLimit, visited);
    }

    private static List<int[][]> dfsRecursive(int[][] state, List<int[][]> path,
                                              int depth, int depthLimit, Set<String> visited) {
        if (Arrays.deepEquals(state, goalState)) {
            path.add(state);
            return path;
        }
        if (depth >= depthLimit) return null;

        visited.add(stateToString(state));

        for (int[][] neighbor : generateNeighbors(state)) {
            String key = stateToString(neighbor);
            if (!visited.contains(key)) {
                List<int[][]> newPath = new ArrayList<>(path);
                newPath.add(state);
                List<int[][]> result = dfsRecursive(neighbor, newPath, depth + 1, depthLimit, visited);
                if (result != null) return result;
            }
        }
        return null;
    }

    private static void printSolution(List<int[][]> solution) {
        if (solution == null) {
            System.out.println("No solution found.");
            return;
        }
        System.out.println("\nSolution found in " + (solution.size() - 1) + " moves:");
        for (int[][] step : solution) {
            for (int[] row : step) {
                System.out.println(Arrays.toString(row));
            }
            System.out.println("----");
        }
    }

       private static int[][] inputState(Scanner sc) {
        int[][] state = new int[3][3];
        System.out.println("Enter the puzzle state row by row (use 0 for blank):");
        for (int i = 0; i < 3; i++) {
            System.out.print("Row " + (i + 1) + ": ");
            for (int j = 0; j < 3; j++) {
                state[i][j] = sc.nextInt();
            }
        }
        return state;
    }

    private static int[][] deepCopy(int[][] matrix) {
        int[][] copy = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i].clone();
        }
        return copy;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- 8 Puzzle Solver ---");
            System.out.println("1. Solve using BFS");
            System.out.println("2. Solve using DFS");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            String choice = sc.next();

            if (choice.equals("1")) {
                int[][] start = inputState(sc);
                System.out.println("\nSolving with BFS...");
                List<int[][]> solution = bfs(start);
                printSolution(solution);

            } else if (choice.equals("2")) {
                int[][] start = inputState(sc);
                System.out.println("\nSolving with DFS...");
                List<int[][]> solution = dfs(start, 50);
                printSolution(solution);

            } else if (choice.equals("3")) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice! Please try again.");
            }
        }
        sc.close();
    }
}

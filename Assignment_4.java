import java.util.*;

public class Assignment_4 {
    static class Node implements Comparable<Node> {
        int f, g;
        Object state;
        List<Object> path;

        Node(int f, int g, Object state, List<Object> path) {
            this.f = f;
            this.g = g;
            this.state = state;
            this.path = path;
        }

        public int compareTo(Node o) {
            return Integer.compare(this.f, o.f);
        }
    }

    static Object[] aStarSearch(Object start, Object goal,
                                java.util.function.Function<Object, List<Object[]>> neighborsFn,
                                java.util.function.BiFunction<Object, Object, Integer> heuristicFn) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        openList.add(new Node(heuristicFn.apply(start, goal), 0, start, new ArrayList<>(Arrays.asList(start))));
        Set<Object> closedSet = new HashSet<>();

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            if (current.state.equals(goal)) return new Object[]{current.path, current.g};
            if (closedSet.contains(current.state)) continue;
            closedSet.add(current.state);

            for (Object[] neighbor : neighborsFn.apply(current.state)) {
                Object nextState = neighbor[0];
                int cost = (int) neighbor[1];
                if (closedSet.contains(nextState)) continue;
                int gNew = current.g + cost;
                int fNew = gNew + heuristicFn.apply(nextState, goal);
                List<Object> newPath = new ArrayList<>(current.path);
                newPath.add(nextState);
                openList.add(new Node(fNew, gNew, nextState, newPath));
            }
        }
        return new Object[]{null, Integer.MAX_VALUE};
    }

    static Map<String, List<Object[]>> graph = new HashMap<>();
    static {
        graph.put("A", Arrays.asList(new Object[]{"B",1}, new Object[]{"C",3}));
        graph.put("B", Arrays.asList(new Object[]{"D",3}, new Object[]{"E",1}));
        graph.put("C", Arrays.<Object[]>asList(new Object[]{"F",5}));
        graph.put("D", Arrays.<Object[]>asList(new Object[]{"G",2}));
        graph.put("E", Arrays.<Object[]>asList(new Object[]{"G",2}));
        graph.put("F", Arrays.<Object[]>asList(new Object[]{"G",1}));
        graph.put("G", new ArrayList<>());
    }

    static List<Object[]> neighborsGraph(Object node) {
        return graph.getOrDefault((String)node, new ArrayList<>());
    }

    static int heuristicGraph(Object node, Object goal) {
        Map<String,Integer> h = new HashMap<>();
        h.put("A",7); h.put("B",6); h.put("C",4);
        h.put("D",2); h.put("E",1); h.put("F",2); h.put("G",0);
        return h.get(node);
    }

    static void graphDemo() {
        System.out.println("\n--- A* Search on Graph ---");
        String start="A", goal="G";
        Object[] result = aStarSearch(start, goal, Assignment_4::neighborsGraph, Assignment_4::heuristicGraph);
        System.out.println("Path from "+start+" to "+goal+": "+result[0]+", Cost = "+result[1]);
    }

    static int[][] goalState = {{1,2,3},{4,5,6},{7,8,0}};

    static List<Object[]> neighborsPuzzle(Object st) {
        int[][] state = (int[][]) st;
        List<Object[]> neighbors = new ArrayList<>();
        int n=3,x=0,y=0;
        for (int i=0;i<n;i++) for (int j=0;j<n;j++) if (state[i][j]==0) {x=i;y=j;}
        int[][] moves={{0,1},{1,0},{0,-1},{-1,0}};
        for (int[] mv:moves) {
            int nx=x+mv[0], ny=y+mv[1];
            if (0<=nx && nx<n && 0<=ny && ny<n) {
                int[][] newState=new int[n][n];
                for (int i=0;i<n;i++) newState[i]=state[i].clone();
                int tmp=newState[x][y];
                newState[x][y]=newState[nx][ny];
                newState[nx][ny]=tmp;
                neighbors.add(new Object[]{newState,1});
            }
        }
        return neighbors;
    }

    static int heuristicPuzzle(Object st, Object goal) {
        int[][] state=(int[][])st, g=(int[][])goal;
        int dist=0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                int val=state[i][j];
                if(val!=0){
                    int gi=0,gj=0;
                    for(int r=0;r<3;r++)
                        for(int c=0;c<3;c++)
                            if(g[r][c]==val){gi=r;gj=c;}
                    dist+=Math.abs(i-gi)+Math.abs(j-gj);
                }
            }
        }
        return dist;
    }

    static void puzzleDemo() {
        System.out.println("\n--- A* Search on 8-Puzzle ---");
        int[][] startState={{1,2,3},{4,0,6},{7,5,8}};
        Object[] result = aStarSearch(startState, goalState, Assignment_4::neighborsPuzzle, Assignment_4::heuristicPuzzle);
        System.out.println("Solution found in "+result[1]+" moves");
        System.out.println("Final State:");
        for(int[] row:goalState) System.out.println(Arrays.toString(row));
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        while(true){
            System.out.println("\n--- A* Algorithm Applications ---");
            System.out.println("1. Shortest Path in Graph");
            System.out.println("2. Solve 8-Puzzle Problem");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            String choice=sc.nextLine();
            if(choice.equals("1")) graphDemo();
            else if(choice.equals("2")) puzzleDemo();
            else if(choice.equals("3")) {System.out.println("Exiting...");break;}
            else System.out.println("Invalid choice!");
        }
        sc.close();
    }
}

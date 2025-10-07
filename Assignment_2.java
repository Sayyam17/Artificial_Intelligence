import java.util.*;
import java.util.function.BiFunction;

class CSP {
    List<String> variables;
    Map<String, List<Integer>> domains;
    Map<String, List<Constraint>> constraints = new HashMap<>();
    Map<String, Set<String>> neighbors = new HashMap<>();

    static class Constraint {
        String var;
        BiFunction<Integer, Integer, Boolean> fn;
        Constraint(String var, BiFunction<Integer, Integer, Boolean> fn) {
            this.var = var;
            this.fn = fn;
        }
    }

    CSP(List<String> vars, Map<String, List<Integer>> doms) {
        variables = new ArrayList<>(vars);
        domains = new HashMap<>();
        for (String v : variables) domains.put(v, new ArrayList<>(doms.get(v)));
        for (String v : variables) {
            constraints.put(v, new ArrayList<>());
            neighbors.put(v, new HashSet<>());
        }
    }

    void addConstraint(String xi, String xj, BiFunction<Integer, Integer, Boolean> fn) {
        constraints.get(xi).add(new Constraint(xj, fn));
        constraints.get(xj).add(new Constraint(xi, (y, x) -> fn.apply(x, y)));
        neighbors.get(xi).add(xj);
        neighbors.get(xj).add(xi);
    }

    boolean isConsistent(String var, Map<String,Integer> assignment) {
        int val = assignment.get(var);
        for (Constraint c : constraints.get(var)) {
            if (assignment.containsKey(c.var) && !c.fn.apply(val, assignment.get(c.var)))
                return false;
        }
        return true;
    }
}

public class Assignment_2 {
    static boolean ac3(CSP csp, Deque<String[]> queue) {
        if (queue == null) {
            queue = new ArrayDeque<>();
            for (String xi : csp.variables)
                for (CSP.Constraint c : csp.constraints.get(xi))
                    queue.add(new String[]{xi, c.var});
        }
        while (!queue.isEmpty()) {
            String[] pair = queue.poll();
            if (revise(csp, pair[0], pair[1])) {
                if (csp.domains.get(pair[0]).isEmpty()) return false;
                for (String xk : csp.neighbors.get(pair[0]))
                    if (!xk.equals(pair[1]))
                        queue.add(new String[]{xk, pair[0]});
            }
        }
        return true;
    }

    static boolean revise(CSP csp, String xi, String xj) {
        boolean revised = false;
        List<BiFunction<Integer,Integer,Boolean>> fns = new ArrayList<>();
        for (CSP.Constraint c : csp.constraints.get(xi))
            if (c.var.equals(xj)) fns.add(c.fn);
        List<Integer> newDom = new ArrayList<>();
        for (int vi : csp.domains.get(xi)) {
            boolean ok = false;
            for (int vj : csp.domains.get(xj)) {
                boolean all = true;
                for (BiFunction<Integer,Integer,Boolean> fn : fns)
                    if (!fn.apply(vi, vj)) { all = false; break; }
                if (all) { ok = true; break; }
            }
            if (ok) newDom.add(vi); else revised = true;
        }
        if (revised) csp.domains.put(xi, newDom);
        return revised;
    }

    static String selectUnassignedVariable(Map<String,Integer> assignment, CSP csp) {
        return csp.variables.stream()
            .filter(v -> !assignment.containsKey(v))
            .min(Comparator.comparingInt((String v) -> csp.domains.get(v).size())
                    .thenComparing((String v) -> -csp.neighbors.get(v).size()))
            .orElse(null);
    }

    static List<Integer> orderDomainValues(String var, Map<String,Integer> assignment, CSP csp) {
        List<int[]> counts = new ArrayList<>();
        for (int val : csp.domains.get(var)) {
            int count = 0;
            for (String nbr : csp.neighbors.get(var)) {
                if (assignment.containsKey(nbr)) continue;
                List<BiFunction<Integer,Integer,Boolean>> fns = new ArrayList<>();
                for (CSP.Constraint c : csp.constraints.get(var))
                    if (c.var.equals(nbr)) fns.add(c.fn);
                for (int val2 : csp.domains.get(nbr)) {
                    boolean all = true;
                    for (BiFunction<Integer,Integer,Boolean> fn : fns)
                        if (!fn.apply(val, val2)) { all = false; break; }
                    if (!all) count++;
                }
            }
            counts.add(new int[]{count,val});
        }
        counts.sort(Comparator.comparingInt(a -> a[0]));
        List<Integer> result = new ArrayList<>();
        for (int[] pair : counts) result.add(pair[1]);
        return result;
    }

    static Map<String,Integer> backtrackingSearch(CSP csp) {
        CSP copy = copyCSP(csp);
        if (!ac3(copy,null)) return null;
        return backtrack(new HashMap<>(), copy);
    }

    static Map<String,Integer> backtrack(Map<String,Integer> assignment, CSP csp) {
        if (assignment.size() == csp.variables.size()) return assignment;
        String var = selectUnassignedVariable(assignment, csp);
        for (int value : orderDomainValues(var, assignment, csp)) {
            assignment.put(var, value);
            if (csp.isConsistent(var, assignment)) {
                Map<String,List<Integer>> saved = new HashMap<>();
                for (String v : csp.variables) saved.put(v, new ArrayList<>(csp.domains.get(v)));
                csp.domains.put(var, Arrays.asList(Integer.valueOf(value)));
                Deque<String[]> queue = new ArrayDeque<>();
                for (String nbr : csp.neighbors.get(var)) queue.add(new String[]{nbr,var});
                if (ac3(csp,queue)) {
                    Map<String,Integer> result = backtrack(assignment,csp);
                    if (result!=null) return result;
                }
                csp.domains = saved;
            }
            assignment.remove(var);
        }
        return null;
    }

    static CSP copyCSP(CSP c) {
        Map<String,List<Integer>> doms = new HashMap<>();
        for (String v : c.variables) doms.put(v, new ArrayList<>(c.domains.get(v)));
        CSP copy = new CSP(c.variables, doms);
        for (String v : c.variables) {
            for (CSP.Constraint ct : c.constraints.get(v)) {
                copy.constraints.get(v).add(new CSP.Constraint(ct.var, ct.fn));
            }
            copy.neighbors.put(v, new HashSet<>(c.neighbors.get(v)));
        }
        return copy;
    }

    static void australiaMapColoring() {
        List<String> vars = Arrays.asList("WA","NT","SA","Q","NSW","V","T");
        List<Integer> colors = Arrays.asList(1,2,3);
        Map<String,List<Integer>> doms = new HashMap<>();
        for (String v : vars) doms.put(v, colors);
        CSP csp = new CSP(vars, doms);
        String[][] edges = {
            {"WA","NT"},{"WA","SA"},{"NT","SA"},{"NT","Q"},
            {"SA","Q"},{"SA","NSW"},{"SA","V"},{"Q","NSW"},{"NSW","V"}
        };
        for (String[] e : edges)
            csp.addConstraint(e[0],e[1],(Integer x, Integer y)->!x.equals(y));
        Map<String,Integer> sol = backtrackingSearch(csp);
        System.out.println("Australia Map Coloring Solution: " + sol);
    }

    static void sudoku() {
        int[][] puzzle = {
            {5,3,0,0,7,0,0,0,0},
            {6,0,0,1,9,5,0,0,0},
            {0,9,8,0,0,0,0,6,0},
            {8,0,0,0,6,0,0,0,3},
            {4,0,0,8,0,3,0,0,1},
            {7,0,0,0,2,0,0,0,6},
            {0,6,0,0,0,0,2,8,0},
            {0,0,0,4,1,9,0,0,5},
            {0,0,0,0,8,0,0,7,9}
        };
        List<String> vars = new ArrayList<>();
        Map<String,List<Integer>> doms = new HashMap<>();
        for (int r=0;r<9;r++) {
            for (int c=0;c<9;c++) {
                String name="r"+r+"c"+c;
                vars.add(name);
                if (puzzle[r][c]!=0) doms.put(name, Arrays.asList(puzzle[r][c]));
                else {
                    List<Integer> range=new ArrayList<>();
                    for (int i=1;i<=9;i++) range.add(i);
                    doms.put(name, range);
                }
            }
        }
        CSP csp = new CSP(vars,doms);
        BiFunction<Integer,Integer,Boolean> neq=(Integer a, Integer b)->!a.equals(b);

        // Row constraints
        for (int r=0;r<9;r++)
            for (int c1=0;c1<9;c1++)
                for (int c2=c1+1;c2<9;c2++)
                    csp.addConstraint("r"+r+"c"+c1,"r"+r+"c"+c2,neq);

        // Column constraints
        for (int c=0;c<9;c++)
            for (int r1=0;r1<9;r1++)
                for (int r2=r1+1;r2<9;r2++)
                    csp.addConstraint("r"+r1+"c"+c,"r"+r2+"c"+c,neq);

        // Block constraints
        for (int br=0;br<3;br++)
            for (int bc=0;bc<3;bc++) {
                List<int[]> cells=new ArrayList<>();
                for (int dr=0;dr<3;dr++)
                    for (int dc=0;dc<3;dc++)
                        cells.add(new int[]{br*3+dr, bc*3+dc});
                for (int i=0;i<9;i++)
                    for (int j=i+1;j<9;j++) {
                        int[] c1=cells.get(i), c2=cells.get(j);
                        csp.addConstraint("r"+c1[0]+"c"+c1[1],"r"+c2[0]+"c"+c2[1],neq);
                    }
            }

        Map<String,Integer> sol = backtrackingSearch(csp);
        System.out.println("Sudoku Solution:");
        if (sol!=null) {
            for (int r=0;r<9;r++) {
                for (int c=0;c<9;c++) System.out.print(sol.get("r"+r+"c"+c)+" ");
                System.out.println();
            }
        } else System.out.println("No solution found.");
    }

    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        while(true) {
            System.out.println("\n--- Constraint Satisfaction Problem Solver ---");
            System.out.println("1. Solve Australia Map Coloring");
            System.out.println("2. Solve Sudoku");
            System.out.println("3. Exit");
            String choice=sc.nextLine();
            if (choice.equals("1")) australiaMapColoring();
            else if (choice.equals("2")) sudoku();
            else if (choice.equals("3")) {System.out.println("Exiting..."); break;}
            else System.out.println("Invalid choice, try again.");
        }
    }
}

import java.util.*;

public class Assignment_8 {
    static Map<String, List<String>> rules = new HashMap<>();
    static Set<String> facts = new HashSet<>();

    static boolean infer(String goal) {
        if (facts.contains(goal)) return true;
        if (!rules.containsKey(goal)) return false;
        for (String condition : rules.get(goal))
            if (!infer(condition)) return false;
        facts.add(goal);
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of rules: ");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter conclusion for rule " + (i + 1) + ": ");
            String conclusion = sc.nextLine();
            System.out.print("Enter number of conditions for " + conclusion + ": ");
            int m = sc.nextInt();
            sc.nextLine();
            List<String> conditions = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                System.out.print("Enter condition " + (j + 1) + ": ");
                conditions.add(sc.nextLine());
            }
            rules.put(conclusion, conditions);
        }

        System.out.print("Enter number of known facts: ");
        int f = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < f; i++) {
            System.out.print("Enter fact " + (i + 1) + ": ");
            facts.add(sc.nextLine());
        }

        System.out.print("Enter goal: ");
        String goal = sc.nextLine();

        if (infer(goal)) System.out.println("Goal achieved");
        else System.out.println("Goal not achieved");
    }
}

import java.util.*;

public class Assignment_7 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, List<String>> rules = new HashMap<>();
        Set<String> facts = new HashSet<>();

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

        System.out.print("Enter number of initial facts: ");
        int f = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < f; i++) {
            System.out.print("Enter fact " + (i + 1) + ": ");
            facts.add(sc.nextLine());
        }

        System.out.print("Enter goal: ");
        String goal = sc.nextLine();

        boolean added;
        do {
            added = false;
            for (String conclusion : rules.keySet()) {
                if (!facts.contains(conclusion)) {
                    List<String> conditions = rules.get(conclusion);
                    if (facts.containsAll(conditions)) {
                        facts.add(conclusion);
                        added = true;
                    }
                }
            }
        } while (added);

        if (facts.contains(goal)) System.out.println("Goal achieved");
        else System.out.println("Goal not achieved");
    }
}

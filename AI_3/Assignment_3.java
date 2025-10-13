import java.util.*;

public class Assignment_3 {
    static List<String[]> parentFacts = Arrays.asList(
        new String[]{"john","mary"},
        new String[]{"john","mike"},
        new String[]{"susan","mary"},
        new String[]{"susan","mike"},
        new String[]{"mary","alice"},
        new String[]{"mike","bob"}
    );

    static boolean isParent(String x, String y) {
        return parentFacts.stream().anyMatch(f -> f[0].equals(x) && f[1].equals(y));
    }

    static boolean siblings(String x, String y) {
        for (String[] p1 : parentFacts) {
            for (String[] p2 : parentFacts) {
                if (p1[0].equals(p2[0]) && p1[1].equals(x) && p2[1].equals(y) && !x.equals(y))
                    return true;
            }
        }
        return false;
    }

    static boolean grandparents(String x, String y) {
        for (String[] p : parentFacts) {
            if (p[0].equals(x)) {
                for (String[] p2 : parentFacts) {
                    if (p2[0].equals(p[1]) && p2[1].equals(y)) return true;
                }
            }
        }
        return false;
    }

    static String queryFamilyTree(String query) {
        String[] tokens = query.toLowerCase().split(" ");
        if (tokens.length < 3) return "Query too short.";

        if (tokens[0].equals("parents") && tokens[1].equals("of")) {
            String child = tokens[2];
            List<String> res = new ArrayList<>();
            for (String[] f : parentFacts) if (f[1].equals(child)) res.add(f[0]);
            return res.isEmpty() ? "No parents found." : String.join(", ", res);

        } else if (tokens[0].equals("children") && tokens[1].equals("of")) {
            String parent = tokens[2];
            List<String> res = new ArrayList<>();
            for (String[] f : parentFacts) if (f[0].equals(parent)) res.add(f[1]);
            return res.isEmpty() ? "No children found." : String.join(", ", res);

        } else if (tokens[0].equals("siblings")) {
            if (tokens.length == 4 && tokens[2].equals("and")) {
                return siblings(tokens[1], tokens[3]) ? "Yes" : "No";
            } else if (tokens.length == 3) {
                return siblings(tokens[1], tokens[2]) ? "Yes" : "No";
            } else {
                return "Invalid siblings query. Use: 'siblings mary and mike'";
            }

        } else if (tokens[0].equals("grandparent")) {
            if (tokens.length == 4 && tokens[2].equals("of")) {
                return grandparents(tokens[1], tokens[3]) ? "Yes" : "No";
            } else {
                return "Invalid grandparent query. Use: 'grandparent john of alice'";
            }

        } else {
            return "Query not understood.";
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Family Tree Knowledge Base Parser");
        System.out.println("Examples: 'parents of mary', 'children of john', 'siblings mary and mike', 'grandparent john of alice'");
        while (true) {
            System.out.print("\nEnter query (or 'exit'): ");
            String q = sc.nextLine();
            if (q.equalsIgnoreCase("exit")) break;
            System.out.println("Answer: " + queryFamilyTree(q));
        }
        sc.close();
    }
}

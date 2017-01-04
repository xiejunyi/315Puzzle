

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by junyi on 1/4/17.
 */
public class Puzzle315 {
    private final boolean allowDup;
    private final int upper;

    private Puzzle315(int upper, boolean allowDup) {
        this.allowDup = allowDup;
        this.upper = upper;
    }

    public static void main(String args[]) {
        try {
            int upper = Integer.valueOf(args[0]);
            if (upper < 1) {
                usage(null);
            }
            boolean allDup = Boolean.valueOf(args[1]);
            final Puzzle315 p = new Puzzle315(upper, allDup);
            final List<Pair> solutions = p.solve();
            System.out.println("Range of numbers between 1 and " + upper + " " +
                               "inclusively.");
            System.out.println("allow duplicates? " + allDup);
            System.out.println("Solution pairs: " +
                               Arrays.toString(solutions.toArray()));

        } catch (NumberFormatException e) {
            usage(null);
        }
    }
    private List<Pair> solve() {
        final List<Pair> solution = new ArrayList<>();
        for (int i = 1; i <= upper; i++) {
            for (int j = i; j <= upper; j++) {
                if (!allowDup && (i == j)) {
                    continue;
                }

                final Pair sol = new Pair(i, j);
                if (isValidSolution(sol)) {
                    solution.add(sol);
                }
            }
        }

        return solution;
    }

    private boolean isValidSolution(Pair sol) {
        final int sum = sol.first + sol.second;
        final int prod = sol.first * sol.second;

        final List<Pair> allSumSolution = getAllSumSolution(sum);
        /* A does not know the answer in the 1st round */
        if (allSumSolution.size() == 1) {
            return false;
        }

        final List<Pair> allProdSolution = getAllProdSolution(prod);
        /* B does not know the answer in the first round */
        if (allProdSolution.size() == 1) {
            return false;
        }

        /* A must know the solution in the 2nd round */
        if (!canAKnowInSecondRound(allSumSolution)) {
            return false;
        }

        final List<Pair> eliminationB = new ArrayList<>();
        for (Pair s : allProdSolution) {
            if (canAKnowInSecondRound(getAllSumSolution(s.first + s.second))) {
                eliminationB.add(s);
            }
        }

        /* B must know the answer in the second round */
        return eliminationB.size() <= 1;
    }

    private boolean canAKnowInSecondRound(List<Pair> allSumSolution) {

        final List<Pair> eliminationA = new ArrayList<>();
        for (Pair s :  allSumSolution) {
            final List<Pair> prodSols = getAllProdSolution(s.first * s.second);

            /* remove solution since B does not know in 1st round */
            if (prodSols.size() != 1) {
                eliminationA.add(s);
            }
        }

        /* A must know the answer in the second round */
        return eliminationA.size() <= 1;
    }

    private List<Pair> getAllSumSolution(int sum) {
        final List<Pair> ret = new ArrayList<>();
        for (int i = 1; i <= sum/2; i++) {
            final int j = sum - i;
            if (!allowDup && (i == j)) {
                continue;
            }
            if (i <= upper && j <= upper) {
                ret.add(new Pair(i, sum - i));
            }
        }
        return ret;
    }

    private List<Pair> getAllProdSolution(int prod) {
        final List<Pair> ret = new ArrayList<>();
        for (int i = 1; i <= Math.sqrt(prod); i++) {
            final int j = prod / i;
            if (!allowDup && (i == j)) {
                continue;
            }
            if (i * j == prod && i <= upper && j <= upper) {
                ret.add(new Pair(i, j));
            }
        }
        return ret;
    }

    private static void usage(final String message) {
        if (message != null) {
            System.err.println("\n" + message + "\n");
        }

        System.err.println("usage: + java Puzzle315 ");
        System.err.println("\t[limit]\t[true|false]\n");
        System.exit(1);
    }

    class Pair {
        private final int first;
        private final int second;

        Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        public int first() {
            return first;
        }

        public int second() {
            return second;
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }
}


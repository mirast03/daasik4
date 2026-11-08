import java.util.ArrayList;
import java.util.List;

public class Naive {
    public static long comparisons;
    public static List<Integer> search(String text, String pattern) {
        comparisons = 0;
        List<Integer> matches = new ArrayList<>();
        int m = pattern.length();

        for (int i = 0; i <= text.length() - m; i++) {
            int j = 0;
            while (j < m) {
                comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            if (j == m) {
                matches.add(i);
            }
        }
        return matches;
    }
}
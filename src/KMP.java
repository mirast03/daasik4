import java.util.ArrayList;
import java.util.List;

public class KMP {
    public static long comparisons;
    public static int[] buildLPS(String pattern) {
        comparisons = 0;
        int[] lps = new int[pattern.length()];
        int length = 0;
        int i = 1;

        while (i < pattern.length()) {
            comparisons++;
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else if (length > 0) {
                length = lps[length - 1];
            } else {
                i++;
            }
        }

        return lps;
    }
    public static List<Integer> search(String text, String pattern) {
        comparisons = 0;
        List<Integer> matches = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                matches.add(i);
            }
            return matches;
        }
        if (m > n) {
            return matches;
        }

        int[] lps = buildLPS(pattern);
        int i = 0;
        int j = 0;
        while (i < n) {
            comparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    matches.add(i - m);
                    j = lps[j - 1];
                }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return matches;
    }
}
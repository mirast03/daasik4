import java.util.ArrayList;
import java.util.List;

public class RabinKarp {
    public static long comparisons;
    public static long hashComparisons;

    public static List<Integer> search(String text, String pattern) {
        comparisons = 0;
        hashComparisons = 0;
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

        int base = 256;
        int mod = 101;
        int power = 1;
        int patternHash = 0;
        int windowHash = 0;

        for (int i = 1; i < m; i++) {
            power = (power * base) % mod;
        }
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * base + pattern.charAt(i)) % mod;
            windowHash = (windowHash * base + text.charAt(i)) % mod;
        }

        for (int i = 0; i <= n - m; i++) {
            hashComparisons++;
            if (windowHash == patternHash) {
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

            if (i < n - m) {
                windowHash = (base * (windowHash - text.charAt(i) * power)
                        + text.charAt(i + m)) % mod;
                if (windowHash < 0) {
                    windowHash += mod;
                }
            }
        }

        return matches;
    }
}
public class Kasai {
    public static int[] buildLCP(String text, int[] suffixArray) {
        int n = text.length();
        int[] rank = new int[n];
        int[] lcp = new int[n];

        for (int i = 0; i < n; i++) {
            rank[suffixArray[i]] = i;
        }

        int length = 0;
        for (int i = 0; i < n; i++) {
            int r = rank[i];
            if (r == 0) {
                length = 0;
                continue;
            }

            int j = suffixArray[r - 1];
            while (i + length < n && j + length < n
                    && text.charAt(i + length) == text.charAt(j + length)) {
                length++;
            }

            lcp[r] = length;
            if (length > 0) {
                length--;
            }
        }

        return lcp;
    }
}
import java.util.Arrays;
import java.util.List;

public class Tests {
    public static void main(String[] args) {
        check(3, LCS.length("ABC", "ABC"));
        check(0, LCS.length("ABC", "XYZ"));
        check(0, LCS.length("", "ABC"));
        check(3, LCS.length("ABCDEF", "ACE"));
        System.out.println("LCS OK");

        check(0, EditDistance.distance("abc", "abc"));
        check(3, EditDistance.distance("", "abc"));
        check(1, EditDistance.distance("cat", "bat"));
        check(1, EditDistance.distance("cat", "cart"));
        check(1, EditDistance.distance("cart", "cat"));
        check(3, EditDistance.distance("kitten", "sitting"));
        System.out.println("Edit Distance OK");

        match("ABCXYZ", "ABC", 0);
        match("XABCY", "ABC", 1);
        match("XYZABC", "ABC", 3);
        match("AAAA", "AA", 0, 1, 2);
        match("ABC", "Z");
        match("AB", "ABC");
        match("AB", "", 0, 1, 2);
        match("", "", 0);
        match("BDAz", "Az", 2);
        check("[0, 0, 1, 2, 0, 1, 2, 3, 4]",
                Arrays.toString(KMP.buildLPS("ABABCABAB")));
        System.out.println("Naive, KMP, Rabin-Karp OK");

        int[] suffixArray = {5, 3, 1, 0, 4, 2};
        check("[0, 1, 3, 0, 0, 2]", Arrays.toString(Kasai.buildLCP("banana", suffixArray)));
        check("[]", Arrays.toString(Kasai.buildLCP("", new int[0])));
        System.out.println("Kasai OK");
    }

    private static void match(String text, String pattern, Integer... indices) {
        List<Integer> expected = Arrays.asList(indices);
        check(expected, Naive.search(text, pattern));
        check(expected, KMP.search(text, pattern));
        check(expected, RabinKarp.search(text, pattern));
    }

    private static void check(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }
}
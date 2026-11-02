public class LCSTest {
    public static void main(String[] args) {
        check("ABC", "ABC", 3);
        check("ABC", "XYZ", 0);
        check("", "ABC", 0);
        check("ABC", "", 0);
        check("", "", 0);
        check("ABCDEF", "ACE", 3);
        check("ABC", "BAC", 2);
        check("AABA", "ABAA", 3);
        System.out.println("All 8 LCS tests passed.");
    }
    public static void check(String first, String second, int expected) {
        int actual=LCS.length(first, second);
        if (actual!=expected) {
            throw new AssertionError(
                    "Expected "+expected+", got "+actual
            );
        }
    }
}
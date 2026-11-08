import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class Experiments {
    private static final int RUNS = 5;
    private static volatile Object result;

    public static void main(String[] args) throws IOException {
        Random random = new Random(42);
        Files.createDirectories(Path.of("results"));

        try (PrintWriter csv = new PrintWriter("results/timings.csv")) {
            csv.println("algorithm,scenario,size,runs,mean_ms,cell_updates,char_checks,hash_checks");

            for (int size : new int[]{100, 500, 1000}) {
                String first = randomText(size, random);
                String second = randomText(size, random);

                double time = measure(() -> LCS.length(first, second));
                write(csv, "LCS", "dp", size, time, size * size, 0, 0);

                time = measure(() -> EditDistance.distance(first, second));
                write(csv, "Edit Distance", "dp", size, time, size * size, 0, 0);
            }

            for (int size : new int[]{10000, 50000, 100000}) {
                String text = randomText(size, random);
                int start = size / 2;
                String present = text.substring(start, start + 20);
                String absent = present.substring(0, 19) + "E";

                matching(csv, "present", text, present, start);
                matching(csv, "absent", text, absent, -1);
            }

            for (int size : new int[]{1000, 5000, 10000}) {
                String text = randomText(size, random);
                int[] suffixArray = suffixArray(text);

                double time = measure(() -> Kasai.buildLCP(text, suffixArray));
                write(csv, "Kasai", "lcp", size, time, 0, 0, 0);
            }

            if (csv.checkError()) {
                throw new IOException("Could not write results/timings.csv");
            }
        }

        System.out.println("Saved results/timings.csv");
    }

    private static void matching(PrintWriter csv, String scenario,
                                 String text, String pattern, int expectedIndex) {
        List<Integer> expected = Naive.search(text, pattern);
        if (expectedIndex == -1 && !expected.isEmpty()) {
            throw new AssertionError("Absent pattern was found");
        }
        if (expectedIndex >= 0 && !expected.contains(expectedIndex)) {
            throw new AssertionError("Present pattern was missed");
        }
        if (!expected.equals(KMP.search(text, pattern))
                || !expected.equals(RabinKarp.search(text, pattern))) {
            throw new AssertionError("Matchers disagree");
        }

        int size = text.length();

        double time = measure(() -> Naive.search(text, pattern));
        write(csv, "Naive", scenario, size, time, 0, Naive.comparisons, 0);

        time = measure(() -> KMP.search(text, pattern));
        write(csv, "KMP", scenario, size, time, 0, KMP.comparisons, 0);

        time = measure(() -> RabinKarp.search(text, pattern));
        write(csv, "Rabin-Karp", scenario, size, time, 0,
                RabinKarp.comparisons, RabinKarp.hashComparisons);
    }

    private static double measure(Supplier<Object> task) {
        for (int i = 0; i < 3; i++) {
            result = task.get();
        }

        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            Object value = task.get();
            long elapsed = System.nanoTime() - start;

            total += elapsed;
            result = value;
        }

        return total / (RUNS * 1_000_000.0);
    }

    private static String randomText(int size, Random random) {
        String alphabet = "ABCD";
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < size; i++) {
            text.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return text.toString();
    }

    private static int[] suffixArray(String text) {
        int n = text.length();
        String[] suffixes = new String[n];

        for (int i = 0; i < n; i++) {
            suffixes[i] = text.substring(i);
        }
        Arrays.sort(suffixes);

        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = n - suffixes[i].length();
        }

        return indices;
    }

    private static void write(PrintWriter csv, String algorithm, String scenario,
                              int size, double time, int cells, long chars, long hashes) {
        csv.println(algorithm + "," + scenario + "," + size + "," + RUNS + ","
                + time + "," + cells + "," + chars + "," + hashes);
    }
}
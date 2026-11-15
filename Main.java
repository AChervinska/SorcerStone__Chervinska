import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Main {
    private static final Path DEFAULT_FILE = Path.of("src", "txt", "harry.txt");
    private static final int DEFAULT_LIMIT = 30;
    private static final Pattern WORD_PATTERN = Pattern.compile("[a-zA-Z]+");

    private Main() {
    }

    public static void main(String[] args) {
        long startNanos = System.nanoTime();
        try {
            Path file = args.length > 0 ? Path.of(args[0]) : DEFAULT_FILE;
            int limit = args.length > 1 ? parseLimit(args[1]) : DEFAULT_LIMIT;
            if (args.length > 2) {
                throw new IllegalArgumentException("Usage: java Main [file-path] [positive-limit]");
            }
            String content = Files.readString(file, StandardCharsets.UTF_8);
            Map<String, Integer> frequencies = countWords(content);
            List<Map.Entry<String, Integer>> top = topWords(frequencies, limit);
            for (Map.Entry<String, Integer> entry : top) {
                System.out.printf("%s %d%n", entry.getKey(), entry.getValue());
            }
            System.out.println("------");
            System.out.printf("Elapsed: %.3f ms%n", (System.nanoTime() - startNanos) / 1_000_000.0);
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println("Error: " + exception.getMessage());
            System.exit(1);
        }
    }

    private static int parseLimit(String raw) {
        final int limit;
        try {
            limit = Integer.parseInt(raw);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Limit must be a positive integer: " + raw, exception);
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than zero");
        }
        return limit;
    }

    public static Map<String, Integer> countWords(String content) {
        if (content == null) {
            throw new IllegalArgumentException("Text must not be null");
        }
        Map<String, Integer> frequencies = new HashMap<>();
        Matcher matcher = WORD_PATTERN.matcher(content.toLowerCase(Locale.ROOT));
        while (matcher.find()) {
            frequencies.merge(matcher.group(), 1, Integer::sum);
        }
        return frequencies;
    }

    public static List<Map.Entry<String, Integer>> topWords(Map<String, Integer> frequencies, int limit) {
        if (frequencies == null || limit < 0) {
            throw new IllegalArgumentException("Frequencies must not be null and limit must be nonnegative");
        }
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(frequencies.entrySet());
        sorted.sort(Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                .reversed().thenComparing(Map.Entry::getKey));
        return new ArrayList<>(sorted.subList(0, Math.min(limit, sorted.size())));
    }
}

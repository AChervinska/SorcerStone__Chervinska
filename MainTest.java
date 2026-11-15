import java.util.List;
import java.util.Map;

public final class MainTest {
    public static void main(String[] args) {
        assert Main.countWords("").isEmpty();
        assert Main.countWords("!!! 123").isEmpty();
        assert Main.countWords("Harry, HARRY! harry.").get("harry") == 3;
        assert Main.countWords("he the he").equals(Map.of("he", 2, "the", 1));
        assert Main.countWords("a\tb\nc").size() == 3;
        assert Main.countWords("don't re-enter").equals(Map.of("don", 1, "t", 1, "re", 1, "enter", 1));
        assert Main.countWords("école café").equals(Map.of("cole", 1, "caf", 1));
        List<Map.Entry<String, Integer>> top = Main.topWords(Map.of("z", 2, "a", 2, "b", 1), 2);
        assert top.size() == 2;
        assert top.get(0).getKey().equals("a");
        assert top.get(1).getKey().equals("z");
        assert Main.topWords(Map.of("one", 1), 30).size() == 1;
        assert Main.topWords(Map.of(), 30).isEmpty();
        assert Main.topWords(Map.of("one", 1), 0).isEmpty();
        boolean rejectedNull = false;
        try {
            Main.countWords(null);
        } catch (IllegalArgumentException e) {
            rejectedNull = true;
        }
        assert rejectedNull;
        boolean rejectedNegative = false;
        try {
            Main.topWords(Map.of(), -1);
        } catch (IllegalArgumentException e) {
            rejectedNegative = true;
        }
        assert rejectedNegative;
        System.out.println("All tests passed.");
    }
}

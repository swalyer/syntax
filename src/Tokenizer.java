import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\s*(\\(|\\)|\\+|\\-|\\*|\\/|:=|;|\\w+|\\'.\\')\\s*");
    private final String input;
    private final List<String> tokens;
    private int currentIndex;

    public Tokenizer(String input) {
        this.input = input;
        this.tokens = new ArrayList<>();
        tokenize();
    }

    private void tokenize() {
        Matcher matcher = TOKEN_PATTERN.matcher(input);
        while (matcher.find()) {
            tokens.add(matcher.group(1));
        }
    }

    public boolean hasNext() {
        return currentIndex < tokens.size();
    }

    public String next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more tokens");
        }
        return tokens.get(currentIndex++);
    }

    public static void main(String[] args) {
        String input = "x := 5 + y * (z - 3); y := 'a';";
        Tokenizer tokenizer = new Tokenizer(input);
        while (tokenizer.hasNext()) {
            System.out.println(tokenizer.next());
        }
    }
}

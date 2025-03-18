package info.developia.lib;

import java.util.Arrays;

public class Tokenizer {

    static String[] getTokens(String json) {
        return Arrays.stream(json.splitWithDelimiters("[{}\\[\\],]", 0))
                .filter(token -> !token.isBlank())
                .map(String::trim)
                .toArray(String[]::new);
    }
}

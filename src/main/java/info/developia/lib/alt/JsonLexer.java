package info.developia.lib.alt;

import java.util.ArrayList;
import java.util.List;

public class JsonLexer {
    private final String input;
    private int pos;

    public JsonLexer(String input) {
        this.input = input;
    }

    public List<JsonToken> tokenize() {
        List<JsonToken> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (Character.isWhitespace(c)) {
                pos++;
            } else if (c == '{') {
                tokens.add(new JsonToken(JsonTokenType.LBRACE, "{")); pos++;
            } else if (c == '}') {
                tokens.add(new JsonToken(JsonTokenType.RBRACE, "}")); pos++;
            } else if (c == '[') {
                tokens.add(new JsonToken(JsonTokenType.LBRACKET, "[")); pos++;
            } else if (c == ']') {
                tokens.add(new JsonToken(JsonTokenType.RBRACKET, "]")); pos++;
            } else if (c == ':') {
                tokens.add(new JsonToken(JsonTokenType.COLON, ":")); pos++;
            } else if (c == ',') {
                tokens.add(new JsonToken(JsonTokenType.COMMA, ",")); pos++;
            } else if (c == '"') {
                tokens.add(new JsonToken(JsonTokenType.STRING, readString()));
            } else if (Character.isDigit(c) || c == '-') {
                tokens.add(new JsonToken(JsonTokenType.NUMBER, readNumber()));
            } else if (input.startsWith("true", pos)) {
                tokens.add(new JsonToken(JsonTokenType.TRUE, "true")); pos += 4;
            } else if (input.startsWith("false", pos)) {
                tokens.add(new JsonToken(JsonTokenType.FALSE, "false")); pos += 5;
            } else if (input.startsWith("null", pos)) {
                tokens.add(new JsonToken(JsonTokenType.NULL, "null")); pos += 4;
            } else {
                throw new RuntimeException("Unexpected character: " + c);
            }
        }
        tokens.add(new JsonToken(JsonTokenType.EOF, null));
        return tokens;
    }

    private String readString() {
        pos++;
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && input.charAt(pos) != '"') {
            sb.append(input.charAt(pos++));
        }
        pos++;
        return sb.toString();
    }

    private String readNumber() {
        int start = pos;
        while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.' || input.charAt(pos) == '-')) {
            pos++;
        }
        return input.substring(start, pos);
    }
}

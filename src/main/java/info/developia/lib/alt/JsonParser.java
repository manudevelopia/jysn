package info.developia.lib.alt;

import java.util.List;

public class JsonParser {
    private final List<JsonToken> tokens;
    private int pos = 0;

    public JsonParser(List<JsonToken> tokens) {
        this.tokens = tokens;
    }

    public JsonValue parse() {
        return parseValue();
    }

    private JsonValue parseValue() {
        JsonToken token = tokens.get(pos);
        return switch (token.type) {
            case STRING -> new JsonString(consume().value);
            case NUMBER -> new JsonNumber(consume().value);
            case LBRACE -> parseObject();
            case LBRACKET -> parseArray();
            case TRUE -> { consume(); yield new JsonString("true"); }
            case FALSE -> { consume(); yield new JsonString("false"); }
            case NULL -> { consume(); yield new JsonString("null"); }
            default -> throw new RuntimeException("Unexpected token: " + token);
        };
    }

    private JsonObject parseObject() {
        JsonObject obj = new JsonObject();
        consume(JsonTokenType.LBRACE);
        if (peek().type != JsonTokenType.RBRACE) {
            do {
                String key = consume(JsonTokenType.STRING).value;
                consume(JsonTokenType.COLON);
                JsonValue value = parseValue();
                obj.members.put(key, value);
            } while (match(JsonTokenType.COMMA));
        }
        consume(JsonTokenType.RBRACE);
        return obj;
    }

    private JsonArray parseArray() {
        JsonArray array = new JsonArray();
        consume(JsonTokenType.LBRACKET);
        if (peek().type != JsonTokenType.RBRACKET) {
            do {
                array.elements.add(parseValue());
            } while (match(JsonTokenType.COMMA));
        }
        consume(JsonTokenType.RBRACKET);
        return array;
    }

    private JsonToken consume() {
        return tokens.get(pos++);
    }

    private JsonToken consume(JsonTokenType expected) {
        JsonToken token = tokens.get(pos);
        if (token.type != expected) {
            throw new RuntimeException("Expected " + expected + " but found " + token.type);
        }
        return consume();
    }

    private boolean match(JsonTokenType expected) {
        if (peek().type == expected) {
            consume();
            return true;
        }
        return false;
    }

    private JsonToken peek() {
        return tokens.get(pos);
    }
}

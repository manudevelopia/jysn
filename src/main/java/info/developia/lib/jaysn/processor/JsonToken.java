package info.developia.lib.jaysn.processor;

public class JsonToken {
    public final JsonTokenType type;
    public final String value;

    public JsonToken(JsonTokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return type + (value != null ? "(" + value + ")" : "");
    }
}

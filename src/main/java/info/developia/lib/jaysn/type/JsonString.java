package info.developia.lib.jaysn.type;

public class JsonString implements JsonValue {
    public final String value;

    public JsonString(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}


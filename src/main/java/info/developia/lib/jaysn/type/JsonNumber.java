package info.developia.lib.jaysn.type;

public class JsonNumber implements JsonValue {
    public final String value;
    public JsonNumber(String value) { this.value = value; }

    @Override
    public String toString() {
        return value;
    }
}



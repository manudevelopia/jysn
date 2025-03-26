package info.developia.lib.json;

public class JsonProperty extends JsonNode {
    public final String name;
    public final Object value;

    public JsonProperty(String name, Object value, JsonNode parent) {
        super(parent);
        this.name = name;
        this.value = value;
    }
}

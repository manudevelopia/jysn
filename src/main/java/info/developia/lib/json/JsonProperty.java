package info.developia.lib.json;

public class JsonProperty extends JsonNode {
    final String name;
    final Object value;

    public JsonProperty(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}

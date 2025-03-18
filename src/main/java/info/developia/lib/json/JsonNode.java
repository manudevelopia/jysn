package info.developia.lib.json;

public class JsonNode {
    public final JsonNode parent;

    public JsonNode(JsonNode parent) {
        this.parent = parent;
    }
}

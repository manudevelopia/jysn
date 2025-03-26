package info.developia.lib.json;

public abstract class JsonNode {
    public final JsonNode parent;

    public JsonNode(JsonNode parent) {
        this.parent = parent;
    }
}

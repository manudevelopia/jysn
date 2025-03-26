package info.developia.lib.json;

import java.util.ArrayList;
import java.util.List;

public class JsonObject extends JsonNode {
    private final List<JsonProperty> properties = new ArrayList<>();

    public JsonObject(JsonNode parent) {
        super(parent);
    }

    public void add(JsonProperty property) {
        properties.add(property);
    }

    public List<JsonProperty> getProperties() {
        return properties;
    }
}

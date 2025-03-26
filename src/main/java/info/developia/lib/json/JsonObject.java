package info.developia.lib.json;

import java.util.HashMap;
import java.util.Map;

public class JsonObject extends JsonNode {
    public final Map<String, JsonProperty> properties = new HashMap<>();

    public JsonObject(JsonNode parent) {
        super(parent);
    }

    public void add(JsonProperty property) {
        properties.put(property.name, property);
    }
}

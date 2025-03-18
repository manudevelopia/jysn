package info.developia.lib.json;

import java.util.ArrayList;
import java.util.List;

public class JsonObject extends JsonNode {
    final List<JsonProperty> properties = new ArrayList<>();
    public final JsonObject parent;

    public JsonObject(JsonObject parent) {
        this.parent = parent;
    }

    public void add(JsonProperty property) {
        properties.add(property);
    }
}

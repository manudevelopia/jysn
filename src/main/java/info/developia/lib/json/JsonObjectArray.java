package info.developia.lib.json;

import java.util.ArrayList;
import java.util.List;

public class JsonObjectArray extends JsonNode {
    final List<JsonObject> jsonObjects = new ArrayList<>();

    public JsonObjectArray(JsonNode parent) {
        super(parent);
    }

    public void add(JsonObject jsonObject) {
        jsonObjects.add(jsonObject);
    }
}

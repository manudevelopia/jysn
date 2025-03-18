package info.developia.lib.json;

import java.util.List;

public class JsonValueArray extends JsonNode {
    List<String> values;

    public JsonValueArray(JsonNode parent) {
        super(parent);
    }
}

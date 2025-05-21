package info.developia.lib.jaysn.type;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonObject implements JsonValue {
    public final Map<String, JsonValue> members = new LinkedHashMap<>();
}


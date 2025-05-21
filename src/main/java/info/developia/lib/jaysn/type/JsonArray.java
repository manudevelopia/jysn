package info.developia.lib.jaysn.type;

import java.util.ArrayList;
import java.util.List;

public class JsonArray implements JsonValue {
    public final List<JsonValue> elements = new ArrayList<>();
}

package info.developia.lib.alt;

import java.util.ArrayList;
import java.util.List;

public class JsonArray implements JsonValue {
    public final List<JsonValue> elements = new ArrayList<>();
}

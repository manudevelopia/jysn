package info.developia.lib;

import info.developia.lib.json.JsonObject;
import info.developia.lib.json.JsonProperty;

public class Jaysn {

    private static final String leftBrace = "{";
    private static final String rightBrace = "}";
    private static final String leftBracket = "[";
    private static final String rightBracket = "]";
    private static final String comma = ",";
    private static final String colon = ":";

    public static <T> T parse(String json, Class<T> clazz) {
        String[] tokens = Tokenizer.getTokens(json);
        var nodes = getNodes(tokens);
        return null;
    }

    private static JsonObject getNodes(String[] tokens) {
        JsonObject currentJsonObject = null;

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            switch (token) {
                case leftBrace:
                    currentJsonObject = new JsonObject(currentJsonObject);
                    continue;
                case rightBrace:
                    if (currentJsonObject.parent != null) {
                        currentJsonObject = currentJsonObject.parent;
                    }
                    continue;
                case comma:
                    continue;
                default:
                    var propertyNameValue = token.split(":");
                    var name = propertyNameValue[0];
                    if (propertyNameValue.length == 2) {
                        var value = propertyNameValue[1];
                        currentJsonObject.add(new JsonProperty(name, value));
                    } else if (propertyNameValue.length == 1) {
                        var value = new JsonObject(currentJsonObject);
                        currentJsonObject.add(new JsonProperty(name, value));
                        currentJsonObject = value;
                        i++;
                    }
            }
        }
        return currentJsonObject;
    }
}

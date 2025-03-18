package info.developia.lib;

import info.developia.lib.json.JsonNode;
import info.developia.lib.json.JsonObject;
import info.developia.lib.json.JsonObjectArray;
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

    private static JsonNode getNodes(String[] tokens) {
        JsonNode currentJsonObject = null;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            switch (token) {
                case leftBracket:
                    currentJsonObject = new JsonObjectArray(currentJsonObject);
                    continue;
                case leftBrace:
                    if (currentJsonObject instanceof JsonObjectArray) {
                        var object = new JsonObject(currentJsonObject);
                        ((JsonObjectArray) currentJsonObject).add(object);
                        currentJsonObject = object;
                    } else {
                        currentJsonObject = new JsonObject(currentJsonObject);
                    }
                    continue;
                case rightBracket, rightBrace:
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
                        ((JsonObject) currentJsonObject).add(new JsonProperty(name, value, currentJsonObject));
                    } else if (propertyNameValue.length == 1) {
                        JsonObject value;
                        if (leftBracket.equals(tokens[i + 1])) {
                            var array = new JsonObjectArray(currentJsonObject);
                            i++;
                            value = new JsonObject(array);
                            array.add(value);
                        } else {
                            value = new JsonObject(currentJsonObject);
                        }
                        ((JsonObject) currentJsonObject).add(new JsonProperty(name, value, currentJsonObject));
                        currentJsonObject = value;
                        i++;
                    }
            }
        }
        return currentJsonObject;
    }
}

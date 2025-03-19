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
                    var object = new JsonObject(currentJsonObject);
                    if (currentJsonObject instanceof JsonObjectArray) {
                        ((JsonObjectArray) currentJsonObject).add(object);
                    }
                    currentJsonObject = object;
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
                        if (leftBracket.equals(tokens[i + 1])) {
                            JsonNode value = new JsonObjectArray(currentJsonObject);
                            i++;
                            var jsonObjectArray = new JsonObject(value);
                            ((JsonObjectArray) value).add(jsonObjectArray);
                            ((JsonObject) currentJsonObject).add(new JsonProperty(name, value, currentJsonObject));
                            currentJsonObject = jsonObjectArray;
                        } else {
                            JsonNode value = new JsonObject(currentJsonObject);
                            ((JsonObject) currentJsonObject).add(new JsonProperty(name, value, currentJsonObject));
                            currentJsonObject = value;
                        }
                        i++;
                    }
            }
        }
        return currentJsonObject;
    }
}

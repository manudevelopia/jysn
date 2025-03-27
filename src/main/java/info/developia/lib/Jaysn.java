package info.developia.lib;

import info.developia.lib.json.JsonNode;
import info.developia.lib.json.JsonObjectArray;

import java.util.List;


public class Jaysn {

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            String[] tokens = Tokenizer.getTokens(json);
            var nodes = Parser.getNodes(tokens);
            return buildObject(clazz, nodes);
        } catch (Exception e) {
            throw new RuntimeException("Json cannot be parsed to %s %s".formatted(clazz.getName(), e.getMessage()));
        }
    }

    public static <T> List<T> parseList(String json, Class<T> clazz) {
        try {
            String[] tokens = Tokenizer.getTokens(json);
            return ((JsonObjectArray) Parser.getNodes(tokens)).jsonObjects.stream()
                    .map(node -> buildObject(clazz, node))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Json cannot be parsed to %s %s".formatted(clazz.getName(), e.getMessage()));
        }
    }

    private static <T> T buildObject(Class<T> clazz, JsonNode nodes) {
        if (clazz.isRecord()) {
            try {
                return RecordBuilder.build(clazz, nodes);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Object are no supported yet");
    }
}

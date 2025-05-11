package info.developia.lib.alt;


import java.util.List;

public class Jysn {

//    Jysn.from(json).to(UserDao).orElse(null).onFail()

    public static <T> T to(String json, Class<T> clazz) {
        if (clazz.isRecord()) {
            try {
                var nodes = parse(json);
                return buildObject(clazz, nodes);
            } catch (Exception e) {
                throw new RuntimeException("Json cannot be parsed to %s %s".formatted(clazz.getName(), e.getMessage()));
            }
        }
        throw new RuntimeException("Object are no supported yet");
    }

    private static JsonValue parse(String json) {
        JsonLexer lexer = new JsonLexer(json);
        List<JsonToken> tokens = lexer.tokenize();
        JsonParser parser = new JsonParser(tokens);
        return parser.parse();
    }


    private static <T> T buildObject(Class<T> clazz, JsonValue nodes) {
        try {
            return RecordBuilder.build(clazz, nodes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
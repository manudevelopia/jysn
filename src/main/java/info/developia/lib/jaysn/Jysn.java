package info.developia.lib.jaysn;

import info.developia.lib.jaysn.type.JsonValue;

import java.util.List;

public class Jysn {

//    Jysn.from(json).to(UserDao).orElse(null).onFail().parse()

    public static <T> T to(String json, Class<T> clazz) {
//        if (clazz.isRecord()) {
//            try {
//                var nodes = parse(json);
//                return buildObject(clazz, nodes);
//            } catch (Exception e) {
//                throw new RuntimeException("Json cannot be parsed to %s %s".formatted(clazz.getName(), e.getMessage()));
//            }
//        }
        throw new RuntimeException("Object are no supported yet");
    }

    public static JsonValue parse(String json) {
        JsonLexer lexer = new JsonLexer(json);
        List<JsonToken> tokens = lexer.tokenize();
        JsonParser parser = new JsonParser(tokens);
        return parser.parse();
    }


    public static Record buildObject(Class<? extends Record> clazz, JsonValue nodes) {
        try {
            return RecordBuilder.build(clazz, nodes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
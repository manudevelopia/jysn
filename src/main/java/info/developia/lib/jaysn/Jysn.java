package info.developia.lib.jaysn;

import info.developia.lib.jaysn.processor.JsonLexer;
import info.developia.lib.jaysn.processor.JsonParser;
import info.developia.lib.jaysn.processor.RecordBuilder;
import info.developia.lib.jaysn.type.JsonArray;
import info.developia.lib.jaysn.type.JsonObject;
import info.developia.lib.jaysn.type.JsonValue;

import java.util.function.Function;

public class Jysn {

    private final String json;
    private Runnable failAction;
    private Class<? extends Record> record;
    private Object fallback;
    private RuntimeException throwable;

    public Jysn(String json) {
        this.json = json;
    }

    public static Jysn from(String json) {
        return new Jysn(json);
    }

    public <T> T to(Class<? extends Record> record) {
        return parse(record, (nodes) -> {
            if (nodes instanceof JsonObject jsonObject) {
                return (T) RecordBuilder.build(record, jsonObject);
            }
            throw new RuntimeException("Expected a JSON object for record");
        });
    }

    public <T> T toListOf(Class<? extends Record> record) {
        return parse(record, (nodes) -> {
            if (nodes instanceof JsonArray jsonArray) {
                return (T) jsonArray.elements.stream()
                        .map(element -> RecordBuilder.build(record, element))
                        .toList();
            }
            throw new RuntimeException("Expected a JSON array for list of records");
        });
    }

    public Jysn orElse(Object fallback) {
        this.fallback = fallback;
        return this;
    }

    public Jysn onFail(Runnable failAction) {
        this.failAction = failAction;
        return this;
    }

    public Jysn failWith(RuntimeException throwable) {
        this.throwable = throwable;
        return this;
    }

    public <T> T parse(Class<? extends Record> record, Function<JsonValue, T> function) {
        try {
            var nodes = parse(json);
            return function.apply(nodes);
//            return (T) RecordBuilder.build(record, nodes);
        } catch (Exception e) {
            if (failAction != null) failAction.run();
            if (throwable != null) throw throwable;
            if (fallback == null)
                throw new RuntimeException("Json cannot be parsed to %s %s".formatted(record.getName(), e.getMessage()));
        }
        return (T) fallback;
    }

    private JsonValue parse(String json) {
        var lexer = new JsonLexer(json);
        var tokens = lexer.tokenize();
        var parser = new JsonParser(tokens);
        return parser.parse();
    }
}

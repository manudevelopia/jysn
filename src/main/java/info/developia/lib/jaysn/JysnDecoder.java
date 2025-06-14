package info.developia.lib.jaysn;

import info.developia.lib.jaysn.builder.RecordBuilder;
import info.developia.lib.jaysn.processor.JsonLexer;
import info.developia.lib.jaysn.processor.JsonParser;
import info.developia.lib.jaysn.type.JsonArray;
import info.developia.lib.jaysn.type.JsonObject;
import info.developia.lib.jaysn.type.JsonValue;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class JysnDecoder {

    private final String json;
    private Runnable failAction;
    private Object fallback;
    private RuntimeException throwable;

    JysnDecoder(String json) {
        this.json = json;
    }

    public static JysnEncoder from(Record object) {
        return new JysnEncoder(object);
    }

    public static JysnEncoder from(List<Record> object) {
        return new JysnEncoder(object);
    }

    public static JysnDecoder from(String json) {
        return new JysnDecoder(json);
    }

    @SuppressWarnings("unchecked")
    public <T extends Record> T to(Class<T> record) {
        return (T) parse(record, (nodes) -> {
            if (nodes instanceof JsonObject jsonObject) {
                return RecordBuilder.build(record, jsonObject);
            }
            throw new RuntimeException("Expected a JSON object for record");
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends Record> Stream<T> toStream(Class<T> record) {
        return (Stream<T>) parse(record, (nodes) -> {
            if (nodes instanceof JsonArray jsonArray) {
                return jsonArray.elements.stream()
                        .map(element -> RecordBuilder.build(record, element));
            }
            throw new RuntimeException("Expected a JSON array for list of records");
        });
    }

    public <T extends Record> List<T> toListOf(Class<T> record) {
        return toStream(record).toList();
    }

    public JysnDecoder orElse(Object fallback) {
        this.fallback = fallback;
        return this;
    }

    public JysnDecoder onFail(Runnable failAction) {
        this.failAction = failAction;
        return this;
    }

    public JysnDecoder failWith(RuntimeException throwable) {
        this.throwable = throwable;
        return this;
    }

    public <T> Object parse(Class<? extends Record> record, Function<JsonValue, T> function) {
        try {
            var nodes = parse(json);
            return function.apply(nodes);
        } catch (Exception e) {
            if (failAction != null) failAction.run();
            if (throwable != null) throw throwable;
            if (fallback == null)
                throw new RuntimeException("Json cannot be parsed to %s %s".formatted(record.getName(), e.getMessage()));
        }
        return fallback;
    }

    private JsonValue parse(String json) {
        var lexer = new JsonLexer(json);
        var tokens = lexer.tokenize();
        var parser = new JsonParser(tokens);
        return parser.parse();
    }
}

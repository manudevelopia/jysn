package info.developia.lib.jaysn;

import info.developia.lib.jaysn.processor.JsonLexer;
import info.developia.lib.jaysn.processor.JsonParser;
import info.developia.lib.jaysn.processor.RecordBuilder;
import info.developia.lib.jaysn.type.JsonArray;
import info.developia.lib.jaysn.type.JsonValue;

import java.util.List;

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

    public Jysn to(Class<? extends Record> record) {
        this.record = record;
        return this;
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

    public <T extends Record> List<T> parseList() {
        var nodes = parse(json);
        if (!(nodes instanceof JsonArray)) {
            throw new RuntimeException("Json cannot be parsed to a list of %s".formatted(record.getName()));
        }
        return (List<T>) RecordBuilder.build(record, nodes);
    }

    public Object parse() {
        try {
            var nodes = parse(json);
            return RecordBuilder.build(record, nodes);
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

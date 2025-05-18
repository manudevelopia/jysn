package info.developia.lib.inter;

import info.developia.lib.alt.Jysn;

import static info.developia.lib.alt.Jysn.buildObject;

public class Jysnn {

    private final String json;
    private Runnable failAction;
    private Class<? extends Record> record;
    private Record fallback;
    private RuntimeException throwable;

    public Jysnn(String json) {
        this.json = json;
    }

    public static Jysnn from(String json) {
        return new Jysnn(json);
    }

    public Jysnn to(Class<? extends Record> record) {
        this.record = record;
        return this;
    }

    public Jysnn orElse(Record fallback) {
        this.fallback = fallback;
        return this;
    }

    public Jysnn onFail(Runnable failAction) {
        this.failAction = failAction;
        return this;
    }

    public Jysnn failWith(RuntimeException throwable) {
        this.throwable = throwable;
        return this;
    }

    public Record parse() {
        try {
            var nodes = Jysn.parse(json);
            return buildObject(record, nodes);
        } catch (Exception e) {
            if (failAction != null) {
                failAction.run();
            }
            if (throwable != null) {
                throw throwable;
            }
            if (fallback == null) {
                throw new RuntimeException("Json cannot be parsed to %s %s".formatted(record.getName(), e.getMessage()));
            }
        }
        return fallback;
    }
}

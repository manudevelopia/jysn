package info.developia.lib.jaysn;

import java.util.List;

public class Jysn {

    private Jysn() {
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
}

package info.developia.lib;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class Jaysn {
    private static final String TEXT = "Hello, world inside java!";

    @Benchmark
    public int benchmarkStringLength() {
        return TEXT.length();
    }

    @Benchmark
    public String benchmarkToUpperCase() {
        return TEXT.toUpperCase();
    }
}

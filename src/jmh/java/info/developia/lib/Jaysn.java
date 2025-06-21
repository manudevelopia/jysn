package info.developia.lib;

import info.developia.lib.jaysn.Jysn;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.List;
import java.util.concurrent.TimeUnit;

//@State(Scope.Benchmark)
@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Jaysn {
    private final String json = """
            {
              "name": "John",
              "age": 30,
              "profile": {
                 "status": "active",
                           "roles": [ "admin", "user" ],
                 "ids": [ 1, 2, 3 ]
                }
            }""";
    private final User user = new User(
            "John",
            30,
            new Profile(
                    "active",
                    List.of("admin", "user"),
                    List.of(1, 2, 3)));

    @Benchmark
    public void benchmarkToObject() {
        Jysn.from(json).to(User.class);
    }

    @Benchmark
    public void benchmarkToJson() {
        Jysn.from(user).toJson();
    }
}

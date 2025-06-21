package info.developia.lib;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class Jackson {
    private final static ObjectMapper objectMapper = new ObjectMapper();

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
    public void benchmarkToObject() throws JsonProcessingException {
        objectMapper.readValue(json, User.class);
    }

    @Benchmark
    public void benchmarkToJson() throws JsonProcessingException {
        objectMapper.writeValueAsString(user);
    }
}

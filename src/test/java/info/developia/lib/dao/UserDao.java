package info.developia.lib.dao;

import info.developia.lib.annotation.JsonProperty;

public record UserDao(
        @JsonProperty String name,
        @JsonProperty int age
) {
}

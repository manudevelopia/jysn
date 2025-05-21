package info.developia.lib.dao;

public record UserDao(
        String name
        , int age
        , ProfileDao profile
) {
}

package info.developia.lib.model;

import info.developia.lib.jaysn.annotation.JsonProperty;

public class User {
    public String email;
    @JsonProperty()
    public String name;
    @JsonProperty(name = "user_age")
    public int age;
    @JsonProperty()
    public Profile profile;

    public User(String email, String name, int age, Profile profile) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.profile = profile;
    }
}

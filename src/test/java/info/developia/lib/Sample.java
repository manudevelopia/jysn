package info.developia.lib;

import info.developia.lib.jaysn.Jysn;

import java.util.List;

public class Sample {

    public static void main(String[] args) {
        String json = """
                {
                    "name": "John",
                            "age": 30,
                            "profile": {
                        "status": "active",
                                "roles": [ "admin", "user" ],
                        "ids": [ 1, 2, 3 ]
                    }
                }""";
        User user = Jysn.from(json).to(User.class);
        List<User> users = Jysn.from(json).toListOf(User.class);
        System.out.println("Name: " + user.name());
        System.out.println("Age: " + user.age());
        System.out.println("Status: " + user.profile().status());
        System.out.println("Roles: " + String.join(", ", user.profile().roles()));
        System.out.println("IDs: " + user.profile().ids());
    }
}

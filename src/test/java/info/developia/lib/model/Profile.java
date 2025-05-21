package info.developia.lib.model;

import info.developia.lib.jaysn.annotation.JsonProperty;

public class Profile {
    public String id;
    @JsonProperty()
    public String permissions;
    @JsonProperty(name = "system_profile")
    public int system;
}

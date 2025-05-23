package info.developia.lib;

import java.util.List;

public record Profile(
        String status
        , List<String> roles
        , List<Integer> ids
) {
}

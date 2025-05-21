package info.developia.lib.dao;

import java.util.List;

public record ProfileDao(
        String status
        , List<String> roles
        , List<Integer> ids
) {
}

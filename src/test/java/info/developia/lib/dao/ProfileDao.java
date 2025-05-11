package info.developia.lib.dao;

import java.util.List;

public record ProfileDao(
        List<String> roles,
        String status
) {
}

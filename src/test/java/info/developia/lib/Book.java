package info.developia.lib;

import java.util.List;

public record Book(
        String title
        , List<Autor> authors
) {
}

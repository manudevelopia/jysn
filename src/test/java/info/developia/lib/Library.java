package info.developia.lib;

import java.util.List;

public record Library(
        List<Book> books
        , Double averageRating
) {
}

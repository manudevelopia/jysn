package info.developia.lib

import info.developia.lib.jaysn.Jysn
import spock.lang.Specification

class JysnEncoderSpec extends Specification {
    def "should encode object to JSON string"() {
        given:
        def user = new User("Alice", 25, new Profile("active", ["user"], [1, 2, 3]))

        when:
        String json = Jysn.from(user).toJson()

        then:
        json == '''{"name":"Alice", "age":25, "profile":{"status":"active", "roles":["user"], "ids":[1,2,3]}}'''
    }

    def "should encode list of objects to JSON string"() {
        given:
        def books = [
                new Book("The Hobbit", [new Autor("Tolkien")]),
                new Book("1984", [new Autor("Orwell")])
        ]

        when:
        String json = Jysn.toJson(books)

        then:
        json == '''[{"title":"The Hobbit","authors":[{"name":"Tolkien"}]},{"title":"1984","authors":[{"name":"Orwell"}]}]'''
    }
}

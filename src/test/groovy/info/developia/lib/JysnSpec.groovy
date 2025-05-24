package info.developia.lib

import info.developia.lib.jaysn.Jysn
import spock.lang.Specification

class JysnSpec extends Specification {

    def "should deserialize to object with nested fields"() {
        given:
        String json = '''{
            "name": "John",
            "age": 30,
            "profile": {
                "status": "active",
                "roles": [ "admin", "user" ],
                "ids": [ 1, 2, 3 ]
                }
            }'''
        when:
        def result = Jysn.from(json).to(User).parse()
        then:
        with(result) {
            name == 'John'
            age == 30
            profile.status == 'active'
            profile.roles.size() == 2
            profile.roles[0] == 'admin'
            profile.roles[1] == 'user'
            profile.ids.size() == 3
            profile.ids[0] == 1
            profile.ids[1] == 2
            profile.ids[2] == 3
        }
    }

    def "should deserialize to list of object book"() {
        given:
        String json = '''[ 
                { "title": "Lord of Rings", "authors": [{ "name": "Tolkien" }]}, 
                { "title": "1984", "authors": [{ "name": "Orwell" }]}
            ]]'''
        when:
        def result = Jysn.from(json).to(Book).parse()
        then:
        result.size() == 2
        result[0].title == 'Lord of Rings'
        result[1].title == '1984'
    }

    def "should deserialize to object that has a field of object list"() {
        given:
        String json = '''{
            "books" : [ 
                { "title": "Lord of Rings", "authors": [{ "name": "Tolkien" }]}, 
                { "title": "1984", "authors": [{ "name": "Orwell" }]}
            ],
            "averageRating": 4.5}'''
        when:
        def result = Jysn.from(json).to(Library).parse()
        then:
        result.books.size() == 2
        result.books[0].title == 'Lord of Rings'
        result.books[0].authors.size() == 1
        result.books[0].authors[0].name == 'Tolkien'
        result.books[1].title == '1984'
        result.books[1].authors.size() == 1
        result.books[1].authors[0].name == 'Orwell'
        result.averageRating == 4.5
    }
}
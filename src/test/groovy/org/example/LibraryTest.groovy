package org.example

import spock.lang.Specification

class LibraryTest extends Specification {
    def "someLibraryMethod returns true"() {
        given:
        String json = '''{
            "name": "John",
            "age": 30
        }'''
        when:
        def result = Library.parse(json, User.class)
        then:
        with(result) {
            name == 'John'
            age == 30
        }
    }
}

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
        def result = Jysn.from(json).to(User).parse() as User
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
                { "title": "Lord of Rings" }, 
                { "title": "1984" } 
            ]'''
        when:
        def result = Jysn.from(json).to(Book).parse() as Book
        then:
        result.size() == 2
        result[0].title == 'Lord of Rings'
        result[1].title == '1984'
    }

    def "should deserialize to object that has a field of object list"() {
        given:
        String json = '''{
            "books" : [ 
                { "title": "Lord of Rings 1" }, 
                { "title": "Lord of Rings 2" } 
            ]}'''
        when:
        def result = Jysn.from(json).to(Libray).parse()
        then:
        result.books.size() == 2
        result.books[0].title == 'Lord of Rings 1'
        result.books[1].title == 'Lord of Rings 2'
    }

//    def "should read json properties"() {
//        given:
//        String json = '''{
//         "property1" : "value1",
//         "property2" : "value2",
//         "property3" : "value3",
//         "number1" : 5.0,
//         "number2" : 15,
//         "boolean1" : true,
//         "boolean2" : false,
//         "nullable" : null,
//         "object1" : {
//             "property1" : "value1",
//             "property2" : "value2",
//             "property3object1" : {
//                "property1" : "value1",
//                "property2" : "value2"
//             }
//         }
//        }'''
//        when:
//        def result = Jaysn.readProperties(json)
//        then:
//        readProperties.property1 == 'value1'
//    }
//
//    def "should read json propertiess"() {
//        given:
//        String json = '''{
//           "property1":"value1",
//           "number1":5.0,
//           "number2":15,
//           "number3":-15,
//           "boolean1":true,
//           "nullable":null,
//           "object":{
//                "obj-prop1":"value1",
//                "obj-prop2":"value2",
//                "obj-obj1":{
//                    "obj-prop1":"value1",
//                    "obj-prop2":"value2"
//                }
//           },
//           "boolean2":false,
//           "boolean3":false,
//           "array":[
//               {
//                    "array-prop1":"value1",
//                    "array-prop2":"value2"
//                }
//           ]
//        }'''
//        when:
//        Jaysn.parse(json, User)
//        then:
//        noExceptionThrown()
//    }
//
//    def "should read json propertiess"() {
//        given:
//        String json = '''{
//           "property1":"value1",
//           "number1":5.0,
//           "number2":15,
//           "number3":-15,
//           "boolean1":true,
//           "nullable":null
//        }'''
//        when:
//        Jaysn.parse(json, User)
//        then:
//        noExceptionThrown()
//    }
//
//    def "should read nested object properties"() {
//        given:
//        String json = '''{
//           "property1":"value1",
//           "object":{
//                "obj-prop1":"value1",
//                "obj-obj1":{
//                    "obj1-prop1":"value1",
//                    "obj1-prop2":"value2"
//                },
//                "obj-prop2":"value2"
//           }
//           "property2":"value2",
//        }'''
//        when:
//        Jaysn.parse(json, User)
//        then:
//        noExceptionThrown()
//    }
//
//    def "should read objects array"() {
//        given:
//        String json = '''[
//           {
//               "object1-property1":"value1",
//               "object1-property2":"value2",
//           },
//           {
//               "object2-property1":"value1",
//               "object2-property2":"value2",
//           },
//        ]'''
//        when:
//        def jsonNode = Jaysn.parse(json, User)
//        then:
//        noExceptionThrown()
//    }
//
//    def "should read objects nested array"() {
//        given:
//        String json = '''[
//           {
//               "object1-property1":"value1",
//               "object1-property2":"value2",
//           },
//           {
//               "object2-array1": [
//                     {
//                          "array1-object1-property1":"value1",
//                          "array1-object1-property2":"value2",
//                     },
//                     {
//                          "array1-object2-property1":"value1",
//                          "array1-object2-property2":"value2",
//                     }
//               ],
//               "object2-property2":"value2",
//           }
//        ]'''
//        when:
//        def jsonNode = Jaysn.parse(json, User)
//        then:
//        noExceptionThrown()
//    }
}

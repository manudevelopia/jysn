package info.developia.lib


import org.example.User
import spock.lang.Specification

class JaysnTest extends Specification {
    def "should deserialize to object"() {
        given:
        String json = '''{
            "name": "John",
            "age": 30
        }'''
        when:
        def result = Jaysn.parse(json, User.class)
        then:
        with(result) {
            name == 'John'
            age == 30
        }
    }

    def "should deserialize to list of object"() {
        given:
        String json = '''[{
            "name": "John",
            "age": 30
        }]'''
        when:
        def result = Jaysn.parse(json, User.class)
        then:
        with(result) {
            name == 'John'
            age == 30
        }
    }


    def "should read json properties"() {
        given:
        String json = '''{
         "property1" : "value1",
         "property2" : "value2",
         "property3" : "value3",
         "number1" : 5.0,
         "number2" : 15,
         "boolean1" : true,
         "boolean2" : false,
         "nullable" : null,
         "object1" : {
             "property1" : "value1",
             "property2" : "value2",
             "property3object1" : {
                "property1" : "value1",
                "property2" : "value2"
             }
         }
        }'''
        when:
        def result = Jaysn.readProperties(json)
        then:
        readProperties.property1 == 'value1'
    }
    def "should read json propertiess"() {
        given:
        String json = '''{
           "property1":"value1",
           "number1":5.0,
           "number2":15,
           "number3":-15,
           "boolean1":true,
           "nullable":null,
           "object":{
                "obj-prop1":"value1",
                "obj-prop2":"value2",
                "obj-obj1":{
                    "obj-prop1":"value1",
                    "obj-prop2":"value2"
                }
           },
           "boolean2":false,
           "boolean3":false,
        }'''
        when:
        def result = Jaysn.readProperties(json)
        then:
        readProperties.property1 == 'value1'
    }
}

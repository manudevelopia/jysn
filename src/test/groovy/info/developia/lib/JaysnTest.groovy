package info.developia.lib


import spock.lang.Specification

class JaysnTest extends Specification {
    def "should deserialize to object"() {
        given:
        String json = '''{
            "name": "John",
            "age": 30
        }'''
        when:
        def result = Jaysn.parse(json, info.developia.lib.model.User.class)
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
        def result = Jaysn.parse(json, info.developia.lib.model.User.class)
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
           "array":[
               {
                    "array-prop1":"value1",
                    "array-prop2":"value2"
                }
           ]
        }'''
        when:
        Jaysn.parse(json, info.developia.lib.model.User.class)
        then:
        noExceptionThrown()
    }

    def "should read json propertiess"() {
        given:
        String json = '''{
           "property1":"value1",
           "number1":5.0,
           "number2":15,
           "number3":-15,
           "boolean1":true,
           "nullable":null
        }'''
        when:
        Jaysn.parse(json, info.developia.lib.model.User.class)
        then:
        noExceptionThrown()
    }

    def "should read nested object properties"() {
        given:
        String json = '''{
           "property1":"value1",
           "object":{
                "obj-prop1":"value1",
                "obj-obj1":{
                    "obj1-prop1":"value1",
                    "obj1-prop2":"value2"
                },
                "obj-prop2":"value2"
           }
           "property2":"value2",
        }'''
        when:
        Jaysn.parse(json, info.developia.lib.model.User.class)
        then:
        noExceptionThrown()
    }

    def "should read objects array"() {
        given:
        String json = '''[
           {
               "object1-property1":"value1",
               "object1-property2":"value2",
           },
           {
               "object2-property1":"value1",
               "object2-property2":"value2",
           },
        ]'''
        when:
        def jsonNode = Jaysn.parse(json, info.developia.lib.model.User.class)
        then:
        noExceptionThrown()
    }

    def "should read objects nested array"() {
        given:
        String json = '''[
           {
               "object1-property1":"value1",
               "object1-property2":"value2",
           },
           {
               "object2-array1": [
                     {
                          "array1-object1-property1":"value1",
                          "array1-object1-property2":"value2",
                     },
                     {
                          "array1-object2-property1":"value1",
                          "array1-object2-property2":"value2",
                     }
               ],
               "object2-property2":"value2",
           }
        ]'''
        when:
        def jsonNode = Jaysn.parse(json, info.developia.lib.model.User.class)
        then:
        noExceptionThrown()
    }
}

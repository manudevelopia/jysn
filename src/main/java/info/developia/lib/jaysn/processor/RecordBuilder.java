package info.developia.lib.jaysn.processor;

import info.developia.lib.jaysn.type.JsonArray;
import info.developia.lib.jaysn.type.JsonNumber;
import info.developia.lib.jaysn.type.JsonObject;
import info.developia.lib.jaysn.type.JsonString;
import info.developia.lib.jaysn.type.JsonValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.List;

public class RecordBuilder {

    public static Record build(Class<?> clazz, JsonValue nodes) {
        RecordComponent[] components = clazz.getRecordComponents();
        Class<?>[] paramTypes = new Class<?>[components.length];
        Object[] args = new Object[components.length];
        for (int i = 0; i < components.length; i++) {
            RecordComponent comp = components[i];
            paramTypes[i] = comp.getType();
            args[i] = isUserDefinedClass(comp.getType()) ?
                    build(comp.getType(), ((JsonObject) nodes).members.get(comp.getName())) :
                    cast(comp, ((JsonObject) nodes).members.get(comp.getName()));
        }
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(paramTypes);
            return (Record) constructor.newInstance(args);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isUserDefinedClass(Class<?> clazz) {
        return clazz.getClassLoader() != null &&
                !clazz.getName().startsWith("java.") &&
                !clazz.getName().startsWith("javax.");
    }

    public static Object cast(RecordComponent comp, Object value) {
        return switch (comp.getType().getName()) {
            case "java.lang.String" -> value.toString();
            case "int" -> Integer.parseInt(value.toString());
            case "java.lang.Integer" -> Integer.valueOf(value.toString());
            case "long" -> Long.parseLong(value.toString());
            case "java.lang.Long" -> Long.valueOf(value.toString());
            case "java.util.List" -> buildList(comp, (JsonArray) value);
            default -> new RuntimeException("Unsupported type " + comp.getType().getName());
        };
    }

    private static List<?> buildList(RecordComponent comp, JsonArray values) {
        var type = comp.getGenericType().getTypeName();
        return values.elements.stream().map(item -> switch (item) {
            case JsonString string -> string.value;
            case JsonNumber number -> buildNumber(type, number);
            default -> throw new IllegalStateException("Unexpected value: " + item);
        }).toList();

//        return isUserDefinedClass(values.elements.getFirst().value.getClass())
//                ? List.of() //TODO: build(type, values)
//                : values.elements.stream().map(Object::toString).toList();
    }

    private static Object buildNumber(String type, JsonNumber number) {
        return switch (type) {
            case "java.util.List<java.lang.Integer>" -> Integer.valueOf(number.value);
            case "java.util.List<java.lang.Long>" -> Long.valueOf(number.value);
            case "java.util.List<java.lang.Double>" -> Double.valueOf(number.value);
            case "java.util.List<java.lang.Float>" -> Float.valueOf(number.value);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}

package info.developia.lib.jaysn.builder;

import info.developia.lib.jaysn.type.JsonArray;
import info.developia.lib.jaysn.type.JsonNumber;
import info.developia.lib.jaysn.type.JsonObject;
import info.developia.lib.jaysn.type.JsonString;
import info.developia.lib.jaysn.type.JsonValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.List;

public class RecordBuilder {

    public static Object build(Class<?> clazz, JsonValue nodes) {
        return switch (nodes) {
            case JsonObject object -> getInstance(clazz, object);
            case JsonArray array -> array.elements.stream().map(object -> build(clazz, object)).toList();
            default -> throw new IllegalStateException("Unexpected value: " + nodes);
        };
    }

    private static Object getInstance(Class<?> clazz, JsonObject nodes) {
        RecordComponent[] components = clazz.getRecordComponents();
        Class<?>[] paramTypes = new Class<?>[components.length];
        Object[] args = new Object[components.length];
        for (int i = 0; i < components.length; i++) {
            RecordComponent comp = components[i];
            paramTypes[i] = comp.getType();
            args[i] = isUserDefinedClass(comp.getType()) ?
                    build(comp.getType(), nodes.members.get(comp.getName())) :
                    cast(comp, nodes.members.get(comp.getName()));
        }
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(paramTypes);
            return constructor.newInstance(args);
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
            case "double" -> Double.parseDouble(value.toString());
            case "java.lang.Double" -> Double.valueOf(value.toString());
            case "java.util.List" -> buildList(comp, (JsonArray) value);
            default -> new RuntimeException("Unsupported type " + comp.getType().getName());
        };
    }

    private static List<?> buildList(RecordComponent comp, JsonArray values) {
        return values.elements.stream().map(item -> switch (item) {
            case JsonString string -> string.value;
            case JsonNumber number -> buildNumber(readGenericType(comp), number);
            case JsonObject object -> getInstance(readGenericType(comp), object);
            default -> throw new IllegalStateException("Unexpected value: " + item);
        }).toList();
    }

    static Class<?> readGenericType(RecordComponent comp) {
        var type = comp.getGenericType();
//        if (type instanceof ParameterizedType pt) {
//            Type[] typeArgs = pt.getActualTypeArguments();
//            for (Type arg : typeArgs) {
//                if (arg instanceof Class<?> clazz && clazz.isRecord()) {
//                    return getRecord(clazz, object);
//                }
//            }
//        }
        if (type instanceof ParameterizedType pt && pt.getActualTypeArguments().length == 1) {
            if (pt.getActualTypeArguments()[0] instanceof Class<?> clazz) {
                return clazz;
            }
        }
        throw new IllegalStateException("Unexpected type: " + type);
    }

    private static Object buildNumber(Type type, JsonNumber number) {
        return switch (type.getTypeName()) {
            case "java.lang.Integer" -> Integer.valueOf(number.value);
            case "java.lang.Long" -> Long.valueOf(number.value);
            case "java.lang.Double" -> Double.valueOf(number.value);
            case "java.lang.Float" -> Float.valueOf(number.value);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}

package info.developia.lib.jaysn.builder;

import info.developia.lib.jaysn.type.JsonArray;
import info.developia.lib.jaysn.type.JsonNumber;
import info.developia.lib.jaysn.type.JsonObject;
import info.developia.lib.jaysn.type.JsonString;
import info.developia.lib.jaysn.type.JsonValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecordBuilder {

    public static Object build(Class<?> clazz, JsonValue nodes) {
        return switch (nodes) {
            case JsonObject object -> getInstance(clazz, object);
            case JsonArray array -> array.elements.stream().map(object -> build(clazz, object)).toList();
            default -> throw new IllegalStateException("Unexpected value: " + nodes);
        };
    }

    private static Object getInstance(Class<?> clazz, JsonObject nodes) {
        var components = clazz.getRecordComponents();
        var paramTypes = new Class<?>[components.length];
        var args = new Object[components.length];
        for (int i = 0; i < components.length; i++) {
            var comp = components[i];
            paramTypes[i] = comp.getType();
            args[i] = isUserDefinedClass(comp.getType()) ?
                    build(comp.getType(), nodes.members.get(comp.getName())) :
                    cast(comp, nodes.members.get(comp.getName()));
        }
        try {
            var constructor = clazz.getDeclaredConstructor(paramTypes);
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
            case "char", "java.lang.Character" -> value.toString().charAt(0);
            case "int" -> Integer.parseInt(value.toString());
            case "java.lang.Integer" -> Integer.valueOf(value.toString());
            case "long" -> Long.parseLong(value.toString());
            case "java.lang.Long" -> Long.valueOf(value.toString());
            case "double" -> Double.parseDouble(value.toString());
            case "java.lang.Double" -> Double.valueOf(value.toString());
            case "float" -> Float.parseFloat(value.toString());
            case "java.lang.Float" -> Float.valueOf(value.toString());
            case "boolean" -> Boolean.parseBoolean(value.toString());
            case "java.lang.Boolean" -> Boolean.valueOf(value.toString());
            case "short" -> Short.parseShort(value.toString());
            case "java.lang.Short" -> Short.valueOf(value.toString());
            case "byte" -> Byte.parseByte(value.toString());
            case "java.lang.Byte" -> Byte.valueOf(value.toString());
            case "java.util.List" -> buildStream(comp, (JsonArray) value).toList();
            case "java.util.Set" -> buildStream(comp, (JsonArray) value).collect(Collectors.toUnmodifiableSet());
            default -> throw new RuntimeException("Unsupported type " + comp.getType().getName());
        };
    }

    private static Stream<?> buildStream(RecordComponent comp, JsonArray values) {
        return values.elements.stream().map(item -> switch (item) {
            case JsonString string -> string.value;
            case JsonNumber number -> buildNumber(readGenericType(comp), number);
            case JsonObject object -> getInstance(readGenericType(comp), object);
            default -> throw new IllegalStateException("Unexpected value: " + item);
        });
    }

    static Class<?> readGenericType(RecordComponent comp) {
        var type = comp.getGenericType();
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

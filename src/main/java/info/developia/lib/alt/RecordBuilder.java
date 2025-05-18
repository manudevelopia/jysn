package info.developia.lib.alt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;

public class RecordBuilder {

    public static Record build(Class<?> clazz, JsonValue nodes)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        RecordComponent[] components = clazz.getRecordComponents();
        Class<?>[] paramTypes = new Class<?>[components.length];
        Object[] args = new Object[components.length];
        for (int i = 0; i < components.length; i++) {
            RecordComponent comp = components[i];
            paramTypes[i] = comp.getType();
            args[i] = isUserDefinedClass(comp.getType()) ?
                    build(comp.getType(), ((JsonObject) nodes).members.get(comp.getName())) :
                    cast(comp.getType(), ((JsonObject) nodes).members.get(comp.getName()));
        }
        Constructor<?> constructor = clazz.getDeclaredConstructor(paramTypes);
        return (Record) constructor.newInstance(args);
    }

    private static boolean isUserDefinedClass(Class<?> clazz) {
        return clazz.getClassLoader() != null &&
                !clazz.getName().startsWith("java.") &&
                !clazz.getName().startsWith("javax.");
    }

    public static Object cast(Class<?> type, Object value) {
        return switch (type.getName()) {
            case "java.lang.String" -> value.toString();
            case "int" -> Integer.parseInt(value.toString());
            case "java.lang.Integer" -> Integer.valueOf(value.toString());
            case "long" -> Long.parseLong(value.toString());
            case "java.lang.Long" -> Long.valueOf(value.toString());
            default -> new RuntimeException("Unsupported type " + type.getName());
        };
    }
}

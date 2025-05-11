package info.developia.lib.alt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;

public class RecordBuilder {

    public static <T> T build(Class<T> clazz, JsonValue nodes)
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
        Constructor<T> constructor = clazz.getDeclaredConstructor(paramTypes);
        return constructor.newInstance(args);
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
            default -> new RuntimeException("Unsupported type " + type.getName());
        };
    }
}

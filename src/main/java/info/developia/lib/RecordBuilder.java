package info.developia.lib;

import info.developia.lib.json.JsonNode;
import info.developia.lib.json.JsonObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;

public class RecordBuilder {

    public static <T> T build(Class<T> clazz, JsonNode nodes)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        RecordComponent[] components = clazz.getRecordComponents();
        Class<?>[] paramTypes = new Class<?>[components.length];
        Object[] args = new Object[components.length];
        for (int i = 0; i < components.length; i++) {
            RecordComponent comp = components[i];
            paramTypes[i] = comp.getType();
            args[i] = isUserDefinedClass(comp.getType()) ?
                    build(comp.getType(), (JsonNode) ((JsonObject) nodes).properties.get(comp.getName()).value) :
                    Value.cast(comp.getType(), ((JsonObject) nodes).properties.get(comp.getName()).value);
        }
        Constructor<T> constructor = clazz.getDeclaredConstructor(paramTypes);
        return constructor.newInstance(args);
    }

    private static boolean isUserDefinedClass(Class<?> clazz) {
        return clazz.getClassLoader() != null &&
                !clazz.getName().startsWith("java.") &&
                !clazz.getName().startsWith("javax.");
    }
}

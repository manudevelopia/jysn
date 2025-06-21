package info.developia.lib.jaysn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class JysnEncoder {
    private final Object object;

    protected JysnEncoder(Object object) {
        this.object = object;
    }

    public String toJson() {
        return object instanceof List<?> collection ?
                collection.stream().map(this::readObject).collect(Collectors.joining(",", "[", "]")) :
                readObject(object);
    }

    private String readObject(Object object) {
        var jsonObject = new StringJoiner(",", "{", "}");
        for (var component : object.getClass().getRecordComponents()) {
            var property = new StringBuilder();
            property.append(getPropertyName(component));
            property.append(":");
            try {
                property.append(!isUserDefinedClass(component.getType()) ?
                        getPropertyValue(component, object) :
                        readObject(component.getAccessor().invoke(object)));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            jsonObject.add(property);
        }
        return jsonObject.toString();
    }

    private static String getPropertyName(RecordComponent component) {
        return formatToString(component.getName());
    }

    private static String formatToString(Object text) {
        return String.format("\"%s\"", text);
    }

    private String getPropertyValue(RecordComponent component, Object record) throws InvocationTargetException, IllegalAccessException {
        var value = component.getAccessor().invoke(record);
        return value == null ? "null" : switch (component.getType().getName()) {
            case "java.lang.String" -> formatToString(value);
            case "java.util.List","java.util.Set" -> getList((List<?>) value);
            default -> value.toString();
        };
    }

    private String getList(List<?> values) {
        return values.stream()
                .map(item -> isUserDefinedClass(item.getClass()) ? readObject(item) :
                        item instanceof String s ? formatToString(s) : item.toString())
                .collect(Collectors.joining(",", "[", "]"));
    }

    private static boolean isUserDefinedClass(Class<?> clazz) {
        return clazz.getClassLoader() != null &&
                !clazz.getName().startsWith("java.") &&
                !clazz.getName().startsWith("javax.");
    }
}

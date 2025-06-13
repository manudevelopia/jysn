package info.developia.lib.jaysn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class JysnEncoder {
    private final Record record;

    public JysnEncoder(Record record) {
        this.record = record;
    }

    public String toJson() {
        return readRecord(record);
    }

    private String readRecord(Object record) {
        var jsonObject = new StringJoiner(", ", "{", "}");
        for (RecordComponent component : record.getClass().getRecordComponents()) {
            var property = new StringBuilder();
            property.append(getPropertyName(component));
            property.append(":");
            try {
                property.append(!isUserDefinedClass(component.getType()) ?
                        getPropertyValue(component, record) :
                        readRecord(component.getAccessor().invoke(record)));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            jsonObject.add(property);
        }
        return jsonObject.toString();
    }

    private String getPropertyName(RecordComponent component) {
        return formatToString(component.getName());
    }

    private static String formatToString(String text) {
        return String.format("\"%s\"", text);
    }

    private String getPropertyValue(RecordComponent component, Object record) throws InvocationTargetException, IllegalAccessException {
        var value = component.getAccessor().invoke(record);
        return switch (component.getType().getName()) {
            case "java.lang.String" -> String.format("\"%s\"", value);
            case "java.util.List" -> getList((List<?>) value);
//            default -> throw new RuntimeException("Unsupported type " + component.getType().getName());
            default -> value.toString();
        };
    }

    private String getList(List<?> values) {
        return values.stream().map(item -> isUserDefinedClass(item.getClass()) ? readRecord(item) :
                        switch (item) {
                            case String s -> formatToString(s);
                            default -> item.toString();
                        })
                .collect(Collectors.joining(",", "[", "]"));
    }

    private static boolean isUserDefinedClass(Class<?> clazz) {
        return clazz.getClassLoader() != null &&
                !clazz.getName().startsWith("java.") &&
                !clazz.getName().startsWith("javax.");
    }
}

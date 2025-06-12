package info.developia.lib.jaysn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.StringJoiner;

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
        return String.format("\"%s\"", component.getName());
    }

    private String getPropertyValue(RecordComponent component, Object record) throws InvocationTargetException, IllegalAccessException {
        return String.format("\"%s\"", component.getAccessor().invoke(record));
    }

    private static boolean isUserDefinedClass(Class<?> clazz) {
        return clazz.getClassLoader() != null &&
                !clazz.getName().startsWith("java.") &&
                !clazz.getName().startsWith("javax.");
    }
}

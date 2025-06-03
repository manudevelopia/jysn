package info.developia.lib.jaysn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;

public class JysnEncoder {
    private final Record record;

    public JysnEncoder(Record record) {
        this.record = record;
    }

    public String toJson() {
        return readRecord(record).toString();
    }

    private StringBuilder readRecord(Object record) {
        StringBuilder jsonBuilder = new StringBuilder();
        for (RecordComponent component : record.getClass().getRecordComponents()) {
            if (!isUserDefinedClass(component.getType())) {
                try {
                    jsonBuilder.append(component.getName()).append(": ")
                            .append(component.getAccessor().invoke(this.record)).append(", ");
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            try {
                return readRecord(component.getAccessor().invoke(this.record));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonBuilder;
    }

    private static boolean isUserDefinedClass(Class<?> clazz) {
        return clazz.getClassLoader() != null &&
                !clazz.getName().startsWith("java.") &&
                !clazz.getName().startsWith("javax.");
    }
}

package org.example;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Library {
    private static final Pattern JSON_PATTERN = Pattern.compile("\"(\\w+)\":\\s*\"?([^\"]+?)\"?[,}]");

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            Map<String, String> jsonMap = extractJsonFields(json);
            for (Field field : clazz.getDeclaredFields()) {
                String value = jsonMap.get(field.getName());
                if (value != null) {
                    setFieldValue(obj, field, value);
                }
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

    private static Map<String, String> extractJsonFields(String json) {
        Map<String, String> map = new HashMap<>();
        Matcher matcher = JSON_PATTERN.matcher(json);
        while (matcher.find()) {
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }

    private static void setFieldValue(Object obj, Field field, String value) {
        try {
            VarHandle varHandle = MethodHandles.privateLookupIn(field.getDeclaringClass(), MethodHandles.lookup())
                    .unreflectVarHandle(field);

            Object convertedValue = convertValue(value, field.getType());
            varHandle.set(obj, convertedValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set value for field: " + field.getName(), e);
        }
    }

    private static Object convertValue(String value, Class<?> type) {
        if (type == int.class || type == Integer.class) return Integer.parseInt(value);
        if (type == double.class || type == Double.class) return Double.parseDouble(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
        return value; // Default: String
    }
}

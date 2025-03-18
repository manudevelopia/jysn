package info.developia.lib;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jaysn {
    private static final Pattern JSON_PATTERN = Pattern.compile("\\\"(\\w+)\\\"\\s*:\\s*((null)|(true|false)|(-?\\d*\\.?\\d*)|\"(\\w+)\")");
//    private static final Pattern JSON_PATTERN = Pattern.compile("\"(\\w+)\"\\s*:\\s*\\{?(.*),*|}");
//    private static final Pattern JSON_PATTERN = Pattern.compile("\"(\\w+)\"\\s*:\\s*(?:[^{}]*|(\\{.*?}))");
//    private static final Pattern JSON_PATTERN = Pattern.compile("\"(\\w+)\"\\s*:\\s*(?:\"([^\"]*)\"|(-?\\d+(?:\\.\\d+)?)|(true|false|null)|)");
//    private static final Pattern JSON_PATTERN = Pattern.compile("\"(\\w+)\"\\s*:\\s*(\\{[^{}]*}|\"[^\"]*\"|true|false|null|\\d+(?:\\.\\d+)?)");

    public static <T> T parse(String json, Class<T> clazz) {
        var fields = getFieldsToRead(clazz);
        extractFieldsFromJson(json, fields);
        for (var field : fields) {
            System.out.println(field.getName());
        }
        return null;
    }

    private static void extractFieldsFromJson(String json, Field[] fields) {
        for (var field : fields) {
            System.out.println("Extracting field: " + field.getName());
        }
    }

    private static <T> Field[] getFieldsToRead(Class<T> clazz) {
        return clazz.getDeclaredFields();
    }

    private static Map<String, Object> readProperties(String json) {
        System.out.println("Reading JSON: " + json);
        Map<String, Object> result = new HashMap<>();
        Matcher matcher = JSON_PATTERN.matcher(json.replaceAll("\\R", ""));
        while (matcher.find()) {
            String key = matcher.group(1);
            String fullValue = matcher.group(2);
            String nullValue = matcher.group(3);
            String boolValue = matcher.group(4);
            String numberValue = matcher.group(5);
            String stringValue = matcher.group(6);
//
//            if (objectValue != null) {
//                result.put(key, readProperties(objectValue));
//            } else {
            String value = stringValue != null ? stringValue
                    : numberValue != null ? numberValue
                    : boolValue != null ? boolValue
                    : nullValue != null ? null
                    : fullValue;
            result.put(key, value);
//            }
        }
        return result;
    }
}

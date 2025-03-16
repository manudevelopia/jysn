package info.developia.lib;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jaysn {
    private static final Pattern JSON_PATTERN = Pattern.compile("\"(\\w+)\"\\s*:\\s*(?:\"([^\"]*)\"|([\\d.]+)|(true|false)|(null)|(\\{(?:[^{}]*|\\{.*?})}))");
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

    private static Map<String, String> readProperties(String json) {
        System.out.println("Reading JSON: " + json);
        Map<String, String> result = new HashMap<>();
        Matcher matcher = JSON_PATTERN.matcher(json);
        while (matcher.find()) {
            String key = matcher.group(1);
            String stringValue = matcher.group(2);
            String numberValue = matcher.group(3);
            String boolValue = matcher.group(4);
            String nullValue = matcher.group(5);
            String objectValue = matcher.group(6);

            String value = stringValue != null ? stringValue
                    : numberValue != null ? numberValue
                    : boolValue != null ? boolValue
                    : nullValue != null ? null
                    : objectValue;
            result.put(key, value);
        }
        return result;
    }
}

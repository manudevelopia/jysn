package info.developia.lib;

public class Value {

    static Object cast(Class<?> type, Object value) {
        return switch (type.getName()) {
            case "java.lang.String" -> value;
            case "int" -> Integer.parseInt(value.toString());
            case "java.lang.Integer" -> Integer.valueOf(value.toString());
            default -> new RuntimeException("Unsupported type " + type.getName());
        };
    }

    public static String extract(String token) {
        var value = token.trim();
        if (value.startsWith("\"") && value.endsWith("\""))
            return value.substring(1, value.length() - 1);
        return value;
    }
}

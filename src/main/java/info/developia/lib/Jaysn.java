package info.developia.lib;

import info.developia.lib.json.JsonNode;
import info.developia.lib.json.JsonObject;
import info.developia.lib.json.JsonObjectArray;
import info.developia.lib.json.JsonProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Jaysn {

    private static final String leftBrace = "{";
    private static final String rightBrace = "}";
    private static final String leftBracket = "[";
    private static final String rightBracket = "]";
    private static final String comma = ",";
    private static final String colon = ":";

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            var fields = getFields(clazz);
            String[] tokens = Tokenizer.getTokens(json);
            var nodes = getNodes(tokens);
            return buildObject(clazz, fields, nodes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T buildObject(Class<T> clazz, Object fields, JsonNode nodes) {
        if (clazz.isRecord()) {
            try {
                return buildRecord(clazz, fields, nodes);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        var constructor = clazz.getDeclaredConstructors()[0];
        var parameters = getConstructorParametersInstances(constructor);
        try {
            var instance = constructor.newInstance(parameters);
            return clazz.cast(instance);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Field cannot be initialized " + e.getMessage());
        }
    }

    private static <T> T buildRecord(Class<T> clazz, Object fields, JsonNode nodes)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        RecordComponent[] components = clazz.getRecordComponents();
        Class<?>[] paramTypes = new Class<?>[components.length];
        Object[] args = new Object[components.length];
        var properties = ((JsonObject) nodes).getProperties().stream().collect(Collectors
                .toMap(jsonProperty -> jsonProperty.getName().trim().replace("\"", ""),
                        jsonProperty -> jsonProperty.getValue().toString().trim().replace("\"", "")));

        for (int i = 0; i < components.length; i++) {
            RecordComponent comp = components[i];
            paramTypes[i] = comp.getType();
            args[i] = castValue(comp.getType(), properties.get(comp.getName()));
        }
        Constructor<T> constructor = clazz.getDeclaredConstructor(paramTypes);
        return constructor.newInstance(args);
    }

    private static Object castValue(Class<?> type, Object value) {
        return switch (type.getName()) {
            case "java.lang.String" -> value;
            case "int" -> Integer.parseInt(value.toString());
            case "java.lang.Integer" -> Integer.valueOf(value.toString());
            default -> new RuntimeException("Unsupported type " + type.getName());
        };
    }

    private static Object[] getConstructorParametersInstances(Constructor<?> constructor) {
        var parametersInstances = Arrays.stream(constructor.getParameterTypes())
                .toArray();
        if (constructor.getParameterTypes().length != parametersInstances.length)
            throw new RuntimeException("Not all the arguments could be fulfilled for " + constructor.getName() + " constructor");
        return parametersInstances;
    }

    private static <T> Object getFields(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var fields = new ArrayList<String>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(info.developia.lib.annotation.JsonProperty.class)) {
                var annotationName = field.getAnnotation(info.developia.lib.annotation.JsonProperty.class).name();
                fields.add(annotationName.isBlank() ? field.getName() : annotationName);
            }
        }
        return fields;
    }

    private static boolean isUserDefinedClass(Class<?> clazz) {
        return clazz.getClassLoader() != null &&
                !clazz.getName().startsWith("java.") &&
                !clazz.getName().startsWith("javax.");
    }

    private static JsonNode getNodes(String[] tokens) {
        JsonNode currentJsonObject = null;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            switch (token) {
                case leftBracket:
                    currentJsonObject = new JsonObjectArray(currentJsonObject);
                    continue;
                case leftBrace:
                    var object = new JsonObject(currentJsonObject);
                    if (currentJsonObject instanceof JsonObjectArray) {
                        ((JsonObjectArray) currentJsonObject).add(object);
                    }
                    currentJsonObject = object;
                    continue;
                case rightBracket, rightBrace:
                    if (currentJsonObject.parent != null) {
                        currentJsonObject = currentJsonObject.parent;
                    }
                    continue;
                case comma:
                    continue;
                default:
                    var propertyNameValue = token.split(":");
                    var name = propertyNameValue[0];
                    if (propertyNameValue.length == 2) {
                        var value = propertyNameValue[1];
                        ((JsonObject) currentJsonObject).add(new JsonProperty(name, value, currentJsonObject));
                    } else if (propertyNameValue.length == 1) {
                        if (leftBracket.equals(tokens[i + 1])) {
                            JsonNode value = new JsonObjectArray(currentJsonObject);
                            i++;
                            var jsonObjectArray = new JsonObject(value);
                            ((JsonObjectArray) value).add(jsonObjectArray);
                            ((JsonObject) currentJsonObject).add(new JsonProperty(name, value, currentJsonObject));
                            currentJsonObject = jsonObjectArray;
                        } else {
                            JsonNode value = new JsonObject(currentJsonObject);
                            ((JsonObject) currentJsonObject).add(new JsonProperty(name, value, currentJsonObject));
                            currentJsonObject = value;
                        }
                        i++;
                    }
            }
        }
        return currentJsonObject;
    }
}

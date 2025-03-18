package info.developia.lib;

import java.util.Arrays;

public class Jaysn {
    //    private static final String illegal = "ILLEGAL";
//    private static final String eOF = "EOF";
//    private static final String string = "STRING";
//    private static final String number = "NUMBER";
    private static final String leftBrace = "{";
    private static final String rightBrace = "}";
    private static final String leftBracket = "[";
    private static final String rightBracket = "]";
    private static final String comma = ",";
    private static final String colon = ":";
//    private static final String trueValue = "TRUE";
//    private static final String falseValue = "FALSE";
//    private static final String nullValue = "NULL";

    public static <T> T parse(String json, Class<T> clazz) {
        String[] tokens = getTokens(json);
        Node currentNode = null;

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            switch (token) {
                case leftBrace:
                    var name = currentNode == null ? "root" : currentNode.name;
                    currentNode = new Node(Node.Type.object, name, token);
                    continue;
                case rightBrace:
                    currentNode = currentNode == null ? null : currentNode.parent;
                    continue;
                case comma:
                    continue;
                default:
                    currentNode = parseToken(token, currentNode);
            }
        }
        return null;
    }

    private static Node parseToken(String token, Node currentNode) {
        var propertyValue = token.split(":");
        var name = propertyValue[0];
        if (propertyValue.length == 2) {
            var value = propertyValue[1];
            currentNode.addChild(new Node(Node.Type.property, name, value));
            return currentNode;
        } else if (propertyValue.length == 1) {
            var node = new Node(Node.Type.object, name, currentNode);
            currentNode.addChild(node);
            return node;
        }
        return currentNode;
    }

    private static String[] getTokens(String json) {
        return Arrays.stream(json.splitWithDelimiters("[{}\\[\\],]", 0))
                .filter(token -> !token.isBlank())
                .map(String::trim)
                .toArray(String[]::new);
    }
}

package info.developia.lib;

import java.util.ArrayList;
import java.util.List;

class Node {
    final Type type;
    String name;
    Object value;
    List<Node> children;
    Node parent;

    enum Type {object, array, property}

    public Node(Type type, String name, Node parent) {
        this.type = type;
        this.name = name;
        this.parent = parent;
    }

    public Node(Type type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    void addChild(Node node) {
        if (children == null) children = new ArrayList<>();
        node.parent = this;
        children.add(node);
    }
}

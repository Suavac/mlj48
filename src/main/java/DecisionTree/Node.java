package DecisionTree;

import driver.Attribute;

import java.util.HashMap;

/**
 * Created by Suavek on 15/11/2016.
 */
public class Node {

    final HashMap<String, Attribute> attributes;
    private boolean isLeaf;
    private String nodeName;
    private HashMap<String, Node> nodes;

    public Node(final HashMap<String, Attribute> attributes) {
        this.attributes = attributes;
    }
}

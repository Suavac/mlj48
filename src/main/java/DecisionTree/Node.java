package DecisionTree;

import com.google.common.collect.Maps;
import driver.Attribute;

import java.util.HashMap;

/**
 * Created by Suavek on 15/11/2016.
 */
public class Node {

    private boolean isLeaf;
    private String nodeName;
    private HashMap children = Maps.newLinkedHashMap();

    public Node() {
    }

    public Node(String nodeName, boolean isLeaf){
        this.nodeName = nodeName;
        this.isLeaf = isLeaf;
    }

    public boolean isLeaf(){
        return this.isLeaf;
    }

    public String getNodeName(){
        return this.nodeName;
    }

    public void addChild(Node child){
        this.children.put(child.getNodeName(),child);
    }
}

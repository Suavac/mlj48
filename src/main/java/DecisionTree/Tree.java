package DecisionTree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import driver.Gain;
import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Suavek on 15/11/2016.
 */
public class Tree {

    private boolean isLeaf;
    private String nodeName;
    private final String value;
    private final ArrayList<Tree> children = Lists.newArrayList();
    //private final HashMap<String, Tree> children = Maps.newLinkedHashMap();

    public Tree(final String nodeName, final boolean isLeaf) {
        this.nodeName = nodeName;
        this.value = nodeName;
        this.isLeaf = isLeaf;
    }

    public Tree(final Gain gain, final boolean isLeaf) {
        this.nodeName = gain.getAttributeName();
        this.isLeaf = isLeaf;
        this.value = String.valueOf(gain.getThreshold());
    }

    public boolean isLeaf() {
        return this.isLeaf;
    }

    public String getNodeName() {
        return this.nodeName;
    }

    public void addChild(final Tree child) {
        //final ArrayList children = new ArrayList<>(this.children.keySet());
        // if the same labels on both sides then make node a label
        //if(children.size()>0){
         //   if(child.getNodeName().equals(children.get(0))){
         //       this.nodeName = child.nodeName;
         //       this.isLeaf = true;
          //      return;
          //  }
        //}
        this.children.add(child);

    }

    public String getValue() {
        return value;
    }

    public String search(final Tree node, final CSVRecord instance) {
        if (node.isLeaf())
            return node.getNodeName();
        final String value = instance.get(node.getNodeName());
        return new BigDecimal(value).compareTo(new BigDecimal(node.getValue())) <= 0 ?
                search(node.children.get(0), instance) :
                search(node.children.get(1), instance);

//        final ArrayList children = new ArrayList<>(node.children.keySet());
//
//        return new BigDecimal(value).compareTo(new BigDecimal(node.getValue())) <= 0 ?
//                search(node.children.get(children.get(0)), instance) :
//                search(node.children.get(children.get(1%children.size())), instance);
    }
}


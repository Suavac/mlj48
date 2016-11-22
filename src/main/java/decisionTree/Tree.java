package decisionTree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import driver.Attribute;
import driver.Gain;
import org.apache.commons.csv.CSVRecord;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Suavek on 15/11/2016.
 */
public class Tree implements Serializable {
static int o = 0;
    private Attribute attribute;
    private String nodeName;
    private String value;
    private final List childrenCont = Lists.newArrayList();
    private final HashMap<String, Tree> childrenNominal = Maps.newLinkedHashMap();

    /** Constructs a leaf node
     * @param label
     * @param targetAttribute
     */
    public Tree(final String label, Attribute targetAttribute) {
        this.nodeName = label;
        this.value = nodeName;
        this.attribute = targetAttribute;
    }

    /** Constructs a decision node
     * @param gain
     */
    public Tree(final Gain gain) {
        this.nodeName = gain.getAttributeName();
        this.value = gain.getValue();
        this.attribute = gain.getAttribute();
    }


    /** Appends child nodes
     * @param child
     */
    public void addChild(final Tree child) {
        //final ArrayList children = new ArrayList<>(this.children.keySet());
         //if the same labels on both sides of the decision node then make node a label
        //if(child.attribute.isTarget() && children.size() > 0){
        //    if(child.nodeName.equals(children.get(0))){
        //        this.nodeName = child.nodeName;
        //        this.attribute = child.attribute;
         //       return;
         //   }
        //}
        this.childrenCont.add(child);
    }

    /** Appends child nodes of a nominal attributes
     * @param child
     */
    public void addChild(final String childName, final Tree child) {
        this.childrenNominal.put(childName, child);
    }

    /** Search for a label node
     * @param node
     * @param instance
     * @return
     */
    public String search(final Tree node, final CSVRecord instance) {
        o++;
        if(o==39){
            int p = o;
        }
        if (node.attribute.isTarget()){
            return node.nodeName;
        }

        //final ArrayList children = new ArrayList<>(node.children.keySet());
        final String value = instance.get(node.nodeName);
        if(node.attribute.isContinuous()){
            // check if value smaller or greater than threshold and continue search recursively
            int direction = new BigDecimal(value).compareTo(new BigDecimal(node.value)) <= 0 ?
                    0: // left
                    1; // right
            return search((Tree) node.childrenCont.get(direction), instance);
        }
        // TODO - support for discrete attributes
        return search(node.childrenNominal.get(value), instance);
    }
}


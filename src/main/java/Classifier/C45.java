package Classifier;

import driver.Attribute;
import driver.PreprocessedData;
import DecisionTree.Tree;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by 12100888 on 19/11/2016.
 */
public class C45 implements Classifier{

    Tree decisionTree;

    @Override
    public void train(PreprocessedData ppd) {
        final HashMap attributes = ppd.getAttributes();
        System.out.println(ppd.getAttributes().size());
        // choose target - assuming that target is a last column
        final Attribute targetAttribute = (Attribute) attributes.get(ppd.getTargetName());
        final ArrayList attributeNames = ppd.getAttributeNames();
        final List<CSVRecord> dataSet = (List<CSVRecord>) ppd.getDataRecords();
        this.decisionTree = new Tree(dataSet, attributes, attributeNames, targetAttribute);
    }

    @Override
    public void test(PreprocessedData ppd) {
       // HashMap tmp = decisionTree.getTreeNodes();
        //Set<String> keys = tmp.keySet();
        //System.out.println(keys);

    }

    @Override
    public void classify(PreprocessedData ppd) {

    }
}

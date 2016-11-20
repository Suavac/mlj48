package Classifier;

import DecisionTree.Tree;
import DecisionTree.TreeConstructor;
import driver.Attribute;
import driver.PreprocessedData;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 12100888 on 19/11/2016.
 */
public class C45 implements Classifier {

    Tree decisionTreeRoot;

    @Override
    public void train(final PreprocessedData ppd) {
        final HashMap attributes = ppd.getAttributes();
        System.out.println(ppd.getAttributes().size());
        // choose target - assuming that target is a last column
        final Attribute targetAttribute = (Attribute) attributes.get(ppd.getTargetName());
        final ArrayList attributeNames = ppd.getAttributeNames();
        final List<CSVRecord> dataSet = (List<CSVRecord>) ppd.getDataRecords();
        this.decisionTreeRoot = new TreeConstructor(dataSet, attributes, attributeNames, targetAttribute).getDecisionTree();
    }

    @Override
    public void test(final PreprocessedData ppd) {
        final HashMap attributes = ppd.getAttributes();
        // choose target - assuming that target is a last column
        final Attribute targetAttribute = (Attribute) attributes.get(ppd.getTargetName());
        final ArrayList attributeNames = ppd.getAttributeNames();

        final List<CSVRecord> dataSet = (List<CSVRecord>) ppd.getDataRecords();
        int k = 0;
        int l = 0;
        int p = 0;
        for (final CSVRecord instance : dataSet) {
            final String lan = decisionTreeRoot.search(decisionTreeRoot, instance);


            if (lan.equals("LongEaredOwl")) k++;
            if (lan.equals("LongEaredOwl")) l++;
            if (lan.equals("LongEaredOwl")) p++;

        }
        System.out.println(k);
        System.out.println(l);
        System.out.println(p);
        //final CSVRecord instance = Iterables.get(dataSet, 45);

//        if (Double.parseDouble(instance.get(decisionTreeRoot.getNodeName())) <= Double.parseDouble(decisionTreeRoot.getValue().toString())) {
//            System.out.println(instance.get(decisionTreeRoot.getNodeName()));
//        }
    }

    @Override
    public void classify(final PreprocessedData ppd) {

    }
}

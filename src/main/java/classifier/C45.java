package classifier;

import decisionTree.Tree;
import decisionTree.TreeConstructor;
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

    Tree decisionTree;

    @Override
    public void train(final PreprocessedData ppd) {
        final HashMap attributes = ppd.getAttributes();
        // choose target - assuming that target is a last column
        final Attribute targetAttribute = (Attribute) attributes.get(ppd.getTargetName());
        final List<CSVRecord> dataSet = ppd.getTrainingDataSet();
        this.decisionTree = new TreeConstructor(dataSet, attributes, targetAttribute).getDecisionTree();
    }

    @Override
    public void test(final PreprocessedData ppd) {
        final List<CSVRecord> dataSet =  ppd.getTestingDataSet();
        for (final CSVRecord instance : dataSet) {
            final String predicted = decisionTree.search(decisionTree, instance);
        }
    }

    @Override
    public void classify(final PreprocessedData ppd) {

    }
}

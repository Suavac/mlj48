package classifier;

import decisionTree.Tree;
import decisionTree.TreeConstructor;
import driver.Attribute;
import driver.PreprocessedData;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 12100888 on 19/11/2016.
 */
public class C45 implements Classifier {

    public Tree decisionTree;
    private double accuracy;

    @Override
    public void train(final PreprocessedData ppd) {
        final HashMap attributes = ppd.getAttributes();
        // choose target - assuming that target is a last column
        final Attribute targetAttribute = (Attribute) attributes.get(ppd.getTargetName());
        final List<CSVRecord> dataSet = ppd.getTrainingDataSet();
        TreeConstructor treeConstructor = new TreeConstructor(dataSet, attributes, targetAttribute);
        this.decisionTree = treeConstructor.getDecisionTree();

    }

    @Override
    public void test(final PreprocessedData ppd) {

        final List<CSVRecord> dataSet =  ppd.getTestingDataSet();

        try {
            //File file = File.createTempFile("/Users/jamesfallon/Downloads/owls16", ".tmp");
            PrintWriter printWriter = new PrintWriter(new File("/Users/jamesfallon/Downloads/owls17.csv"));
            printWriter.println("actual,predicted,error");

            System.out.println(dataSet.size());
            for (CSVRecord instance : dataSet){

                String actual = instance.get(ppd.getTargetName());
                String predicted = classify(instance);
                String error = actual.equals(predicted) ? "" : "+";

                printWriter.println(actual + "," + predicted + "," + error);
            }

            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

    @Override
    public String classify(final CSVRecord instance) {
        return decisionTree.search(decisionTree, instance);
    }

    @Override
    public ClassifierType getClassifier() {
        return decisionTree;
    }

    @Override
    public double getAccuracy() {
        return accuracy;
    }
}

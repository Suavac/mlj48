package classifier;

import driver.PreprocessedData;
import org.apache.commons.csv.CSVRecord;

/**
 * Created by 12100888 on 19/11/2016.
 */
public interface Classifier {
    void train(PreprocessedData ppd);
    void test(PreprocessedData ppd);
    String classify(CSVRecord instance);
    ClassifierType getClassifier();
    double getAccuracy();
}

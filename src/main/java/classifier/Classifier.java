package classifier;

import driver.PreprocessedData;

/**
 * Created by 12100888 on 19/11/2016.
 */
public interface Classifier {
    void train(PreprocessedData ppd);
    void test(PreprocessedData ppd);
    void classify(PreprocessedData ppd);
}

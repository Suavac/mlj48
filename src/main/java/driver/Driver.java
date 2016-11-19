package driver;

import Classifier.C45;
import Classifier.Classifier;

/**
 * Created by Suavek on 16/11/2016.
 */
public class Driver {


    public static void main(final String... args) throws Exception {

        ///http://clear-lines.com/blog/post/Discretizing-a-continuous-variable-using-Entropy.aspx
        final PreprocessedData ppd = new PreprocessedData("owls15.csv");

        final Classifier classifier = new C45();
        classifier.train(ppd.getTrainingData());
        classifier.test(ppd);

        final String a = "sdsd";
        final int i = 0;
    }
}

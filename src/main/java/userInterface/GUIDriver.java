package userInterface;

import classifier.C45;
import classifier.Classifier;
import driver.PreprocessedData;
import userInterface.GUI;

/**
 * Created by jamesfallon on 23/11/2016.
 */
public class GUIDriver {

    public static void main(final String... args) throws Exception{
        //GUI gui = new GUI();

        PreprocessedData ppd = new PreprocessedData("/Users/jamesfallon/Downloads/owls15.csv");
        ppd.splitTrainingTestPercentage(0.3);
        Classifier classifier = new C45();

        classifier.train(ppd);
        classifier.test(ppd);
    }
}

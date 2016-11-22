package driver;

import classifier.C45;
import classifier.Classifier;
import userInterface.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Suavek on 16/11/2016.
 */
public class Driver {


    public static void main(final String... args) throws Exception {
        //TODO - create GUI - select file, display results
       // GUI a = new GUI();

        ///http://clear-lines.com/blog/post/Discretizing-a-continuous-variable-using-Entropy.aspx
        for(int i = 0; i< 100; i++){
            final PreprocessedData ppd = new PreprocessedData("owls15Discrete.csv");
            //final PreprocessedData ppd = new PreprocessedData("owls15.csv");
            ppd.splitTrainingTestPercentage(0.8);
            final Classifier classifier = new C45();
            classifier.train(ppd);
            classifier.test(ppd);
        }
    }
}

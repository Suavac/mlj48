package driver;

import classifier.C45;
import classifier.Classifier;
import classifier.ClassifierType;
import decisionTree.Tree;
import userInterface.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Suavek on 16/11/2016.
 */
public class Driver {

    private PreprocessedData ppd;
    private double trainingSplitPercentage = 0.7;
    private final Classifier classifier = new C45();

    public Driver ()
    {

    }

    public double test ()
    {
        classifier.test(ppd);
        return classifier.getAccuracy();
    }

    public void train (int numberOfTimesToRun)  throws Exception
    {

        ClassifierType best = null;
        double bestAccuracy = 0.0;


        ///http://clear-lines.com/blog/post/Discretizing-a-continuous-variable-using-Entropy.aspx
        for(int i = 0; i< numberOfTimesToRun ; i++){

            ppd.splitTrainingTestPercentage(trainingSplitPercentage);

            classifier.train(ppd);
            classifier.test(ppd);

            if(best==null){
                best=classifier.getClassifier();
            }
            else if(classifier.getAccuracy() >= bestAccuracy){
                bestAccuracy = classifier.getAccuracy();
                best = classifier.getClassifier();
            }

        }
    }

    public PreprocessedData getPpd ()
    {
        return ppd;
    }

    public void setPpd (PreprocessedData ppd)
    {
        this.ppd = ppd;
    }

    public double getTrainingSplitPercentage ()
    {
        return trainingSplitPercentage;
    }

    public void setTrainingSplitPercentage (double trainingSplitPercentage)
    {
        this.trainingSplitPercentage = trainingSplitPercentage;
    }



}

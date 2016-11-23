package classifier;

import com.google.common.collect.Sets;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Created by jamesfallon on 23/11/2016.
 */
public class Results {

    ConfusionMatrix confusionMatrix = new ConfusionMatrix();
    List<CSVRecord> records;

    public Results (File file)
    {
        this.records = records;

        Set<String> labels = Sets.newHashSet();

        for (CSVRecord record : records) {
            String predicted = record.get("predicted");
            String actual = record.get("actual");

            labels.add(predicted);
            labels.add(actual);

            if(predicted.equals(actual))
            {
                confusionMatrix.incrementTP();
            }
            else{
                confusionMatrix.incrementFP();
            }
        }
    }

    public double getAccuracy ()
    {
        return (double)(confusionMatrix.TN + confusionMatrix.TP)/(double)(confusionMatrix.FP + confusionMatrix.TP + confusionMatrix.FN + confusionMatrix.TN);
    }

    public double getPrecision ()
    {
        return (double)(confusionMatrix.TP)/(double)(confusionMatrix.FP + confusionMatrix.TP);
    }

    public double getRecall ()
    {
        return (double)(confusionMatrix.TP)/(double)(confusionMatrix.FN + confusionMatrix.TP);
    }


    private class ConfusionMatrix {
        int FP;
        int TP;
        int FN;
        int TN;

        public ConfusionMatrix (){}

        public void incrementFP ()
        {
            FP++;
        }
        public void incrementTP ()
        {
            TP++;
        }
        public void incrementFN ()
        {
            FN++;
        }
        public void incrementTN ()
        {
            TN++;
        }

    }
}

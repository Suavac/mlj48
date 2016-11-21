package driver;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dataReader.CSVReader;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by 12100888 on 08/11/2016.
 */
public class PreprocessedData {

    private final CSVReader data;
    private ArrayList<String> attributeNames = Lists.newArrayList();
    private final String targetName;
    final HashMap<String, Attribute> attributes = Maps.newHashMap();

    List trainingSet = Lists.newArrayList();
    List testingSet = Lists.newArrayList();

    public PreprocessedData(final String filePath) throws Exception {
        if (filePath.toLowerCase().endsWith(".csv")) {
            this.data = new CSVReader(filePath);
        } else {
            throw new Exception("Data Format not supported");
        }

        this.attributeNames = this.data.getAttributeNames();
        this.attributeNames.forEach(attributeName ->
                attributes.put(
                        attributeName,
                        createAttribute(this.data.getDataSet(), attributeName)
                )
        );
        this.targetName = attributeNames.get(attributeNames.size() - 1);
        this.attributes.get(targetName).setAsTarget();
    }

    //http://www.saedsayad.com/decision_tree.htm
    public static Attribute createAttribute(final Iterable<CSVRecord> dataRecords, final String attributeName) {
            // Check if attribute is continuous

            boolean isContinuous = true;
            for (final CSVRecord record : dataRecords) {
                try {
                    final String value = record.get(attributeName);
                    if (!value.trim().equals("")) {
                        Double.parseDouble(record.get(attributeName));
                    }
                } catch (final NumberFormatException e) {
                    isContinuous = false;
                    return new Attribute(attributeName, isContinuous);
                }
            }
        return new Attribute(attributeName, isContinuous);
    }

    public void splitTrainingTestPercentage(double splitPercent) {
        int numberOfTrainingSamples = (int) Math.ceil(getDataSet().size() * splitPercent);
        int numberOfTestingSamples = getDataSet().size() - numberOfTrainingSamples;
        for(int i = 0 ; i < numberOfTestingSamples ; i++){
            CSVRecord tmpSample = (CSVRecord) getDataSet().get(ThreadLocalRandom.current().nextInt(0, getDataSet().size()));
            this.testingSet.add(tmpSample);
            getDataSet().remove(tmpSample);
        }
        this.trainingSet = data.getDataSet();
    }

    public List getDataSet() {
        return this.data.getDataSet();
    }
    public List getTrainingDataSet() {
        return this.trainingSet.size() > 0 ?
                this.trainingSet:
                this.data.getDataSet();

    }
    public List getTestingDataSet() {
        return this.testingSet;
    }

    public HashMap<String, Attribute> getAttributes() {
        return this.attributes;
    }
    public ArrayList<String> getAttributeNames() {
        return this.attributeNames;
    }
    public String getTargetName() {
        return this.targetName;
    }

}

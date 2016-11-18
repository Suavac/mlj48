package driver;

import com.google.common.collect.Lists;
import dataReader.CSVReader;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 12100888 on 08/11/2016.
 */
public class PreprocessedData {

    private final CSVReader data;
    private final ArrayList<String> attributeNames;
    private final String targetName;
    final HashMap<String, Attribute> attributes;

    final static ArrayList instancesIndex = Lists.newArrayList();

    public PreprocessedData(final String filePath) throws Exception {

        if (filePath.toLowerCase().endsWith(".csv")) {
            this.data = new CSVReader();
        } else {
            throw new Exception("Data Format not supported");
        }

        attributeNames = data.getAttributeNames();

        attributes = new HashMap<String, Attribute>();
        for (final String attributeName : attributeNames) {
            attributes.put(attributeName, new Attribute(attributeName));
        }
        for (final CSVRecord record : data.getDataSet()) {
            instancesIndex.add(record.getRecordNumber());
        }
        extractAttributes(data.getDataSet(), attributeNames, attributes);
        targetName = attributeNames.remove(attributeNames.size() - 1);
        createTargetAttribute(targetName);
    }

    private void createTargetAttribute(final String targetName) {

    }

    public HashMap<String, Attribute> getAttributes() {
        return attributes;
    }

    public ArrayList<String> getAttributeNames() {
        return this.attributeNames;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public Object getDecisionClass() {
        //return decisionClass;
        return null;
    }

    //http://www.saedsayad.com/decision_tree.htm
    //Extract values of each attribute
    public static void extractAttributes(final Iterable<CSVRecord> dataRecords, final ArrayList<String> attributeNames, final HashMap<String, Attribute> attributes) {

        // Decide if attribute is continuous
        for (final String attributeName : attributeNames) {
            boolean isContinuous = true;
            for (final CSVRecord record : dataRecords) {
                try {
                    final String value = record.get(attributeName);
                    if (!value.trim().equals("")) {
                        Float.parseFloat(record.get(attributeName));
                    }
                } catch (final Exception e) {
                    isContinuous = false;
                    break;
                }
            }
            attributes.put(attributeName, new Attribute(attributeName, isContinuous));
        }
    }

    public Object getTargetAttribute() {
        return attributes.get("type");
    }

    public List<CSVRecord> getDataset() {
        return this.data.getDataSet();
    }

    public Iterable getDataRecords() {
        return data.getDataSet();
    }

    public ArrayList getInstancesIndex() {
        return instancesIndex;
    }
}

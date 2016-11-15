package driver;

import dataReader.CSVReader;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 12100888 on 08/11/2016.
 */
public class PreprocessData {

    private final CSVReader data;
    private final ArrayList<String> attributeNames;
    private final String targetName;
    final HashMap<String, Attribute> attributes;

    public PreprocessData(final String filePath) throws Exception {

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
    public static void extractAttributes(final Iterable<CSVRecord> dataRecords, final ArrayList<String> attributeNames, final HashMap<String, Attribute> atributes) {

        for (final String attributeName : attributeNames) {
            for (final CSVRecord record : dataRecords) {
                final Attribute tmp = atributes.get(attributeName);
                tmp.addValue(record.get(attributeName));
            }
        }

//        for (final CSVRecord record : dataRecords) {
//            for (final String attributeName : attributeList) {
//                final Attribute tmp = atributes.get(attributeName);
//                tmp.addValue(record.get(attributeName));
//            }
//        }
    }

    public Object getTargetAttribute() {
        return attributes.get("type");
    }
}

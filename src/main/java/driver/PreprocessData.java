package driver;

import dataReader.CSVReader;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 12100888 on 08/11/2016.
 */
public class PreprocessData {

    private CSVReader data;

    final HashMap<String, Attribute> attributes;

    public PreprocessData(String filePath) throws Exception {

        if(filePath.toLowerCase().endsWith(".csv")){
            this.data = new CSVReader();
        } else {
            throw new Exception("Data Format not supported");
        }

        // Extract Attribute names
        final ArrayList<String> attributeList = this.data.getAttributeNames();
        //assuming here that last column is a decision class
        final String testClass = attributeList.get(attributeList.size() - 1);
        attributes = new HashMap<String, Attribute>();
        for (final String attributeName : attributeList) {
            attributes.put(attributeName, new Attribute(attributeName));
        }

        extractAttributes(data.getDataSet(), attributeList, attributes);

    }

    public HashMap<String,Attribute> getAttributes() {
        return attributes;
    }

    public ArrayList<String> getAttributeNames() {
        return this.data.getAttributeNames();
    }

    public Object getDecisionClass() {
        //return decisionClass;
        return null;
    }

    //http://www.saedsayad.com/decision_tree.htm
    //Extract values of each attribute
    public static void extractAttributes(final Iterable<CSVRecord> dataRecords, final ArrayList<String> attributeList, final HashMap<String, Attribute> atributes) {
        for (final CSVRecord record : dataRecords) {
            for (final String attributeName : attributeList) {
                final Attribute tmp = atributes.get(attributeName);
                tmp.addValue(record.get(attributeName));
            }

        }
    }

    public Object getTargetAttribute() {
        return attributes.get("type");
    }
}

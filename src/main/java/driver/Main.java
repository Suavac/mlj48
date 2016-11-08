package driver;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class Main {
    public static void main(final String... args) throws IOException {

        // Get and read File
        final ClassLoader classLoader = new Main().getClass().getClassLoader();
        final File file = new File(classLoader.getResource("owls15.csv").getFile());
        final Reader fileReader = new FileReader(file);

        // Parse file
        // Iterable<CSVRecord> instances  = csvFileParser;
        final CSVFormat csvFileFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        final CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);

        // Extract Attribute names
        final ArrayList<String> attributeList = new ArrayList<String>(csvFileParser.getHeaderMap().keySet());
        final String testClass = attributeList.get(attributeList.size() - 5);
        final HashMap<String, Attribute> attributes = new HashMap<String, Attribute>();
        for (final String attributeName : attributeList) {
            attributes.put(attributeName, new Attribute(attributeName));
        }

        extractAttributes(csvFileParser, attributeList, attributes);
        final Attribute a = attributes.get(testClass);
        final ArrayList<String> b = a.getValues();
        System.out.println(b);
        entropy(b);

    }

    //http://www.saedsayad.com/decision_tree.htm
    //Extract values of each attribute
    public static void extractAttributes(final CSVParser csvFileParser, final ArrayList<String> attributeList, final HashMap<String, Attribute> atributes) {
        for (final CSVRecord record : csvFileParser) {
            for (final String attributeName : attributeList) {
                final Attribute tmp = atributes.get(attributeName);
                tmp.addValue(record.get(attributeName));
            }

        }
    }


    public static void entropy(final ArrayList<String> data) {
        // count occurrences of values
        final HashMap<String, Long> countMap = new HashMap<String, Long>();
        for (final String label : data) {
            if (!countMap.containsKey(label)) {
                countMap.put(label, 1L);
            } else {
                Long count = countMap.get(label);
                count = count + 1;
                countMap.put(label, count);
            }
        }
        // printCount(countMap);
        // get probabilities for each label
        // calculate entropy
        float entropy = 0;
        final HashMap<String, Float> probabilities = new HashMap<String, Float>();
        final Set<String> keySet = countMap.keySet();
        for (final String label : keySet) {
            //System.out.println(countMap.get(label) / (float) 16);
            final float percentage = (float) (countMap.get(label) / (float) 135);
            System.out.println(percentage);
            probabilities.put(label, percentage);
            System.out.println(label + " : " + probabilities.get(label));
            entropy -= (probabilities.get(label) * (Math.log(probabilities.get(label)) / Math.log(2)) * probabilities.get(label));
        }

        System.out.println(entropy);
//        System.out.println(Math.log(0.3) / Math.log(2));
    }

    private static void printCount(final HashMap<String, Long> countMap) {
        final Set<String> keySet = countMap.keySet();
        for (final String string : keySet) {
            System.out.println(string + " : " + countMap.get(string));
        }
    }
    // Extract values of each attribute
    //extractValues(csvFileParser, attributes.get(0));
//        System.out.println(extractValues(csvFileParser, attributes.get(1)));
//        System.out.println(extractValues(csvFileParser, attributes.get(2)));
//        for (CSVRecord record : csvFileParser) {
//            System.out.println(record.get(0) + " " + record.get(1));
//        }


    //List csvRecords = csvFileParser.getHeaderMap();
    //CSVRecord csvRecord = (CSVRecord) csvRecords.get(0);
    //System.out.println(attributes.get(0));


//        Reader in = new FileReader(file);
//        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
//
//        //get number and names of all attributes
//        ArrayList<String> attributes = new ArrayList<String>(records.iterator().next().toMap().keySet());
//
//
//        for(int i = 0; i< attributes.size(); i++){
//            System.out.println(attributes.get(i));
//        }
//        System.out.println();
//
//        for (CSVRecord record : records) {
//            //System.out.println(record.get(4));
//
//        }


//        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
//        FileReader fileReader = new FileReader(file);
//        CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);
//        //List csvRecords = csvFileParser.getHeaderMap();
//        //CSVRecord csvRecord = (CSVRecord) csvRecords.get(0);
//        System.out.println(csvFileParser.getHeaderMap().keySet());


//    }
//
//    public static ArrayList<String> extractValues(final CSVParser csvFileParser, final ArrayList<String> name){
//        ArrayList<String> list = new ArrayList<String>();
//        for (CSVRecord record : csvFileParser) {
//            list.add(record.get(name));
//        }
//        return list;
//    }

}

package driver;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class Main {

    public static void main(final String... args) throws Exception {

        PreprocessData ppd = new PreprocessData("owls15.csv");
        final HashMap<String, Attribute> attributes = ppd.getAttributes();
        ppd.getDecisionClass();
        final String attributeName = ppd.getAttributeNames().get(0);
        final Attribute a = attributes.get(attributeName);
        final Attribute target = attributes.get("type");
        a.convertCont();
        final ArrayList<String> b = a.getValues();
        System.out.println(b);
        entropy(b);
        createThresholdsForAttribute(attributes.get(attributeName));
        System.out.println(a.getThresholds());

        final ArrayList<Float> bContin = a.getValuesContinous();

        final ArrayList<String> testingDiscretization = new ArrayList<String>();
        for(Float value : bContin){
            if(value<a.getThresholds().get(0)){
                testingDiscretization.add("A");
            }else{
                testingDiscretization.add("B");
            }
        }

        System.out.println(testingDiscretization);

        calculateGainForPair(target.getValues(), testingDiscretization);
    }

    public static Float calculateGainForPair(ArrayList<String> target, ArrayList<String> attribute){
        float occurancessAB = attribute.size();
        float occurrencesA = Collections.frequency(attribute, "A");
        float occurrencesB = Collections.frequency(attribute, "B");
        float probA = occurrencesA/occurancessAB;
        float probB = occurrencesB/occurancessAB;

        final HashMap<String, Long> countMap = new HashMap<String, Long>();
        // count occurances of decision classes
        int i = 0;
        for(String label : target){
            if(attribute.get(i).equals("B")){
                if (!countMap.containsKey(label)) {
                    countMap.put(label, 1L);
                } else {
                    Long count = countMap.get(label);
                    count = count + 1;
                    countMap.put(label, count);
                }
//            } else if(attribute.get(i).equals("B")){
//                if (!countMap.containsKey(label)) {
//                    countMap.put(label, 1L);
//                } else {
//                    Long count = countMap.get(label);
//                    count = count + 1;
//                    countMap.put(label, count);
//                }
            }
            i++;
        }
        System.out.println("OCCURANCES MATRIX = " + countMap);
        return null;
    }

    public static void createThresholdsForAttribute(Attribute attribute){
        TreeSet<Float> uniqueValues= new TreeSet<Float>();
        for(String value : attribute.getValues()){
            uniqueValues.add(Float.parseFloat(value));
        }
        ArrayList<Float> a = new ArrayList<Float>(uniqueValues);
        System.out.println(uniqueValues);
        ArrayList<Float> t = new ArrayList<Float>();
        for(int i =0 ; i < a.size()-1; i++){
            t.add((a.get(i)+a.get(i+1))/2);
        }
        attribute.storeThresholds(t);
        System.out.println(t);
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
            //System.out.println(percentage);
            probabilities.put(label, percentage);
            //System.out.println(label + " : " + probabilities.get(label));
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

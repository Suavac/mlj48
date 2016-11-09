package driver;

import java.util.*;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class Main {

    public static void main(final String... args) throws Exception {
///http://clear-lines.com/blog/post/Discretizing-a-continuous-variable-using-Entropy.aspx
        final PreprocessData ppd = new PreprocessData("owls15.csv");
        final HashMap<String, Attribute> attributes = ppd.getAttributes();
        ppd.getDecisionClass();
        final String attributeName = ppd.getAttributeNames().get(2);
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
        for (final Float value : bContin) {
            if (value < a.getThresholds().get(7)) {
                testingDiscretization.add("A");
            } else {
                testingDiscretization.add("B");
            }
        }

        System.out.println(testingDiscretization);

        calculateGainForPair(target.getValues(), testingDiscretization);
    }

    public static Float calculateGainForPair(final ArrayList<String> target, final ArrayList<String> attribute) {
        final float occurancessAB = attribute.size();
        final float occurrencesA = Collections.frequency(attribute, "A");
        final float occurrencesB = Collections.frequency(attribute, "B");
        final float probA = occurrencesA / occurancessAB;
        final float probB = occurrencesB / occurancessAB;
        final HashMap<String, Float> a = countOcc(target, attribute, "A");
        final HashMap<String, Float> b = countOcc(target, attribute, "B");
        System.out.println("occurancess AB = " + occurancessAB);
        System.out.println("occurrences A = " + occurrencesA);
        System.out.println("occurrences B = " + occurrencesB);
        System.out.println("probability A = " + probA);
        System.out.println("probability B = " + probB);
        System.out.println("OCCURANCES MATRIX A = " + a);
        System.out.println("OCCURANCES MATRIX B = " + b);

        //calculate entropy for each A or B
        float entropy = 0;
        final float occA = a.get("SnowyOwl") + a.get("BarnOwl") + a.get("LongEaredOwl");
        final float probOccA = occA / occurancessAB;
        //System.out.println(occA);
        //entropy -= (1 * (Math.log(1) / Math.log(2)));
        //entropy -= (0 * (Math.log(0) / Math.log(2)));
        //entropy -= (0 * (Math.log(0) / Math.log(2)));
        entropy *= probOccA;
        System.out.println("ENTROPY 1: " + entropy);

        float entropy2 = 0;
        final float occB = b.get("SnowyOwl") + b.get("BarnOwl") + b.get("LongEaredOwl");
        final float probOccA2 = occB / occurancessAB;
        //System.out.println(occA);
        // final float r = b.get("SnowyOwl") / (occurancessAB - 1);
        entropy2 -= (b.get("SnowyOwl") / (occB) * (Math.log(b.get("SnowyOwl") / (occB)) / Math.log(2)));
        entropy2 -= (b.get("BarnOwl") / (occB) * (Math.log(b.get("BarnOwl") / (occB)) / Math.log(2)));
        entropy2 -= (b.get("LongEaredOwl") / (occB) * (Math.log(b.get("LongEaredOwl") / (occB)) / Math.log(2)));
        System.out.println("ENTROPY before prob 2: " + entropy2);
        entropy2 *= probOccA2;
        System.out.println("Prob 2: " + probOccA2);
        System.out.println("ENTROPY 2: " + entropy2);

        final float occuranceReal1 = 45;
        final float occuranceRealAll = 45;

        float entropy3 = 0;
        entropy3 -= (occuranceReal1 / (occurancessAB) * (Math.log(occuranceReal1 / (occurancessAB)) / Math.log(2)));
        entropy3 -= (occuranceReal1 / (occurancessAB) * (Math.log(occuranceReal1 / (occurancessAB)) / Math.log(2)));
        entropy3 -= (occuranceReal1 / (occurancessAB) * (Math.log(occuranceReal1 / (occurancessAB)) / Math.log(2)));

        System.out.println("ENTROPY Target: " + entropy3);
        System.out.println("Gain Target 1 : " + (entropy3 - entropy2));
        return null;
    }

    public static HashMap<String, Float> countOcc(final ArrayList<String> target, final ArrayList<String> attribute, final String clas) {
        //get unique decision classes from the target
        final Set<String> uniqueValues = new HashSet<String>(target);
        final HashMap<String, Float> countMap = new HashMap<String, Float>();
        //initialise hashmap with decision classes as the keys
        for (final String className : uniqueValues) {
            countMap.put(className, (float) 0L);
        }
        // count occurrences of decision classes
        int i = 0;
        for (final String label : target) {
            if (attribute.get(i).equals(clas)) {
                if (!countMap.containsKey(label)) {
                    countMap.put(label, (float) 1L);
                } else {
                    Float count = countMap.get(label);
                    count = count + 1;
                    countMap.put(label, count);
                }
            }
            i++;
        }
        return countMap;
    }

    public static void createThresholdsForAttribute(final Attribute attribute) {
        final TreeSet<Float> uniqueValues = new TreeSet<Float>();
        for (final String value : attribute.getValues()) {
            uniqueValues.add(Float.parseFloat(value));
        }
        final ArrayList<Float> a = new ArrayList<Float>(uniqueValues);
        System.out.println(uniqueValues);
        final ArrayList<Float> t = new ArrayList<Float>();
        for (int i = 0; i < a.size() - 1; i++) {
            t.add((a.get(i) + a.get(i + 1)) / 2);
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

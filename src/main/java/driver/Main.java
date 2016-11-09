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

        // choose attribute
        final String attributeName = ppd.getAttributeNames().get(3);
        final Attribute a = attributes.get(attributeName);
        // choose target
        final Attribute targetAttribute = attributes.get("type");

        // convert attribute to numeric values
        a.convertCont();

        // Calculate Target Entropy
        float targetEntropy = calculateEntropy(targetAttribute.getValues());
        System.out.println("TARGET ENTROPY = " + targetEntropy);

        // Create thresholds for an attribute data
        createThresholdsForAttribute(attributes.get(attributeName));
        //System.out.println(a.getThresholds().size());
        //System.out.println(a.getThresholds());

        // Get values of an attribute as numerical data
        final ArrayList<Float> bContin = a.getValuesContinous();
//        ArrayList<String> testingDiscretization;
//
//        for(int i = 0;i <a.getThresholds().size();i++){
//            testingDiscretization = new ArrayList<String>();
//            for (final Float value : bContin) {
//                if (value < a.getThresholds().get(i)) {
//                    testingDiscretization.add("A");
//                } else {
//                    testingDiscretization.add("B");
//                }
//            }
//            calculateGainForPair(targetAttribute.getValues(), testingDiscretization, targetEntropy,a.getThresholds().get(i) );
//            System.out.println("_______________________________________");
//        }



        ///AFTER SHRIKING

        final ArrayList<Float> shrinkedData = new ArrayList<Float>();
        final ArrayList<String> shrinkedTarget = new ArrayList<String>();
        Attribute secondIterationValues = new Attribute("secondIteration");
        Attribute secondIterationTarget = new Attribute("secondIterationTarget");
        int uu=0;
        for(Float aui : bContin){
            if(aui > (float) 1.6){
                secondIterationValues.addValue(aui.toString());
                shrinkedData.add(aui);
                secondIterationTarget.addValue(targetAttribute.getValues().get(uu));
            }
            uu++;
        }
        secondIterationValues.convertCont();
        createThresholdsForAttribute(secondIterationValues);


        ArrayList<String> testingDiscretization;

        for(int i = 0;i <secondIterationValues.getThresholds().size();i++){
            testingDiscretization = new ArrayList<String>();
            for (final Float value : secondIterationValues.getValuesContinous()) {
                if (value < secondIterationValues.getThresholds().get(i)) {
                    testingDiscretization.add("A");
                } else {
                    testingDiscretization.add("B");
                }
            }
            calculateGainForPair(secondIterationTarget.getValues(), testingDiscretization, targetEntropy,secondIterationValues.getThresholds().get(i) );
            System.out.println("_______________________________________");
        }

    }

    public static Float calculateGainForPair(final ArrayList<String> target, final ArrayList<String> attribute, float targetEntropy, float threshold) {
        final float occurancessAB = attribute.size();
        final float occurrencesA = Collections.frequency(attribute, "A");
        final float occurrencesB = Collections.frequency(attribute, "B");
        final float probA = occurrencesA / occurancessAB;
        final float probB = occurrencesB / occurancessAB;

//        System.out.println("occurancess AB = " + occurancessAB);
//        System.out.println("occurrences A = " + occurrencesA);
//        System.out.println("occurrences B = " + occurrencesB);
//        System.out.println("probability A = " + probA);
//        System.out.println("probability B = " + probB);

        // Divide target into 2 groups 1. a < threshold , 2. a > threshold
        ArrayList<String> leftNode = new ArrayList<String>();
        ArrayList<String> rightNode = new ArrayList<String>();
        int i = 0;
        for(String value : attribute){
            if(value.equals("A")){
                leftNode.add(target.get(i));
            } else{
                rightNode.add(target.get(i));
            }
            i++;
        }

        float entropyA = calculateEntropy(leftNode);
        System.out.println("ENTROPY A = " + entropyA);
        float entropyB = calculateEntropy(rightNode);
        System.out.println("ENTROPY B = " + entropyB);

        //Calculate gain for the given threshold
        float gain = targetEntropy - (probA*entropyA) - (probB*entropyB);
        System.out.println("Threshold = " + threshold);
        System.out.println("INFORMATION GAIN = " + gain);



        final HashMap<String, Float> a = countOcc(target, attribute, "A");
        final HashMap<String, Float> b = countOcc(target, attribute, "B");
        System.out.println("OCCURANCES MATRIX A = " + a);
        System.out.println("OCCURANCES MATRIX B = " + b);

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
        //System.out.println(uniqueValues);
        final ArrayList<Float> t = new ArrayList<Float>();
        for (int i = 0; i < a.size() - 1; i++) {
            t.add((a.get(i) + a.get(i + 1)) / 2);
        }
        attribute.storeThresholds(t);
        //System.out.println(t);
    }


    public static Float calculateEntropy(final ArrayList<String> data) {
        float numberOfSamples = data.size();
        // count occurrences of each decision class
        final HashMap<String, Long> countMap = new HashMap<String, Long>();
        for (final String label : data) {
            if (!countMap.containsKey(label)) {
                countMap.put(label, 1L);
            } else {
                Long count = countMap.get(label);
                count = count + 1L;
                countMap.put(label, count);
            }
        }
        float entropy = 0;
        for (final String label : countMap.keySet()) {
            final float probabilityOfValue = (countMap.get(label) / numberOfSamples);
            entropy -= (probabilityOfValue * (Math.log(probabilityOfValue) / Math.log(2)));
        }
        return entropy;
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

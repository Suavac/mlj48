package driver;

import java.io.Serializable;
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
        //a.convertCont();

        // Calculate Target Entropy
        final float targetEntropy = calculateEntropy(targetAttribute.getValues());
        System.out.println("TARGET ENTROPY = " + targetEntropy);

        // Create thresholds for an attribute data
        createThresholdsForAttribute(attributes.get(attributeName));
        //System.out.println(a.getThresholds().size());
        //System.out.println(a.getThresholds());

        // Get values of an attribute as numerical data
        final ArrayList<? extends Serializable> bContin = (a.isContinuous()) ? a.getValues() : a.getValues();
        final Gain finalGain = getGain(a, targetAttribute, targetEntropy, bContin);
        final ArrayList<Integer> o = finalGain.getRedundant();
        //final ArrayList<Integer> p = finalGain.getRedundant();
        System.out.println(o);
        for (final int iooo : o) {
            a.remove(iooo);
            targetAttribute.remove(iooo);
        }


        createThresholdsForAttribute(attributes.get(attributeName));
        final float targetEntropy2 = targetEntropy - finalGain.getEntropy();
        final ArrayList<? extends Serializable> bContin2 = (a.isContinuous()) ? a.getValues() : a.getValues();
        final Gain finalGain2 = getGain(a, targetAttribute, targetEntropy2, bContin);
        final ArrayList<Integer> o2 = finalGain2.getRedundant();
        //final ArrayList<Integer> p = finalGain.getRedundant();
        System.out.println(o2);
        for (final int iooo : o) {
            a.remove(iooo);
            targetAttribute.remove(iooo);
        }

        //AFTER SHRIKING

//        final ArrayList<Float> shrinkedData = new ArrayList<Float>();
//        final ArrayList<String> shrinkedTarget = new ArrayList<String>();
//        final Attribute secondIterationValues = new Attribute("secondIteration");
//        final Attribute secondIterationTarget = new Attribute("secondIterationTarget");
//        int uu = 0;
//        for (final Serializable aui : bContin) {
//            if (Float.parseFloat(aui.toString()) > 0.8) {
//                secondIterationValues.addValue(aui.toString());
//                shrinkedData.add(Float.parseFloat(aui.toString()));
//                secondIterationTarget.addValue(targetAttribute.getValues().get(uu));
//            }
//            uu++;
//        }
//        secondIterationValues.convertCont();
//        createThresholdsForAttribute(secondIterationValues);
//
//
//        ArrayList<String> testingDiscretization;
//
//        for (int i = 0; i < secondIterationValues.getThresholds().size(); i++) {
//            testingDiscretization = new ArrayList<String>();
//            for (final Float value : secondIterationValues.getValuesContinous()) {
//                if (value < secondIterationValues.getThresholds().get(i)) {
//                    testingDiscretization.add("A");
//                } else {
//                    testingDiscretization.add("B");
//                }
//            }
//            calculateGainForPair(secondIterationTarget.getValues(), testingDiscretization, targetEntropy, secondIterationValues.getThresholds().get(i));
//            System.out.println("_______________________________________");
//        }

    }

    private static Gain getGain(final Attribute attribute, final Attribute classLabels, final float targetEntropy, final ArrayList<? extends Serializable> bContin) {
        ArrayList<String> testingDiscretization;

        Gain finalGain = new Gain();

        for (int i = 0; i < attribute.getThresholds().size(); i++) {
            testingDiscretization = new ArrayList<String>();


            for (final Serializable value : bContin) {
                if (Float.parseFloat(value.toString()) < attribute.getThresholds().get(i)) {
                    testingDiscretization.add("A");

                } else {
                    testingDiscretization.add("B");

                }

            }
            final Gain tmp = calculateGainForPair(classLabels.getValues(), testingDiscretization, targetEntropy, attribute.getThresholds().get(i));

            if (tmp.getGain() > finalGain.getGain()) {
                finalGain = tmp;
            }
            //System.out.println(indexListA.size() + " " + indexListA);
            //System.out.println(indexListB.size() + " " + indexListB);
            System.out.println("_______________________________________");
        }
        return finalGain;
    }

    public static Gain calculateGainForPair(final ArrayList<? extends Serializable> target, final ArrayList<String> attribute, final float targetEntropy, final float threshold) {
        final float occurancesAB = attribute.size();
        final float occurrencesA = Collections.frequency(attribute, "A");
        final float occurrencesB = Collections.frequency(attribute, "B");
        final float probA = occurrencesA / occurancesAB;
        final float probB = occurrencesB / occurancesAB;

//        System.out.println("occurancess AB = " + occurancessAB);
//        System.out.println("occurrences A = " + occurrencesA);
//        System.out.println("occurrences B = " + occurrencesB);
//        System.out.println("probability A = " + probA);
//        System.out.println("probability B = " + probB);

        // Divide target into 2 groups 1. a < threshold , 2. a > threshold
        final ArrayList leftNode = new ArrayList<String>();
        final ArrayList rightNode = new ArrayList<String>();
        int i = 0;

        final ArrayList<Integer> indexListA = new ArrayList<Integer>();
        final ArrayList<Integer> indexListB = new ArrayList<Integer>();


        for (final String value : attribute) {
            if (value.equals("A")) {
                leftNode.add(target.get(i));
                indexListA.add(i);
            } else {
                rightNode.add(target.get(i));
                indexListB.add(i);
            }
            i++;
        }


        final float entropyA = calculateEntropy(leftNode);
        System.out.println("ENTROPY A = " + entropyA);
        final float entropyB = calculateEntropy(rightNode);
        System.out.println("ENTROPY B = " + entropyB);

        //Calculate gain for the given threshold
        final float gain = targetEntropy - (probA * entropyA) - (probB * entropyB);
        System.out.println("Threshold = " + threshold);
        System.out.println("INFORMATION GAIN = " + gain);


        final HashMap<Serializable, Float> a = countDecisionClassLabels(target, attribute, "A");
        final HashMap<Serializable, Float> b = countDecisionClassLabels(target, attribute, "B");
        System.out.println("OCCURANCES MATRIX A = " + a);
        System.out.println("OCCURANCES MATRIX B = " + b);

        return new Gain(entropyA, entropyB, threshold, gain, a, b, indexListA, indexListB);
    }

    /**
     * Method counts occurrences of decision class labels for a given set of data
     *
     * @param decisionClassLabels
     * @param attribute
     * @param subset
     * @return
     */
    public static HashMap<Serializable, Float> countDecisionClassLabels(final ArrayList<? extends Serializable> decisionClassLabels, final ArrayList<String> attribute, final String subset) {

        final HashMap<Serializable, Float> countMap = new HashMap<Serializable, Float>();
        // Count occurrences of decision class labels
        int i = 0;
        for (final Serializable decisionClassLabel : decisionClassLabels) {
            if (attribute.get(i).equals(subset)) {
                if (!countMap.containsKey(decisionClassLabel)) {
                    countMap.put(decisionClassLabel, (float) 1L);
                } else {
                    Float count = countMap.get(decisionClassLabel);
                    count = count + 1;
                    countMap.put(decisionClassLabel, count);
                }
            }
            i++;
        }
        return countMap;
    }

    public static void createThresholdsForAttribute(final Attribute attribute) {
        // get unique values from the attribute's values collection
        final TreeSet<Float> uniqueValues = new TreeSet<Float>((Collection<? extends Float>) attribute.getValues());
        // convert to ArrayList
        final ArrayList<Float> a = new ArrayList<Float>(uniqueValues);
        final ArrayList<Float> thresholds = new ArrayList<Float>();
        // Calculate and store thresholds
        for (int i = 0; i < uniqueValues.size() - 1; i++) {
            thresholds.add((a.get(i) + a.get(i + 1)) / 2);
        }
        attribute.storeThresholds(thresholds);
    }


    public static Float calculateEntropy(final ArrayList<? extends Serializable> dataSet) {
        final float numberOfSamples = dataSet.size();
        // count occurrences of each decision class
        final HashMap<Serializable, Long> countMap = new HashMap<Serializable, Long>();
        for (final Serializable decisionClassLabel : dataSet) {
            if (!countMap.containsKey(decisionClassLabel)) {
                countMap.put(decisionClassLabel, 1L);
            } else {
                Long count = countMap.get(decisionClassLabel);
                count = count + 1L;
                countMap.put(decisionClassLabel, count);
            }
        }
        float ENTROPY = 0;
        for (final Serializable decisionClassLabel : countMap.keySet()) {
            final float probabilityOfValue = (countMap.get(decisionClassLabel) / numberOfSamples);
            ENTROPY -= (probabilityOfValue * (Math.log(probabilityOfValue) / Math.log(2)));
        }
        return ENTROPY;
    }

}

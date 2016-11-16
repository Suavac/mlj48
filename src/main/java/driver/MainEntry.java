package driver;

import java.io.Serializable;
import java.util.*;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class MainEntry {

    private final HashMap<String, MainEntry> tree = new HashMap<String, MainEntry>();

    public MainEntry(final HashMap<String, Attribute> attributes, final ArrayList<String> attributeNames, final Attribute targetAttribute) {


        Gain finalGain = null;

        finalGain = null;
        for (int i = 0; i < attributeNames.size(); i++) {
            // choose attribute
            final String attributeName = attributeNames.get(i);
            final Attribute attribute = attributes.get(attributeName);
            // Calculate Target Entropy
            final float targetEntropy = calculateEntropy(targetAttribute.getValues());
            //System.out.println("TARGET ENTROPY = " + targetEntropy);
            //getSubsetOfData(attributes);
            final Gain gainOfAnAttribute = getGain(attribute, targetAttribute, targetEntropy);
            //final ArrayList<Integer> o = finalGain.getRedundant();
            if (finalGain == null) {
                finalGain = gainOfAnAttribute;
            } else if (gainOfAnAttribute.getGain() >= finalGain.getGain()) {
                finalGain = gainOfAnAttribute;
            }

        }

        final ArrayList<Integer> indexesOfRedundantValues = finalGain.getRedundant();
        for (int j = 0; j < attributes.size() - 1; j++) {
            for (final int k : indexesOfRedundantValues) {
                final Attribute a = attributes.get(attributeNames.get(j));
                a.remove(k);
            }
        }
        for (final int k : indexesOfRedundantValues) {
            final Attribute a = attributes.get(targetAttribute.getName());
            a.remove(k);
        }

        System.out.println("Feature: " + finalGain.getAttributeName() + "\nGAIN: " + finalGain.getGain());
        final float mdlprinc = getMDL(finalGain);
        final float gain = finalGain.getGain();
        //System.out.println(mdlprinc);


        if (finalGain.getEntropy() > 0)
            this.tree.put(finalGain.getAttributeName(), new MainEntry(attributes, attributeNames, targetAttribute));


    }

    /**
     * Method calculates split criterion based on mdl principle
     * <p>
     * gain >= (1/N) x log2(N-1) + (1/N) x [ log2 ((3^|AuB|)-2) - ( |AuB| x Entropy(A+B) &ndash; |A| x Entropy(A) &ndash; |B| x Entropy(B) ]
     * where:
     * N - number of samples in the set
     * A - subset of values < threshold
     * B -  subset of values > threshold
     * |AuB| - number of possible class labels in entire set
     * |A| - in subset A
     * |B| - in subset B
     *
     * @param gain
     * @return boolean
     */
    public static float getMDL(final Gain gain) {

        final float gainValue = gain.getGain();
        final float entropyA = gain.getEntropyA();
        final float entropyB = gain.getEntropyB();
        final float entropyAB = gain.getEntropy();

        final float N = gain.indexListA.size() + gain.indexListB.size();
        final float A = gain.ossucranceA.size();
        final float B = gain.ossucranceB.size();
        // |AuB| - number of possible class labels in entire set
        final TreeSet<String> classesNames = new TreeSet<String>();
        final Set a = gain.ossucranceA.keySet();
        final Set b = gain.ossucranceB.keySet();
        classesNames.addAll(a);
        classesNames.addAll(b);
        final float AuB = classesNames.size();

        final float leftSideOfFormula = ((1 / N) * (float) (Math.log(N - 1) / Math.log(2))) +
                (1 / N) * ((float) (Math.log(Math.pow(3, AuB) - 2) / Math.log(2)) - (AuB * entropyAB) - (A * entropyA) - (B * entropyB));
        final boolean isSplit = (gainValue >= leftSideOfFormula);
        System.out.println("MDL: " + leftSideOfFormula + "\nsplit:" + isSplit + "\nThreshold " + gain.getThreshold() + "\n");
        return leftSideOfFormula;
    }

    public static HashMap<String, Attribute> getSubsetOfData(final HashMap<String, Attribute> attributes) {
        //System.out.println("MDL PRINCIPLE " + attributes.keySet());
        return null;
    }


    private static Gain getGain(final Attribute attribute, final Attribute classLabels, final float targetEntropy) {

        final ArrayList<? extends Serializable> attributeValues = attribute.getValues();
        ArrayList<String> testingDiscretization;

        // Create thresholds for an attribute data
        createThresholdsForAttribute(attribute);
        Gain finalGain = new Gain();
        final ArrayList<Float> thresholds = createThresholdsForAttribute(attribute);

        for (int i = 0; i < thresholds.size(); i++) {
            testingDiscretization = new ArrayList<String>();


            for (final Serializable value : attributeValues) {
                if (Float.parseFloat(value.toString()) < thresholds.get(i)) {
                    testingDiscretization.add("A");

                } else {
                    testingDiscretization.add("B");

                }

            }
            final Gain tmp = calculateGainForPair(classLabels.getValues(), attribute.getName(), testingDiscretization, targetEntropy, thresholds.get(i));

            if (tmp.getGain() > finalGain.getGain()) {
                finalGain = tmp;
            }
            //System.out.println(indexListA.size() + " " + indexListA);
            //System.out.println(indexListB.size() + " " + indexListB);
            //System.out.println("_______________________________________");
        }
        return finalGain;
    }

    public static Gain calculateGainForPair(final ArrayList<? extends Serializable> target, final String attributeName, final ArrayList<String> attribute, final float targetEntropy, final float threshold) {
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
        //System.out.println("ENTROPY A = " + entropyA);
        final float entropyB = calculateEntropy(rightNode);
        //System.out.println("ENTROPY B = " + entropyB);

        //Calculate gain for the given threshold
        final float gain = targetEntropy - (probA * entropyA) - (probB * entropyB);
        //System.out.println("Threshold = " + threshold);
        //System.out.println("INFORMATION GAIN = " + gain);


        final HashMap<Serializable, Float> a = countDecisionClassLabels(target, attribute, "A");
        final HashMap<Serializable, Float> b = countDecisionClassLabels(target, attribute, "B");
        //System.out.println("OCCURANCES MATRIX A = " + a);
        //System.out.println("OCCURANCES MATRIX B = " + b);

        return new Gain(attributeName, entropyA, entropyB, threshold, gain, a, b, indexListA, indexListB);
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

    public static ArrayList<Float> createThresholdsForAttribute(final Attribute attribute) {
        // get unique values from the attribute's values collection
        final TreeSet<Float> uniqueValues = new TreeSet<Float>((Collection<? extends Float>) attribute.getValues());
        // convert to ArrayList
        final ArrayList<Float> values = new ArrayList<Float>(uniqueValues);
        final ArrayList<Float> thresholds = new ArrayList<Float>();
        // Calculate and store thresholds
        for (int i = 0; i < uniqueValues.size() - 1; i++) {
            thresholds.add((values.get(i) + values.get(i + 1)) / 2);
        }

        return thresholds;
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

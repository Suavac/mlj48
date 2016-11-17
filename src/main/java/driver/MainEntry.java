package driver;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class MainEntry {

    private final HashMap<String, MainEntry> tree;
    private boolean isLeaf;
    private Gain gain;

    public MainEntry(final HashMap<String, Attribute> attributes, final ArrayList<String> attributeNames, final Attribute targetAttribute) {

        this.tree = new HashMap<String, MainEntry>();
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
        //final HashMap<String, Attribute> leftNode = finalGain.getRightSubset(attributes, attributeNames, targetAttribute);
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
        System.out.println(Pruning.getMDL(finalGain));

        if (Pruning.getMDL(finalGain)) {
            tree.put(finalGain.getAttributeName(), new MainEntry(attributes, attributeNames, attributes.get(targetAttribute.getName())));
        }

    }


    public static HashMap<String, Attribute> getSubsetOfData(final HashMap<String, Attribute> attributes) {
        //System.out.println("MDL PRINCIPLE " + attributes.keySet());
        return null;
    }


    private static Gain getGain(final Attribute attribute, final Attribute target, final float targetEntropy) {

        // Get threshold values of an attribute
        final ArrayList<Float> thresholds = createThresholdsForAttribute(attribute, target);

        Gain finalGain = new Gain();
        for (int i = 0; i < thresholds.size(); i++) {
            final Gain tmp = calculateGainForPair(target.getValues(), attribute.getName(), attribute.getValues(), targetEntropy, thresholds.get(i));
            if (tmp.getGain() >= finalGain.getGain()) {
                finalGain = tmp;
            }
        }
        return finalGain;
    }

    public static Gain calculateGainForPair(final ArrayList<? extends Serializable> targetValues, final String attributeName, final ArrayList<? extends Serializable> attributeValues, final float targetEntropy, final float threshold) {
        // holds index value of a sample
        final ArrayList<Integer> indexListA = new ArrayList<Integer>();
        final ArrayList<Integer> indexListB = new ArrayList<Integer>();
        // holds a sample's decision label
        final ArrayList leftNode = new ArrayList<String>();
        final ArrayList rightNode = new ArrayList<String>();

        //final TreeSet<Serializable> uniqueValues = new TreeSet<Serializable>(targetValues);
        // if (uniqueValues.size() > 1) {
        int i = 0;
        for (final Serializable value : attributeValues) {
            if (Float.parseFloat(value.toString()) <= threshold) {
                leftNode.add(targetValues.get(i));
                indexListA.add(i);
            } else {
                rightNode.add(targetValues.get(i));
                indexListB.add(i);
            }
            i++;
        }

        //}

        final float occurrencesA = indexListA.size();
        final float occurrencesB = indexListB.size();
        final float occurrencesAB = occurrencesA + occurrencesB;
        final float probA = occurrencesA / occurrencesAB;
        final float probB = occurrencesB / occurrencesAB;

        // Calculate subset's entropy
        final float entropyA = calculateEntropy(leftNode);
        final float entropyB = calculateEntropy(rightNode);

        //Calculate gain for the given threshold
        final float gain = targetEntropy - (probA * entropyA) - (probB * entropyB);

        final HashMap<Serializable, Float> decisionClassesLeft = countDecisionClassLabels(leftNode);
        final HashMap<Serializable, Float> decisionClassesRight = countDecisionClassLabels(rightNode);

        return new Gain(attributeName, entropyA, entropyB, threshold, gain, decisionClassesLeft, decisionClassesRight, indexListA, indexListB);
    }

    /**
     * Method counts occurrences of decision class labels for a given set of data
     *
     * @param decisionClassLabels
     * @return
     */
    public static HashMap<Serializable, Float> countDecisionClassLabels(final ArrayList<? extends Serializable> decisionClassLabels) {
        final HashMap<Serializable, Float> countMap = new HashMap<Serializable, Float>();
        // Count occurrences of decision class labels
        for (final Serializable decisionClassLabel : decisionClassLabels) {
            if (!countMap.containsKey(decisionClassLabel)) {
                countMap.put(decisionClassLabel, (float) 1L);
            } else {
                Float count = countMap.get(decisionClassLabel);
                count = count + 1;
                countMap.put(decisionClassLabel, count);
            }
        }
        return countMap;
    }

    public static ArrayList<Float> createThresholdsForAttribute(final Attribute attribute, final Attribute target) {
        final Multimap<String, Float> multiMap = LinkedListMultimap.create();
        for (int i = 0; i < target.getValues().size(); i++) {
            multiMap.put(target.getValues().get(i).toString(), Float.parseFloat(attribute.getValues().get(i).toString()));
        }

        /* our output, 'newArrayList()' is just a guava convenience function */
        final ArrayList<String> sortedTargets = Lists.newArrayList();
        final ArrayList<Float> sortedData = Lists.newArrayList();
        final ArrayList<Float> thresholds = Lists.newArrayList();
        Ordering.natural().sortedCopy(multiMap.keys());
        /* cycle through a sorted copy of the MultiMap's keys... */
        System.out.println(Ordering.natural().sortedCopy(multiMap.keys()));
        for (final String name : Ordering.natural().sortedCopy(multiMap.keys())) {
            //GET SIZE OF THE LIST
            /* ...and add all of the associated values to the lists */
            for (final Float value : multiMap.get(name)) {

                sortedTargets.add(name);
                sortedData.add(value);
            }
        }
        //System.out.println(sortedTargets);
        //System.out.println(sortedData);
        for (int i = 1; i < sortedTargets.size() - 1; i++) {
            if (!sortedTargets.get(i).equals(sortedTargets.get(i - 1))) {
                thresholds.add(sortedData.get(i));
//                System.out.println(sortedTargets.get(i));
//                System.out.println(sortedData.get(i - 1));
//                System.out.println(sortedData.get(i));
            }
        }
        final int i = 0;
//        // get unique values from the attribute's values collection
//        final TreeSet<Float> uniqueValues = new TreeSet<Float>((Collection<? extends Float>) attribute.getValues());
//        // convert to ArrayList
//        return new ArrayList<Float>(uniqueValues);
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

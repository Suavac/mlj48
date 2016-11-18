package driver;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class DecisionTree {

    private final HashMap treeNodes = Maps.newHashMap();
    private boolean isLeaf;
    private Gain gain;
    private String nodeName;

    public DecisionTree() {
    }

    public DecisionTree train(final PreprocessedData ppd) {
        final HashMap attributes = ppd.getAttributes();
        System.out.println(ppd.getAttributes().size());
        // choose target - assuming that target is a last column
        final Attribute targetAttribute = (Attribute) attributes.get(ppd.getTargetName());
        final ArrayList attributeNames = ppd.getAttributeNames();
        final Iterable dataSet = ppd.getDataRecords();
        final ArrayList instancesIndex = ppd.getInstancesIndex();
        return new DecisionTree(dataSet, instancesIndex, attributes, attributeNames, targetAttribute);
    }

    public File test(final PreprocessedData ppd) {
        return null;
    }


    private DecisionTree(final Iterable dataSet, final ArrayList instancesIndex, final HashMap attributes, final ArrayList attributeNames, final Attribute targetAttribute) {

        // Calculate Target Entropy
        //final double targetEntropy = calculateEntropy(targetAttribute.getValues());
        final double targetEntropy = calculateEntropy(dataSet, instancesIndex, targetAttribute);
        if (!(targetEntropy > 0)) {
            this.isLeaf = true;
            final CSVRecord instance = (CSVRecord) Iterables.get(dataSet, ((Long) instancesIndex.get(0)).intValue());
            this.nodeName = instance.get(targetAttribute.getName());
            System.out.println("\n------------------" + this.nodeName + "\n");
            return;
        }
        Gain finalGain = null;
        for (final Object attributeName : attributeNames) {
            // choose attribute
            final Attribute attribute = (Attribute) attributes.get(attributeName);
            //getSubsetOfData(attributes);
            final Gain gainOfAnAttribute = getGain(dataSet, instancesIndex, attribute, targetAttribute, targetEntropy);
//            final Gain gainOfAnAttribute = getGain(attribute, targetAttribute, targetEntropy);
            //final ArrayList<Integer> o = finalGain.getRedundant();
            if (finalGain == null) {
                finalGain = gainOfAnAttribute;
            } else if (gainOfAnAttribute.getGain() >= finalGain.getGain()) {
                finalGain = gainOfAnAttribute;
            }

        }
        //final HashMap leftNode = finalGain.getLeftSubset(attributes, attributeNames, targetAttribute);
        //final HashMap rightNode = finalGain.getRightSubset(attributes, attributeNames, targetAttribute);

        if (Pruning.getMDL(finalGain))
            System.out.println("\nFeature: " + finalGain.getAttributeName() +
                    "\nGAIN: " + finalGain.getGain() +
                    "\n THRESHOLD: " + finalGain.getThreshold() +
                    "\nSPLIT :" + Pruning.getMDL(finalGain) +
                    "");


        if (Pruning.getMDL(finalGain)) {
            this.gain = finalGain;
            this.nodeName = finalGain.getAttributeName();
            treeNodes.put(finalGain.getThreshold(), new DecisionTree(dataSet, finalGain.getIndexListA(), attributes, attributeNames, targetAttribute));
            treeNodes.put("right", new DecisionTree(dataSet, finalGain.getIndexListB(), attributes, attributeNames, targetAttribute));
        } else {
            this.gain = finalGain;
            this.isLeaf = true;
            this.nodeName = finalGain.getMostOccurringLabel();
            System.out.println("\n------------------" + finalGain.getMostOccurringLabel() + "\n");
        }

    }


    private static Gain getGain(final Iterable<CSVRecord> dataSet, final ArrayList instancesIndex, final Attribute attribute, final Attribute target, final double targetEntropy) {
        // Get threshold values of an attribute
        final ArrayList thresholds = createThresholdsForAttribute(dataSet, instancesIndex, attribute);
        Gain finalGain = new Gain();
        for (int i = 0; i < thresholds.size(); i++) {
            final Gain tmp = calculateGainForPair(dataSet, instancesIndex, attribute, target, targetEntropy, (Double) thresholds.get(i));
            if (tmp.getGain() >= finalGain.getGain()) {
                finalGain = tmp;
            }
        }
        return finalGain;
    }

    public static Gain calculateGainForPair(final Iterable<CSVRecord> dataSet, final ArrayList instancesIndex, final Attribute attribute, final Attribute target, final double targetEntropy, final double threshold) {
        // holds index value of a sample
        final ArrayList indexListA = Lists.newArrayList();
        final ArrayList indexListB = Lists.newArrayList();
        // holds a sample's decision label
        final ArrayList leftNode = Lists.newArrayList();
        final ArrayList rightNode = Lists.newArrayList();

        for (final CSVRecord instance : dataSet) {
            if (instancesIndex.contains(instance.getRecordNumber())) {
                final Double value = Double.valueOf(instance.get(attribute.getName()));
                final long instanceNumber = instance.getRecordNumber();
                final String label = instance.get(target.getName());
                if (value <= threshold) {
                    leftNode.add(label);
                    indexListA.add(instanceNumber);
                } else {
                    rightNode.add(label);
                    indexListB.add(instanceNumber);
                }
            }
        }

        final double occurrencesA = indexListA.size();
        final double occurrencesB = indexListB.size();
        final double occurrencesAB = occurrencesA + occurrencesB;
        final double probA = occurrencesA / occurrencesAB;
        final double probB = occurrencesB / occurrencesAB;

        // Calculate subset's entropy
        final double entropyA = calculateEntropy(dataSet, indexListA, target);
        final double entropyB = calculateEntropy(dataSet, indexListB, target);

        //Calculate gain for the given threshold
        final double gain = targetEntropy - (probA * entropyA) - (probB * entropyB);

        final HashMap decisionClassesLeft = countDecisionClassLabels(leftNode);
        final HashMap decisionClassesRight = countDecisionClassLabels(rightNode);

        return new Gain(attribute.getName(), entropyA, entropyB, threshold, gain, decisionClassesLeft, decisionClassesRight, indexListA, indexListB);
    }

    /**
     * Method counts occurrences of decision class labels for a given set of data
     *
     * @param decisionClassLabels
     * @return
     */
    public static HashMap countDecisionClassLabels(final ArrayList decisionClassLabels) {
        final HashMap countMap = Maps.newHashMap();
        // Count occurrences of decision class labels
        for (final Object decisionClassLabel : decisionClassLabels) {
            if (!countMap.containsKey(decisionClassLabel)) {
                countMap.put(decisionClassLabel, (double) 1L);
            } else {
                Double count = (Double) countMap.get(decisionClassLabel);
                count = count + 1;
                countMap.put(decisionClassLabel, count);
            }
        }
        return countMap;
    }

    public static ArrayList createThresholdsForAttribute(final Iterable<CSVRecord> dataSet, final ArrayList instancesIndex, final Attribute attribute) {
//        final Multimap<String, Double> multiMap = LinkedListMultimap.create();
//        for (int i = 0; i < target.getValues().size(); i++) {
//            multiMap.put(target.getValues().get(i).toString(), Double.parseDouble(attribute.getValues().get(i).toString()));
//        }
//
//        /* our output, 'newArrayList()' is just a guava convenience function */
//        final ArrayList<String> sortedTargets = Lists.newArrayList();
//        final ArrayList<Double> sortedData = Lists.newArrayList();
//        final ArrayList<Double> thresholds = Lists.newArrayList();
//        //Ordering.natural().sortedCopy(multiMap.keySet());
//        /* cycle through a sorted copy of the MultiMap's keys... */
//        //System.out.println(Ordering.natural().sortedCopy(multiMap.keys()));
//        for (final String name : Ordering.natural().sortedCopy(multiMap.keySet())) {
//            //GET SIZE OF THE LIST
//            /* ...and add all of the associated values to the lists */
//            for (final Double value : multiMap.get(name)) {
//
//                sortedTargets.add(name);
//                sortedData.add(value);
//            }
//        }
//        //System.out.println(sortedTargets);
//        //System.out.println(sortedData);
//        for (int i = 1; i < sortedTargets.size() - 1; i++) {
//            if (!sortedTargets.get(i).equals(sortedTargets.get(i - 1))) {
//                thresholds.add(sortedData.get(i-1));
////                System.out.println(sortedTargets.get(i));
////                System.out.println(sortedData.get(i - 1));
////                System.out.println(sortedData.get(i));
//            }
//        }
//        final int i = 0;
        // get unique values from the attribute's values collection
        final TreeSet uniqueValues = new TreeSet();
        for (final CSVRecord instance : dataSet) {
            if (instancesIndex.contains(instance.getRecordNumber())) {
                final Double thresholdValue = Double.valueOf(instance.get(attribute.getName()));
                uniqueValues.add(thresholdValue);
            }
        }

        return new ArrayList(uniqueValues);
    }

    public static Double calculateEntropy(final Iterable<CSVRecord> dataSet, final ArrayList instancesIndex, final Attribute targetAttribute) {
        final double numberOfSamples = instancesIndex.size();
        // count occurrences of each decision class
        final HashMap countMap = Maps.newHashMap();
        for (final CSVRecord instance : dataSet) {
            if (instancesIndex.contains(instance.getRecordNumber())) {
                final String decisionClassLabel = instance.get(targetAttribute.getName());
                if (!countMap.containsKey(decisionClassLabel)) {
                    countMap.put(decisionClassLabel, 1L);
                } else {
                    Long count = (Long) countMap.get(decisionClassLabel);
                    count = count + 1L;
                    countMap.put(decisionClassLabel, count);
                }
            }
        }
        double ENTROPY = 0;
        for (final Object decisionClassLabel : countMap.keySet()) {
            final double probabilityOfValue = (Double.parseDouble(countMap.get(decisionClassLabel).toString()) / numberOfSamples);
            ENTROPY -= (probabilityOfValue * (Math.log(probabilityOfValue) / Math.log(2)));
        }
        return ENTROPY;
    }

}

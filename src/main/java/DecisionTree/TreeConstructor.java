package DecisionTree;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import driver.Attribute;
import driver.Gain;
import driver.Pruning;
import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class TreeConstructor {

    Tree root;

    public TreeConstructor(final List<CSVRecord> dataSet, final HashMap attributes, final ArrayList attributeNames, final Attribute targetAttribute) {
        this.root = constructDecisionTree(dataSet, attributes, attributeNames, targetAttribute);
    }

    private static Tree constructDecisionTree(final List<CSVRecord> dataSet, final HashMap attributes, final ArrayList attributeNames, final Attribute targetAttribute) {
        // Calculate Target Entropy
        final double targetEntropy = calculateEntropy(dataSet, targetAttribute);
        if (!(targetEntropy > 0)) {
            //this.isLeaf = true;
            final CSVRecord instance = Iterables.get(dataSet, 0);
            //this.nodeName = instance.get(targetAttribute.getName());
            System.out.println("\n------------------" + instance.get(targetAttribute.getName()) + "\n");
            //dataSet.removeAll(dataSet);  // remove processed data
            return new Tree(instance.get(targetAttribute.getName()), true);
        }
        Gain finalGain = null;
        for (final Object attributeName : attributeNames) {
            // choose attribute
            final Attribute attribute = (Attribute) attributes.get(attributeName);
            final Gain gainOfAnAttribute = getGain(dataSet, attribute, targetAttribute, targetEntropy);
            if (finalGain == null) {
                finalGain = gainOfAnAttribute;
            } else if (gainOfAnAttribute.getGain() >= finalGain.getGain()) {
                finalGain = gainOfAnAttribute;
            }
        }

        if (Pruning.getSplitCriterion(finalGain))
            System.out.println("\nFeature: " + finalGain.getAttributeName() + "\nGAIN: " + finalGain.getGain() + "\nTHRESHOLD: " + finalGain.getThreshold() + "\nSPLIT :" + Pruning.getSplitCriterion(finalGain));
        dataSet.removeAll(dataSet);  // remove processed data
        final Tree rootNode;
        if (Pruning.getSplitCriterion(finalGain)) {
            rootNode = new Tree(finalGain, false);
            if (((Attribute) attributes.get(finalGain.getAttributeName())).isContinuous()) {
                rootNode.addChild(
                        constructDecisionTree(
                                finalGain.getLeftSubset(),
                                attributes,
                                attributeNames,
                                targetAttribute));
                rootNode.addChild(
                        constructDecisionTree(
                                finalGain.getRightSubset(),
                                attributes,
                                attributeNames,
                                targetAttribute));
            } else {
                // TODO -Deal with discrete Attribute
            }
        } else {
            System.out.println("\n------------------" + finalGain.getMostOccurringLabel() + "\n");
            return new Tree(finalGain.getMostOccurringLabel(), true);
        }
        return rootNode;
    }


    private static Gain getGain(final List<CSVRecord> dataSet, final Attribute attribute, final Attribute target, final double targetEntropy) {
        if (attribute.isContinuous()) {
            // Get threshold values of an attribute
            final ArrayList<String> thresholds = createThresholdsForAttribute(dataSet, attribute, target);
            Gain finalGain = new Gain();
            final int i = 0;
            for (final String threshold : thresholds) {
                final int j = 0;
                final Gain tmp = getContinuousAttributeGain(dataSet, attribute, target, targetEntropy, threshold);
                if (tmp.getGain() >= finalGain.getGain()) {
                    finalGain = tmp;
                }
            }
            return finalGain;
        } else {
            //TODO - support nominal data
            return null;
        }
    }

    public static Gain getContinuousAttributeGain(final List<CSVRecord> dataSet, final Attribute attribute, final Attribute target, final double targetEntropy, final String threshold) {
        // holds subsets of data, below and above the threshold
        final List instancesBelowThreshold = Lists.newArrayList();
        final List instancesAboveThreshold = Lists.newArrayList();

        for (final CSVRecord instance : dataSet) {
            final String value = instance.get(attribute.getName());
            if (new BigDecimal(value).compareTo(new BigDecimal(threshold)) <= 0) {
                instancesBelowThreshold.add(instance);
            } else {
                instancesAboveThreshold.add(instance);
            }
        }

        final double numInstancesBelowThreshold = instancesBelowThreshold.size();
        final double numInstancesAboveThreshold = instancesAboveThreshold.size();
        final double numInstances = numInstancesBelowThreshold + numInstancesAboveThreshold;
        final double probA = numInstancesBelowThreshold / numInstances;
        final double probB = numInstancesAboveThreshold / numInstances;

        // Calculate subset's entropy
        final double entropyA = calculateEntropy(instancesBelowThreshold, target);
        final double entropyB = calculateEntropy(instancesAboveThreshold, target);

        //Calculate gain for the given threshold
        final double gain = targetEntropy - (probA * entropyA) - (probB * entropyB);

        final HashMap decisionClassesLeft = countDecisionClassLabels(instancesBelowThreshold, target);
        final HashMap decisionClassesRight = countDecisionClassLabels(instancesAboveThreshold, target);

        return new Gain(attribute.getName(), entropyA, entropyB, threshold, gain, decisionClassesLeft, decisionClassesRight, instancesBelowThreshold, instancesAboveThreshold);
    }

    public static ArrayList createThresholdsForAttribute(final List<CSVRecord> dataSet, final Attribute attribute, final Attribute target) {
        // sort records in ascending order
        Collections.sort(dataSet, (r1, r2) -> r1.get(attribute.getName()).compareTo(r2.get(attribute.getName())));
        // extract thresholds
        final TreeSet thresholds = new TreeSet<>();
        for (int i = 1; i < dataSet.size() - 1; i++) {
            final String currentLabel = dataSet.get(i).get(target.getName());
            final String previousLabel = dataSet.get(i - 1).get(target.getName());
            if (!currentLabel.equals(previousLabel)) {
                final Object threshold = dataSet.get(i - 1).get(attribute.getName()).toString();
                thresholds.add(threshold);
            }
        }
        return new ArrayList<>(thresholds);
    }

    /**
     * Method calculates an entropy of a given set of data
     *
     * @param dataSet
     * @param target
     * @return
     */
    public static Double calculateEntropy(final List<CSVRecord> dataSet, final Attribute target) {
        final double numberOfSamples = dataSet.size();
        // count occurrences of each decision class
        final HashMap countMap = countDecisionClassLabels(dataSet, target);
        double ENTROPY = 0;
        for (final Object label : countMap.keySet()) {
            final double probabilityOfValue = (Double.parseDouble(countMap.get(label).toString()) / numberOfSamples);
            ENTROPY -= (probabilityOfValue * (Math.log(probabilityOfValue) / Math.log(2)));
        }
        return ENTROPY;
    }

    /**
     * Method counts occurrences of decision class labels in a given set of data
     *
     * @param dataSet
     * @param target
     * @return
     */
    public static HashMap countDecisionClassLabels(final List<CSVRecord> dataSet, final Attribute target) {
        final HashMap countMap = Maps.newHashMap();
        for (final CSVRecord instance : dataSet) {
            final String label = instance.get(target.getName()).toString();
            if (!countMap.containsKey(label)) {
                countMap.put(label, (double) 1L);
            } else {
                Double count = (Double) countMap.get(label);
                count = count + 1;
                countMap.put(label, count);
            }
        }
        return countMap;
    }

    public Tree getDecisionTree() {
        return root;
    }
}

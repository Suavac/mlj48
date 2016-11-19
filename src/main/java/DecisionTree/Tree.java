package DecisionTree;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import driver.Attribute;
import driver.Gain;
import driver.Pruning;
import org.apache.commons.csv.CSVRecord;

import java.util.*;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class Tree {

    //protected final HashMap treeNodes = Maps.newLinkedHashMap();
    //private boolean isLeaf;
    //private Gain gain;
    //private String nodeName;
    Node root;

    private Tree() {}

    public Tree(final List<CSVRecord> dataSet, final HashMap attributes, final ArrayList attributeNames, final Attribute targetAttribute) {
        Node rootNode = null;
        this.root = constructDecisionTree(rootNode, dataSet, attributes, attributeNames, targetAttribute);
    }

    private static Node constructDecisionTree(Node rootNode, final List<CSVRecord> dataSet, final HashMap attributes, final ArrayList attributeNames, final Attribute targetAttribute){
        // Calculate Target Entropy
        final double targetEntropy = calculateEntropy(dataSet, targetAttribute);
        if (!(targetEntropy > 0)) {
            //this.isLeaf = true;
            final CSVRecord instance = Iterables.get(dataSet, 0);
            //this.nodeName = instance.get(targetAttribute.getName());
            System.out.println("\n------------------" + instance.get(targetAttribute.getName()) + "\n");
            //dataSet.removeAll(dataSet);  // remove processed data
            return new Node(instance.get(targetAttribute.getName()), true);
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

        if (Pruning.getMDL(finalGain))
            System.out.println("\nFeature: " + finalGain.getAttributeName() +
                    "\nGAIN: " + finalGain.getGain() +
                    "\n THRESHOLD: " + finalGain.getThreshold() +
                    "\nSPLIT :" + Pruning.getMDL(finalGain) +
                    "");
        dataSet.removeAll(dataSet);  // remove processed data
        if (Pruning.getMDL(finalGain)) {
                rootNode = new Node(finalGain.getAttributeName(), false);
            if(((Attribute)attributes.get(finalGain.getAttributeName())).isContinuous()){
                rootNode.addChild(constructDecisionTree(rootNode, finalGain.getLeftSubset(), attributes, attributeNames, targetAttribute));
                rootNode.addChild(constructDecisionTree(rootNode, finalGain.getRightSubset(), attributes, attributeNames, targetAttribute));
            } else {
                // TODO -Deal with discrete Attribute
            }
        } else {
            //this.gain = finalGain;
            //this.isLeaf = true;
            //this.nodeName = finalGain.getMostOccurringLabel();
            System.out.println("\n------------------" + finalGain.getMostOccurringLabel() + "\n");
            System.out.println("------------------" + finalGain.getA());
            System.out.println("------------------" + finalGain.getB());
            return new Node(finalGain.getMostOccurringLabel(),true);
        }
        return rootNode;
    }



    private static Gain getGain(final List<CSVRecord> dataSet, final Attribute attribute, final Attribute target, final double targetEntropy) {
        if (attribute.isContinuous()) {
            // Get threshold values of an attribute
            final ArrayList thresholds = createThresholdsForAttribute((List<CSVRecord>) dataSet, attribute, target);
            Gain finalGain = new Gain();
            for (int i = 0; i < thresholds.size(); i++) {
                final Gain tmp = getContinuousAttributeGain(dataSet, attribute, target, targetEntropy, (Double) thresholds.get(i));
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

    public static Gain getContinuousAttributeGain(final List<CSVRecord> dataSet, final Attribute attribute, final Attribute target, final double targetEntropy, final double threshold) {
        // holds subsets of data, below and above the threshold
        final List instancesBelowThreshold = Lists.newArrayList();
        final List instancesAboveThreshold = Lists.newArrayList();

        for (final CSVRecord instance : dataSet) {
            final Double value = Double.valueOf(instance.get(attribute.getName()));
            if (value <= threshold) {
                instancesBelowThreshold.add(instance);
            } else {
                instancesAboveThreshold.add(instance);
            }
        }

        final double occurrencesA = instancesBelowThreshold.size();
        final double occurrencesB = instancesAboveThreshold.size();
        final double occurrencesAB = occurrencesA + occurrencesB;
        final double probA = occurrencesA / occurrencesAB;
        final double probB = occurrencesB / occurrencesAB;

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
        final TreeSet uniqueValues = new TreeSet();
        for (int i = 1; i < dataSet.size() - 1; i++) {
            final CSVRecord current = dataSet.get(i);
            final CSVRecord previous = dataSet.get(i - 1);
            final String currentLabel = current.get(target.getName());
            final String previousLabel = previous.get(target.getName());
            if (!currentLabel.equals(previousLabel)) {
                final Double threshold = Double.parseDouble(previous.get(attribute.getName()).toString());
                uniqueValues.add(threshold);
            }
        }
        return new ArrayList(uniqueValues);
    }

    /**
     * Method calculates an entropy of a given set of data
     *
     * @param dataSet
     * @param target
     * @return
     */
    public static Double calculateEntropy(final List<CSVRecord> dataSet, final Attribute target) {
        final double numberOfSamples = ((Collection<?>) dataSet).size();
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
}
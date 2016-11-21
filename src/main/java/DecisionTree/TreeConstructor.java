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

    public TreeConstructor(final List<CSVRecord> dataSet, final HashMap attributes, final Attribute targetAttribute) {
        this.root = constructDecisionTree(dataSet, attributes, targetAttribute);
    }
    //http://blog.takipi.com/benchmark-how-java-8-lambdas-and-streams-can-make-your-code-5-times-slower/ lambda performance
    private static Tree constructDecisionTree(final List<CSVRecord> dataSet, final HashMap<String, Attribute> attributes, final Attribute target) {
        // Calculate Target Entropy
        final double targetEntropy = calculateEntropy(dataSet, target);
        if (!(targetEntropy > 0)) {
            //this.isLeaf = true;b
            final CSVRecord instance = Iterables.get(dataSet, 0);
            //this.nodeName = instance.get(targetAttribute.getName());
            System.out.println("\n------------------" + instance.get(target.getName()) + "\n");
            //dataSet.removeAll(dataSet);  // remove processed data
            return new Tree(instance.get(target.getName()),target);
        }

        final Gain[] maxGain = {null};
        attributes.forEach((attributeName, attribute )-> {
            if(attribute.isTarget())
                return;
            final Gain gainOfAnAttribute = getGain(dataSet, attribute, target, targetEntropy);
            if (maxGain[0] == null) {
                maxGain[0] = gainOfAnAttribute;
            } else if (gainOfAnAttribute.getGain() >= maxGain[0].getGain()) {
                maxGain[0] = gainOfAnAttribute;
            }
        });

        if (Pruning.getSplitCriterion(maxGain[0]))
            System.out.println("\nFeature: " + maxGain[0].getAttributeName() + "\nGAIN: " + maxGain[0].getGain() + "\nTHRESHOLD: " + maxGain[0].getValue() + "\nSPLIT :" + Pruning.getSplitCriterion(maxGain[0]));
        dataSet.removeAll(dataSet);  // remove processed data
        final Tree rootNode;
        if (Pruning.getSplitCriterion(maxGain[0])) {
            rootNode = new Tree(maxGain[0]);
            if ((attributes.get(maxGain[0].getAttributeName())).isContinuous()) {
                rootNode.addChild(
                        constructDecisionTree(
                                maxGain[0].getLeftSubset(),
                                attributes,
                                target));
                rootNode.addChild(
                        constructDecisionTree(
                                maxGain[0].getRightSubset(),
                                attributes,
                                target));
            } else {
                // TODO -Deal with discrete Attribute
            }
        } else {
            System.out.println("\n------------------" + maxGain[0].getMostOccurringLabel() + "\n");
            return new Tree(maxGain[0].getMostOccurringLabel(), target);
        }
        return rootNode;
    }


    private static Gain getGain(final List<CSVRecord> dataSet, final Attribute attribute, final Attribute target, final double targetEntropy) {
        if (attribute.isContinuous()) {
            // Get threshold values of an attribute
            final ArrayList<String> thresholds = createThresholdsForAttribute(dataSet, attribute, target);
            final Gain[] finalGain = {new Gain()};
            thresholds.forEach( threshold -> {
                final Gain tmp = getContinuousAttributeGain(dataSet, attribute, target, targetEntropy, threshold);
                if (tmp.getGain() >= finalGain[0].getGain()) {
                    finalGain[0] = tmp;
                }
            });
            return finalGain[0];
        } else {
            //TODO - support discrete data
            return null;
        }
    }

    public static Gain getContinuousAttributeGain(final List<CSVRecord> dataSet, final Attribute attribute, final Attribute target, final double targetEntropy, final String threshold) {
        // holds subsets of data, below and above the threshold
        final List instancesBelowThreshold = Lists.newArrayList();
        final List instancesAboveThreshold = Lists.newArrayList();

        dataSet.forEach( instance -> {
            final String value = instance.get(attribute.getName());
                if (new BigDecimal(value).compareTo(new BigDecimal(threshold)) <= 0) {
                    instancesBelowThreshold.add(instance);
                } else {
                    instancesAboveThreshold.add(instance);
                }
        });

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

        return new Gain(attribute, entropyA, entropyB, threshold, gain, decisionClassesLeft, decisionClassesRight, instancesBelowThreshold, instancesAboveThreshold);
    }

    public static ArrayList createThresholdsForAttribute(final List<CSVRecord> dataSet, final Attribute attribute, final Attribute target) {
        // sort records in ascending order
        Collections.sort(dataSet, (r1, r2) -> r1.get(attribute.getName()).compareTo(r2.get(attribute.getName())));
        // extract thresholds
        final TreeSet thresholds = new TreeSet<>();
        final CSVRecord[] previousInstance = {dataSet.get(0)};
        dataSet.forEach( instance -> {
            final String previousLabel = previousInstance[0].get(target.getName());
            final String currentLabel = instance.get(target.getName());
            if (!currentLabel.equals(previousLabel)) {
                final Object threshold = previousInstance[0].get(attribute.getName()).toString();
                thresholds.add(threshold);
            }
            previousInstance[0] = instance;

        });
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
        final double[] entropy = {0};
        countMap.keySet().forEach( label -> {
            final double probabilityOfValue = (Double.parseDouble(countMap.get(label).toString()) / numberOfSamples);
            entropy[0] -= (probabilityOfValue * (Math.log(probabilityOfValue) / Math.log(2)));

        });
        return entropy[0];
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
        dataSet.forEach( instance -> {
            final String label = instance.get(target.getName()).toString();
            if (!countMap.containsKey(label)) {
                countMap.put(label, (double) 1L);
            } else {
                Double count = (Double) countMap.get(label);
                count = count + 1;
                countMap.put(label, count);
            }
        });
        return countMap;
    }

    public Tree getDecisionTree() {
        return root;
    }
}

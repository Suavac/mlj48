package driver;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class DecisionTreeOLD {
//
//    private final HashMap treeNodes = Maps.newHashMap();
//    private boolean isLeaf;
//    private Gain gain;
//    private String nodeName;
//
//    public DecisionTreeOLD() {
//    }
//
//    public DecisionTreeOLD train(final PreprocessedData ppd) {
//        final HashMap attributes = ppd.getAttributes();
//        System.out.println(ppd.getAttributes().size());
//        // choose target - assuming that target is a last column
//        final Attribute targetAttribute = (Attribute) attributes.get(ppd.getTargetName());
//        final ArrayList attributeNames = ppd.getAttributeNames();
//        return new DecisionTreeOLD(attributes, attributeNames, targetAttribute);
//    }
//
//    public File test(final PreprocessedData ppd) {
//        return null;
//    }
//
//
//    private DecisionTreeOLD(final HashMap attributes, final ArrayList attributeNames, final Attribute targetAttribute) {
//
//        // Calculate Target Entropy
//        final float targetEntropy = calculateEntropy(targetAttribute.getValues());
//        if (!(targetEntropy > 0)) {
//            this.isLeaf = true;
//            this.nodeName = (String) targetAttribute.getValues().get(0);
//            System.out.println("\n------------------" + targetAttribute.getValues().get(0) + "\n");
//            return;
//        }
//        Gain finalGain = null;
//        for (final Object attributeName : attributeNames) {
//            // choose attribute
//            final Attribute attribute = (Attribute) attributes.get(attributeName);
//            //getSubsetOfData(attributes);
//            final Gain gainOfAnAttribute = getGain(attribute, targetAttribute, targetEntropy);
//            //final ArrayList<Integer> o = finalGain.getRedundant();
//            if (finalGain == null) {
//                finalGain = gainOfAnAttribute;
//            } else if (gainOfAnAttribute.getGain() >= finalGain.getGain()) {
//                finalGain = gainOfAnAttribute;
//            }
//
//        }
//        final HashMap leftNode = finalGain.getLeftSubset(attributes, attributeNames, targetAttribute);
//        final HashMap rightNode = finalGain.getRightSubset(attributes, attributeNames, targetAttribute);
//
//        if (Pruning.getMDL(finalGain))
//            System.out.println("\nFeature: " + finalGain.getAttributeName() +
//                    "\nGAIN: " + finalGain.getGain() +
//                    "\n THRESHOLD: " + finalGain.getThreshold() +
//                    "\nSPLIT :" + Pruning.getMDL(finalGain) +
//                    "");
//
//
//        if (Pruning.getMDL(finalGain)) {
//            this.gain = finalGain;
//            this.nodeName = finalGain.getAttributeName();
//            treeNodes.put("left", new DecisionTreeOLD(leftNode, attributeNames, (Attribute) leftNode.get(targetAttribute.getName())));
//            treeNodes.put("right", new DecisionTreeOLD(rightNode, attributeNames, (Attribute) rightNode.get(targetAttribute.getName())));
//        } else {
//            this.gain = finalGain;
//            this.isLeaf = true;
//            this.nodeName = finalGain.getMostOccurringLabel();
//            System.out.println("\n------------------" + finalGain.getMostOccurringLabel() + "\n");
//        }
//
//    }
//
//
//    public static HashMap<String, Attribute> getSubsetOfData(final HashMap<String, Attribute> attributes) {
//        //System.out.println("MDL PRINCIPLE " + attributes.keySet());
//        return null;
//    }
//
//    private static Gain getGain(final Attribute attribute, final Attribute target, final float targetEntropy) {
//        // Get threshold values of an attribute
//        final ArrayList thresholds = createThresholdsForAttribute(attribute, target);
//        Gain finalGain = new Gain();
//        for (int i = 0; i < thresholds.size(); i++) {
//            final Gain tmp = calculateGainForPair(target.getValues(), attribute.getName(), attribute.getValues(), targetEntropy, (Float) thresholds.get(i));
//            if (tmp.getGain() >= finalGain.getGain()) {
//                finalGain = tmp;
//            }
//        }
//        return finalGain;
//    }
//
//    public static Gain calculateGainForPair(final ArrayList targetValues, final String attributeName, final ArrayList<? extends Serializable> attributeValues, final float targetEntropy, final float threshold) {
//        // holds index value of a sample
//        final ArrayList indexListA = Lists.newArrayList();
//        final ArrayList indexListB = Lists.newArrayList();
//        // holds a sample's decision label
//        final ArrayList leftNode = Lists.newArrayList();
//        final ArrayList rightNode = Lists.newArrayList();
//
//        //final TreeSet<Serializable> uniqueValues = new TreeSet<Serializable>(targetValues);
//        // if (uniqueValues.size() > 1) {
//        int i = 0;
//        for (final Serializable value : attributeValues) {
//            if (Float.parseFloat(value.toString()) <= threshold) {
//                leftNode.add(targetValues.get(i));
//                indexListA.add(i);
//            } else {
//                rightNode.add(targetValues.get(i));
//                indexListB.add(i);
//            }
//            i++;
//        }
//
//        //}
//
//        final float occurrencesA = indexListA.size();
//        final float occurrencesB = indexListB.size();
//        final float occurrencesAB = occurrencesA + occurrencesB;
//        final float probA = occurrencesA / occurrencesAB;
//        final float probB = occurrencesB / occurrencesAB;
//
//        // Calculate subset's entropy
//        final float entropyA = calculateEntropy(leftNode);
//        final float entropyB = calculateEntropy(rightNode);
//
//        //Calculate gain for the given threshold
//        final float gain = targetEntropy - (probA * entropyA) - (probB * entropyB);
//
//        final HashMap decisionClassesLeft = countDecisionClassLabels(leftNode);
//        final HashMap decisionClassesRight = countDecisionClassLabels(rightNode);
//
//        return new Gain(attributeName, entropyA, entropyB, threshold, gain, decisionClassesLeft, decisionClassesRight, indexListA, indexListB);
//    }
//
//    /**
//     * Method counts occurrences of decision class labels for a given set of data
//     *
//     * @param decisionClassLabels
//     * @return
//     */
//    public static HashMap countDecisionClassLabels(final ArrayList decisionClassLabels) {
//        final HashMap countMap = Maps.newHashMap();
//        // Count occurrences of decision class labels
//        for (final Object decisionClassLabel : decisionClassLabels) {
//            if (!countMap.containsKey(decisionClassLabel)) {
//                countMap.put(decisionClassLabel, (float) 1L);
//            } else {
//                Float count = (Float) countMap.get(decisionClassLabel);
//                count = count + 1;
//                countMap.put(decisionClassLabel, count);
//            }
//        }
//        return countMap;
//    }
//
//    public static ArrayList createThresholdsForAttribute(final Attribute attribute, final Attribute target) {
////        final Multimap<String, Float> multiMap = LinkedListMultimap.create();
////        for (int i = 0; i < target.getValues().size(); i++) {
////            multiMap.put(target.getValues().get(i).toString(), Float.parseFloat(attribute.getValues().get(i).toString()));
////        }
////
////        /* our output, 'newArrayList()' is just a guava convenience function */
////        final ArrayList<String> sortedTargets = Lists.newArrayList();
////        final ArrayList<Float> sortedData = Lists.newArrayList();
////        final ArrayList<Float> thresholds = Lists.newArrayList();
////        //Ordering.natural().sortedCopy(multiMap.keySet());
////        /* cycle through a sorted copy of the MultiMap's keys... */
////        //System.out.println(Ordering.natural().sortedCopy(multiMap.keys()));
////        for (final String name : Ordering.natural().sortedCopy(multiMap.keySet())) {
////            //GET SIZE OF THE LIST
////            /* ...and add all of the associated values to the lists */
////            for (final Float value : multiMap.get(name)) {
////
////                sortedTargets.add(name);
////                sortedData.add(value);
////            }
////        }
////        //System.out.println(sortedTargets);
////        //System.out.println(sortedData);
////        for (int i = 1; i < sortedTargets.size() - 1; i++) {
////            if (!sortedTargets.get(i).equals(sortedTargets.get(i - 1))) {
////                thresholds.add(sortedData.get(i-1));
//////                System.out.println(sortedTargets.get(i));
//////                System.out.println(sortedData.get(i - 1));
//////                System.out.println(sortedData.get(i));
////            }
////        }
////        final int i = 0;
//        // get unique values from the attribute's values collection
//        final TreeSet uniqueValues = new TreeSet(attribute.getValues());
//        // convert to ArrayList
//        return new ArrayList(uniqueValues);
////        return thresholds;
//    }
//
//
//    public static Float calculateEntropy(final ArrayList dataSet) {
//        final float numberOfSamples = dataSet.size();
//        // count occurrences of each decision class
//        final HashMap countMap = Maps.newHashMap();
//        for (final Object decisionClassLabel : dataSet) {
//            if (!countMap.containsKey(decisionClassLabel)) {
//                countMap.put(decisionClassLabel, 1L);
//            } else {
//                Long count = (Long) countMap.get(decisionClassLabel);
//                count = count + 1L;
//                countMap.put(decisionClassLabel, count);
//            }
//        }
//        float ENTROPY = 0;
//        for (final Object decisionClassLabel : countMap.keySet()) {
//            final float probabilityOfValue = (Float.parseFloat(countMap.get(decisionClassLabel).toString()) / numberOfSamples);
//            ENTROPY -= (probabilityOfValue * (Math.log(probabilityOfValue) / Math.log(2)));
//        }
//        return ENTROPY;
//    }

}

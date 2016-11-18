package driver;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Suavek on 11/11/2016.
 */
public class Gain {

    private String attributeName;
    private double entropyA;
    private double entropyB;
    private double threshold;
    private double informationGain;
    HashMap<String, Double> occurrenceA = Maps.newHashMap();
    HashMap<String, Double> occurrenceB = Maps.newHashMap();
    ArrayList indexListA;
    ArrayList indexListB;

    public ArrayList getIndexListA() {
        return indexListA;
    }

    public ArrayList getIndexListB() {
        return indexListB;
    }

    public Gain(final String attributeName, final double entropyA, final double entropyB, final double threshold, final double gain, final HashMap a, final HashMap b, final ArrayList indexListA, final ArrayList indexListB) {
        this.attributeName = attributeName;
        this.entropyA = entropyA;
        this.entropyB = entropyB;
        this.threshold = threshold;
        this.informationGain = gain;
        this.occurrenceA = a;
        this.occurrenceB = b;
        this.indexListA = indexListA;
        this.indexListB = indexListB;
    }

    public Gain() {

    }


//    public final HashMap getSubset(final HashMap attributes, final ArrayList attributeNames, final Attribute target) {
//
//        final ArrayList<Integer> indexesOfRedundantValues = getRedundant();
//        for (int j = 0; j < attributes.size() - 1; j++) {
//            for (final int k : indexesOfRedundantValues) {
//                final Attribute a = (Attribute) attributes.get(attributeNames.get(j));
//                a.remove(k);
//            }
//        }
//        for (final int k : indexesOfRedundantValues) {
//            final Attribute a = (Attribute) attributes.get(target.getName());
//            a.remove(k);
//        }
//
//
//        return null;
//    }
//
//    public ArrayList getRedundant() {
//        try {
//            if (entropyA < entropyB) {
//                Collections.sort(indexListA, Collections.reverseOrder());
//                return indexListA;
//            }
//            Collections.sort(indexListB, Collections.reverseOrder());
//        } catch (final Exception e) {
//
//        }
//        return indexListB;
//    }


    public double getGain() {
        return this.informationGain;
    }

    public double getEntropy() {
        return this.entropyB;
    }

    public double getEntropyA() {
        return entropyA;
    }

    public double getEntropyB() {
        return entropyB;
    }

    public double getThreshold() {
        return threshold;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getMostOccurringLabel() {
        String label = "";
        double occurrence = 0;

        for (final String l : occurrenceA.keySet()) {
            if (occurrenceA.get(l) > occurrence) {
                occurrence = occurrenceA.get(l);
                label = (String) l;
            }
        }
        for (final Serializable l : occurrenceB.keySet()) {
            if (occurrenceB.get(l) > occurrence) {
                label = (String) l;
            }
        }

        return label;
    }

//    public HashMap getLeftSubset(final HashMap attributes, final ArrayList attributeNames, final Attribute targetAttribute) {
//        return getDataSubset(attributes, attributeNames, targetAttribute, indexListB);
//    }
//
//    public HashMap<String, Attribute> getRightSubset(final HashMap attributes, final ArrayList attributeNames, final Attribute targetAttribute) {
//        return getDataSubset(attributes, attributeNames, targetAttribute, indexListA);
//    }
//
//    private HashMap getDataSubset(final HashMap<String, Attribute> attributes, final ArrayList<String> attributeNames, final Attribute targetAttribute, final ArrayList<Integer> indexList) {
//        // create copy of a list
//        final HashMap dataSubset = (HashMap) SerializationUtils.clone(attributes);
//        //create a sublist
//        try {
//            Collections.sort(indexList, Collections.reverseOrder());
//        } catch (final Exception e) {
//        }
//
//        final ArrayList<Integer> indexesOfRedundantValues = indexList;
//        for (int j = 0; j < dataSubset.size() - 1; j++) {
//            for (final int k : indexesOfRedundantValues) {
//                final Attribute tmp = (Attribute) dataSubset.get(attributeNames.get(j));
//                tmp.remove(k);
//            }
//        }
//        for (final int k : indexesOfRedundantValues) {
//            final Attribute tmp = (Attribute) dataSubset.get(targetAttribute.getName());
//            tmp.remove(k);
//        }
//        return dataSubset;
//    }

}

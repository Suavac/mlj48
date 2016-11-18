package driver;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;

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
    List indexListA;
    List indexListB;

    public List getLeftSubset() {
        return indexListA;
    }

    public List getRightSubset() {
        return indexListB;
    }
    public HashMap getA() {
        return occurrenceA;
    }
    public HashMap getB() {
        return occurrenceB;
    }

    public Gain(final String attributeName, final double entropyA, final double entropyB, final double threshold, final double gain, final HashMap a, final HashMap b, final List indexListA, final List indexListB) {
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
                label = l;
            }
        }
        for (final String l : occurrenceB.keySet()) {
            if (occurrenceB.get(l) > occurrence) {
                label =  l;
            }
        }
        return label;
    }

}

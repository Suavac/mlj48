package driver;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.csv.CSVRecord;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Suavek on 11/11/2016.
 */
public class Gain {

    private Attribute attribute;
    private double entropyA;
    private double entropyB;
    private double entropyAB;
    private String value;
    private double informationGain;
    HashMap<String, Double> occurrenceA = Maps.newHashMap();
    HashMap<String, Double> occurrenceB = Maps.newHashMap();
    List<CSVRecord> indexListA;
    List<CSVRecord> indexListB;

    List<List<CSVRecord>> subsets = Lists.newArrayList();
    Map<String, List<CSVRecord>> subsetsDiscrete;
    Map<String, Map> occurrencesOfLabelsInSubsets;
    Map subsetsEntropy;


    public Map getSubsetsDiscrete(){
        return this.subsetsDiscrete;
    }
    public Gain(final Attribute attribute, final double entropyA, final double entropyB, final String threshold, final double gain, final HashMap a, final HashMap b, final List indexListA, final List indexListB, double targetEntr) {
        this.attribute = attribute;
        this.entropyA = entropyA;
        this.entropyB = entropyB;
        this.value = threshold;
        this.informationGain = gain;
        this.occurrenceA = a;
        this.occurrenceB = b;
        this.indexListA = indexListA;
        this.indexListB = indexListB;
        this.subsets.add(indexListA);
        this.subsets.add(indexListB);
        this.entropyAB=targetEntr;
    }

    public Gain(){
    }

    public Gain(Attribute attribute, double[] gain, Map<String, List<CSVRecord>> subsets, Map<String, Map> occurrencesOfLabelsInSubsets, Map subsetsEntropy) {
        this.attribute = attribute;
        this.informationGain = gain[0];
        this.subsetsDiscrete = subsets;
        this.occurrencesOfLabelsInSubsets = occurrencesOfLabelsInSubsets;
        this.subsetsEntropy = subsetsEntropy;
    }

    public String getMostOccurringLabel() {
        final Map<String, Double> mergedMaps = Stream.concat(occurrenceA.entrySet().stream(), occurrenceB.entrySet().stream())
                .collect(Collectors.toMap(
                        entry -> entry.getKey(), // key
                        entry -> entry.getValue(), // value
                        (occurrenceA, occurrenceB) -> occurrenceA + occurrenceB) // merger
                );
        return Collections.max(mergedMaps.entrySet(), Map.Entry.comparingByValue()).getKey(); // key of the biggest value
    }

    public double getGain() {
        return this.informationGain;
    }

    public double getEntropyA() {
        return entropyA;
    }

    public double getEntropyB() {
        return entropyB;
    }
    public double getEntropyAB() {
        return entropyAB;
    }

    public String getValue() {
        return value;
    }

    public String getAttributeName() {
        return attribute.getName();
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public List getLeftSubset() {
        return indexListA;
    }

    public List getRightSubset() {
        return indexListB;
    }

    public List getSubsets() {
        return subsets;
    }


}

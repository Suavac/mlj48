package driver;

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

    private String attributeName;
    private double entropyA;
    private double entropyB;
    private String threshold;
    private double informationGain;
    HashMap<String, Double> occurrenceA = Maps.newHashMap();
    HashMap<String, Double> occurrenceB = Maps.newHashMap();
    List<CSVRecord> indexListA;
    List<CSVRecord> indexListB;
    private double value;

    public Gain() {
    }

    public Gain(final String attributeName, final double entropyA, final double entropyB, final String threshold, final double gain, final HashMap a, final HashMap b, final List indexListA, final List indexListB) {
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

    public String getThreshold() {
        return threshold;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public List getLeftSubset() {
        return indexListA;
    }

    public List getRightSubset() {
        return indexListB;
    }

    public String getMostOccurringLabel() {
        System.out.println("--------------------Occ A Key------------------" +occurrenceA.keySet()+"\n");
        System.out.println("--------------------Occ B Key------------------" +occurrenceB.keySet()+"\n");
        final Map<String, Double> mergedMaps = Stream.concat(occurrenceA.entrySet().stream(), occurrenceB.entrySet().stream())
                .collect(Collectors.toMap(
                        entry -> entry.getKey(), // key
                        entry -> entry.getValue(), // value
                        (occurrenceA, occurrenceB) -> occurrenceA + occurrenceB) // merger
                );
        //try{
        System.out.println("-----------------------------------------------\n" +mergedMaps.keySet() +"\n\n");
            return Collections.max(mergedMaps.entrySet(), Map.Entry.comparingByValue()).getKey(); // key of the biggest value
//        } catch (Exception e){
//            return "";
//        }

    }

    public double getValue() {
        return value;
    }
}

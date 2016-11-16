package driver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Suavek on 11/11/2016.
 */
public class Gain {
    private String attributeName;
    private float entropyA;
    private float entropyB;
    private float threshold;
    private float informationGain;
    HashMap<Serializable, Float> ossucranceA;
    HashMap<Serializable, Float> ossucranceB;
    ArrayList<Integer> indexListA;
    ArrayList<Integer> indexListB;

    public Gain(final String attributeName, final float entropyA, final float entropyB, final float threshold, final float gain, final HashMap<Serializable, Float> a, final HashMap<Serializable, Float> b, final ArrayList<Integer> indexListA, final ArrayList<Integer> indexListB) {
        this.attributeName = attributeName;
        this.entropyA = entropyA;
        this.entropyB = entropyB;
        this.threshold = threshold;
        this.informationGain = gain;
        this.ossucranceA = a;
        this.ossucranceB = b;
        this.indexListA = indexListA;
        this.indexListB = indexListB;
    }

    public Gain() {

    }

    public ArrayList<Integer> getRedundant() {
        try {
            if (entropyA < entropyB) {
                Collections.sort(indexListA, Collections.reverseOrder());
                return indexListA;
            }
            Collections.sort(indexListB, Collections.reverseOrder());
        } catch (final Exception e) {

        }
        return indexListB;
    }

    public float getGain() {
        return this.informationGain;
    }

    public float getEntropy() {
        return this.entropyB;
    }

    public float getEntropyA() {
        return entropyA;
    }

    public float getEntropyB() {
        return entropyB;
    }

    public float getThreshold() {
        return threshold;
    }

    public String getAttributeName() {
        return attributeName;
    }

    
}

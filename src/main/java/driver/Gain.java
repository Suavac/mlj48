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

    public Gain(final float entropyA, final float entropyB, final float threshold, final float gain, final HashMap<Serializable, Float> a, final HashMap<Serializable, Float> b, final ArrayList<Integer> indexListA, final ArrayList<Integer> indexListB) {
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
        Collections.reverse(indexListA);
        return indexListA;
    }

    public float getGain() {
        return this.informationGain;
    }

    public float getEntropy() {
        return this.entropyB;
    }
}

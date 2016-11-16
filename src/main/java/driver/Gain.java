package driver;

import org.apache.commons.lang.SerializationUtils;

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


    public final HashMap<String, Attribute> getSubset(HashMap<String, Attribute> attributes,final ArrayList<String> attributeNames, Attribute target) {

        final ArrayList<Integer> indexesOfRedundantValues = getRedundant();
        for (int j = 0; j < attributes.size() - 1; j++) {
            for (final int k : indexesOfRedundantValues) {
                final Attribute a = attributes.get(attributeNames.get(j));
                a.remove(k);
            }
        }
        for (final int k : indexesOfRedundantValues) {
            final Attribute a = attributes.get(target.getName());
            a.remove(k);
        }


        return null;
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

        public HashMap<String, Attribute> getLeftSubset(HashMap<String, Attribute> attributes, ArrayList<String> attributeNames, Attribute targetAttribute) {
        // create copy of a list
        HashMap<String, Attribute> leftNode = (HashMap<String, Attribute>) SerializationUtils.clone(attributes);
        //create a sublist
        Collections.sort(indexListB, Collections.reverseOrder());
        final ArrayList<Integer> indexesOfRedundantValues = indexListB;
        for (int j = 0; j < leftNode.size() - 1; j++) {
            for (final int k : indexesOfRedundantValues) {
                final Attribute tmp = leftNode.get(attributeNames.get(j));
                tmp.remove(k);
            }
        }
        for (final int k : indexesOfRedundantValues) {
            final Attribute tmp = leftNode.get(targetAttribute.getName());
            tmp.remove(k);
        }
        return leftNode;
    }

    public HashMap<String, Attribute> getRightSubset(HashMap<String, Attribute> attributes, ArrayList<String> attributeNames, Attribute targetAttribute) {
        // create copy of a list
        HashMap<String, Attribute> rightNode = (HashMap<String, Attribute>) SerializationUtils.clone(attributes);
        //create a sublist
        Collections.sort(indexListA, Collections.reverseOrder());
        final ArrayList<Integer> indexesOfRedundantValues = indexListA;
        for (int j = 0; j < rightNode.size() - 1; j++) {
            for (final int k : indexesOfRedundantValues) {
                final Attribute tmp = rightNode.get(attributeNames.get(j));
                tmp.remove(k);
            }
        }
        for (final int k : indexesOfRedundantValues) {
            final Attribute tmp = rightNode.get(targetAttribute.getName());
            tmp.remove(k);
        }
        return rightNode;
    }

}

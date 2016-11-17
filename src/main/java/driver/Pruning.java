package driver;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Suavek on 17/11/2016.
 */
public class Pruning {

    /**
     * Method calculates split criterion based on mdl principle
     * <p>
     * gain >= (1/N) x log2(N-1) + (1/N) x [ log2 ((3^|AuB|)-2) - ( |AuB| x Entropy(A+B) &ndash; |A| x Entropy(A) &ndash; |B| x Entropy(B) ]
     * where:
     * N - number of samples in the set
     * A - subset of values < threshold
     * B -  subset of values > threshold
     * |AuB| - number of possible class labels in entire set
     * |A| - in subset A
     * |B| - in subset B
     *
     * @param gain
     * @return boolean
     */
    public static boolean getMDL(final Gain gain) {

        final float gainValue = gain.getGain();
        final float entropyA = gain.getEntropyA();
        final float entropyB = gain.getEntropyB();
        final float entropyAB = gain.getEntropy();

        final float N = gain.indexListA.size() + gain.indexListB.size();
        final float A = gain.occurrenceA.size();
        final float B = gain.occurrenceB.size();
        // |AuB| - number of possible class labels in entire set
        final TreeSet<String> classesNames = new TreeSet<String>();
        final Set a = gain.occurrenceA.keySet();
        final Set b = gain.occurrenceB.keySet();
        classesNames.addAll(a);
        classesNames.addAll(b);
        final float AuB = classesNames.size();

        final float leftSideOfFormula = ((1 / N) * (float) (Math.log(N - 1) / Math.log(2))) +
                (1 / N) * ((float) (Math.log(Math.pow(3, AuB) - 2) / Math.log(2)) - (AuB * entropyAB) - (A * entropyA) - (B * entropyB));
        final boolean isSplit = (gainValue >= leftSideOfFormula);
        //System.out.println("MDL: " + leftSideOfFormula + "\nsplit:" + isSplit + "\nThreshold " + gain.getThreshold() + "\n");
        return gainValue >= leftSideOfFormula;
    }


}

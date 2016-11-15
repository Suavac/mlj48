package driver;

import org.apache.commons.lang.mutable.MutableBoolean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class Attribute {

    private final String name;
    private final ArrayList<Serializable> valuesAlphaNumeric = new ArrayList<Serializable>();
    private final ArrayList<Float> valuesNumeric = new ArrayList<Float>();
    private ArrayList<Float> thresholds = new ArrayList<Float>();

    private MutableBoolean isContinuous = null;

    public Attribute(final String name) {
        this.name = name;
    }

    /**
     * Method checks if attribute consists of continuous values
     *
     * @return boolaen
     */
    public boolean isContinuous() {
        if (this.isContinuous == null) {
            isContinuous = new MutableBoolean();
            for (final Serializable value : valuesAlphaNumeric) {
                try {
                    this.valuesNumeric.add(Float.parseFloat(String.valueOf(value)));
                    this.isContinuous.setValue(true);
                } catch (final NumberFormatException e) {
                    this.isContinuous.setValue(false);
                }
            }
        }
        return this.isContinuous.booleanValue();
    }

    public void addValue(final Serializable value) {
        valuesAlphaNumeric.add(value);
    }

    public final ArrayList<? extends Serializable> getValues() {
        if (isContinuous()) {
            return this.valuesNumeric;
        }
        return valuesAlphaNumeric;
    }

    public ArrayList<Float> getValuesContinous() {
        return valuesNumeric;
    }

    public void storeThresholds(final ArrayList<Float> thresholds) {
        this.thresholds = thresholds;
    }

    public ArrayList<Float> getThresholds() {
        return this.thresholds;
    }

    public void convertCont() {
        for (final Serializable value : valuesAlphaNumeric) {
            valuesNumeric.add(Float.parseFloat((String) value));
        }

    }

    public void remove(final int i) {
        try {
            valuesAlphaNumeric.remove(i);
            valuesNumeric.remove(i);
        } catch (final Exception e) {

        }

    }
}

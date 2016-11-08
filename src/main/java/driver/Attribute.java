package driver;

import java.util.ArrayList;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class Attribute {

    private String name;
    private ArrayList<String> values = new ArrayList<String>();
    private ArrayList<Float> valuesContinous = new ArrayList<Float>();
    private ArrayList<Float> thresholds = new ArrayList<Float>();

    public Attribute(final String name){
        this.name = name;
    }

    public void addValue(String value){
        values.add(value);
    }
    public ArrayList<String> getValues(){
        return values;
    }

    public ArrayList<Float> getValuesContinous(){
        return valuesContinous;
    }

    public void storeThresholds(ArrayList<Float> thresholds) {
        this.thresholds = thresholds;
    }

    public ArrayList<Float> getThresholds() {
        return this.thresholds;
    }

    public void convertCont(){
        for(String value : values){
            valuesContinous.add(Float.parseFloat(value));
        }

    }
}

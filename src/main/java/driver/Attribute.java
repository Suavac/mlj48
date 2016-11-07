package driver;

import java.util.ArrayList;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class Attribute {

    private String name;
    private ArrayList<String> values = new ArrayList<String>();

    public Attribute(final String name){
        this.name = name;
    }

    public void addValue(String value){
        values.add(value);
    }
    public ArrayList<String> getValues(){
        return values;
    }

}

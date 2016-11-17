package driver;

import org.apache.commons.lang.mutable.MutableBoolean;

import java.io.Serializable;

/**
 * Created by 12100888 on 17/11/2016.
 */
public class Attribute2 {

    private final String name;
    private final MutableBoolean isContinuous = new MutableBoolean();

    public Attribute2(String name){
        this.name = name;
    }

    public Attribute2(String name, boolean isContinuous){
        this.name = name;
        this.isContinuous.setValue(isContinuous);
    }

    public String getName(){
        return this.name;
    }

    public boolean isContinuous(){
        return this.isContinuous.booleanValue();
    }

}

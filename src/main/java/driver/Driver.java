package driver;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Suavek on 16/11/2016.
 */
public class Driver {


    public static void main(final String... args) throws Exception {

        ///http://clear-lines.com/blog/post/Discretizing-a-continuous-variable-using-Entropy.aspx
        final PreprocessedData ppd = new PreprocessedData("owls15.csv");
        final HashMap<String, Attribute> attributes = ppd.getAttributes();
        System.out.println(ppd.getAttributes().size());
        // choose target - assuming that target is a last column
        final Attribute targetAttribute = attributes.get(ppd.getTargetName());
        final ArrayList<String> attributeNames = ppd.getAttributeNames();


        final MainEntry tree = new MainEntry(attributes, attributeNames, targetAttribute);
        tree.toString();
        final String a = "sdsd";
        final int i = 0;
    }
}

package driver;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by 12100888 on 07/11/2016.
 */
public class Main {
    public static void main(String... args) throws IOException {

        // Get and read File
        ClassLoader classLoader = new Main().getClass().getClassLoader();
        File file = new File(classLoader.getResource("owls15.csv").getFile());
        Reader fileReader = new FileReader(file);

        // Parse file
        // Iterable<CSVRecord> instances  = csvFileParser;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);

        // Extract Attribute names
        ArrayList<String> attributeList = new ArrayList<String>(csvFileParser.getHeaderMap().keySet());

        HashMap<String, Attribute> atributes = new HashMap<String, Attribute>();
        for(String attributeName : attributeList){
            atributes.put(attributeName, new Attribute(attributeName));
        }

        extractAttributes(csvFileParser, attributeList,atributes);
        Attribute a = atributes.get("type");
        ArrayList<String> b = a.getValues();
        System.out.println(b);

    }
    //Extract values of each attribute
        public static void extractAttributes(CSVParser csvFileParser, ArrayList<String> attributeList, HashMap<String, Attribute> atributes){
            for (CSVRecord record : csvFileParser) {
                for(String attributeName : attributeList ){
                    Attribute tmp = atributes.get(attributeName);
                    tmp.addValue(record.get(attributeName));
                }

            }
        }
        // Extract values of each attribute
        //extractValues(csvFileParser, attributes.get(0));
//        System.out.println(extractValues(csvFileParser, attributes.get(1)));
//        System.out.println(extractValues(csvFileParser, attributes.get(2)));
//        for (CSVRecord record : csvFileParser) {
//            System.out.println(record.get(0) + " " + record.get(1));
//        }



        //List csvRecords = csvFileParser.getHeaderMap();
        //CSVRecord csvRecord = (CSVRecord) csvRecords.get(0);
        //System.out.println(attributes.get(0));




//        Reader in = new FileReader(file);
//        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
//
//        //get number and names of all attributes
//        ArrayList<String> attributes = new ArrayList<String>(records.iterator().next().toMap().keySet());
//
//
//        for(int i = 0; i< attributes.size(); i++){
//            System.out.println(attributes.get(i));
//        }
//        System.out.println();
//
//        for (CSVRecord record : records) {
//            //System.out.println(record.get(4));
//
//        }


//        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
//        FileReader fileReader = new FileReader(file);
//        CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);
//        //List csvRecords = csvFileParser.getHeaderMap();
//        //CSVRecord csvRecord = (CSVRecord) csvRecords.get(0);
//        System.out.println(csvFileParser.getHeaderMap().keySet());



//    }
//
//    public static ArrayList<String> extractValues(final CSVParser csvFileParser, final ArrayList<String> name){
//        ArrayList<String> list = new ArrayList<String>();
//        for (CSVRecord record : csvFileParser) {
//            list.add(record.get(name));
//        }
//        return list;
//    }

}

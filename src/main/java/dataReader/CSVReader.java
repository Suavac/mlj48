package dataReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12100888 on 08/11/2016.
 */
public class CSVReader {

    private final CSVParser csvFileParser; // this is object of type iterable
    private final List<CSVRecord> csvRecordList;

    public CSVReader(String filePath) throws IOException {
        // Get and read File
        //final File file = new File("/Users/jamesfallon/Documents/College Work/16_17/Machine Learning [CT475]/Assignment 3/mlj48/src/main/resources/owls15.csv");
        final File file = new File(getClass().getClassLoader().getResource(filePath).getFile());
        final Reader fileReader = new FileReader(file);
        // Parse file
        final CSVFormat csvFileFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        this.csvFileParser = new CSVParser(fileReader, csvFileFormat);
        this.csvRecordList = csvFileParser.getRecords();
    }

    public ArrayList getAttributeNames() {
        return new ArrayList(csvFileParser.getHeaderMap().keySet());
    }

    public List<CSVRecord> getDataSet() {
        return this.csvRecordList;
    }
}

package dataReader;

import driver.MainEntry;
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
    private final Iterable<CSVRecord> records;
    private final List<CSVRecord> csvRecordList;

    public CSVReader() throws IOException {
        // Get and read File
        //final ClassLoader classLoader = new MainEntry().getClass().getClassLoader();
        final File file = new File(getClass().getClassLoader().getResource("owls15.csv").getFile());
        final Reader fileReader = new FileReader(file);
        // Parse file
        // Iterable<CSVRecord> instances  = csvFileParser;
        final CSVFormat csvFileFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        this.csvFileParser = new CSVParser(fileReader, csvFileFormat);
        this.records = csvFileParser;
        this.csvRecordList = csvFileParser.getRecords();

    }

    public ArrayList<String> getAttributeNames() {
        return new ArrayList<String>(csvFileParser.getHeaderMap().keySet());
    }

    public Iterable<CSVRecord> getDataSet() {
        return this.csvRecordList;
    }
}

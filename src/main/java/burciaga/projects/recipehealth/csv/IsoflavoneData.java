package burciaga.projects.recipehealth.csv;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.AbstractRowProcessor;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by bmb0205 on 4/22/16.
 */
public class IsoflavoneData {

    // private class variable declaration
    private File outFile;
    private final String inFileName;
    private final Set<String> writeNdbSet = new HashSet<>();

    //  public constructor
    public IsoflavoneData(File outFile, String inFileName) {
        this.outFile = outFile;
        this.inFileName = inFileName;
    }

    // Returns new BufferedReader for each input file
    public BufferedReader getReader(String inFileName) throws Exception {
        return new BufferedReader(new FileReader(inFileName));
    }

    // Uses switch statement to set CsvWriter settings before returning CsvWriter object
    public CsvWriterSettings setWriterSettings() throws Exception {
        CsvWriterSettings writerSettings = new CsvWriterSettings();
        switch (this.inFileName) {
            case "ISO_DATA_SRC.csv":
                writerSettings.setHeaders("DataSrc_ID", "Title", "Year", "Journal");
                break;
            case "ISO_DATA_SRCLN.csv":
                writerSettings.setHeaders("NDB_No", "Nutr_No", "DataSrc_ID");
                break;
            case "ISO_NUTR_DEF.csv":
                writerSettings.setHeaders("Nutr_No", "NutrDesc", "Unit");
                break;
            case "ISO_ISFL_DAT.csv":
                writerSettings.setHeaders("NDB_No", "Nutr_No", "Isfl_Val", "CC");
                break;
            case "ISO_FOOD_DES.csv":
                writerSettings.setHeaders("NDB_No", "FdGrp_Cd", "Long_Desc");
                break;
            default:
                if (this.inFileName.endsWith(".out")) {
                    break;
                } else {
                    throw new IllegalArgumentException("Invalid file type!");
                }
        }
        writerSettings.getFormat().setDelimiter('|');
        return writerSettings;
    }

    // Returns new CsvWriter object using settings indicated in setWriterSettings()
    public CsvWriter generateCsvWriter() throws Exception {
        OutputStreamWriter outStreamWriter = new OutputStreamWriter(new FileOutputStream(this.outFile));
        CsvWriterSettings writerSettings = setWriterSettings();
        return new CsvWriter(outStreamWriter, writerSettings);
    }

    // Uses switch statement to set CsvParser settings before returning CsvParserSettings object
    public CsvParserSettings setParserSettings() throws Exception {
        CsvParserSettings parserSettings = new CsvParserSettings();
        switch (this.inFileName) {
            case "ISO_DATA_SRC.csv":
                parserSettings.setHeaders("DataSrc_ID", "Authors", "Title", "Year", "Journal", "Vol", "Start_Page", "End_Page");
                parserSettings.selectFields("DataSrc_ID", "Title", "Year", "Journal");
                break;
            case "ISO_DATA_SRCLN.csv":
                parserSettings.setHeaders("NDB_No", "Nutr_No", "DataSrc_ID");
                parserSettings.selectFields("NDB_No", "Nutr_No", "DataSrc_ID");
                break;
            case "ISO_NUTR_DEF.csv":
                parserSettings.setHeaders("Nutr_No", "NutrDesc", "Unit");
                parserSettings.selectFields("Nutr_No", "NutrDesc", "Unit");
                break;
            case "ISO_ISFL_DAT.csv":
                parserSettings.setHeaders("NDB_No", "Nutr_No", "Isfl_Val", "SD", "n", "Min", "Max", "CC", "DataSrc_ID");
                parserSettings.selectFields("NDB_No", "Nutr_No", "Isfl_Val", "CC");
                break;
            case "ISO_FOOD_DES.csv":
                parserSettings.setHeaders("NDB_No", "FdGrp_Cd", "Long_Desc");
                parserSettings.selectFields("NDB_No", "FdGrp_Cd", "Long_Desc");
                break;
            default:
                throw new IllegalArgumentException("Invalid file type!");
        }
        parserSettings.setNullValue("");
        parserSettings.setRowProcessor(createRowProcessor());
        return parserSettings;
    }

    /**
     * Returns new AbstractRowProcessor() after overriding rowProcessed() and processEnded()
     * UnivocityParser methods. Adds logic for whether row should be processed or written depending
     * on file input.
     */
    public RowProcessor createRowProcessor() throws Exception {
        final CsvWriter csvWriter = generateCsvWriter();
        System.out.println(writeNdbSet.size());

//        csvWriter.writeHeaders();
        return new AbstractRowProcessor() {

            @Override
            public void rowProcessed(String[] row, ParsingContext context) {
                StringBuilder builder = new StringBuilder();
                if (shouldWriteRow(row)) {
                    for (int i = 0; i < row.length; i++) {
                        row[i] = StringUtils.replace(row[i], "\"", "inches");
                    }
                    if ( inFileName.equals("ISO_DATA_SRCLN.csv")) {
                        builder.append(StringUtils.join(row, "|"));
                        builder.append("|");
                        builder.append(UUID.randomUUID());
                        csvWriter.writeRow(builder.toString());
                    } else {
                        String joined = StringUtils.join(row, "|");
                        csvWriter.writeRow(joined);
                    }
                } else {
                    // do nothing
                }
            }

            // add logic for if row should be written/filtered
            private boolean shouldWriteRow(String[] row) {
                String NDB_No;
//                System.out.println(inFileName);
                if (inFileName.equals("ISO_FOOD_DES.csv")) {
                    NDB_No = row[0];
                    writeNdbSet.add(NDB_No);
//                    System.out.println(writeNdbSet.size());
                    return true;
//                } else if ( inFileName.equals("ISO_DATA_SRCLN.csv")) {
//                    NDB_No = row[0];
////                    System.out.println(writeNdbSet.size());
//                    if (!writeNdbSet.contains(NDB_No)) {
//                        return false;
//                    } else {
////                        System.out.println(NDB_No);
//                        return true;
//                    }
                } else {
                    return true;
                }
            }

            @Override
            public void processEnded(ParsingContext context) {
                csvWriter.close();
            }
        };
    }

    public static void main(String[] args) throws Exception {

    }
}
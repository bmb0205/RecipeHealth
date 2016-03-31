package burciaga.projects.recipehealth;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.csv.*;
import com.univocity.parsers.common.processor.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * FlavonoidData class is called from ParseCSV class in ParseCSV.java.
 * This class sets CsvParser and CsvWriter settings for UniVocity Parser library, and
 * overrides the parser's row processing methods for custom CSV parsing
 * processor methods.
 */
public class FlavonoidData {

    // private class variable declaration
    private File outFile;
    private final String inFileName;


    //  public constructor
    public FlavonoidData(File outFile, String inFileName) {
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
            case "FLAV_DAT.txt":
                writerSettings.setHeaders("NDB_No", "Nutr_No", "Flav_Val", "CC");
                break;
            case "FLAV_IND.txt":
                writerSettings.setHeaders("NDB_No", "DataSrc_ID", "Food_No", "Food_Indiv_Desc", "Cmpd_Val");
                break;
            case "NUTR_DEF.txt":
                writerSettings.setHeaders("Nutr_No", "Description", "Flav_CLass", "Unit");
                break;
            case "FOOD_DES.txt":
                writerSettings.setHeaders("NDB_No", "FdGrp_Cd", "Long_Desc");
                break;
            case "FD_GROUP.txt":
                writerSettings.setHeaders("FdGrp_Cd", "FdGrp_Desc");
                break;
            case "DATA_SRC.txt":
                writerSettings.setHeaders("DataSrc_ID", "Title", "Year", "Journal");
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
            case "FLAV_DAT.txt":
                parserSettings.setHeaders("NDB_No", "Nutr_No", "Flav_Val", "SE", "n", "Min", "Max", "CC");
                parserSettings.selectFields("NDB_No", "Nutr_No", "Flav_Val", "CC");
                break;
            case "FLAV_IND.txt":
                parserSettings.setHeaders("NDB_No", "DataSrc_ID", "Food_No", "Food_Indiv_Desc", "Method", "Cmpd_Name",
                        "Rptd_CmpdVal", "Rptd_StdDev", "Num_Data_Pts", "LT", "Fresh_Dry_Wt", "Rptd_Units",
                        "Quant_Std", "Conv_Factor_G", "Conv_Factor_M", "Conv_Factor_SpGr", "Cmpd_Val",
                        "Cmpd_StdDev");
                parserSettings.selectFields("NDB_No", "DataSrc_ID", "Food_No", "Food_Indiv_Desc", "Cmpd_Name", "Cmpd_Val");
                break;
            case "NUTR_DEF.txt":
                parserSettings.setHeaders("Nutr_No", "Description", "Flav_Class", "Tag_Name", "Unit");
                parserSettings.selectFields("Nutr_No", "Description", "Flav_Class", "Unit");
                break;
            case "FOOD_DES.txt":
                parserSettings.setHeaders("NDB_No", "FdGrp_Cd", "Long_Desc");
                parserSettings.selectFields("NDB_No", "FdGrp_Cd", "Long_Desc");
                break;
            case "FD_GROUP.txt":
                parserSettings.setHeaders("FdGrp_Cd", "FdGrp_Desc");
                parserSettings.selectFields("FdGrp_Cd", "FdGrp_Desc");
                break;
            case "DATA_SRC.txt":
                parserSettings.setHeaders("DataSrc_ID", "Authors", "Title", "Year", "Journal", "Vol", "Start_Page", "End_Page");
                parserSettings.selectFields("DataSrc_ID", "Title", "Journal", "Year");
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
        csvWriter.writeHeaders();
        return new AbstractRowProcessor() {

            @Override
            public void rowProcessed(String[] row, ParsingContext context) {
                if (shouldWriteRow(row)) {
                    String joined = StringUtils.join(row, "|");
                    csvWriter.writeRow(joined);
                } else {
                    // do nothing
                }
            }

            private boolean shouldWriteRow(String[] row) {
                if (inFileName.equals("FLAV_IND.txt")) {
                    if (!row[5].equals("0.00")) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (inFileName.equals("FLAV_DAT.txt")) {
                    return true;
                } else if (inFileName.equals("NUTR_DEF.txt")) {
                    return true;
                } else if (inFileName.equals("FOOD_DES.txt")) {
                    return true;
                } else if (inFileName.equals("FD_GROUP.txt")) {
                    return true;
                } else if (inFileName.equals("DATA_SRC.txt")) {
                    return true;
            } else {
                    return true;
            }
        }

//
            @Override
            public void processEnded(ParsingContext context) {
                csvWriter.close();
            }
        };
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (!(obj instanceof FlavanoidData))
//            return false;
//        if (obj == this)
//            return true;
//
//        FlavanoidData fd = (FlavanoidData) obj;
//        return new EqualsBuilder().
//    }
//
//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder(5, 11).
//                append(inFileName).
//                append("FLAV_IND.txt").
//                toHashCode();
//    }

    public static void main(String[] args) throws Exception {

    }
}

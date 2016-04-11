package burciaga.projects.recipehealth;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.AbstractRowProcessor;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

/**
 * StandardReference class is called from ParseCSV class in ParseCSV.java.
 * This class sets CsvParser and CsvWriter settings for UniVocity Parser library, and
 * overrides the parser's row processing methods for custom CSV parsing
 * processor methods.
 */
public class StandardReferenceData {

    // private class variable declaration
    private final File outFile;
    private final String inFileName;


    //  public constructor
    public StandardReferenceData(File outFile, String inFileName) {
        this.outFile = outFile;
        this.inFileName = inFileName;
    }

    // Returns new BufferedReader for each input file
    public BufferedReader getReader(String inFileName) throws Exception {
        return new BufferedReader(new InputStreamReader(new FileInputStream(inFileName), "UTF-8"));
    }

    // Uses switch statement to set CsvWriter settings before returning CsvWriter object
    public CsvWriterSettings setWriterSettings() throws Exception {
        CsvWriterSettings writerSettings = new CsvWriterSettings();
        switch (this.inFileName) {
            case "SR_FOOD_DES.txt":
                writerSettings.setHeaders("NDB_No", "FdGrp_Cd", "Long_Desc");
                break;
            case "SR_NUT_DATA.txt":
                writerSettings.setHeaders("NDB_No", "Nutr_No", "Nutr_Val");
                break;
            case "SR_NUTR_DEF.txt":
                writerSettings.setHeaders("Nutr_No", "Units", "NutrDesc");
                break;
            case "SR_WEIGHT.txt":
                writerSettings.setHeaders("NDB_No", "Amount", "Msre_Desc", "Gm_Wgt");
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
        OutputStreamWriter outStreamWriter = new OutputStreamWriter(new FileOutputStream(this.outFile), "UTF-8");
        CsvWriterSettings writerSettings = setWriterSettings();
        return new CsvWriter(outStreamWriter, writerSettings);
    }

    // Uses switch statement to set CsvParser settings before returning CsvParserSettings object
    public CsvParserSettings setParserSettings() throws Exception {
        CsvParserSettings parserSettings = new CsvParserSettings();
        switch (this.inFileName) {
            case "SR_FOOD_DES.txt":
                parserSettings.setHeaders("NDB_No", "FdGrp_Cd", "Long_Desc", "Shrt_Desc", "ComName", "ManufacName",
                        "Survey", "Ref_desc", "Refuse", "SciName", "N_Factor", "Pro_Factor", "Fat_Factor", "CHO_Factor");
                parserSettings.selectFields("NDB_No", "FdGrp_Cd", "Long_Desc");
                break;
            case "SR_NUT_DATA.txt":
                parserSettings.setHeaders("NDB_No", "Nutr_No", "Nutr_Val", "Num_Data_Pts", "Std_Error", "Src_Cd",
                        "Deriv_Cd", "Ref_NDB_No", "Add_Nutr_Mark", "Num_Studies", "Min", "Max", "DF", "Low_EB", "Up_EB",
                        "Stat_cmt", "AddMod_Date", "CC");
                parserSettings.selectFields("NDB_No", "Nutr_No", "Nutr_Val");
                break;
            case "SR_NUTR_DEF.txt":
                parserSettings.setHeaders("Nutr_No", "Units", "Tagname", "NutrDesc", "Num_Desc", "SR_Order");
                parserSettings.selectFields("Nutr_No", "Units", "NutrDesc");
                break;
            case "SR_WEIGHT.txt":
                parserSettings.setHeaders("NDB_No", "Seq", "Amount", "Msre_Desc", "Gm_Wgt", "Num_Data_Pts");
                parserSettings.selectFields("NDB_No", "Amount", "Msre_Desc", "Gm_Wgt");
                break;
            default:
                if (this.inFileName.endsWith(".out")) {
                    break;
                } else {
                    throw new IllegalArgumentException("Invalid file type!");
                }
        }
        parserSettings.setNullValue("");
        CsvFormat parserFormat = parserSettings.getFormat();
        parserFormat.setDelimiter('^');
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
                    for (int i = 0; i < row.length; i++) {
                        row[i] = StringUtils.strip(row[i], "~");
                        row[i] = StringUtils.strip(row[i], "\"");
                    }
                    String joined = StringUtils.join(row, "|");
                    csvWriter.writeRow(joined);
                } else {
                    // do nothing
                }
            }

            private boolean shouldWriteRow(String[] row) {
                if (inFileName.equals("SR_FOOD_DES.txt")) {
                    return true;
                } else if (inFileName.equals("SR_NUT_DATA.txt")) {
                    return true;
                } else if (inFileName.equals("SR_NUTR_DEF.txt")) {
                    return true;
                } else if (inFileName.equals("SR_WEIGHT.txt")) {
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
}

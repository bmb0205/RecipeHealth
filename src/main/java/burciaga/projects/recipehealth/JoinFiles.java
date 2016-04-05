package burciaga.projects.recipehealth;

import com.google.common.collect.*;
import com.univocity.parsers.csv.*;
import org.apache.commons.lang3.StringUtils;


import java.io.*;
import java.util.*;

/**
 * Created by bmb0205 on 3/31/16.
 */
public class JoinFiles {

    // private class variable declaration
    private final List<String> joinedFileList;
    private final String file1;
    private final String file2;

    // public constructor
    public JoinFiles(List<String> joinedFileList) throws FileNotFoundException, UnsupportedEncodingException {
        this.joinedFileList = joinedFileList;
        this.file1 = joinedFileList.get(0);
        this.file2 = joinedFileList.get(1);
    }

    private List<String> getJoinedFileList() {
        return this.joinedFileList;
    }

    private String getFile1() {
        return this.file1;
    }

    private String getFile2() {
        return this.file2;
    }

    // Returns new BufferedReader for each input file
    private BufferedReader getReader(String inFileName) throws Exception {
        return new BufferedReader(new InputStreamReader(new FileInputStream(inFileName), "UTF-8"));
    }

    public void parseFiles() throws Exception {

        // SR_NUT_DATA.txt.out or FLAV_DAT.txt.out
        String fileString1 = getFile1();
        CsvParser dataParser1 = getFileParser1();
        dataParser1.beginParsing(getReader(fileString1));

        // SR_NUTR_DEF.txt.out or FLAV_NUTR_DEF.txt.out
        String fileString2 = getFile2();
        CsvParser dataParser2 = getFileParser2();
        dataParser2.beginParsing(getReader(fileString2));

        CsvWriter csvWriter = getCsvWriter();

        System.out.println("beep");
        String[] row;
        SortedSetMultimap<String, String> nutrMap = TreeMultimap.create();
        while ((row = dataParser1.parseNext()) != null) { // SR_NUT_DATA.txt.out or FLAV_DAV.txt.out
            String Nutr_No = row[0];
            for ( int i = 1; i <= row.length - 1; i++) {
                nutrMap.put(Nutr_No, row[i]);
            }
        }
        System.out.println("boooop");

        String[] dataRow;
        while ((dataRow = dataParser2.parseNext()) != null) { // SR_NUTR_DEF.txt.out or FLAV_NUTR_DEF.txt.out
            List<String> dataList = new ArrayList<>();
            dataList.addAll(Arrays.asList(dataRow).subList(0, dataRow.length));
            SortedSet<String> myAttributes = nutrMap.get(dataRow[1]);
            for (String attr : myAttributes) {
                dataList.add(attr);
            }
            String dataString = StringUtils.join(dataList, '|');
//            System.out.println(dataString);
            csvWriter.writeRow(dataString);
        }
        csvWriter.close();
    }

    public CsvWriter getCsvWriter() throws IOException {
        CsvWriterSettings writerSettings = new CsvWriterSettings();
        String outFileString;
        if (this.file1.endsWith("FLAV_NUTR_DEF.txt.out")) {
            writerSettings.setHeaders("NDB_No", "Nutr_No", "Flav_Val", "Units", "Description");
            outFileString = "/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FLAV_MERGED.txt.out";
        } else {
            writerSettings.setHeaders("NDB_No", "Nutr_No", "Nutr_Val", "Units", "NutrDesc");
            outFileString = "/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_MERGED.txt.out";
        }
        System.out.println(this.file1);
        System.out.println(this.file2);
        System.out.println(outFileString);
        writerSettings.getFormat().setDelimiter('|');
        OutputStreamWriter outStreamWriter = new OutputStreamWriter(new FileOutputStream(new File(outFileString)));
        return new CsvWriter(outStreamWriter, writerSettings);
    }

    public CsvParser getFileParser1() throws IOException { // SR_NUT_DATA.txt.out or FLAV_DAV.txt.out

        CsvParserSettings parserSettings1 = new CsvParserSettings();
        if (this.file1.endsWith("FLAV_DAT.txt.out")) {
            parserSettings1.setHeaders("NDB_No", "Nutr_No", "Flav_Val", "CC");
            parserSettings1.selectFields("NDB_No", "Nutr_No", "Flav_Val", "CC");

        } else { //
            parserSettings1.setHeaders("NDB_No", "Nutr_No", "Nutr_Val");
            parserSettings1.selectFields("NDB_No", "Nutr_No", "Nutr_Val");
        }
        parserSettings1.setNullValue("");
        CsvFormat parserFormat = parserSettings1.getFormat();
        parserFormat.setDelimiter('|');
        return new CsvParser(parserSettings1);
    }

    public CsvParser getFileParser2() { // SR_NUTR_DEF.txt.out or FLAV_NUTR_DEF.txt.out

        CsvParserSettings parserSettings2 = new CsvParserSettings();
//        System.out.println(this.file2);
        if (this.file2.endsWith("SR_NUTR_DEF.txt.out")) {
            parserSettings2.setHeaders("Nutr_No", "Units", "NutrDesc");
            parserSettings2.selectFields("Nutr_No", "Units", "NutrDesc");
        } else { // NUTR_DEF.txt.out
            parserSettings2.setHeaders("Nutr_No", "Description", "Flav_Class", "Units");
            parserSettings2.selectFields("Nutr_No", "Description", "Flav_Class", "Units");
        }
        parserSettings2.setNullValue("");
        CsvFormat parserFormat = parserSettings2.getFormat();
        parserFormat.setDelimiter('|');
        return new CsvParser(parserSettings2);
    }

    public static void main(String[] args) throws Exception {

    }

}



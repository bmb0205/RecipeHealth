package burciaga.projects.recipehealth;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

/**
 * ParseCSV class Uses UniVocity Parser CsvWriter and CsvParser to parse
 * through files of subdirectories in the top level source root directory.
 * main() lists subdirs from top directory, and iterates files within them according to respective class
 * within this package.
 */
public class ParseCSV {

    public static void joinFiles(List<String> joinedFileList) throws Exception {
        System.out.println(joinedFileList);
        String weightString = joinedFileList.get(0);
        String nutDataString = joinedFileList.get(1);
        BufferedReader weightReader = null;
        BufferedReader nutDataReader = null;
        try {
            JoinFiles joinObject = new JoinFiles();

            weightReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(weightString)), "UTF-8"));
            nutDataReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(nutDataString)), "UTF-8"));

            CsvParser weightParser = joinObject.createWeightParser();
            CsvParser nutDataParser = joinObject.createNutDataParser();




            String line;
            while ((line = dataReader.readLine()) != null) {
//                System.out
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        File[] subDirs = new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA").listFiles();

        final Long startTime = System.currentTimeMillis();

        // differentiate sources
        if (subDirs != null) {
            for (File subDir : subDirs) {
                String subDirName = subDir.toString().substring(subDir.toString().lastIndexOf('/') + 1);

                if (subDirName.equals("Flavonoid")) {
                    File[] flavFiles = subDir.listFiles();

                    for (File flavFile : flavFiles) {
                        String fileString = flavFile.toString();

                        if (!fileString.endsWith(".out")) {
                            File outFile = new File(fileString + ".out");
                            String fileName = flavFile.toString().substring(flavFile.toString().lastIndexOf('/') + 1);
                            FlavonoidData flavData = new FlavonoidData(outFile, fileName);
                            CsvParserSettings parserSettings = flavData.setParserSettings();
                            CsvParser csvParser = new CsvParser(parserSettings);

                            try {
                                csvParser.parse(flavData.getReader(fileString));
                            } catch (IOException e) {
                                throw new IllegalStateException("Cannot parse file " + fileString, e);
                            }
                        }
                    }
                } else if (subDirName.equals("StandardReference")) {
                    File[] srFiles = subDir.listFiles();
                    try {
                        for (File srFile : srFiles) {
                            String fileString = srFile.toString();

                            if (!fileString.endsWith(".out")) {
                                File outFile = new File(fileString + ".out");
                                String fileName = srFile.toString().substring(srFile.toString().lastIndexOf('/') + 1);
                                StandardReferenceData srData = new StandardReferenceData(outFile, fileName);
                                CsvParserSettings parserSettings = srData.setParserSettings();
                                CsvParser csvParser = new CsvParser(parserSettings);

                                try {
                                    csvParser.parse(srData.getReader(fileString));
                                } catch (IOException e) {
                                    throw new IllegalStateException("Cannot parse file " + fileString, e);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("!!!!");
                        List<String> joinFileList = new ArrayList<>();
                        for (File srFile : srFiles) {
                            String fileString = srFile.toString();
                            if (fileString.endsWith("NUT_DATA.txt.out") || fileString.endsWith("WEIGHT.txt.out")) {
                                joinFileList.add(fileString);
                            }
                        }
                        joinFiles(joinFileList);
                    }
                }
            }
        }
        final Long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime) + " milliseconds.");
    }
}
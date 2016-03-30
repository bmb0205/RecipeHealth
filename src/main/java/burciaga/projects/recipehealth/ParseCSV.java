package burciaga.projects.recipehealth;

import java.io.File;
import java.io.IOException;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

/**
 * ParseCSV class Uses UniVocity Parser CsvWriter and CsvParser to parse
 * through files of subdirectories in the top level source root directory.
 * main() lists subdirs from top directory, and iterates files within them according to respective class
 * within this package.
 */
public class ParseCSV {

    public static void main(String[] args) throws Exception {

        File[] subDirs = new File("/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA").listFiles();

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
                            FlavanoidData flavData = new FlavanoidData(outFile, fileName);
                            CsvParserSettings parserSettings = flavData.setParserSettings();
                            CsvParser csvParser = new CsvParser(parserSettings);

                            try {
                                csvParser.parse(flavData.getReader(fileString));
                            } catch (IOException e) {
                                throw new IllegalStateException("Cannot parse file " + fileString, e);
                            }
                        }
                    }
                }
            }
        }
        final Long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime));
    }
}
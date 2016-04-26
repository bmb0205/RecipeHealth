package burciaga.projects.recipehealth.csv;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

/**
 * ParseCSV class Uses UniVocity Parser CsvWriter and CsvParser to parse
 * through files of subdirectories in the top level source root directory.
 * main() lists subdirs from top directory, and iterates files within them according to respective class
 * within this package. Renames the files for good convention and also joins selected files into
 * one csv to lower number of tables in future PostgreSQL database
 */
public class ParseCSV {

    public static List<File> foodDescFileList = new ArrayList<File>();
    public static List<File> foodDataFileList = new ArrayList<File>();
    public static List<File> foodDefFileList = new ArrayList<File>();

    public static void main(String[] args) throws Exception {

        File[] subDirs = new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA").listFiles();

        final Long startTime = System.currentTimeMillis();

        // differentiate sources
        if (subDirs != null) {
            for (File subDir : subDirs) {
                String subDirName = subDir.toString().substring(subDir.toString().lastIndexOf('/') + 1);
                if (subDirName.equals("Flavonoid")) {
                    parseFlavonoidData(subDir);
                } else if (subDirName.equals("StandardReference")) {
                    parseStandardReferenceData(subDir);
                } else if (subDirName.equals("Isoflavone")) {
                    parseIsoflavoneData(subDir);
                }
            }
        }
        CombineFoodDescFiles combineFiles = new CombineFoodDescFiles((ArrayList<File>) foodDescFileList);
        CombineDataFiles combineDataFiles = new CombineDataFiles((ArrayList<File>) foodDataFileList);
        CombineDataFiles combineDefFiles = new CombineDataFiles((ArrayList<File>) foodDefFileList);
        combineFiles.appendFiles();
        combineDataFiles.appendDataFiles();
        combineDefFiles.appendDataFiles();
        final Long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime) + " milliseconds.");
    }

    // Adds FL_, SR_, or ISO_ prefix to file names for easier identification and consistency
    private static void RenameFiles(File subDir) {
        File[] files = subDir.listFiles();
        Path source = FileSystems.getDefault().getPath(subDir.toString());
        String sourceType = null;
        if (source.toString().endsWith("Flavonoid")) {
            sourceType = "/FL_";
        } else if (source.toString().endsWith("Isoflavone")){
            sourceType = "/ISO_";
        } else {
            sourceType = "/SR_";
        }
        for (File file : files) {
            try {
                Path oldFilePath = FileSystems.getDefault().getPath(file.toString());
                Path newFilePath = FileSystems.getDefault().getPath(subDir.toString() + sourceType + file.getName());
                File newFile = newFilePath.toFile();

                if (file.getName().startsWith("SR_") || file.getName().startsWith("FL_") || file.getName().startsWith("ISO_")) {
                    //  do nothing
                } else {
                    System.out.println("Creating outfile: " + newFile.toString());
                    try {
                        Files.move(oldFilePath, newFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void parseIsoflavoneData(File subDir) throws Exception {
        RenameFiles(subDir);
        File[] isoFiles = subDir.listFiles();
        try {
            for (File isoFile : isoFiles) {
                String fileString = isoFile.toString();
                System.out.println(fileString);
                if (!fileString.endsWith(".out")) {
                    File outFile = new File(fileString + ".out");
                    if (fileString.endsWith("ISO_FOOD_DES.csv")) {
                        foodDescFileList.add(outFile);
                    } else if (fileString.endsWith("ISO_ISFL_DAT.csv")) {
                        foodDataFileList.add(outFile);
                    } else if (fileString.endsWith("ISO_NUTR_DEF.csv")) {
                        foodDefFileList.add(outFile);
                    }
                    String fileName = isoFile.toString().substring(isoFile.toString().lastIndexOf('/') + 1);
                    IsoflavoneData isoData = new IsoflavoneData(outFile, fileName);
                    CsvParserSettings parserSettings = isoData.setParserSettings();
                    CsvParser csvParser = new CsvParser(parserSettings);
                    try {
                        csvParser.parse(isoData.getReader(fileString));
                    } catch (IOException e) {
                        throw new IllegalStateException("Cannot parse file " + fileString, e);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseFlavonoidData(File subDir) throws Exception {
        RenameFiles(subDir);
        File[] flavFiles = subDir.listFiles();
        try {
            for (File flavFile : flavFiles) {
                String fileString = flavFile.toString();

                if (!fileString.endsWith(".out")) {
                    File outFile = new File(fileString + ".out");
                    if (fileString.endsWith("FL_FOOD_DES.txt")) {
                        foodDescFileList.add(outFile);
                    } else if (fileString.endsWith("FL_FLAV_DAT.txt")) {
                        foodDataFileList.add(outFile);
                    } else if (fileString.endsWith("FL_NUTR_DEF.txt")) {
                        foodDefFileList.add(outFile);
                    }
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
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private static void parseStandardReferenceData(File subDir) throws Exception {
        RenameFiles(subDir);
        File[] srFiles = subDir.listFiles();
        try {
            for (File srFile : srFiles) {
                String fileString = srFile.toString();

                if (!fileString.endsWith(".out")) {
                    File outFile = new File(fileString + ".out");
                    if (fileString.endsWith("SR_FOOD_DES.txt")) {
                        foodDescFileList.add(outFile);
                    } else if (fileString.endsWith("SR_NUT_DATA.txt")) {
                        foodDataFileList.add(outFile);
                    } else if (fileString.endsWith("SR_NUTR_DEF.txt")) {
                        foodDefFileList.add(outFile);
                    }
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

        }
    }
}
package burciaga.projects.recipehealth;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static void RenameFiles(File subDir) {
        File[] files = subDir.listFiles();
        System.out.println(Arrays.toString(files));
        Path source = FileSystems.getDefault().getPath(subDir.toString());
        String sourceType = null;
        if (source.toString().endsWith("Flavonoid")) {
            sourceType = "/FL_";
        } else {
            sourceType = "/SR_";
        }
        for (File file : files) {
            Path oldFilePath = FileSystems.getDefault().getPath(file.toString());
            Path newFilePath = FileSystems.getDefault().getPath(subDir.toString() + sourceType + file.getName());
            File newFile = newFilePath.toFile();
            if(newFile.exists() || newFile.exists()) {
                System.out.println("Exists!!!" + newFile.toString());
                continue;
            } else {
                System.out.println("Does not exist!! Make it!! " + newFile.toString());
                try {
                    Files.move(oldFilePath, newFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

        File[] subDirs = new File("/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA").listFiles();

        final Long startTime = System.currentTimeMillis();

        // differentiate sources
        if (subDirs != null) {
            for (File subDir : subDirs) {
                String subDirName = subDir.toString().substring(subDir.toString().lastIndexOf('/') + 1);

                if (subDirName.equals("Flavonoid")) {
                    RenameFiles(subDir);
                    File[] flavFiles = subDir.listFiles();
                    try {
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

                    } catch (IOException e) {
                        e.printStackTrace();

                    } finally {
                        List<String> joinedFileList = new ArrayList<String>();
                        String flavFile1 = "/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_NUTR_DEF.txt.out";
                        String flavFile2 = "/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_FLAV_DAT.txt.out";
                        joinedFileList.add(flavFile1);
                        joinedFileList.add(flavFile2);
                        JoinFiles joinObject = new JoinFiles(joinedFileList);
                        System.out.println(joinedFileList.toString());
                        joinObject.parseFiles();
                    }

                } else if (subDirName.equals("StandardReference")) {
                    RenameFiles(subDir);
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
                        List<String> joinedFileList = new ArrayList<String>();

                        String srFile1 = "/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_NUTR_DEF.txt.out";
                        String srFile2 = "/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_NUT_DATA.txt.out";
                        joinedFileList.add(srFile1);
                        joinedFileList.add(srFile2);
                        JoinFiles joinObject = new JoinFiles(joinedFileList);
                        joinObject.parseFiles();
                    }
                }
            }
        }
        final Long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime) + " milliseconds.");
    }
}
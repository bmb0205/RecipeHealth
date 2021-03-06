package burciaga.projects.recipehealth.csv;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

import static org.apache.commons.lang3.ArrayUtils.remove;

/**
 * Created by bmb0205 on 4/25/16.
 * Combines two pairs of files:
 *     * ALL_FOOD_DATA.csv.out
 *     * ALL_NUTR_DEF.csv.out
 */
public class CombineDataFiles {

    private File f1;
    private File f2;
    private File f3;

    public CombineDataFiles(ArrayList<File> fileList) {
        this.f1 = fileList.get(0);
        this.f2 = fileList.get(1);
        this.f3 = fileList.get(2);
    }

    // appends the three data files together into one outfile
    public void appendDataFiles() throws Exception {

        Set<String> outLines = new HashSet<>();
        BufferedReader f1Reader = new BufferedReader(new FileReader(this.f1));
        BufferedReader f2Reader = new BufferedReader(new FileReader(this.f2));
        BufferedReader f3Reader = new BufferedReader(new FileReader(this.f3));
        BufferedWriter writer = null;

        if (f1.toString().contains("DATA")) {
            writer = new BufferedWriter(new FileWriter(new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/ALL_FOOD_DATA.csv.out")));
        } else if (f1.toString().contains("DEF")) {
            writer = new BufferedWriter(new FileWriter(new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/ALL_NUTR_DEF.csv.out")));
        }

        String srLine;
        while ((srLine = f1Reader.readLine()) != null) {
            outLines.add(srLine);
        }

        String flavLine;
        String[] splitLine;
        while ((flavLine = f2Reader.readLine()) != null) {
            splitLine = StringUtils.split(flavLine, "|");
            if (f2.toString().contains("DEF")) {
                if (splitLine.length == 4) {
                    List<String> choppedList = new ArrayList<>();
                    splitLine = ArrayUtils.remove(splitLine, 2);
                    choppedList.add(splitLine[0]);
                    choppedList.add(splitLine[2]);
                    choppedList.add(splitLine[1]);
                    outLines.add(StringUtils.join(choppedList, "|"));
                } else {
                    outLines.add(flavLine);
                }
            } else {
                outLines.add(flavLine);
            }
        }

        String isoLine;
        while ((isoLine = f3Reader.readLine()) != null) {
            splitLine = StringUtils.split(isoLine, "|");
            if (f3.toString().contains("DEF")) {
                    List<String> choppedList = new ArrayList<>();
                    choppedList.add(splitLine[0]);
                    choppedList.add(splitLine[2]);
                    choppedList.add(splitLine[1]);
                    outLines.add(StringUtils.join(choppedList, "|"));
            } else {
                outLines.add(isoLine);
            }
        }
        for (String line : outLines) {
            writer.write(line + "\n");
        }
        writer.close();
    }
}
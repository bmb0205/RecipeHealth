package burciaga.projects.recipehealth.csv;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

/**
 * Created by bmb0205 on 4/23/16.
 */
public class CombineFoodDescFiles {

    private List<File> foodDescFileList;
    private File f1;
    private File f2;
    private File f3;

    public CombineFoodDescFiles(ArrayList<File> foodDescFileList) {
        this.foodDescFileList = foodDescFileList;
        this.f1 = this.foodDescFileList.get(0);
        this.f2 = this.foodDescFileList.get(1);
        this.f3 = this.foodDescFileList.get(2);
    }

    public void appendFiles() throws Exception {

        Set<String> outLines = new HashSet<>();
        BufferedReader f1Reader = new BufferedReader(new FileReader(this.f1));
        BufferedReader f2Reader = new BufferedReader(new FileReader(this.f2));
        BufferedReader f3Reader = new BufferedReader(new FileReader(this.f3));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/ALL_FOOD_DES.csv.out")));
        Set<String> ndbSet = new HashSet<>();

        String srLine;
        String NDB_No;
        while ((srLine = f1Reader.readLine()) != null) {
            outLines.add(srLine);
            NDB_No = StringUtils.split(srLine, "|")[0];
            ndbSet.add(NDB_No);
        }

        String flavLine;
        while ((flavLine = f2Reader.readLine()) != null) {
            NDB_No = StringUtils.split(flavLine, "|")[0];
            if (!ndbSet.contains(NDB_No)) {
                outLines.add(flavLine);
                ndbSet.add(NDB_No);
            } else {
                // do nothing
            }
        }

        String isoLine;
        String foodGroupCd;
        while ((isoLine = f3Reader.readLine()) != null) {
            String[] lineList = StringUtils.split(isoLine, "|");
            NDB_No = lineList[0];
            foodGroupCd = lineList[1];
//            System.out.println(NDB_No);
            if (NDB_No.length() == 4) {
//                System.out.println("yooo " + NDB_No + "  " + NDB_No.length());
                lineList[0] = "0" + NDB_No;
                lineList[1] = "0" + foodGroupCd;
//                System.out.println(Arrays.toString(lineList));
                if (!ndbSet.contains(lineList[0])) {
//                    System.out.println("LOL" + Arrays.toString(lineList));
                    ndbSet.add(lineList[0]);
//                    ndbSet.add(NDB_No);
                    outLines.add(StringUtils.join(lineList, '|'));
//                    System.out.println(StringUtils.join(lineList, '|'));
                } else {
                    //  do nothing
                }

            }else if (!ndbSet.contains(NDB_No)) {
                outLines.add(isoLine);
            } else {
                //  do nothing
            }
        }
        for (String line : outLines) {
//                System.out.println("hehe" + line);
            writer.write(line + "\n");
        }
        writer.close();
    }
}
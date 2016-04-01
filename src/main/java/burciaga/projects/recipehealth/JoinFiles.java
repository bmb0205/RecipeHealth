package burciaga.projects.recipehealth;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.*;
import java.util.List;

/**
 * Created by bmb0205 on 3/31/16.
 */
public class JoinFiles {

    public CsvParser createNutDataParser() {
        CsvParserSettings nutDataSettings = new CsvParserSettings();
        nutDataSettings.setHeaders("NDB_No", "Nutr_No", "Nutr_Val");
        nutDataSettings.selectFields("NDB_No", "Nutr_No", "Nutr_Val");
        CsvParser nutDataParser = new CsvParser(nutDataSettings);
    }

    public CsvParser createWeightParser() {
        CsvParserSettings weightDataSettings = new CsvParserSettings();
        weightDataSettings.setHeaders("NDB_No", "Amount", "Msure_Desc", "Gm_Wgt");
        weightDataSettings.selectFields("N);
        CsvParser weightParser = new CsvParser(weightDataSettings);
    }

    public static void main(String[] args) throws Exception {

    }

}

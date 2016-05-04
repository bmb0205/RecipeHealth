package burciaga.projects.recipehealth.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class runs external process using Runtime.getRuntime().exec()
 * External processes is SQL script run from psql command line.
 * SQL script create_and_import.sql creates tables and imports delimited data into them.
 */
public class CreateTables {


    public static void main(String[] args) throws Exception {

        //  Leave only desired SQL script uncommented

        //  create_and_import.sql
        String psqlScript =
                "psql -U bmb0205 -d recipehealth -f /home/bmb0205/Resume/Java/RecipeHealth/src/main/SQL/create_and_import.sql";

//         query1.sql
//        String psqlScript =
//                "psql -U bmb0205 -d recipehealth -f /home/bmb0205/Resume/Java/RecipeHealth/src/main/SQL/query1.sql";

//        String psqlScript =
//                "psql -U bmb0205 -d recipehealth -f /home/bmb0205/Resume/Java/RecipeHealth/src/main/SQL/query2.sql";

//        String psqlScript =
//                "psql -U bmb0205 -d recipehealth -f /home/bmb0205/Resume/Java/RecipeHealth/src/main/SQL/query3.sql";

        runSQLScript(psqlScript);  // runs SQL script that is not commented out above
    }

    private static void runSQLScript(String psqlScript) {
        try {
            Process extProcess = Runtime.getRuntime().exec(psqlScript);
            BufferedReader inReader = new BufferedReader(new InputStreamReader(extProcess.getInputStream()));
            BufferedReader errReader = new BufferedReader(new InputStreamReader(extProcess.getErrorStream()));
            String line;
            while ((line = inReader.readLine()) != null) {
                System.out.println(line);
            }
            inReader.close();
            while ((line = errReader.readLine()) != null) {
                System.out.println(line);
            }
            extProcess.waitFor();
            System.out.println("Done!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

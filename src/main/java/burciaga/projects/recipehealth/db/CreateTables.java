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

        try {
            Process extProcess = Runtime.getRuntime().exec(
                    "psql -U bmb0205 -d recipehealth -f /home/bmb0205/Resume/Java/RecipeHealth/src/main/SQL/create_and_import.sql");
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

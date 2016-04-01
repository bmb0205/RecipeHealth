package burciaga.projects.recipehealth.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import burciaga.projects.recipehealth.db.DbContract;

/**
 * Created by bburciag on 4/1/16.
 */
public class TestConnection {


    public static void main(String[] args) throws Exception {

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(
                    DbContract.HOST + DbContract.DB_NAME,
                    DbContract.USERNAME,
                    DbContract.PASSWORD);

            System.out.println("Connected to PostgreSQL database!");
        } catch (ClassNotFoundException | SQLException e) {
                   e.printStackTrace();
        }
    }
}

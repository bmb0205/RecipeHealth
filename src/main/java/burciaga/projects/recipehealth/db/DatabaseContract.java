package burciaga.projects.recipehealth.db;

/*
 * Created by bmb0205 on 4/26/16.
 * Database contract containing information needed to access postgresql recipehealth database
 */
public interface DatabaseContract {

    public static final String HOST = "jdbc:postgresql://localhost:5432/";
    public static final String DB_NAME = "recipehealth";
    public static final String USERNAME = "bmb0205";
    public static final String PASSWORD = "mypassword";

}

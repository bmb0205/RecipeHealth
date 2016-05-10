package burciaga.projects.recipehealth.recipe;

import burciaga.projects.recipehealth.db.DatabaseContract;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by bmb0205 on 4/26/16.
 */
public class QueryIngredients {

    public QueryIngredients() {}

    public static void main(String[] args) throws Exception {

        QueryIngredients url = new QueryIngredients();
        Connection conn = url.connectToDatabase();
        String holder = "http://allrecipes.com/recipe/12196/blueberry-pie/";
        url.queryRecipe(conn, holder);
    }

    public ResultSet queryRecipe(Connection conn, String url) throws IOException {
        Document recipeDoc;
        Set<String> bbSet = new HashSet<>();
        ResultSet resultSet = null;
        try {
            System.out.println(url);
            recipeDoc = Jsoup.connect(url).get();
            Elements ingredientElements = recipeDoc.select("li > label > span.recipe-ingred_txt.added");
            RecipeParser rp = new RecipeParser();
            rp.setIngredients(ingredientElements);

            for (String ingredient : rp.getRecipeFoodList()) {
                String[] multiWordIngredientList = StringUtils.split(ingredient, " ");

                if (multiWordIngredientList.length > 1) {
                    ingredient = StringUtils.join(multiWordIngredientList, '%');
                }
            }
//                System.out.println(ingredient);
//                String query = "SELECT\n" +
//                        "    all_nutrient_definition.nutrient_desc,\n" +
//                        "    all_food_data.ndb_no, all_food_data.nutrient_no, all_food_data.nutrient_val, all_food_description.long_desc\n" +
//                        "        --fl_flav_ind.food_indiv_description\n" +
//                        "FROM all_nutrient_definition\n" +
//                        "    JOIN all_food_data\n" +
//                        "        ON all_food_data.nutrient_no = all_nutrient_definition.nutrient_no\n" +
//                        "    JOIN all_food_description\n" +
//                        "        ON all_food_description.ndb_no = all_food_data.ndb_no\n" +
//                        "    WHERE LOWER(all_food_description.long_desc) == LOWER('Blueberries, raw')\n" +
//                        "    AND all_food_data.ndb_no = all_food_description.ndb_no\n" +
//                        "    AND all_food_data.nutrient_no = all_nutrient_definition.nutrient_no;";
////                        "    AND all_food_data.nutrient_val != '0'\n" +
////                        "    AND all_food_data.nutrient_val != '0.0'\n" +
////                        "    AND all_food_data.nutrient_val != '0.00'\n" +
////                        "    AND all_food_data.nutrient_val != '0.000';";

//                String query =
//                    "SELECT "+
//                    "round((all_food_data.nutrient_val / 100.0) * sr_weight.gram_weight, 3),"+
//                    "all_nutrient_definition.unit, all_nutrient_definition.nutrient_desc,"+
//                    "all_food_description.long_desc "+
//                        "FROM all_nutrient_definition "+
//                            "JOIN all_food_data "+
//                                "ON all_food_data.nutrient_no = all_nutrient_definition.nutrient_no "+
//                            "JOIN all_food_description "+
//                                "ON all_food_description.ndb_no = all_food_data.ndb_no "+
//                            "JOIN sr_weight "+
//                                "ON all_food_description.ndb_no = sr_weight.ndb_no "+
//                    "WHERE all_food_description.long_desc LIKE 'Blueberries, raw'"+
//                    "AND sr_weight.amount = '1'"+
//                    "AND sr_weight.measure_desc = 'cup'"+
//                    "AND all_food_data.nutrient_val != ANY ('{0, 0.0, 0.00, 0.000}'::numeric[]);";

            //  Query for pubmed info from nutrient in blueberry
            String query = "SELECT "+
                    "	round((all_food_data.nutrient_val / 100.0) * sr_weight.gram_weight, 3), "+
                    "	all_nutrient_definition.unit, all_nutrient_definition.nutrient_desc, "+
                    "	pmid_mesh.pmid, "+
                    "	pubmed_info.title "+
                    "FROM all_nutrient_definition "+
                    "	JOIN all_food_data "+
                    "		ON all_food_data.nutrient_no = all_nutrient_definition.nutrient_no "+
                    "	JOIN all_food_description "+
                    "		ON all_food_description.ndb_no = all_food_data.ndb_no "+
                    "	JOIN sr_weight "+
                    "		ON all_food_description.ndb_no = sr_weight.ndb_no "+
                    "	JOIN pmid_mesh "+
                    "		ON LOWER(pmid_mesh.meshterm) = LOWER(all_nutrient_definition.nutrient_desc) "+
                    "	JOIN pubmed_info "+
                    "		ON pmid_mesh.pmid = pubmed_info.pmid "+
                    "	WHERE all_food_description.long_desc LIKE 'Blueberries, raw' "+
                    "		AND sr_weight.amount = '1' "+
                    "		AND sr_weight.measure_desc = 'cup' "+
                    "	AND all_food_data.nutrient_val != ANY ('{0, 0.0, 0.00, 0.000}'::numeric[]);";

            Statement statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
//                System.out.println("here");

            return resultSet;
//                StringBuilder sb = new StringBuilder();
//                for (String option : bbSet) {
//                    sb.append(option);
//                    sb.append("\n");
//                }
//                return sb.toString();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public Connection connectToDatabase() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(
                    DatabaseContract.HOST + DatabaseContract.DB_NAME,
                    DatabaseContract.USERNAME,
                    DatabaseContract.PASSWORD);

            System.out.println("Connected to PostgreSQL database!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}

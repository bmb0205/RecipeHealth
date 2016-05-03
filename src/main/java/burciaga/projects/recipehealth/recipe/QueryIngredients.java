package burciaga.projects.recipehealth.recipe;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.sql.*;
import java.util.Arrays;
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
        String holder = "";
        url.queryRecipe(conn, holder);
    }

    public String queryRecipe(Connection conn, String url) {
        Document recipeDoc;
        Set<String> bbSet = new HashSet<>();

        try {
            recipeDoc = Jsoup.connect(url).get();
            Elements ingredientElements = recipeDoc.select("li > label > span.recipe-ingred_txt.added");
            RecipeParser rp = new RecipeParser();
            rp.setIngredients(ingredientElements);

            for(String ingredient : rp.getRecipeFoodList()) {
                String[] multiWordIngredientList = StringUtils.split(ingredient, " ");

                if (multiWordIngredientList.length > 1) {
                    ingredient = StringUtils.join(multiWordIngredientList, '%');
                }
//                System.out.println(ingredient);

                String query = "SELECT\n" +
                        "\tall_nutrient_definition.nutrient_des,\n" +
                        "\tall_food_data.ndb_no, all_food_data.nutrient_no, all_food_data.nutrient_val, all_food_description.long_desc\n" +
                        "\t\t--fl_flav_ind.food_indiv_description\n" +
                        "FROM all_nutrient_definition\n" +
                        "\tJOIN all_food_data\n" +
                        "\t\tON all_food_data.nutrient_no = all_nutrient_definition.nutrient_no\n" +
                        "\tJOIN all_food_description\n" +
                        "\t\tON all_food_description.ndb_no = all_food_data.ndb_no\n" +
                        "\tWHERE LOWER(all_food_description.long_desc) LIKE LOWER('Blueberr%')\n" +
//                        "\tWHERE all_food_description.long_desc LIKE '%" + ingredient + "%'\n" +
                        "\tAND all_food_data.ndb_no = all_food_description.ndb_no\n" +
                        "\tAND all_food_data.nutrient_no = all_nutrient_definition.nutrient_no;";
//                        "\tAND all_food_data.nutrient_val != '0'\n" +
//                        "\tAND all_food_data.nutrient_val != '0.0'\n" +
//                        "\tAND all_food_data.nutrient_val != '0.00'\n" +
//                        "\tAND all_food_data.nutrient_val != '0.000';";

                if (ingredient.equals("fresh%blueberries")) {

                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    //  gather types of this specific ingredient (raw, frozen, etc) for USER to choose from
                    while (resultSet.next()) {
                        bbSet.add(resultSet.getString(5));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (String option : bbSet) {
            sb.append(option);
            sb.append("\n");
        }
        return sb.toString();
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

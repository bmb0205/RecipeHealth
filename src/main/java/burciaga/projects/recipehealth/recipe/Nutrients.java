package burciaga.projects.recipehealth.recipe;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by bmb0205 on 4/27/16.
 */
public class Nutrients {

    private String nutrientDescription = "";
    private String ndbNo = "";
    private String nutrientNo = "";
    private String nutrientValue = "";
    private String foodDescription = "";

    public Nutrients(ResultSet resultSet) {
        try {
            this.nutrientDescription = resultSet.getString(1);
            this.ndbNo = resultSet.getString(2);
            this.nutrientNo = resultSet.getString(3);
            this.nutrientValue = resultSet.getString(4);
            this.foodDescription = resultSet.getString(5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNutrientDescription() {
        return this.nutrientDescription;
    }

    public String getNdbNo() {
        return this.ndbNo;
    }

    public String getNutrientNo() {
        return this.nutrientNo;
    }

    public String getNutrientValue() {
        return this.nutrientValue;
    }

    public String getFoodDescription() {
        return this.foodDescription;
    }
}

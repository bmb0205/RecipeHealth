package burciaga.projects.recipehealth.recipe;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bmb0205 on 4/26/16.
 */
public class RecipeParser {

    private static final List<String> numList =
            new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9",
            "1/2", "1/4", "1/3", "3/4", "0.25", "0.5", "0.75"));

    private static final List<String> unitList =
            new ArrayList<>((Arrays.asList("pound", "pounds", "cup", "cups", "teaspoon",
            "teaspoons", "tablespoon", "tablespoons")));

    private List<String> recipeFoodList = new ArrayList<>();

    public void RecipeParser() {
//        this.recipeAmountList = "";
//        this.recipeUnitList = "";
        this.recipeFoodList = null;
    }

    public List<String> getRecipeFoodList() {
        return this.recipeFoodList;
    }

    public void setIngredients(Elements ingredientElements) {

        for (Element ingredient : ingredientElements) {
            List<String> recipeFoodList = new ArrayList<>();
            String[] ingredientSchema = StringUtils.split(ingredient.text(), ' ');
            String amount = ingredientSchema[0];
            String unit = ingredientSchema[1];

            //  starts with number, fraction or decimal
            if (numList.contains(amount)) {
                //
                if (unitList.contains(unit)) {
                    this.recipeFoodList.add(StringUtils.join(Arrays.copyOfRange(ingredientSchema, 2, ingredientSchema.length), " "));
//                    System.out.println(ingredientSchema.length + Arrays.toString(ingredientSchema));
//                    this.amount = amount;
//                    this.unit = unit;
//                    this.food = StringUtils.join(Arrays.copyOfRange(ingredientSchema, 2, ingredientSchema.length), " ");
                }

            } else { }
        }
    }

    public static void main(String[] args) throws Exception {
//        Document recipeDoc = null;
//        try {
//            recipeDoc = Jsoup.connect("http://allrecipes.com/recipe/12196/blueberry-pie/").get();
//            Elements ingredientElements = recipeDoc.select("li > label > span.recipe-ingred_txt.added");
//            RecipeParser rp = new RecipeParser();
//            rp.parseIngredients(ingredientElements);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
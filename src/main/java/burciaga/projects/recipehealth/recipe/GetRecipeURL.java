package burciaga.projects.recipehealth.recipe;

import java.io.*;
import java.sql.Connection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


/**
 * Created by bmb0205 on 4/29/16.
 */

public class GetRecipeURL extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();

        BufferedReader reader = request.getReader();
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        String jsonString = builder.toString();
//        JsonObject jObj = new JsonObject
//        out.print(jsonString);
//        out.close();

        Recipe recipe = new Gson().fromJson(jsonString, Recipe.class);
        String url = recipe.getUrl();
////
        QueryIngredients parsedUrl = new QueryIngredients();
        Connection conn = parsedUrl.connectToDatabase();
        String omg = parsedUrl.queryRecipe(conn, url);
        out.println(omg);



        out.close();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleRequest(request, response);

    }

}

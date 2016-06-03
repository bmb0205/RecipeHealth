package burciaga.projects.recipehealth.recipe;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


/**
 * Created by bmb0205 on 4/29/16.
 * Servlet handles HTTP requests from browser side of recipe health app
 * JavaScript AJAX requests used to GET and POST data
 */
public class GetRecipeURL extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // handles requests from client
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // set response type and create PrintWriter for writing response
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // use StringBuilder to access HttpServletRequest object payload
        BufferedReader reader = request.getReader();
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        // Convert JSON to Java object using Recipe class
        String jsonString = builder.toString();
        Recipe recipe = new Gson().fromJson(jsonString, Recipe.class);
        String url = recipe.getUrl();


        // write response by putting data into HTML table which gets appended to DOM element
        try {
            QueryIngredients parsedUrl = new QueryIngredients();
            Connection conn = parsedUrl.connectToDatabase();
            ResultSet resultSet = parsedUrl.queryRecipe(conn, url);
            out.println("<table class=\"resultstable\" border=\"1\"");
            out.println("<tr>");
            out.println("<h2>Nutrients found in this recipe and the research behind them</h2>");
            out.println("<th>Nutrient Description</th>");
            out.println("<th>PubMed ID</th>");
            out.println("<th>Research article title</th>");
            out.println("</tr>");

            // Nutrient, PMID, Article title (title is hyperlink to actual article on PMID)
            while (resultSet.next()) {
                out.println("<tr>");
                out.println("<td>"+ resultSet.getArray(3) + "</td>" +
                        "<td>" + resultSet.getArray(4) + "</td>"+
                        "<td><a href=\"http://www.ncbi.nlm.nih.gov/pubmed/"+ resultSet.getArray(4) + "\">" + resultSet.getArray(5)+"</td>");
                out.println("</tr");
            }
            out.println("</table>");

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

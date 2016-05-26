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
 */

public class GetRecipeURL extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


        BufferedReader reader = request.getReader();
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        String jsonString = builder.toString();
        Recipe recipe = new Gson().fromJson(jsonString, Recipe.class);
        String url = recipe.getUrl();

        try {
            QueryIngredients parsedUrl = new QueryIngredients();
            Connection conn = parsedUrl.connectToDatabase();
            ResultSet resultSet = parsedUrl.queryRecipe(conn, url);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            out.println("<table id=\"resultstable\" value=\"sup\" border=\"1\""); // style=\"width:1200px\" height=\"400px\" overflow=\"auto\"");
            out.println("<tr>");
            out.println("<h2>Nutrients found in this recipe and the research behind them</h2>");
            // add column headers
            out.println("<th>"+metaData.getColumnName(3)+"</th>");
            out.println("<th>"+metaData.getColumnName(4)+"</th>");
            out.println("<th>"+metaData.getColumnName(5)+"</th>");
            out.println("</tr>");

            while (resultSet.next()) {
                out.println("<tr>");
                out.println("  <td>"+ resultSet.getArray(3) + "</td>" +
                        " <td>" + resultSet.getArray(4) + "</td>"+
                        " <td>" + resultSet.getArray(5)+"</td>");
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

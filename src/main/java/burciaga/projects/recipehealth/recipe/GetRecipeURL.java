package burciaga.projects.recipehealth.recipe;

import java.io.*;
import java.util.Date;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by bmb0205 on 4/29/16.
 */

public class GetRecipeURL extends HttpServlet {

    private static final long serialVersionUID = 1L;


    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println(request.getParameterNames());
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Hello World!</title></head>");
        out.println("<body><h1>Hello World!</h1></body></html>");
    }


//    public GetRecipeURL() {
//        super();
//    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        String mimeType = request.getContentType();
//        System.out.println(mimeType);
//
//
//        System.out.println("here");
//        Enumeration<String> a = request.getParameterNames();
//
//        StringBuilder buffer = new StringBuilder();
//        BufferedReader reader = request.getReader();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            buffer.append(line);
//        }
//        String data = buffer.toString();
//
//        PrintWriter out = response.getWriter();
//        out.write(a.toString());
//        out.write(data);
//
//        doGet(request, response);
////        PrintWriter out = response.getWriter();
////        Date currentTime = new Date();
////        String message = String.format("current time is %tr on %tD", currentTime, currentTime);
////        out.print(message);
//
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        System.out.println("here2");
//        String mimeType = request.getContentType();
//        System.out.println(mimeType);
//        // Read from request
//        StringBuilder buffer = new StringBuilder();
//        BufferedReader reader = request.getReader();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            buffer.append(line);
//        }
//        String data = buffer.toString();
//
//        PrintWriter out = response.getWriter();
//
//        out.println(data);
//
//    }

}

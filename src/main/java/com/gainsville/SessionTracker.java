package com.gainsville;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Result;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@WebServlet(name = "SessionTracker",urlPatterns = {"/session"})
public class SessionTracker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process_request(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process_request(request,response);

    }

    protected void process_request(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        ArrayList<Integer> last_viewed = (ArrayList<Integer>) session.getAttribute("last_viewed");
        ArrayList<Integer> already_displayed = new ArrayList<Integer>();

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String sql = null;
        Statement stmt = null;
        ResultSet rs = null;

        Collections.reverse(last_viewed);
        int counter = 0;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://myawsdb.cxvfj1ifaryh.us-east-2.rds.amazonaws.com:3306/gainsville", "choya2", "Gear2ndpwns!");
            out.println("<ul>");
            for (int i = 0; i < last_viewed.size(); i++) {
                if(counter == 10){
                    break;
                }
                int pid = last_viewed.get(i);
                sql = "SELECT * FROM products WHERE pid=" + pid;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                rs.next();

                if(!already_displayed.contains(pid)) {
                    out.println("<li> <a href ='productdetail?id=" + rs.getInt("id") + "'>"
                            + rs.getString("name") + "</a>");
                    out.println("<a <a href ='productdetail?id=" + rs.getInt("id") + "'>");
                    out.println("<img class='product_image' src='" + rs.getString("image1") + "'>");
                    out.println("</a>");
                    out.println("</li>");
                    already_displayed.add(pid);
                    counter++;
                }
            }

            out.println("</ul>");
            conn.close();
        }catch(SQLException e){
            response.sendError(500);
        }
    }
}

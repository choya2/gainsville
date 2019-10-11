package com.gainsville;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "ProductDetail",urlPatterns = {"/productdetail"})
public class ProductDetail extends HttpServlet {
    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            Connection conn =  DriverManager.getConnection("jdbc:mysql://myawsdb.cxvfj1ifaryh.us-east-2.rds.amazonaws.com:3306/gainsville", "choya2", "Gear2ndpwns!");
            HttpSession session = request.getSession(true);
            ArrayList<Integer> last_viewed = (ArrayList<Integer>) session.getAttribute("last_viewed");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            String sql = "SELECT * FROM products WHERE id=" + request.getParameter("id");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>"+ rs.getString("title") +"</title>");
            out.println("<link rel='stylesheet' type='text/css' href='home_style.css'/>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div id='container'>");
            out.println("<div id='header'>");
            out.println("<h1> Gainsville </h1>");
            out.println("</div>");

            out.println("<div id='footer'>");
            out.println("<a href='shoppingcart'> Cart </a>");
            out.println("<br>");
            out.println("<a href='checkout'> Check Out </a>");
            out.println("</div>");

            out.println("<div id='content'>");

            //Navigation
            out.println("<div id='nav'>");
            out.println("<h3> Navigation </h3>");
            out.println("<ul>");
            out.println("<li> <a class='select' href='index.html'> Home </a> </li>");
            out.println("<li> <a class='select' href='products'> Products </a> </li>");
            out.println("<li> <a class='select' href='index.html'> About me </a> </li>");
            out.println("</ul>");

            //session tracking

            out.println("<h3> Last Viewed Products </h3>");
            RequestDispatcher rd = request.getRequestDispatcher("session");
            rd.include(request, response);

            out.println("</div>");

            out.println("<div id='product_info'>");
            out.println("<h1>" + rs.getString("name") + "</h1>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<td> <img class='product_image' src='" + rs.getString("image1") + "' </td>");
            out.println("<td> <img class='product_image' src='" + rs.getString("image2") + "' </td>");
            out.println("<td> <img class='product_image' src='" + rs.getString("image3") + "' </td>");
            out.println("</tr>");
            out.println("</table>");

            out.println("<div id='about_product'>");
            out.println("<h5> About this product </h5>");
            out.println("<p> " + rs.getString("description") + " </p>");
            out.println("</div>");

            out.println("<div id='add_cart'>");
            out.println("<h5> $"+ rs.getFloat("price") +"</h5>");
            out.println("<form action='shoppingcart' method='post'>");
            out.println("<input type='hidden' name='pid' value='"+rs.getInt("pid")+"'> </input>");
            out.println("<h5> <button type='submit'> Add to Cart</button> </h5>");
            out.println("</form>");
            out.println("</div>");

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

            //add viewed product to session
            last_viewed.add(rs.getInt("pid"));
            session.setAttribute("last_viewed",last_viewed);

            conn.close();

        }catch(Exception e){
            response.sendError(500);
        }

    }
}

package com.gainsville;

import sun.misc.Request;

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



@WebServlet(name = "ProductsPage",urlPatterns = {"/products"})
public class ProductsPage extends HttpServlet {
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
            HttpSession session = request.getSession(true);
            ArrayList<Integer> last_viewed = null;
            ArrayList<Integer> cart = null;

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            Connection conn = null;
            try{
                conn = DriverManager.getConnection("jdbc:mysql://myawsdb.cxvfj1ifaryh.us-east-2.rds.amazonaws.com:3306/gainsville", "choya2", "Gear2ndpwns!");

                String p1SQL = "SELECT * FROM products WHERE pid = 1";
                Statement stmt1 = conn.createStatement();
                ResultSet rs1 = stmt1.executeQuery(p1SQL);
                rs1.next();

                String p2SQL = "SELECT * FROM products WHERE pid = 2";
                Statement stmt2 = conn.createStatement();
                ResultSet rs2 = stmt2.executeQuery(p2SQL);
                rs2.next();

                String p3SQL = "SELECT * FROM products WHERE pid = 3";
                Statement stmt3 = conn.createStatement();
                ResultSet rs3 = stmt3.executeQuery(p3SQL);
                rs3.next();

                String p4SQL = "SELECT * FROM products WHERE pid = 4";
                Statement stmt4 = conn.createStatement();
                ResultSet rs4 = stmt4.executeQuery(p4SQL);
                rs4.next();

                String p5SQL = "SELECT * FROM products WHERE pid = 5";
                Statement stmt5 = conn.createStatement();
                ResultSet rs5 = stmt5.executeQuery(p5SQL);
                rs5.next();

                String p6SQL = "SELECT * FROM products WHERE pid = 6";
                Statement stmt6 = conn.createStatement();
                ResultSet rs6 = stmt6.executeQuery(p6SQL);
                rs6.next();

                String p7SQL = "SELECT * FROM products WHERE pid = 7";
                Statement stmt7 = conn.createStatement();
                ResultSet rs7 = stmt7.executeQuery(p7SQL);
                rs7.next();

                String p8SQL = "SELECT * FROM products WHERE pid = 8";
                Statement stmt8 = conn.createStatement();
                ResultSet rs8 = stmt8.executeQuery(p8SQL);
                rs8.next();

                String p9SQL = "SELECT * FROM products WHERE pid = 9";
                Statement stmt9 = conn.createStatement();
                ResultSet rs9 = stmt9.executeQuery(p9SQL);
                rs9.next();

                String p10SQL = "SELECT * FROM products WHERE pid = 10";
                Statement stmt10 = conn.createStatement();
                ResultSet rs10 = stmt10.executeQuery(p10SQL);
                rs10.next();


                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Products</title>");
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
                if(session.isNew()){
                    last_viewed = new ArrayList<Integer>();
                    session.setAttribute("last_viewed",last_viewed);

                    cart = new ArrayList<Integer>();
                    session.setAttribute("cart",cart);
                }

                else {
                    RequestDispatcher rd = request.getRequestDispatcher("session");
                    rd.include(request, response);
                }

                out.println("</div>");

                out.println("<div id='products'>");
                out.println("<h1> Products </h1>");

                //First category
                out.println("<h4> Pre-Workout </h4>");
                out.println("<table>");

                out.println("<tr>");
                out.println("<th>" + rs1.getString("name") + "</th>");
                out.println("<th>" + rs2.getString("name") + "</th>");
                out.println("<th>" + rs3.getString("name") + "</th>");
                out.println("</tr>");

                out.println("<tr>");

                out.println("<td>");
                out.println("<a href='productdetail?id=" + rs1.getInt("id") + "'>");
                out.println("<img class='product_image' src='" + rs1.getString("image1")+"'>");
                out.println("</a>");
                out.println("</td>");

                out.println("<td>");
                out.println("<a href='productdetail?id=" + rs2.getInt("id") + "'>");
                out.println("<img class='product_image' src='" + rs2.getString("image1")+"'>");
                out.println("</a>");
                out.println("</td>");

                out.println("<td>");
                out.println("<a href='productdetail?id=" + rs3.getInt("id") + "'>");
                out.println("<img class='product_image' src='" + rs3.getString("image1")+"'>");
                out.println("</a>");
                out.println("</td>");

                out.println("</tr>");

                out.println("<tr>");

                out.println("<td> $" + rs1.getFloat("price") + "</td>" );
                out.println("<td> $" + rs2.getFloat("price") + "</td>" );
                out.println("<td> $" + rs3.getFloat("price") + "</td>" );

                out.println("</tr>");

                out.println("</table>");

                //second category
                out.println("<h4> Whey Protein </h4>");
                out.println("<table>");

                out.println("<tr>");
                out.println("<th>" + rs4.getString("name") + "</th>");
                out.println("<th>" + rs5.getString("name") + "</th>");
                out.println("<th>" + rs6.getString("name") + "</th>");
                out.println("</tr>");

                //images
                out.println("<tr>");

                out.println("<td>");
                out.println("<a href='productdetail?id=" + rs4.getInt("id") + "'>");
                out.println("<img class='product_image' src='" + rs4.getString("image1")+"'>");
                out.println("</a>");
                out.println("</td>");

                out.println("<td>");
                out.println("<a href='productdetail?id=" + rs5.getInt("id") + "'>");
                out.println("<img class='product_image' src='" + rs5.getString("image1")+"'>");
                out.println("</a>");
                out.println("</td>");

                out.println("<td>");
                out.println("<a href='productdetail?id=" + rs6.getInt("id") + "'>");
                out.println("<img class='product_image' src='" + rs6.getString("image1")+"'>");
                out.println("</a>");
                out.println("</td>");

                out.println("</tr>");

                out.println("<tr>");
                out.println("<td> $" + rs4.getFloat("price") + "</td>" );
                out.println("<td> $" + rs5.getFloat("price") + "</td>" );
                out.println("<td> $" + rs6.getFloat("price") + "</td>" );
                out.println("</tr>");

                out.println("<tr>");
                out.println("<th>" + rs7.getString("name") + "</th>");
                out.println("</tr>");

                out.println("<tr>");
                out.println("<td>");
                out.println("<a href='productdetail?id=" + rs7.getInt("id") + "'>");
                out.println("<img class='product_image' src='" + rs7.getString("image1")+"'>");
                out.println("</a>");
                out.println("</td>");
                out.println("</tr>");

                out.println("<tr>");
                out.println("<td> $" + rs7.getFloat("price") + "</td>" );
                out.println("</tr>");

                out.println("</table>");

                //third category
                out.println("<h4> Branched Chain Amino Acids </h4>");
                out.println("<table>");
                out.println("<tr>");
                out.println("<th>" + rs8.getString("name") + "</th>");
                out.println("<th>" + rs9.getString("name") + "</th>");
                out.println("<th>" + rs10.getString("name") + "</th>");
                out.println("</tr>");

                //images
                out.println("<tr>");

                out.println("<td>");
                out.println("<a href='productdetail?id=" + rs8.getInt("id") + "'>");
                out.println("<img class='product_image' src='" + rs8.getString("image1")+"'>");
                out.println("</a>");
                out.println("</td>");

                out.println("<td>");
                out.println("<a href='productdetail?id=" + rs9.getInt("id") + "'>");
                out.println("<img class='product_image' src='" + rs9.getString("image1")+"'>");
                out.println("</a>");
                out.println("</td>");

                out.println("<td>");
                out.println("<a href='productdetail?id=" + rs10.getInt("id") + "'>");
                out.println("<img class='product_image' src='" + rs10.getString("image1")+"'>");
                out.println("</a>");
                out.println("</td>");

                out.println("</tr>");

                out.println("<tr>");
                out.println("<td> $" + rs8.getFloat("price") + "</td>" );
                out.println("<td> $" + rs9.getFloat("price") + "</td>" );
                out.println("<td> $" + rs10.getFloat("price") + "</td>" );
                out.println("</tr>");
                out.println("</table>");

                out.println("</div>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");

                conn.close();

            }catch(Exception e){
                out.println(e);
                //response.sendError(500);
            }

    }
}

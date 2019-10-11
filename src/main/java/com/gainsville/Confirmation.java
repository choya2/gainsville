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
import java.util.HashMap;
import java.util.Random;

@WebServlet(name = "Confirmation",urlPatterns = {"/confirmation"})
public class Confirmation extends HttpServlet {
    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        ArrayList<Integer> cart = (ArrayList<Integer>) session.getAttribute("cart");
        ArrayList<Integer> already_seen = new ArrayList<Integer>();

        HashMap<Integer,Integer> quant = new HashMap<Integer,Integer>();
        for(int i = 0;i<cart.size();i++){
            if(!quant.containsKey(cart.get(i))){
                quant.put(cart.get(i),1);
            }
            else{
                quant.put(cart.get(i), quant.get(cart.get(i)) + 1);
            }
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://myawsdb.cxvfj1ifaryh.us-east-2.rds.amazonaws.com:3306/gainsville", "choya2", "Gear2ndpwns!");

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            double total = (Double) session.getAttribute("subtotal");
            String shipping = request.getParameter("shipping");
            if(shipping.equals("air")){
                total += 9.99;
            }
            else if(shipping.equals("postal")){
                total += 13.99;
            }

            total = Math.round(total * 100.0) / 100.0;
            int number_of_products = cart.size();
            Random rand = new Random();
            int confirmation = rand.nextInt(900000000) + 100000000;
            String address = request.getParameter("street") + " " + request.getParameter("city") + ", " +
                    request.getParameter("state") + " " + request.getParameter("zip");



            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title> Cart </title>");
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
            out.println("<li> <a class='select' href='about.html'> About me </a> </li>");
            out.println("</ul>");

            //session tracking
            out.println("<h3> Last Viewed Products </h3>");
            RequestDispatcher rd = request.getRequestDispatcher("session");
            rd.include(request, response);
            out.println("</div>");

            out.println("<div id='product_info'>");
            out.println("<h1> Confirmation Page </h1>");
            out.println("<p>" +
                    "You have successfully made your purchase! You can find " +
                    "the details of your order below. Please review your order" +
                    "and make sure that everything is correct. If you have any" +
                    "problems, please send me an email. Thank you! </p>");
            out.println("<table>");

            out.println("<tr>");
            out.println("<td> Name: </td>");
            out.println("<td>" + request.getParameter("first") +" " + request.getParameter("last") + "</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td> Email: </td>");
            out.println("<td>" + request.getParameter("email") + "</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td> Phone Number: </td>");
            out.println("<td>" + request.getParameter("phone") + "</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td> Confirmation Number: </td>");
            out.println("<td>" + confirmation + "</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td> Credit Card: </td>");
            out.println("<td>" + request.getParameter("card") + "</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td> Address: </td>");
            out.println("<td>" + address + "</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td> Shipping Method: </td>");
            out.println("<td>" + request.getParameter("shipping") + "</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td> Products: </td>");
            out.println("<td>");
            String sql = null;
            Statement stmt = null;
            ResultSet rs = null;
            for(int i =0; i< cart.size();i++){
                if(!already_seen.contains(cart.get(i))){
                    sql = "SELECT * FROM products WHERE pid=" + cart.get(i);
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(sql);
                    rs.next();

                    out.println(rs.getString("name") + " <br>");

                    already_seen.add(cart.get(i));
                }
            }
            out.println("</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td> Number of Items: </td>");
            out.println("<td>" + cart.size() + "</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td> Subtotal: </td>");
            out.println("<td> $" + session.getAttribute("subtotal") + "</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td> Total: </td>");
            out.println("<td> $" + total + "</td>");
            out.println("</tr>");



            out.println("</table>");
            out.println("</body>");
            out.println("</html>");

            //add to database

            String pSQL = "INSERT INTO customers (first_name,last_name,email,phone_number,street_address,city,state,zip,card_number)" +
                    "VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(pSQL);
            pst.setString(1,request.getParameter("first"));
            pst.setString(2,request.getParameter("last"));
            pst.setString(3,request.getParameter("email"));
            pst.setString(4,request.getParameter("phone"));
            pst.setString(5,request.getParameter("street"));
            pst.setString(6,request.getParameter("city"));
            pst.setString(7,request.getParameter("state"));
            pst.setString(8,request.getParameter("zip"));
            pst.setString(9,request.getParameter("card"));

            pst.executeUpdate();

            pst.close();
            String pSQL2 = "INSERT INTO orders(order_id,quantity,shipping_method,total,email) " +
                    "VALUES(?,?,?,?,?)";
            PreparedStatement pst2 = conn.prepareStatement(pSQL2);
            pst2.setInt(1,confirmation);
            pst2.setInt(2,number_of_products);
            pst2.setString(3,request.getParameter("shipping"));
            pst2.setFloat(4,(float) total);
            pst2.setString(5,request.getParameter("email"));

            pst2.executeUpdate();

            pst2.close();


            conn.close();
        }catch(Exception e){
            response.sendError(500);
        }




    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

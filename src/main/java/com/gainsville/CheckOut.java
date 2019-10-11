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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "CheckOut",urlPatterns = {"/checkout"})
public class CheckOut extends HttpServlet {
    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process_request(request,response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process_request(request,response);
    }

    protected void process_request(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

            double subtotal = 0.00;
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title> Cart </title>");
            out.println("<link rel='stylesheet' type='text/css' href='home_style.css'/>");
            out.println("</head>");
            out.println("<body>");
            out.println("<script src='validate.js'> </script>");
            out.println("<div id='container'>");
            out.println("<div id='header'>");
            out.println("<h1> Gainsville </h1>");
            out.println("</div>");

            out.println("<div id='footer'>");
            out.println("<a href='shoppingcart'> Cart </a>");
            out.println("<br>");
            out.println("<a href=''> Check Out </a>");
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
            out.println("<h1> Check Out </h1>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th> Product </th>");
            out.println("<th> Picture </th>");
            out.println("<th> Price </th>");
            out.println("<th> Quantity </th>");
            out.println("</tr>");

            String sql = null;
            Statement stmt = null;
            ResultSet rs = null;

            for(int i = 0; i<cart.size(); i++){
                int pid = cart.get(i);
                if(!already_seen.contains(pid)){
                    sql = "SELECT * FROM products WHERE pid=" + pid;
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(sql);
                    rs.next();
                    out.println("<tr>");
                    out.println("<td> " + rs.getString("name") + "</td>");
                    out.println("<td> <img class='table_image' src='" + rs.getString("image1") + "'/> </td>");
                    out.println("<td> $" + rs.getFloat("price") + " </td>");
                    out.println("<td> " + quant.get(pid) + "</td>");
                    subtotal += (rs.getFloat("price") * quant.get(pid));
                    out.println("</tr>");

                    already_seen.add(pid);
                }
            }

            subtotal = Math.round(subtotal * 100.0) / 100.0;
            session.setAttribute("subtotal",subtotal);

            out.println("<tr>");
            out.println("<th> </th>");
            out.println("<th> </th>");
            out.println("<th> </th>");
            out.println("<th> Subtotal: $"+subtotal+ "</th>");
            out.println("</tr>");
            out.println("</table>");

            out.println("<h1> Order Information </h1>");
            out.println("<form name='orderForm' action='confirmation'" +
                    " onSubmit='return(validateForm());' method='post'>");
            out.println("<fieldset>");
            out.println("<legend> Personal Information </legend>");
            out.println("First Name: <input type='text' name='first'/><br>");
            out.println("Last Name: <input type='text' name='last'/><br>");
            out.println("E-mail Name: <input type='text' name='email'/><br>");
            out.println("Phone Number: <input type='text' name='phone'/><br>");
            out.println("Credit Card Number: <input type='text' name='card'/>");
            out.println("</fieldset>");

            out.println("<fieldset>");
            out.println("<legend> Shipping Address </legend>");
            out.println("Street Address: <input type='text' name='street'/><br>");
            out.println("Zip Code: <input type='text' name='zip'/><br>");
            out.println("City: <input type='text' name='city'/><br>");
            out.println("State: <input type='text' name='state'/><br>");
            out.println("</fieldset>");

            out.println("<fieldset>");
            out.println("<legend> Shipping Method </legend>");
            out.println("UPS Ground<input type='radio' name='shipping' value='ground' id='ship'>Free<br>");
            out.println("UPS Next Day<input type='radio' name='shipping' value='air' id='ship'>$9.99<br>");
            out.println("US Postal Service<input type='radio' name='shipping' value='postal' id='ship'>$13.99");
            out.println("<input type ='submit' value='Place Your Order!'/>");
            out.println("</fieldset>");

            out.println("</form>");
            out.println("</body>");
            out.println("</html>");

            conn.close();
        }catch(Exception e){
            response.sendError(500);
        }
    }
}

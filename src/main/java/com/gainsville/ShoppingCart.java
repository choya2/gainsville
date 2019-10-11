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

@WebServlet(name = "ShoppingCart",urlPatterns = {"/shoppingcart"})
public class ShoppingCart extends HttpServlet {
    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       process_request(request,response,true);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process_request(request,response,false);
    }

    protected void process_request(HttpServletRequest request, HttpServletResponse response, boolean add) throws ServletException,IOException{
        HttpSession session = request.getSession(true);
        ArrayList<Integer> cart = (ArrayList<Integer>) session.getAttribute("cart");
        HashMap<Integer,Integer> quant = new HashMap<Integer,Integer>();

        ArrayList<Integer> already_seen = new ArrayList<Integer>();
        if(add) {
            int id = Integer.parseInt(request.getParameter("pid"));
            cart.add(id);
            session.setAttribute("cart", cart);
        }

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
            out.println("<a href=''> Cart </a>");
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
            out.println("<h1> Cart </h1>");
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
                    out.println("</tr>");

                    already_seen.add(pid);
                }
            }

            out.println("<tr>");
            out.println("<td></td>");
            out.println("<td></td>");
            out.println("<td></td>");
            out.println("<td>");
            out.println("<form action='checkout' method ='get'>");
            out.println("<input type='submit' value='Check Out' />");
            out.println("</form>");
            out.println("</td>");
            out.println("</tr>");


            out.println("</table>");
            out.println("</body>");
            out.println("</html>");

            conn.close();
        }catch(Exception e){

            //response.sendError(500);
        }

    }
}

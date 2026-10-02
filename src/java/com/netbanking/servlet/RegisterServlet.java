package com.netbanking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            Connection con = null;
            PreparedStatement ps = null;

            try {

                // MySQL Driver
                Class.forName("com.mysql.jdbc.Driver");

                // Database connection
                String url = "jdbc:mysql://localhost:3306/netbanking";
                String username = "root";
                String dbPassword = "";

                con = DriverManager.getConnection(
                        url,
                        username,
                        dbPassword);

                // Insert user
                String sql = "INSERT INTO users(name, email, password) "
                        + "VALUES (?, ?, ?)";

                ps = con.prepareStatement(sql);

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);

                ps.executeUpdate();

                // Successful registration
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Registration Successful</title>");

                out.println("<style>");

                out.println("body {");
                out.println("font-family: Arial, sans-serif;");
                out.println("background-color: #f3f7fb;");
                out.println("text-align: center;");
                out.println("padding-top: 100px;");
                out.println("}");

                out.println(".container {");
                out.println("width: 450px;");
                out.println("margin: auto;");
                out.println("background: white;");
                out.println("padding: 35px;");
                out.println("border-radius: 15px;");
                out.println("box-shadow: 0 5px 25px rgba(0,0,0,0.08);");
                out.println("}");

                out.println("h2 {");
                out.println("color: #315c8c;");
                out.println("}");

                out.println("a {");
                out.println("display: inline-block;");
                out.println("margin-top: 20px;");
                out.println("padding: 12px 25px;");
                out.println("background-color: #5b8db8;");
                out.println("color: white;");
                out.println("text-decoration: none;");
                out.println("border-radius: 8px;");
                out.println("}");

                out.println("</style>");
                out.println("</head>");

                out.println("<body>");

                out.println("<div class='container'>");

                out.println("<h2>Registration Successful!</h2>");

                out.println("<p>Welcome " + name + "</p>");

                out.println("<p>Your account has been created successfully.</p>");

                out.println("<a href='index.html'>Go to Login</a>");

                out.println("</div>");

                out.println("</body>");
                out.println("</html>");

            } catch (Exception e) {

                // Registration failed
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Registration Failed</title>");
                out.println("</head>");

                out.println("<body>");

                out.println("<h2>Registration Failed</h2>");

                out.println("<p>Error: "
                        + e.getMessage()
                        + "</p>");

                out.println("</body>");
                out.println("</html>");

            } finally {

                try {

                    if (ps != null) {
                        ps.close();
                    }

                    if (con != null) {
                        con.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Handles the HTTP GET method.
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /**
     * Handles the HTTP POST method.
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {

        return "Register Servlet";
    }
}
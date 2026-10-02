/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbanking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author admin
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP GET and POST methods.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {

                Class.forName("com.mysql.jdbc.Driver");

                String url = "jdbc:mysql://localhost:3306/netbanking";
                String username = "root";
                String dbPassword = "";

                con = DriverManager.getConnection(
                        url,
                        username,
                        dbPassword
                );

                String sql
                        = "SELECT * FROM users WHERE email=? AND password=?";

                ps = con.prepareStatement(sql);

                ps.setString(1, email);
                ps.setString(2, password);

                rs = ps.executeQuery();

                if (rs.next()) {

                    HttpSession session = request.getSession();

                    session.setAttribute("email", email);
                    session.setAttribute("name", rs.getString("name"));

                    response.sendRedirect("dashboard.jsp");

                } else {

                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Login Failed</title>");
                    out.println("</head>");
                    out.println("<body>");

                    out.println("<h2>Login Failed</h2>");
                    out.println("<p>Invalid email or password.</p>");
                    out.println("<a href='index.html'>Try Again</a>");

                    out.println("</body>");
                    out.println("</html>");
                }

            } catch (Exception e) {

                out.println("<h2>Database Error</h2>");
                out.println("<p>" + e.getMessage() + "</p>");

            } finally {

                try {

                    if (rs != null) {
                        rs.close();
                    }

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Login Servlet";
    }
}

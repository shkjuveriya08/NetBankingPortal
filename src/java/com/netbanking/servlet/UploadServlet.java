/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbanking.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author admin
 */
@WebServlet(name = "UploadServlet", urlPatterns = {"/UploadServlet"})
@MultipartConfig
public class UploadServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP GET and POST methods.
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            // --------------------------------
            // CHECK LOGIN SESSION
            // --------------------------------

            HttpSession session =
                    request.getSession(false);

            if (session == null ||
                    session.getAttribute("email") == null) {

                out.println("<h2>Please login first.</h2>");
                out.println("<a href='index.html'>Login</a>");

                return;
            }

            String email =
                    (String) session.getAttribute("email");


            // --------------------------------
            // GET UPLOADED FILE
            // --------------------------------

            Part filePart =
                    request.getPart("addressProof");

            if (filePart == null) {

                out.println("<h2>No file was received.</h2>");
                out.println("<a href='upload-address.html'>Try Again</a>");

                return;
            }

            String fileName =
                    filePart.getSubmittedFileName();

            if (fileName == null ||
                    fileName.trim().equals("")) {

                out.println("<h2>No file selected.</h2>");
                out.println("<a href='upload-address.html'>Try Again</a>");

                return;
            }


            // --------------------------------
            // CHECK PDF
            // --------------------------------

            if (!fileName.toLowerCase().endsWith(".pdf")) {

                out.println("<h2>Only PDF files are allowed.</h2>");
                out.println("<a href='upload-address.html'>Try Again</a>");

                return;
            }


            // --------------------------------
            // UPLOAD FOLDER
            // --------------------------------

            String uploadPath =
                    "E:\\COLLEGE\\PRACTICALS\\JEE\\NetBankingPortal\\uploads";

            File uploadDir =
                    new File(uploadPath);

            if (!uploadDir.exists()) {

                if (!uploadDir.mkdirs()) {

                    out.println("<h2>Cannot create uploads folder.</h2>");
                    out.println("<p>Path: "
                            + uploadPath
                            + "</p>");

                    return;
                }
            }


            // --------------------------------
            // CREATE FILE PATH
            // --------------------------------

            String filePath =
                    uploadPath
                    + File.separator
                    + fileName;


            // --------------------------------
            // SAVE PDF FILE
            // --------------------------------

            InputStream inputStream =
                    filePart.getInputStream();

            FileOutputStream outputStream =
                    new FileOutputStream(filePath);

            byte[] buffer =
                    new byte[1024];

            int bytesRead;

            while ((bytesRead =
                    inputStream.read(buffer)) != -1) {

                outputStream.write(
                        buffer,
                        0,
                        bytesRead
                );
            }

            outputStream.close();
            inputStream.close();


            // --------------------------------
            // DATABASE CONNECTION
            // --------------------------------

            Connection con = null;
            PreparedStatement ps = null;

            try {

                Class.forName(
                        "com.mysql.jdbc.Driver"
                );

                String url =
                        "jdbc:mysql://localhost:3306/netbanking";

                String username =
                        "root";

                String dbPassword =
                        "";

                con =
                        DriverManager.getConnection(
                                url,
                                username,
                                dbPassword
                        );


                // --------------------------------
                // INSERT DOCUMENT
                // --------------------------------

                String sql =
                        "INSERT INTO documents "
                        + "(email, filename, filepath) "
                        + "VALUES (?, ?, ?)";

                ps =
                        con.prepareStatement(sql);

                ps.setString(
                        1,
                        email
                );

                ps.setString(
                        2,
                        fileName
                );

                ps.setString(
                        3,
                        filePath
                );


                int result =
                        ps.executeUpdate();


                // --------------------------------
                // SUCCESS
                // --------------------------------

                if (result > 0) {

                    out.println("<!DOCTYPE html>");
                    out.println("<html>");

                    out.println("<head>");
                    out.println("<title>Upload Successful</title>");

                    out.println("<style>");

                    out.println("body {");
                    out.println("font-family: Arial;");
                    out.println("background: #f3f7fb;");
                    out.println("text-align: center;");
                    out.println("padding-top: 80px;");
                    out.println("}");

                    out.println(".box {");
                    out.println("background: white;");
                    out.println("width: 450px;");
                    out.println("margin: auto;");
                    out.println("padding: 35px;");
                    out.println("border-radius: 15px;");
                    out.println("box-shadow: 0 5px 25px rgba(0,0,0,0.08);");
                    out.println("}");

                    out.println("h2 {");
                    out.println("color: #315c8c;");
                    out.println("}");

                    out.println("p {");
                    out.println("color: #555;");
                    out.println("}");

                    out.println("a {");
                    out.println("color: #315c8c;");
                    out.println("text-decoration: none;");
                    out.println("}");

                    out.println("</style>");

                    out.println("</head>");

                    out.println("<body>");

                    out.println("<div class='box'>");

                    out.println("<h2>");
                    out.println("PDF Uploaded Successfully!");
                    out.println("</h2>");

                    out.println("<p>");
                    out.println("File Name: "
                            + fileName);
                    out.println("</p>");

                    out.println("<p>");
                    out.println("Email: "
                            + email);
                    out.println("</p>");

                    out.println("<p>");
                    out.println("Document saved in database successfully.");
                    out.println("</p>");

                    out.println("<br>");

                    out.println("<a href='dashboard.jsp'>");
                    out.println("Back to Dashboard");
                    out.println("</a>");

                    out.println("</div>");

                    out.println("</body>");
                    out.println("</html>");

                } else {

                    out.println("<h2>Database Insert Failed</h2>");
                }


            } catch (Exception e) {

                out.println("<!DOCTYPE html>");
                out.println("<html>");

                out.println("<head>");
                out.println("<title>Database Error</title>");
                out.println("</head>");

                out.println("<body>");

                out.println("<h2>Database Error</h2>");

                out.println("<p>");
                out.println(e.toString());
                out.println("</p>");

                out.println("<p>");
                out.println("Message: "
                        + e.getMessage());
                out.println("</p>");

                out.println("<br>");

                out.println("<a href='upload-address.html'>");
                out.println("Back to Upload");
                out.println("</a>");

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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP GET method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /**
     * Handles the HTTP POST method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Address Proof Upload Servlet";
    }// </editor-fold>

}
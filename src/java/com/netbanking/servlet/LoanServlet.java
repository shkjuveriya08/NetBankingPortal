package com.netbanking.servlet;

import com.netbanking.ejb.LoanCalculatorBean;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LoanServlet", urlPatterns = {"/LoanServlet"})
public class LoanServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {

            // Get EJB using JNDI
            InitialContext context = new InitialContext();

            LoanCalculatorBean loanBean =
                    (LoanCalculatorBean) context.lookup(
                    "java:global/NetBankingPortal/LoanCalculatorBean");

            // Get values from loan form
            double amount =
                    Double.parseDouble(
                    request.getParameter("amount"));

            double rate =
                    Double.parseDouble(
                    request.getParameter("rate"));

            int years =
                    Integer.parseInt(
                    request.getParameter("years"));

            // Calculate interest using EJB
            double interest =
                    loanBean.calculateInterest(
                    amount,
                    rate,
                    years);

            // Calculate total amount using EJB
            double total =
                    loanBean.calculateTotalAmount(
                    amount,
                    rate,
                    years);

            // Display result
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");

            out.println("<title>Loan Calculation Result</title>");

            out.println("<style>");

            out.println("body {");
            out.println("font-family: Arial, sans-serif;");
            out.println("background-color: #f3f7fb;");
            out.println("margin: 0;");
            out.println("padding: 0;");
            out.println("}");

            out.println(".container {");
            out.println("width: 450px;");
            out.println("margin: 80px auto;");
            out.println("background-color: white;");
            out.println("padding: 35px;");
            out.println("border-radius: 15px;");
            out.println("box-shadow: 0 5px 25px rgba(0,0,0,0.08);");
            out.println("}");

            out.println("h2 {");
            out.println("text-align: center;");
            out.println("color: #315c8c;");
            out.println("margin-bottom: 30px;");
            out.println("}");

            out.println(".result {");
            out.println("background-color: #f7f9fc;");
            out.println("padding: 15px;");
            out.println("margin: 10px 0;");
            out.println("border-radius: 8px;");
            out.println("font-size: 16px;");
            out.println("}");

            out.println(".total {");
            out.println("background-color: #eaf3fb;");
            out.println("font-weight: bold;");
            out.println("color: #315c8c;");
            out.println("}");

            out.println(".back {");
            out.println("display: block;");
            out.println("text-align: center;");
            out.println("margin-top: 25px;");
            out.println("text-decoration: none;");
            out.println("color: #315c8c;");
            out.println("font-weight: bold;");
            out.println("}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            out.println("<div class='container'>");

            out.println("<h2>Loan Calculation Result</h2>");

            // %.2f prevents scientific notation such as 2.0E7
            out.println("<div class='result'>");
            out.println("Loan Amount: ₹"
                    + String.format("%.2f", amount));
            out.println("</div>");

            out.println("<div class='result'>");
            out.println("Interest Rate: "
                    + String.format("%.2f", rate) + "%");
            out.println("</div>");

            out.println("<div class='result'>");
            out.println("Loan Period: "
                    + years + " years");
            out.println("</div>");

            out.println("<div class='result'>");
            out.println("Interest Amount: ₹"
                    + String.format("%.2f", interest));
            out.println("</div>");

            out.println("<div class='result total'>");
            out.println("Total Amount: ₹"
                    + String.format("%.2f", total));
            out.println("</div>");

            out.println("<a class='back' href='dashboard.jsp'>");
            out.println("Back to Dashboard");
            out.println("</a>");

            out.println("</div>");

            out.println("</body>");
            out.println("</html>");

        } catch (NamingException e) {

            out.println("<html>");
            out.println("<body>");

            out.println("<h3>EJB Error</h3>");

            out.println("<p>");
            out.println(e.getMessage());
            out.println("</p>");

            out.println("</body>");
            out.println("</html>");

        } catch (NumberFormatException e) {

            out.println("<html>");
            out.println("<body>");

            out.println("<h3>Invalid Input</h3>");

            out.println("<p>");
            out.println("Please enter valid loan amount, rate and years.");
            out.println("</p>");

            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Loan Calculator Servlet";
    }
}
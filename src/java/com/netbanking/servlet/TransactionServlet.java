package com.netbanking.servlet;

import com.netbanking.entity.Transaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TransactionServlet", urlPatterns = {"/TransactionServlet"})
public class TransactionServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {

            emf = Persistence.createEntityManagerFactory("NetBankingPU");

            em = emf.createEntityManager();

            String action = request.getParameter("action");

            /*
             * INSERT TRANSACTION
             */
            if ("insert".equals(action)) {

                String email = request.getParameter("email");
                String type = request.getParameter("type");

                double amount = Double.parseDouble(
                        request.getParameter("amount"));

                Transaction t = new Transaction();

                t.setEmail(email);
                t.setType(type);
                t.setAmount(amount);

                em.getTransaction().begin();

                em.persist(t);

                em.getTransaction().commit();

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Transaction Successful</title>");

                out.println("<style>");

                out.println("body {");
                out.println("font-family: Arial, sans-serif;");
                out.println("background-color: #f3f7fb;");
                out.println("margin: 0;");
                out.println("padding: 0;");
                out.println("}");

                out.println(".container {");
                out.println("width: 450px;");
                out.println("margin: 70px auto;");
                out.println("background: white;");
                out.println("padding: 35px;");
                out.println("border-radius: 15px;");
                out.println("box-shadow: 0 5px 25px rgba(0,0,0,0.08);");
                out.println("}");

                out.println("h2 {");
                out.println("text-align: center;");
                out.println("color: #315c8c;");
                out.println("}");

                out.println(".success {");
                out.println("text-align: center;");
                out.println("color: #3d7a52;");
                out.println("font-weight: bold;");
                out.println("}");

                out.println(".details {");
                out.println("background-color: #f7f9fc;");
                out.println("padding: 15px;");
                out.println("border-radius: 8px;");
                out.println("margin-top: 20px;");
                out.println("}");

                out.println(".btn {");
                out.println("display: block;");
                out.println("text-align: center;");
                out.println("margin-top: 15px;");
                out.println("padding: 12px;");
                out.println("background-color: #5b8db8;");
                out.println("color: white;");
                out.println("text-decoration: none;");
                out.println("border-radius: 8px;");
                out.println("}");

                out.println("</style>");
                out.println("</head>");

                out.println("<body>");

                out.println("<div class='container'>");

                out.println("<h2>Transaction Successful</h2>");

                out.println("<p class='success'>");
                out.println("Transaction saved successfully!");
                out.println("</p>");

                out.println("<div class='details'>");

                out.println("<p><b>Email:</b> "
                        + email + "</p>");

                out.println("<p><b>Transaction Type:</b> "
                        + type + "</p>");

                out.println("<p><b>Amount:</b> ₹"
                        + String.format("%.2f", amount)
                        + "</p>");

                out.println("</div>");

                out.println("<a class='btn' "
                        + "href='TransactionServlet?action=view'>");
                out.println("View Transaction History");
                out.println("</a>");

                out.println("<a class='btn' "
                        + "href='TransactionServlet'>");
                out.println("New Transaction");
                out.println("</a>");

                out.println("<a class='btn' "
                        + "href='dashboard.jsp'>");
                out.println("Back to Dashboard");
                out.println("</a>");

                out.println("</div>");

                out.println("</body>");
                out.println("</html>");
            }

            /*
             * VIEW TRANSACTIONS
             */
            else if ("view".equals(action)) {

                List<Transaction> transactions =
                        em.createQuery(
                        "SELECT t FROM Transaction t",
                        Transaction.class)
                        .getResultList();

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");

                out.println("<title>Transaction History</title>");

                out.println("<style>");

                out.println("body {");
                out.println("font-family: Arial, sans-serif;");
                out.println("background-color: #f3f7fb;");
                out.println("margin: 0;");
                out.println("padding: 30px;");
                out.println("}");

                out.println(".container {");
                out.println("max-width: 950px;");
                out.println("margin: auto;");
                out.println("background: white;");
                out.println("padding: 30px;");
                out.println("border-radius: 15px;");
                out.println("box-shadow: 0 5px 25px rgba(0,0,0,0.08);");
                out.println("}");

                out.println("h2 {");
                out.println("text-align: center;");
                out.println("color: #315c8c;");
                out.println("}");

                out.println("table {");
                out.println("width: 100%;");
                out.println("border-collapse: collapse;");
                out.println("margin-top: 20px;");
                out.println("}");

                out.println("th, td {");
                out.println("padding: 12px;");
                out.println("border: 1px solid #d8dee8;");
                out.println("text-align: center;");
                out.println("}");

                out.println("th {");
                out.println("background-color: #eaf3fb;");
                out.println("color: #315c8c;");
                out.println("}");

                out.println("tr:nth-child(even) {");
                out.println("background-color: #f8fafc;");
                out.println("}");

                out.println(".btn {");
                out.println("display: inline-block;");
                out.println("margin: 20px 5px 0;");
                out.println("padding: 12px 20px;");
                out.println("background-color: #5b8db8;");
                out.println("color: white;");
                out.println("text-decoration: none;");
                out.println("border-radius: 8px;");
                out.println("}");

                out.println("</style>");

                out.println("</head>");
                out.println("<body>");

                out.println("<div class='container'>");

                out.println("<h2>Transaction History</h2>");

                out.println("<table>");

                out.println("<tr>");

                out.println("<th>ID</th>");
                out.println("<th>Email</th>");
                out.println("<th>Transaction Type</th>");
                out.println("<th>Amount</th>");
                out.println("<th>Date</th>");

                out.println("</tr>");

                for (Transaction t : transactions) {

                    out.println("<tr>");

                    out.println("<td>"
                            + t.getId()
                            + "</td>");

                    out.println("<td>"
                            + t.getEmail()
                            + "</td>");

                    out.println("<td>"
                            + t.getType()
                            + "</td>");

                    out.println("<td>₹"
                            + String.format("%.2f", t.getAmount())
                            + "</td>");

                    out.println("<td>"
                            + t.getTransaction_date()
                            + "</td>");

                    out.println("</tr>");
                }

                out.println("</table>");

                out.println("<a class='btn' "
                        + "href='TransactionServlet'>");
                out.println("New Transaction");
                out.println("</a>");

                out.println("<a class='btn' "
                        + "href='dashboard.jsp'>");
                out.println("Back to Dashboard");
                out.println("</a>");

                out.println("</div>");

                out.println("</body>");
                out.println("</html>");
            }

            /*
             * DEFAULT - SHOW TRANSACTION FORM
             */
            else {

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");

                out.println("<title>Transaction Management</title>");

                out.println("<style>");

                out.println("body {");
                out.println("font-family: Arial, sans-serif;");
                out.println("background-color: #f3f7fb;");
                out.println("margin: 0;");
                out.println("padding: 0;");
                out.println("}");

                out.println(".container {");
                out.println("width: 450px;");
                out.println("margin: 60px auto;");
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

                out.println("label {");
                out.println("display: block;");
                out.println("margin-top: 15px;");
                out.println("margin-bottom: 6px;");
                out.println("color: #444;");
                out.println("}");

                out.println("input, select {");
                out.println("width: 100%;");
                out.println("padding: 12px;");
                out.println("box-sizing: border-box;");
                out.println("border: 1px solid #d8dee8;");
                out.println("border-radius: 8px;");
                out.println("}");

                out.println("button {");
                out.println("width: 100%;");
                out.println("padding: 12px;");
                out.println("margin-top: 25px;");
                out.println("border: none;");
                out.println("border-radius: 8px;");
                out.println("background-color: #5b8db8;");
                out.println("color: white;");
                out.println("font-size: 15px;");
                out.println("cursor: pointer;");
                out.println("}");

                out.println(".link {");
                out.println("display: block;");
                out.println("text-align: center;");
                out.println("margin-top: 20px;");
                out.println("color: #315c8c;");
                out.println("text-decoration: none;");
                out.println("font-weight: bold;");
                out.println("}");

                out.println("</style>");

                out.println("</head>");
                out.println("<body>");

                out.println("<div class='container'>");

                out.println("<h2>Transaction Management</h2>");

                out.println("<form action='TransactionServlet' method='post'>");

                out.println("<input type='hidden' "
                        + "name='action' value='insert'>");

                out.println("<label>Email</label>");

                out.println("<input type='email' "
                        + "name='email' required>");

                out.println("<label>Transaction Type</label>");

                out.println("<select name='type' required>");

                out.println("<option value=''>Select Transaction Type</option>");
                out.println("<option value='Deposit'>Deposit</option>");
                out.println("<option value='Withdrawal'>Withdrawal</option>");
                out.println("<option value='Transfer'>Transfer</option>");

                out.println("</select>");

                out.println("<label>Amount</label>");

                out.println("<input type='number' "
                        + "name='amount' "
                        + "step='0.01' "
                        + "min='1' required>");

                out.println("<button type='submit'>");
                out.println("Save Transaction");
                out.println("</button>");

                out.println("</form>");

                out.println("<a class='link' "
                        + "href='TransactionServlet?action=view'>");
                out.println("View Transaction History");
                out.println("</a>");

                out.println("<a class='link' "
                        + "href='dashboard.jsp'>");
                out.println("Back to Dashboard");
                out.println("</a>");

                out.println("</div>");

                out.println("</body>");
                out.println("</html>");
            }

        } catch (NumberFormatException e) {

            out.println("<h2>Invalid Amount</h2>");
            out.println("<p>Please enter a valid transaction amount.</p>");

        } catch (PersistenceException e) {

            out.println("<h2>Database Error</h2>");
            out.println("<p>Unable to save or retrieve transaction.</p>");
            out.println("<p>" + e.getMessage() + "</p>");

        } catch (Exception e) {

            out.println("<h2>Transaction Error</h2>");
            out.println("<p>" + e.getMessage() + "</p>");

        } finally {

            if (em != null) {

                if (em.isOpen()) {
                    em.close();
                }
            }

            if (emf != null) {

                if (emf.isOpen()) {
                    emf.close();
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

        return "Transaction Servlet";
    }
}
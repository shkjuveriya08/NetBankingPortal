<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("email") == null) {
        response.sendRedirect("index.html");
        return;
    }

    String name = (String) session.getAttribute("name");
    String email = (String) session.getAttribute("email");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Net Banking Dashboard</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
<div class="dashboard">
    <h1>Net Banking</h1>
    <div class="welcome">
        <h2>Welcome, <%= name %></h2>
        <p>
            Account Email:
            <strong><%= email %></strong>
        </p>=
    </div>
    <div class="menu">
        <a href="upload-address.html">
            <div class="card">
                <h3>Address Proof</h3>
                <p>Upload your proof of address PDF.</p>
            </div>
        </a>
        <a href="loan.html">
            <div class="card">
                <h3>Loan Calculator</h3>
                <p>Calculate your loan interest.</p>
            </div>
        </a>
        <a href="TransactionServlet">
            <div class="card">
                <h3>Transactions</h3>
                <p>View your transaction history.</p>
            </div>
        </a>
        <a href="LogoutServlet">
            <div class="card">
                <h3>Logout</h3>
                <p>Logout from your account.</p>
            </div>
        </a>
    </div>
</div>
</body>
</html>
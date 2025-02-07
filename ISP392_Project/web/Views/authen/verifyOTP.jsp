<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Verify OTP</title>
        <link rel="stylesheet" href="/styles/style.css"> <!-- Thêm CSS nếu có -->
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f4f4;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
            }
            .container {
                background: #fff;
                padding: 25px;
                border-radius: 10px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                width: 400px;
                text-align: center;
            }
            .error {
                color: red;
                margin-bottom: 15px;
            }
            input[type="text"] {
                width: 100%;
                padding: 12px;
                margin: 12px 0;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 16px;
                box-sizing: border-box;
                text-align: center;
            }
            button {
                width: 100%;
                padding: 12px;
                background: #007bff;
                color: white;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 16px;
                transition: background 0.3s;
            }
            button:hover {
                background: #0056b3;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Verify OTP</h2>
            <p>Enter the OTP sent to your email to proceed.</p>


            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>

            <form action="verifyOTP" method="post">
                <input type="text" name="otp" placeholder="Enter OTP" required>
                <button type="submit">Verify OTP</button>
            </form>
        </div>
    </body>
</html>

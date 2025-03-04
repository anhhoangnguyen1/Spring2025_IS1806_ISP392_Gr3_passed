<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
            .success {
                color: green;
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
                margin-bottom: 10px;
            }
            button:hover {
                background: #0056b3;
            }
            button:disabled {
                background: #cccccc;
                cursor: not-allowed;
            }
            .timer {
                margin-top: 10px;
                font-size: 14px;
                color: #666;
            }
            #resendBtn {
                background-color: #6c757d;
            }
            #resendBtn:hover {
                background-color: #5a6268;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Verify OTP</h2>
            <p>Enter the verification code sent to your email.</p>
            
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            
            <c:if test="${not empty message}">
                <p class="success">${message}</p>
            </c:if>

            <form action="verifyOTP" method="post">
                <input type="text" name="otp" id="otpInput" placeholder="Enter OTP" required>
                <div class="timer">Time remaining: <span id="countdown">60</span> seconds</div>
                <button type="submit" id="submitBtn">Verify OTP</button>
            </form>
            
            <form action="resendOTP" method="post">
                <input type="hidden" name="email" value="${email}">
                <button type="submit" id="resendBtn" disabled>Resend OTP</button>
            </form>
            
            <script>
                // Timer functionality
                let timeLeft = 60;
                const countdownEl = document.getElementById('countdown');
                const resendBtn = document.getElementById('resendBtn');
                const otpInput = document.getElementById('otpInput');
                const submitBtn = document.getElementById('submitBtn');
                
                function updateTimer() {
                    countdownEl.textContent = timeLeft;
                    
                    if (timeLeft <= 0) {
                        // Disable OTP input and submit when time expires
                        otpInput.disabled = true;
                        submitBtn.disabled = true;
                        
                        // Enable resend button
                        resendBtn.disabled = false;
                        
                        // Clear the interval
                        clearInterval(timerInterval);
                        countdownEl.textContent = "Expired";
                    }
                    timeLeft--;
                }
                
                // Start the countdown
                updateTimer();
                const timerInterval = setInterval(updateTimer, 1000);
                
                // If there's an error message about maximum attempts, disable verify button and enable resend button
                <c:if test="${error != null && error.contains('Maximum attempts reached')}">
                    otpInput.disabled = true;
                    submitBtn.disabled = true;
                    resendBtn.disabled = false;
                </c:if>
            </script>
        </div>
    </body>
</html>

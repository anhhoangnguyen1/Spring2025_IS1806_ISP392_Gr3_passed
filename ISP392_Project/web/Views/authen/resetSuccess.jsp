<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="refresh" content="3;url=${pageContext.request.contextPath}/views/loginServlet">
        <title>Password Reset Success</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/izitoast/1.4.0/css/iziToast.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
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
            .success-container {
                background: #fff;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                width: 400px;
                text-align: center;
            }
            .success-icon {
                color: #4CAF50;
                font-size: 60px;
                margin-bottom: 20px;
            }
            h2 {
                color: #333;
                margin-bottom: 15px;
            }
            p {
                color: #666;
                margin-bottom: 20px;
            }
            .redirect-text {
                font-size: 14px;
                color: #888;
                margin-top: 20px;
            }
            .countdown {
                font-weight: bold;
                color: #007bff;
            }
        </style>
    </head>
    <body>
        <div class="success-container">
            <div class="success-icon">
                <i class="fas fa-check-circle"></i>
            </div>
            <h2>Password Reset Successful</h2>
            <p>${successMessage}</p>
            <p class="redirect-text">You will be redirected to the login page in <span class="countdown" id="countdown">3</span> seconds....</p>
        </div>

        <script>
            // Countdown timer
            let seconds = 3;
            const countdownElement = document.getElementById('countdown');

            const interval = setInterval(function () {
                seconds--;
                countdownElement.textContent = seconds;

                if (seconds <= 0) {
                    clearInterval(interval);
                }
            }, 1000);
        </script>
    </body>
</html> 
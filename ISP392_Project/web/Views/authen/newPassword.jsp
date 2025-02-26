<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/izitoast/1.4.0/css/iziToast.min.css">
        <title>Reset Password</title>
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
            input[type="password"] {
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
            .password-requirements {
                text-align: left;
                margin: 10px 0;
                font-size: 14px;
                color: #666;
            }
            .password-match {
                font-size: 14px;
                margin-top: 5px;
                display: none;
            }
            .match-success {
                color: green;
            }
            .match-error {
                color: red;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Reset Password</h2>
            <p>Enter your new password to reset your account.</p>

            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>

            <form action="newPassword" method="post" id="resetPasswordForm">
                <input type="password" name="new_password" id="newPassword" placeholder="Enter new password" required>
                <input type="password" name="confirm_password" id="confirmPassword" placeholder="Confirm new password" required>
                <div class="password-match" id="passwordMatch"></div>
                <button type="submit" id="submitBtn">Reset Password</button>
            </form>

            <script>
                const newPasswordInput = document.getElementById('newPassword');
                const confirmPasswordInput = document.getElementById('confirmPassword');
                const passwordMatchDiv = document.getElementById('passwordMatch');
                const submitBtn = document.getElementById('submitBtn');
                const form = document.getElementById('resetPasswordForm');

                // Check if passwords match
                function checkPasswordsMatch() {
                    const newPassword = newPasswordInput.value;
                    const confirmPassword = confirmPasswordInput.value;

                    if (confirmPassword.length > 0) {
                        passwordMatchDiv.style.display = 'block';

                        if (newPassword === confirmPassword) {
                            passwordMatchDiv.textContent = 'Passwords match';
                            passwordMatchDiv.className = 'password-match match-success';
                        } else {
                            passwordMatchDiv.textContent = 'Passwords do not match';
                            passwordMatchDiv.className = 'password-match match-error';
                        }
                    } else {
                        passwordMatchDiv.style.display = 'none';
                    }
                }

                // Check passwords match when confirm password changes
                confirmPasswordInput.addEventListener('input', checkPasswordsMatch);
                newPasswordInput.addEventListener('input', checkPasswordsMatch);

                // Form submission validation - only check if passwords match
                form.addEventListener('submit', function (event) {
                    const newPassword = newPasswordInput.value;
                    const confirmPassword = confirmPasswordInput.value;

                    // Only check if passwords match
                    if (newPassword !== confirmPassword) {
                        event.preventDefault();
                        alert('Passwords do not match. Please try again.');
                    }
                });
            </script>

            <script src="https://cdnjs.cloudflare.com/ajax/libs/izitoast/1.4.0/js/iziToast.min.js"></script>
            <script>
                document.addEventListener('DOMContentLoaded', function() {
                    var toastMessage = "${sessionScope.toastMessage}";
                    var toastType = "${sessionScope.toastType}";
                    if (toastMessage) {
                        iziToast.show({
                            title: toastType === 'success' ? 'Success' : 'Error',
                            message: toastMessage,
                            position: 'topRight',
                            color: toastType === 'success' ? 'green' : 'red',
                            timeout: 5000,
                            onClosing: function() {
                                fetch('${pageContext.request.contextPath}/remove-toast', {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/x-www-form-urlencoded',
                                    },
                                }).then(response => {
                                    if (!response.ok) {
                                        console.error('Failed to remove toast attributes');
                                    }
                                }).catch(error => {
                                    console.error('Error:', error);
                                });
                            }
                        });
                    }
                });
            </script>
        </div>
    </body>
</html>

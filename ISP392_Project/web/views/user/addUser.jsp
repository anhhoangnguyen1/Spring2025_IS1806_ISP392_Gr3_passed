<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add Staff</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    </head>
    <style>
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
    <body>
        <div class="page-wrapper">
            <!--   *** Top Bar Starts ***   -->
            <div class="top-bar">
                <div class="top-bar-left">
                    <div class="hamburger-btn">
                        <span></span>
                        <span></span>
                        <span></span>
                    </div>
                    <div class="logo">
                        <img src="/ISP392_Project/views/dashboard/images/logo.png" style="width: 170px; height: 70px" />
                    </div>
                </div>
                <div class="top-bar-right">
                    <div class="mode-switch">
                        <i class="fa-solid fa-moon"></i>
                    </div>
                    <div class="notifications">
                        <i class="fa-solid fa-bell"></i>
                    </div>
                    <div class="profile">
                        <img src="/ISP392_Project/views/dashboard/images/profile-img.jpg" id="profile-img" />
                        <div class="profile-menu">
                            <ul>
                                <li><a href="/ISP392_Project/user"><i class="fa-solid fa-pen"></i> User Profile</a></li>
                                <li><a href="/ISP392_Project/change-password"><i class="fa-solid fa-pen"></i> Change Password</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <aside class="side-bar">
                <span class="menu-label">MENU</span>
                <ul class="navbar-links navbar-links-1">
                    <li>
                        <a href="/ISP392_Project/dashboard">
                            <span class="nav-icon">
                                <i class="fa-solid fa-house"></i>
                            </span>
                            <span class="nav-text">Dashboard</span>
                        </a>
                    </li>
                    <li class="#">
                        <a href="/ISP392_Project/Products">
                            <span class="nav-icon">
                                <i class="fas fa-box"></i>
                            </span>
                            <span class="nav-text">Products</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <span class="nav-icon">
                                <i class="fa-solid fa-table"></i>
                            </span>
                            <span class="nav-text">Zones</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <span class="nav-icon">
                                <i class="fa-solid fa-user"></i>
                            </span>
                            <span class="nav-text">Customers</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <span class="nav-icon">
                                <i class="fa-solid fa-file-invoice"></i>
                            </span>
                            <span class="nav-text">Invoices</span>
                        </a>
                    </li>
                    <li>
                        <a href="/ISP392_Project/Debts">
                            <span class="nav-icon">
                                <i class="fa-solid fa-money-bill"></i>
                            </span>
                            <span class="nav-text">Debts History</span>
                        </a>
                    </li>
                </ul>
                <span class="menu-label">OWNER ZONE</span>
                <ul class="navbar-links navbar-links-2">
                    <li>
                        <a href="#">
                            <span class="nav-icon">
                                <i class="fa-solid fa-user-tie"></i>
                            </span>
                            <span class="nav-text">Staffs</span>
                        </a>
                    </li>
                </ul>
                <!--   === Side Bar Footer Starts ===   -->
                <div class="sidebar-footer">
                    <div class="settings">
                        <span class="gear-icon">
                            <i class="fa-solid fa-gear"></i>
                        </span>
                        <span class="text">Settings</span>
                    </div>
                    <div class="logoutBtn">
                        <a href="/ISP392_Project/logout">
                            <span class="logout-icon">
                                <i class="fa-solid fa-right-from-bracket"></i>
                            </span>
                            <span class="text"><a href="/ISP392_Project/logout">Logout</a></span>
                    </div>
                </div>
                <!--   === Side Bar Footer Ends ===   -->
            </aside>
            <div class="contents">
                <div class="panel-bar1">
                    <h2>Create Account</h2>
                    <!-- Show error messages if any -->
                    <c:if test="${not empty usernameError}">
                        <div class="alert alert-danger">${usernameError}</div>
                    </c:if>
                    <c:if test="${not empty passwordError}">
                        <div class="alert alert-danger">${passwordError}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/Users" method="POST" enctype="multipart/form-data">
                        <input type="hidden" name="service" value="createAccount"/>
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input type="text" class="form-control" name="username" required>
                        </div>

                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" class="form-control" name="password"  id="password" required>
                        </div>

                        <div class="form-group">
                            <label for="confirmPassword">Confirm password</label>
                            <input type="password" class="form-control" name="confirmPassword" id="confirmPassword" required>
                            <div class="password-match" id="passwordMatch"></div>
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-primary" style="background-color: #007bff ">Register</button>
                            <a href="${pageContext.request.contextPath}/Users?service=users" class="btn btn-secondary">Back to Users list</a>
                        </div>
                    </form>
                        
                    <script>
                        const passwordInput = document.getElementById('password');
                        const confirmPasswordInput = document.getElementById('confirmPassword');
                        const passwordMatchDiv = document.getElementById('passwordMatch');
                        const submitBtn = document.getElementById('submitBtn');
                        const form = document.getElementById('resetPasswordForm');

                        // Check if passwords match
                        function checkPasswordsMatch() {
                            const password = passwordInput.value;
                            const confirmPassword = confirmPasswordInput.value;

                            if (confirmPassword.length > 0) {
                                passwordMatchDiv.style.display = 'block';

                                if (password === confirmPassword) {
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
                        passwordInput.addEventListener('input', checkPasswordsMatch);

                        // Form submission validation - only check if passwords match
                        form.addEventListener('submit', function (event) {
                            const password = passwordInput.value;
                            const confirmPassword = confirmPasswordInput.value;

                            // Only check if passwords match
                            if (password !== confirmPassword) {
                                event.preventDefault();
                                alert('Passwords do not match. Please try again.');
                            }
                        });
                    </script>
                    
                </div>
            </div>
            <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
            <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
            <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
    </body>
</html>

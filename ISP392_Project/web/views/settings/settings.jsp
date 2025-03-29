<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Settings</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    </head>
    <body>
        <div class="page-wrapper">
            <!-- Top Bar Starts -->
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
            <!-- Top Bar Ends -->

            <!-- Side Bar Starts -->
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
                    <li>
                        <a href="/ISP392_Project/Products">
                            <span class="nav-icon">
                                <i class="fas fa-box"></i>
                            </span>
                            <span class="nav-text">Products</span>
                        </a>
                    </li>
                    <li>
                        <a href="/ISP392_Project/zones">
                            <span class="nav-icon">
                                <i class="fa-solid fa-table"></i>
                            </span>
                            <span class="nav-text">Zones</span>
                        </a>
                    </li>
                    <li>
                        <a href="/ISP392_Project/Customers">
                            <span class="nav-icon">
                                <i class="fa-solid fa-user"></i>
                            </span>
                            <span class="nav-text">Customers</span>
                        </a>
                    </li>
                    <li>
                        <a href="sale">
                            <span class="nav-icon">
                                <i class="fa-solid fa-file-invoice"></i>
                            </span>
                            <span class="nav-text">Orders</span>
                        </a>
                    </li>
                    <li>
                        <a href="orders">
                            <span class="nav-icon">
                                <i class="fa-solid fa-file-invoice"></i>
                            </span>
                            <span class="nav-text">Orders History</span>
                        </a>
                    </li>
                    <li>
                        <a href="/ISP392_Project/Imports">
                            <span class="nav-icon">
                                <i class="fa-solid fa-file-invoice"></i>
                            </span>
                            <span class="nav-text">Imports</span>
                        </a>
                    </li>
                    <li>
                        <a href="/ISP392_Project/Stores">
                            <span class="nav-icon">
                                <i class="fa-solid fa-store"></i>
                            </span>
                            <span class="nav-text">Stores</span>
                        </a>
                    </li>
                    <c:if test="${sessionScope.role == 'owner'}">
                        <li>
                            <a href="/ISP392_Project/Users">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-user-tie"></i>
                                </span>
                                <span class="nav-text">Staffs</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
                <div class="sidebar-footer">
                    <div class="settings active">
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
            </aside>
            <!-- Side Bar Ends -->

            <!-- Contents Section Starts -->
            <div class="contents">
                <div class="panel-bar">
                    <div class="row-1">
                        <h1>Settings</h1>
                    </div>
                </div>

                <div class="settings-container">
                    <div class="settings-section">
                        <h2>Low Stock Alert Settings</h2>
                        <form action="${pageContext.request.contextPath}/settings" method="POST">
                            <input type="hidden" name="service" value="updateLowStockThreshold">
                            <div class="form-group">
                                <label for="lowStockThreshold">Low Stock Threshold (units)</label>
                                <input type="number" class="form-control" id="lowStockThreshold" name="lowStockThreshold" 
                                       value="${lowStockThreshold}" min="1" required>
                                <small class="form-text text-muted">
                                    Products with quantity below this number will be shown in the low stock alert.
                                </small>
                            </div>
                            <button type="submit" class="btn btn-primary">Save Changes</button>
                        </form>
                    </div>
                </div>
            </div>
            <!-- Contents Section Ends -->
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
    </body>
</html> 
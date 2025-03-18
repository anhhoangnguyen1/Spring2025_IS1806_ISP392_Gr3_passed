<%-- 
    Document   : profile
    Created on : Feb 4, 2025, 1:51:14 PM
    Author     : THC
--%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="entity.Users"%>
<!DOCTYPE html>
<html>
    <head>
        <title>User Profile</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    </head>
    <body>
        <!--   *** Page Wrapper Starts ***   -->
        <div class="page-wrapper">
            <!--   *** Top Bar Starts ***   -->
            <div class="top-bar">
                <div class="top-bar-left">
                    <div class="hamburger-btn">
                        <span></span>
                        <span></span>
                        <span></span>
                    </div>
                    <div class="logo mr-5">
                        <img src="/ISP392_Project/views/customer/images/logo.png" style="width: 170px; height: 70px" />
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
                        <img src="/ISP392_Project/views/customer/images/profile-img.jpg" />
                    </div>
                </div>
            </div>
            <!--   *** Top Bar Ends ***   -->

            <!--   === Side Bar Starts ===   -->
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
                        <a href="/ISP392_Project/Zones">
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
                        <a href="/ISP392_Project/Invoices">
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
                <div class="sidebar-footer">
                    <div class="settings">
                        <span class="gear-icon">
                            <i class="fa-solid fa-gear"></i>
                        </span>
                        <span class="text">Settings</span>
                    </div>
                    <div class="logoutBtn">
                        <span class="logout-icon">
                            <i class="fa-solid fa-right-from-bracket"></i>
                        </span>
                        <span class="text">Logout</span>
                    </div>
                </div>
            </aside>
            <!--   === Side Bar Ends ===   -->

            <!--   === Profile Content Starts ===   -->
            <div class="contents">
                <div class="panel-bar1">
                    <h2>User Profile</h2>

                    <div class="profile-container">
                        <!-- Avatar Section -->
                        <style>
                            .avatar {
                                width: 100px; /* Adjust width */
                                height: 100px; /* Adjust height */
                                background-size: cover;
                                background-position: center;
                                border-radius: 50%; /* Makes the image round */
                                border: 2px solid #007bff; /* Optional: Adds a border around the avatar */
                            }
                        </style>

                        <div class="avatar-section">
                            <div class="avatar" style="background-image: url('${pageContext.request.contextPath}/avatars/${user.image}');"></div>
                            <div class="user-name"><c:out value="${user.name}" /></div>
                        </div>
                        <!-- User Info Section -->
                        <div class="user-info">
                            <p class="info"><strong>Name:</strong> <c:out value="${user.name}" /></p>
                            <p class="info"><strong>Role:</strong> <c:out value="${user.role}" /></p>
                            <p class="info"><strong>Gender:</strong> <c:out value="${user.gender}" /></p>
                            <p class="info"><strong>Date of Birth:</strong> <c:out value="${user.dob}" /></p>
                            <p class="info"><strong>Phone:</strong> <c:out value="${user.phone}" /></p>
                            <p class="info"><strong>Email:</strong> <c:out value="${user.email}" /></p>
                            <p class="info"><strong>Address:</strong> <c:out value="${user.address}" /></p>
                            <p class="info"><strong>Status:</strong> <c:out value="${user.status}" /></p>
                            <a href="editprofile" class="button">Edit Profile</a>
                            <a href="<%= request.getContextPath() %>/dashboard" class="button back-button">Back</a>
                        </div>
                    </div>
                </div>
            </div>
            <!--   === Profile Content Ends ===   -->
        </div>
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
    </body>
</html>


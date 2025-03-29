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
                    <c:if test="${sessionScope.role == 'owner' or sessionScope.role == 'staff'}">
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
                    </c:if>
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
                    <h1>Profile</h1>

                    <div class="profile-container">
                        <!-- Avatar Section -->
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

                            .avatar {
                                width: 150px;
                                height: 150px;
                                border-radius: 50%;
                                border: 3px solid #007BFF;
                                background-size: cover;
                                background-position: center;
                                background-repeat: no-repeat;
                                display: inline-block;
                                margin-bottom: 15px;
                            }

                            h1 {
                                font-size: 32px;
                                font-weight: 500;
                                color: #333;
                                margin-bottom: 40px;
                            }

                            h2 {
                                font-size: 20px;
                                font-weight: 500;
                                color: #777;
                               
                            }

                            .profile-container {
                                display: flex;
                                flex-direction: column;
                                gap: 20px;
                            }

                            .profile-header {
                                display: flex;
                                align-items: flex-start;
                                gap: 20px;
                            }

                            .form-container {
                                flex: 1;
                                order: -1; 
                            }

                            .avatar-section {
                                order: 1; 
                            }

                            .form-row {
                                display: flex;
                                gap: 15px;
                                margin-bottom: 15px;
                            }

                            .form-group {
                                flex: 1;
                                display: flex;
                                flex-direction: column;
                            }

                            .form-group label {
                                font-size: 14px;
                                font-weight: bold; 
                                color: #333;
                                margin-bottom: 8px;
                            }

                            .form-group .info {
                                font-size: 18px;
                                color: #333;
                                margin-bottom: 10px;
                            }


                           

                            .btn:hover {
                                background-color: #0056b3;
                            }

                            @media (max-width: 768px) {
                                .profile-header {
                                    flex-direction: column;
                                    align-items: center;
                                }

                                .form-row {
                                    flex-direction: column;
                                    gap: 15px;
                                }

                                body {
                                    padding: 20px;
                                }
                            }
                        </style>

                        <div class="profile-container">
                            <div class="profile-header">
                                <div class="avatar-section">
                                    <div class="avatar" style="background-image: url('${pageContext.request.contextPath}/avatars/${user.image}');"></div>
                                </div>
                                <div class="form-container">
                                    <h2>Personal Information</h2>

                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="name">Name</label>
                                            <p class="info"><c:out value="${user.name}" /></p>
                                        </div>
                                        <div class="form-group">
                                            <label for="gender">Gender</label>
                                            <p class="info"><c:out value="${user.gender}" /></p>
                                        </div>
                                    </div>

                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="dob">Date of Birth</label>
                                            <p class="info"><c:out value="${user.dob}" /></p>
                                        </div>

                                        <div class="form-group">
                                            <label for="email">Email</label>
                                            <p class="info"><c:out value="${user.email}" /></p>
                                        </div>
                                    </div>

                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="phone">Phone</label>
                                            <p class="info"><c:out value="${user.phone}" /></p>
                                        </div>
                                        <div class="form-group">
                                            <label for="address">Address</label>
                                            <p class="info"><c:out value="${user.address}" /></p>
                                        </div>
                                    </div>


                                    <a href="${pageContext.request.contextPath}/views/profile/editProfile.jsp" class="btn btn-primary" style="background-color: #007bff ">Edit Profile</a>
                                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary" style="background-color: #6c757d ">Back</a>
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


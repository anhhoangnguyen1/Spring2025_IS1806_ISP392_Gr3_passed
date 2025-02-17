<!-- <%-- 
    Document : dashboard.jsp 
    Created on : Jan 28, 2025, 10:04:28 AM 
    Author :
    bsd12418 
--%> 
<%@page import="java.util.List" %>
<%@page import="java.lang.String" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%> -->
<!DOCTYPE html>
<html>
    <head>
        <!--   ***** Link To Custom Stylesheet *****   -->
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/views/dashboard/style.css" />

        <!-- *******  Font Awesome Icons Link  ******* -->
        <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"
            />

        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Dashboard</title>
    </head>
    <body>

        <% 
        List<String[]> topProducts = (List<String[]>) request.getAttribute("topProducts");
        %>
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
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <!--   *** Top Bar Ends ***   -->

            <!--   === Side Bar Starts ===   -->
            <aside class="side-bar">
                <!--   === Nav Bar Links Starts ===   -->
                <span class="menu-label">MENU</span>
                <ul class="navbar-links navbar-links-1">
                    <li class="active">
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
                        <a href="#">
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
                        <a href="/ISP392_Project/Users">
                            <span class="nav-icon">
                                <i class="fa-solid fa-user-tie"></i>
                            </span>
                            <span class="nav-text">Staffs</span>
                        </a>
                    </li>
                </ul>
                <!--   === Nav Bar Links Ends ===   -->
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
            <!--   === Side Bar Ends ===   -->

            <!--   === Contents Section Starts ===   -->
            <div class="contents">
                <!--   === Panel Bar Starts ===   -->
                <div class="panel-bar">
                    <div class="row-1">
                        <h1>Dashboard</h1>
                    </div>
                    <div class="row-2">
                        <a href="#" class="active">Overwiew</a>
                    </div>
                </div>
                <!--   === Panel Bar Ends ===   -->
                <!--   === Description Starts ===   -->
                <div class="description">
                    <!--   === Column 1 Starts ===   -->
                    <div class="col-1">
                        <!--   === Boxes Row Starts ===   -->
                        <div class="boxes-row">
                            <div class="balance-box">
                                <div class="subject-row">
                                    <div class="text">
                                        <h3>Total Income</h3>
                                        <h1>$70,452<sub>(USD)</sub></h1>
                                    </div>
                                    <div class="icon">
                                        <i class="fa-solid fa-arrow-up"></i>
                                    </div>
                                </div>
                                <div class="progress-row">
                                    <div class="subject">progress</div>
                                    <div class="progress-bar">
                                        <div
                                            class="progress-line"
                                            value="91%"
                                            style="max-width: 91%"
                                            ></div>
                                    </div>
                                </div>
                            </div>

                            <div class="balance-box">
                                <div class="subject-row">
                                    <div class="text">
                                        <h3>Total Expense</h3>
                                        <h1>$64,261<sub>(USD)</sub></h1>
                                    </div>
                                    <div class="icon">
                                        <i class="fa-solid fa-arrow-down"></i>
                                    </div>
                                </div>
                                <div class="progress-row">
                                    <div class="subject">progress</div>
                                    <div class="progress-bar">
                                        <div
                                            class="progress-line"
                                            value="73%"
                                            style="max-width: 73%"
                                            ></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--   === Boxes Row Ends ===   -->
                        <!--   === Analytics Chart Starts ===   -->
                        <div class="chart">
                            <div class="chart-header">
                                <h2>Revenue Analytics</h2>
                                <input type="month" class="date" value="2023-12" />
                            </div>
                            <div class="chart-contents">
                                <section class="chart-grid">
                                    <div class="grid-line" value="100%"></div>
                                    <div class="grid-line" value="80%"></div>
                                    <div class="grid-line" value="60%"></div>
                                    <div class="grid-line" value="40%"></div>
                                    <div class="grid-line" value="20%"></div>
                                    <div class="grid-line" value="0%"></div>
                                </section>
                                <section class="chart-value-wrapper">
                                    <div class="chart-value" style="max-height: 62%"></div>
                                    <div class="chart-value" style="max-height: 76%"></div>
                                    <div class="chart-value" style="max-height: 70%"></div>
                                    <div class="chart-value" style="max-height: 82%"></div>
                                    <div class="chart-value" style="max-height: 78%"></div>
                                    <div class="chart-value" style="max-height: 94%"></div>
                                </section>
                                <section class="chart-labels">
                                    <div>Jul</div>
                                    <div>Aug</div>
                                    <div>Sep</div>
                                    <div>Oct</div>
                                    <div>Nov</div>
                                    <div>Dec</div>
                                </section>
                            </div>
                        </div>
                        <!--   === Analytics Chart Ends ===   -->
                    </div>
                    <!--   === Column 1 Ends ===   -->
                    <!--   === Column 2 Starts ===   -->
                    <div class="col-2">
                        <!--   === Top Products Starts ===   -->
                        <div class="top-products">
                            <header class="products-header">
                                <h1>Top Products</h1>
                            </header>
                            <div class="products-wrapper">
                                <% if (topProducts != null && !topProducts.isEmpty()) { %>
                                <% for (String[] product : topProducts) { %>
                                <div class="product">
                                    <div class="product-img">
                                        <img src="/ISP392_Project/views/dashboard/images/products/product-1.jpg" /> <!-- Thay bằng đường dẫn ảnh thực tế nếu có -->
                                    </div>
                                    <div class="product-desc">
                                        <div class="product-row-1">
                                            <h2><%= product[0] %></h2> <!-- Tên sản phẩm -->
                                        </div>
                                        <div class="product-row-2">
                                            <p>Đã bán: <%= product[1] %> đơn</p> <!-- Số lượng bán -->
                                        </div>
                                    </div>
                                </div>
                                <% } %>
                                <% } else { %>
                                <p>Không có dữ liệu.</p>
                                <% } %>
                            </div>
                        </div>
                        <!--   === Top Products Ends ===   -->
                        <!--   === Total Balance Card Starts ===   -->
                        <div class="balance-card">
                            <div class="balance-card-top">
                                <div class="text">
                                    <h3>Total Income</h3>
                                    <h1>$70,452<sub>(USD)</sub></h1>
                                </div>
                                <div class="icon">
                                    <i class="fa-solid fa-arrow-up"></i>
                                </div>
                            </div>
                            <div class="balance-card-middle">
                                <div class="subject">Progress</div>
                                <div class="progress-bar">
                                    <div
                                        class="progress-line"
                                        value="93%"
                                        style="max-width: 93%"
                                        ></div>
                                </div>
                            </div>
                            <div class="balance-card-bottom">
                                <button class="btn-top-up">
                                    Top Up<i class="fa-solid fa-arrow-up"></i>
                                </button>
                                <button class="btn-transfer">
                                    Transfer<i class="fa-solid fa-arrow-up"></i>
                                </button>
                            </div>
                        </div>
                        <!--   === Total Balance Card Ends ===   -->
                    </div>
                    <!--   === Column 2 Ends ===   -->
                </div>
                <!--   === Description Ends ===   -->
            </div>
            <!--   === Contents Section Ends ===   -->
        </div>
        <!--   *** Page Wrapper Ends ***   -->

        <!--   *** Link To Custom Script File ***   -->
        <script type="text/javascript" src="<%= request.getContextPath() %>/views/dashboard/script.js"></script>
    </body>
</html>

<%-- Document : dashboard.jsp Created on : Jan 28, 2025, 10:04:28 AM Author :
bsd12418 --%> <%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <!--   ***** Link To Custom Stylesheet *****   -->
        <link rel="stylesheet" type="text/css" href="style_1.css" />

        <!-- *******  Font Awesome Icons Link  ******* -->
        <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"
            />

        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <style>
            .contents .description {
                width: 100%;
                display: grid;
                padding: 10px 0px;
                gap: 16px;
            }
        </style>
        <title>Dashboard</title>
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
                    <div class="logo">
                        <img src="images/logo.png" />
                    </div>
                </div>

                <div class="search-box">
                    <input type="text" class="input-box" placeholder="Search..." />
                    <span class="search-btn">
                        <i class="fa-solid fa-search"></i>
                    </span>
                </div>

                <div class="top-bar-right">
                    <div class="mode-switch">
                        <i class="fa-solid fa-moon"></i>
                    </div>
                    <div class="notifications">
                        <i class="fa-solid fa-bell"></i>
                    </div>
                    <div class="profile">
                        <img src="images/profile-img.jpg" id="profile-img" />
                        <div class="profile-menu">
                            <ul>
                                <li><i class="fa-solid fa-user"></i> Thông tin User</li>
                                <li><i class="fa-solid fa-pen"></i> Chỉnh sửa User</li>
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
                    <c:if test="${sessionScope.role == 'owner' || sessionScope.role == 'staff'}">
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
                    </c:if>
                    <li>
                        <a href="/ISP392_Project/Stores">
                            <span class="nav-icon">
                                <i class="fa-solid fa-store"></i>
                            </span>
                            <span class="nav-text">Stores</span>
                        </a>
                    </li>
                    <c:if test="${sessionScope.role == 'owner' || sessionScope.role == 'admin'}">
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
                <!--   === Nav Bar Links Ends ===   -->
                <!--   === Side Bar Footer Starts ===   -->
                <div class="sidebar-footer">
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
                        <a href="dashboard.jsp">Overwiew</a>
                        <a href="#" class="active">Top Products</a>
                    </div>
                </div>
                <!--   === Panel Bar Ends ===   -->
                <!--   === Description Starts ===   -->
                <div class="description">
                    <style>
                        .contents .description {
                            width: 100%;
                            display: grid;
                            padding: 10px 0px;
                            gap: 16px;
                        }
                    </style>
                    <!--   === Column 1 Ends ===   -->
                    <!--   === Column 2 Starts ===   -->
                    <div class="col-2">
                        <!--   === Top Products Starts ===   -->
                        <div class="top-products">
                            <header class="products-header">
                                <h1>Top Products</h1>
                            </header>
                            <div class="products-wrapper">
                                <table class="product-table">
                                    <thead>
                                        <tr>
                                            <th>Image</th>
                                            <th>Product Name</th>
                                            <th>Category</th>
                                            <th>Price</th>
                                            <th>Stock</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <tr>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                        </tr>
                                    </tbody>
                                    <tbody>
                                        <tr>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                        </tr>
                                    </tbody>
                                    <tbody>
                                        <tr>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                        </tr>
                                    </tbody>
                                    <tbody>
                                        <tr>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                        </tr>
                                    </tbody>
                                    <tbody>
                                        <tr>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                        </tr>
                                    </tbody>
                                    <tbody>
                                        <tr>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                        </tr>
                                    </tbody>
                                    <tbody>
                                        <tr>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                        </tr>
                                    </tbody>
                                    <tbody>
                                        <tr>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                            <td>hehe</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <!--   === Top Products Ends ===   -->
                    </div>
                    <!--   === Column 2 Ends ===   -->
                </div>
                <!--   === Description Ends ===   -->
            </div>
            <!--   === Contents Section Ends ===   -->
        </div>
        <!--   *** Page Wrapper Ends ***   -->

        <!--   *** Link To Custom Script File ***   -->
        <script type="text/javascript" src="script.js"></script>
    </body>
</html>

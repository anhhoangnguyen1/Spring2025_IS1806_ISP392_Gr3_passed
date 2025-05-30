<!-- <%-- 
    Document : dashboard.jsp 
    Created on : Jan 28, 2025, 10:04:28 AM 
    Author :
    bsd12418 
--%> 


<%@page contentType="text/html" pageEncoding="UTF-8"%> -->
<!DOCTYPE html>
<html>
    <head>
        <!--   ***** Link To Custom Stylesheet *****   -->
        <link rel="stylesheet" type="text/css" href="style.css" />

        <!-- *******  Font Awesome Icons Link  ******* -->
        <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"
            />

        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
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
                    <div class="notifications" style="font-size: 16px; color: #333; margin-right: 15px;">
                        Howdy, <span style="font-weight: 600;">${sessionScope.user.name}</span>
                    </div>
                    <div class="profile">
                        <img src="${pageContext.request.contextPath}/views/profile/image/${sessionScope.user.image}" id="profile-img" onerror="this.src='${pageContext.request.contextPath}/views/dashboard/images/profile-img.jpg'"/>
                        <div class="profile-menu">
                            <ul>
                                <li><a href="/ISP392_Project/user"><i class="fa-solid fa-pen"></i> User Profile</a></li>
                                <li><a href="/ISP392_Project/change-password"><i class="fa-solid fa-pen"></i> Change Password</a></li>
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
                            <a href="/ISP392_Project/Orders">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-file-invoice"></i>
                                </span>
                                <span class="nav-text">Orders</span>
                            </a>
                        </li>
                        <li>
                            <a href="/ISP392_Project/InvoiceHistory">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-file-invoice"></i>
                                </span>
                                <span class="nav-text">Invoice History</span>
                            </a>
                        </li>
                        <li>
                            <a href="/ISP392_Project/Import">
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
                        <a href="#" class="active">Overwiew</a>
                        <a href="top_product.jsp">Top Products</a>
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
                                <div class="product">
                                    <div class="product-img">
                                        <img src="images/products/product-1.jpg" />
                                    </div>
                                    <div class="product-desc">
                                        <div class="product-row-1">
                                            <h2>Ear Phones</h2>
                                            <i class="fa-solid fa-shopping-cart"></i>
                                        </div>
                                        <div class="product-row-2">
                                            <p>Lorem ipsum dolor sit amet.</p>
                                        </div>
                                    </div>
                                </div>

                                <div class="product">
                                    <div class="product-img">
                                        <img src="images/products/product-2.jpg" />
                                    </div>
                                    <div class="product-desc">
                                        <div class="product-row-1">
                                            <h2>Cosmetics</h2>
                                            <i class="fa-solid fa-shopping-cart"></i>
                                        </div>
                                        <div class="product-row-2">
                                            <p>Lorem ipsum dolor sit amet.</p>
                                        </div>
                                    </div>
                                </div>

                                <div class="product">
                                    <div class="product-img">
                                        <img src="images/products/product-3.jpg" />
                                    </div>
                                    <div class="product-desc">
                                        <div class="product-row-1">
                                            <h2>Mouse</h2>
                                            <i class="fa-solid fa-shopping-cart"></i>
                                        </div>
                                        <div class="product-row-2">
                                            <p>Lorem ipsum dolor sit amet.</p>
                                        </div>
                                    </div>
                                </div>

                                <div class="product">
                                    <div class="product-img">
                                        <img src="images/products/product-4.jpg" />
                                    </div>
                                    <div class="product-desc">
                                        <div class="product-row-1">
                                            <h2>Ceramic</h2>
                                            <i class="fa-solid fa-shopping-cart"></i>
                                        </div>
                                        <div class="product-row-2">
                                            <p>Lorem ipsum dolor sit amet.</p>
                                        </div>
                                    </div>
                                </div>

                                <div class="product">
                                    <div class="product-img">
                                        <img src="images/products/product-5.jpg" />
                                    </div>
                                    <div class="product-desc">
                                        <div class="product-row-1">
                                            <h2>Laptop</h2>
                                            <i class="fa-solid fa-shopping-cart"></i>
                                        </div>
                                        <div class="product-row-2">
                                            <p>Lorem ipsum dolor sit amet.</p>
                                        </div>
                                    </div>
                                </div>

                                <div class="product">
                                    <div class="product-img">
                                        <img src="images/products/product-6.jpg" />
                                    </div>
                                    <div class="product-desc">
                                        <div class="product-row-1">
                                            <h2>Head Phones</h2>
                                            <i class="fa-solid fa-shopping-cart"></i>
                                        </div>
                                        <div class="product-row-2">
                                            <p>Lorem ipsum dolor sit amet.</p>
                                        </div>
                                    </div>
                                </div>

                                <div class="product">
                                    <div class="product-img">
                                        <img src="images/products/product-7.jpg" />
                                    </div>
                                    <div class="product-desc">
                                        <div class="product-row-1">
                                            <h2>Nick Shoes</h2>
                                            <i class="fa-solid fa-shopping-cart"></i>
                                        </div>
                                        <div class="product-row-2">
                                            <p>Lorem ipsum dolor sit amet.</p>
                                        </div>
                                    </div>
                                </div>

                                <div class="product">
                                    <div class="product-img">
                                        <img src="images/products/product-8.jpg" />
                                    </div>
                                    <div class="product-desc">
                                        <div class="product-row-1">
                                            <h2>Leather Shoes</h2>
                                            <i class="fa-solid fa-shopping-cart"></i>
                                        </div>
                                        <div class="product-row-2">
                                            <p>Lorem ipsum dolor sit amet.</p>
                                        </div>
                                    </div>
                                </div>
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
        <script type="text/javascript" src="script.js"></script>
    </body>
</html>

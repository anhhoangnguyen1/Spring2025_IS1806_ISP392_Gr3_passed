<!-- <%-- 
    Document : dashboard.jsp 
    Created on : Jan 28, 2025, 10:04:28 AM 
    Author :
    bsd12418 
--%> 
<%@page import="java.util.List" %>
<%@page import="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%> -->
<!DOCTYPE html>
<html>
    <head>
        <!--   ***** Link To Custom Stylesheet *****   -->
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/views/dashboard/style.css" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.7.1/chart.min.js"></script>

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
        <%
            String selectedPeriod = (String) request.getAttribute("selectedPeriod");
            if (selectedPeriod == null) {
                selectedPeriod = "last7days"; // Mặc định nếu không có giá trị
            }
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
                    <li>
                        <a href="/ISP392_Project/Stores">
                            <span class="nav-icon">
                                <i class="fa-solid fa-store"></i>
                            </span>
                            <span class="nav-text">Stores</span>
                        </a>
                    </li>
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
                <!-- Low Stock Products Alert - One Line -->
                <c:if test="${not empty lowStockProducts}">
                    <div class="low-stock-alert">
                        <span class="close-btn" onclick="this.parentElement.style.display = 'none';">&times;</span>
                        ⚠ <strong>Warning! Low Stock Products:   </strong> 
                        <c:forEach var="product" items="${lowStockProducts}" varStatus="loop">
                            <strong>${product.name}</strong> (<span class="low-stock-qty">${product.quantity} units</span>)
                            <c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    </div>
                </c:if>
                <style>

                    /* Hiệu ứng xuất hiện (fade-in) */
                    @keyframes fadeIn {
                        from {
                            opacity: 0;
                            transform: translateY(-10px);
                        }
                        to {
                            opacity: 1;
                            transform: translateY(0);
                        }
                    }

                    /* Hiệu ứng biến mất (fade-out) */
                    @keyframes fadeOut {
                        from {
                            opacity: 1;
                            transform: translateY(0);
                        }
                        to {
                            opacity: 0;
                            transform: translateY(-10px);
                        }
                    }

                    /* Low Stock Alert - One Line */
                    .low-stock-alert {
                        background-color: #F8D7DA;
                        color: #333;
                        padding: 10px 15px;
                        border-radius: 12px;
                        font-size: 16px;
                        margin: 10px 0;
                        position: relative;
                        box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.1);
                        max-width: 100%;
                        display: flex;
                        align-items: center;
                        justify-content: left; /* Căn cách đều các phần tử */
                        white-space: nowrap; /* Không xuống dòng */
                        overflow: hidden;
                        text-overflow: ellipsis;
                        gap: 10px; /* Khoảng cách giữa các phần tử */
                        animation: fadeIn 0.5s ease-out; /* Áp dụng hiệu ứng fade-in */
                    }


                    .low-stock-qty {
                        font-weight: bold;
                        color: red;
                    }

                    .close-btn {
                        font-size: 20px;
                        cursor: pointer;
                        margin-left: 10px;
                        color: #333;
                    }

                    .close-btn:hover {
                        color: red;
                    }
                    /* Ẩn thông báo với hiệu ứng fade-out */
                    .low-stock-alert.fade-out {
                        animation: fadeOut 0.5s ease-out;
                        opacity: 0;
                        pointer-events: none; /* Ngăn người dùng tương tác khi đang ẩn */
                    }
                </style>
                <script>
                    document.addEventListener("DOMContentLoaded", function () {
                        const closeButton = document.querySelector(".low-stock-alert .close-btn");
                        if (closeButton) {
                            closeButton.addEventListener("click", function () {
                                const alertBox = this.parentElement;
                                alertBox.classList.add("fade-out");
                                setTimeout(() => alertBox.style.display = "none", 500); // Chờ hiệu ứng hoàn tất
                            });
                        }
                    });
                </script>



                <!--   === Description Starts ===   -->
                <div class="description">
                    <!--   === Column 1 Starts ===   -->
                    <div class="col-1">
                        <!--   === Boxes Row Starts ===   -->
                        <div class="boxes-row">
                            <div class="balance-box">
                                <div class="subject-row">
                                    <div class="text">
                                        <h3>Total Income Today</h3>
                                        <h1 id="counter" data-value="<fmt:formatNumber value="${revenueToday}" type="number" groupingUsed="true" />">
                                            <sub>(VND)</sub>
                                        </h1>
                                    </div>
                                    <div class="icon">
                                        <i class="fa-solid fa-circle-dollar-to-slot"></i>
                                    </div>
                                </div>
                                <div class="progress-row">
                                    <div class="subject">
                                        <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy" />
                                    </div>
                                </div>
                            </div>
                            <div class="balance-box">
                                <div class="subject-row">
                                    <div class="text">
                                        <h3>Total Invoices Today</h3>
                                        <h1 id="invoiceCounter" data-value="${invoiceCountToday}">
                                            ${invoiceCountToday} Invoices
                                        </h1>
                                    </div>
                                    <div class="icon">
                                        <i class="fas fa-file-invoice"></i>
                                    </div>
                                </div>
                                <div class="progress-row">
                                    <div class="subject">
                                        <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy" />
                                    </div>
                                </div>
                            </div>
                            <script>
                                document.addEventListener("DOMContentLoaded", function () {
                                    function animateCounter(element, targetValue, unit = "", duration = 2000) {
                                        let startTime = null;

                                        function easeOutQuad(t) {
                                            return t * (2 - t); // Hàm easing giúp làm chậm lại về cuối
                                        }

                                        function updateCounter(timestamp) {
                                            if (!startTime)
                                                startTime = timestamp;
                                            let progress = (timestamp - startTime) / duration;
                                            if (progress > 1)
                                                progress = 1;

                                            let currentValue = Math.floor(targetValue * easeOutQuad(progress));
                                            element.innerHTML = currentValue.toLocaleString() + unit;

                                            if (progress < 1) {
                                                requestAnimationFrame(updateCounter);
                                            } else {
                                                element.innerHTML = targetValue.toLocaleString() + unit; // Đảm bảo hiển thị giá trị chính xác
                                            }
                                        }

                                        requestAnimationFrame(updateCounter);
                                    }

                                    // Total Income
                                    const counterElement = document.getElementById("counter");
                                    if (counterElement) {
                                        let targetValue = parseInt(counterElement.getAttribute("data-value").replace(/\D/g, ""), 10);
                                        animateCounter(counterElement, targetValue, ' <sub>(VND)</sub>');
                                    }

                                    // Total Invoices
                                    const invoiceCounterElement = document.getElementById("invoiceCounter");
                                    if (invoiceCounterElement) {
                                        let targetInvoices = parseInt(invoiceCounterElement.getAttribute("data-value").replace(/\D/g, ""), 10);
                                        animateCounter(invoiceCounterElement, targetInvoices, " Invoices");
                                    }
                                });

                            </script>
                        </div>
                        <!--   === Boxes Row Ends ===   -->
                        <!--   === Analytics Chart Starts ===   -->
                        <div class="chart">
                            <div class="chart-header">
                                <h2>Revenue Analytics</h2>
                                <form method="GET" action="dashboard" id="revenueForm">
                                    <label for="period">Select Period:</label>
                                    <select id="period" name="period" onchange="document.getElementById('revenueForm').submit();">
                                        <option value="today" <%= "today".equals(selectedPeriod) ? "selected" : "" %>>Today</option>
                                        <option value="yesterday" <%= "yesterday".equals(selectedPeriod) ? "selected" : "" %>>Yesterday</option>
                                        <option value="last7days" <%= "last7days".equals(selectedPeriod) ? "selected" : "" %>>Last 7 Days</option>
                                        <option value="thismonth" <%= "thismonth".equals(selectedPeriod) ? "selected" : "" %>>This Month</option>
                                        <option value="lastmonth" <%= "lastmonth".equals(selectedPeriod) ? "selected" : "" %>>Last Month</option>
                                    </select>
                                </form>
                            </div>
                            <div class="chart-contents">
                                <canvas id="revenueChart" ></canvas>

                                <script>
                                    let chartData = JSON.parse('<%= request.getAttribute("revenueData") %>');
                                    let labels = [];
                                    let values = [];

                                    if (typeof chartData === "number") {
                                        labels.push("Revenue");
                                        values.push(chartData);
                                    } else {
                                        chartData.forEach(item => {
                                            labels.push(item.saleDate);
                                            values.push(item.revenue);
                                        });
                                    }

                                    let ctx = document.getElementById("revenueChart").getContext("2d");
                                    new Chart(ctx, {
                                        type: "bar",
                                        data: {
                                            labels: labels,
                                            datasets: [{
                                                    label: "Revenue",
                                                    data: values,
                                                    backgroundColor: "blue",
                                                    borderColor: "darkblue",
                                                    borderWidth: 1
                                                }]
                                        },
                                        options: {
                                            responsive: true,
                                            scales: {
                                                y: {beginAtZero: true}
                                            }
                                        }
                                    });
                                </script>
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
                                            <p>Sold: <span class="count-up" data-count="<%= product[1] %>">0</span> products</p> <!-- Hiệu ứng đếm số -->
                                        </div>
                                    </div>
                                </div>
                                <% } %>
                                <% } else { %>
                                <p>Không có dữ liệu.</p>
                                <% } %>
                            </div>
                        </div>
                        <script>
                            document.addEventListener("DOMContentLoaded", function () {
                                function animateCountUp(element, start, end, duration) {
                                    let startTime = null;
                                    const step = (timestamp) => {
                                        if (!startTime)
                                            startTime = timestamp;
                                        const progress = Math.min((timestamp - startTime) / duration, 1);
                                        element.textContent = Math.floor(progress * (end - start) + start);
                                        if (progress < 1) {
                                            requestAnimationFrame(step);
                                        }
                                    };
                                    requestAnimationFrame(step);
                                }

                                document.querySelectorAll(".count-up").forEach(el => {
                                    const targetNumber = parseInt(el.getAttribute("data-count"), 10);
                                    animateCountUp(el, 0, targetNumber, 2000); // Thời gian chạy 2 giây
                                });
                            });
                        </script>

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
        <script type="text/javascript" src="<%= request.getContextPath() %>/views/dashboard/script.js"></script>
    </body>
</html>

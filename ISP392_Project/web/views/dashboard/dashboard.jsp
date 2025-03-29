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
                            <div class="balance-box">
                                <div class="subject-row">
                                    <div class="text">
                                        <h3>Total Money In</h3>
                                        <h1 id="moneyInCounter" data-value="<fmt:formatNumber value="${totalMoneyIn}" type="number" groupingUsed="true" />">
                                            <sub>(VND)</sub>
                                        </h1>
                                    </div>
                                    <div class="icon">
                                        <i class="fa-solid fa-arrow-down-to-arc"></i>
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
                                        <h3>Total Money Out</h3>
                                        <h1 id="moneyOutCounter" data-value="<fmt:formatNumber value="${totalMoneyOut}" type="number" groupingUsed="true" />">
                                            -<sub>(VND)</sub>
                                        </h1>
                                    </div>
                                    <div class="icon">
                                        <i class="fa-solid fa-arrow-up-from-arc"></i>
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
                                        <h3>Net Profit Today</h3>
                                        <h1 id="netProfitCounter" data-value="<fmt:formatNumber value="${netProfit}" type="number" groupingUsed="true" />">
                                            <sub>(VND)</sub>
                                        </h1>
                                    </div>
                                    <div class="icon">
                                        <i class="fa-solid fa-chart-line"></i>
                                    </div>
                                </div>
                                <div class="progress-row">
                                    <div class="subject">
                                        <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--   === Boxes Row Ends ===   -->
                        <!--   === Analytics Chart Starts ===   -->
                        <div class="chart">
                            <div class="chart-header">
                                <h2>Income Chart</h2>
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
                                        <img src="<%= request.getContextPath() %>/views/product/images/<%= product[2] != null ? product[2] : "default-product.jpg" %>" alt="<%= product[0] %>" />
                                    </div>
                                    <div class="product-desc">
                                        <div class="product-row-1">
                                            <h2><%= product[0] %></h2>
                                        </div>
                                        <div class="product-row-2">
                                            <p>Sold: <span class="count-up" data-count="<%= product[1] %>">0</span> products</p>
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
                        <div class="cash-flow-history">
                            <header class="cash-flow-header">
                                <h1>Lịch Sử Dòng Tiền Hôm Nay</h1>
                            </header>
                            <div class="cash-flow-summary">
                                <div class="summary-item income">
                                    <span class="label">Tổng thu:</span>
                                    <span class="value">
                                        <fmt:formatNumber value="${totalMoneyIn}" type="number" groupingUsed="true" /> VND
                                    </span>
                                </div>
                                <div class="summary-item expense">
                                    <span class="label">Tổng chi:</span>
                                    <span class="value">
                                        <fmt:formatNumber value="${totalMoneyOut}" type="number" groupingUsed="true" /> VND
                                    </span>
                                </div>
                                <div class="summary-item net">
                                    <span class="label">Thực tế:</span>
                                    <span class="value">
                                        <fmt:formatNumber value="${netProfit}" type="number" groupingUsed="true" /> VND
                                    </span>
                                </div>
                            </div>
                            <div class="cash-flow-table">
                                <table>
                                    <thead>
                                        <tr>
                                            <th style="width: 15%">Thời Gian</th>
                                            <th style="width: 10%">Loại</th>
                                            <th style="width: 20%">Số Tiền</th>
                                            <th style="width: 25%">Mô Tả</th>
                                            <th style="width: 30%">Trạng Thái</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="debt" items="${todayDebts}">
                                            <tr class="${debt.type == '-' ? 'money-out' : 'money-in'}">
                                                <td>
                                                    <fmt:parseDate value="${debt.createdAt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                                    <fmt:formatDate value="${parsedDate}" pattern="HH:mm" />
                                                </td>
                                                <td>${debt.type == '-' ? 'Chi' : 'Thu'}</td>
                                                <td class="amount">
                                                    <fmt:formatNumber value="${debt.amount}" type="number" groupingUsed="true" /> VND
                                                </td>
                                                <td>${debt.description}</td>
                                                <td>
                                                    <span class="status ${
                                                        debt.description == 'Customer repays debt' ? 'customer-repay' :
                                                        debt.description == 'Customer pays' ? 'customer-pay' :
                                                        debt.description == 'Owner repays debt' ? 'owner-repay' :
                                                        debt.description == 'Owner borrows debt' ? 'owner-borrow' : 'unknown'
                                                    }">
                                                        ${debt.description}
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
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

                                // Total Money In
                                const moneyInCounterElement = document.getElementById("moneyInCounter");
                                if (moneyInCounterElement) {
                                    let targetMoneyIn = parseInt(moneyInCounterElement.getAttribute("data-value").replace(/\D/g, ""), 10);
                                    animateCounter(moneyInCounterElement, targetMoneyIn, ' <sub>(VND)</sub>');
                                }

                                // Total Money Out
                                const moneyOutCounterElement = document.getElementById("moneyOutCounter");
                                if (moneyOutCounterElement) {
                                    let targetMoneyOut = parseInt(moneyOutCounterElement.getAttribute("data-value").replace(/\D/g, ""), 10);
                                    animateCounter(moneyOutCounterElement, targetMoneyOut, ' <sub>(VND)</sub>');
                                }

                                // Net Profit
                                const netProfitCounterElement = document.getElementById("netProfitCounter");
                                if (netProfitCounterElement) {
                                    let targetNetProfit = parseInt(netProfitCounterElement.getAttribute("data-value").replace(/\D/g, ""), 10);
                                    animateCounter(netProfitCounterElement, targetNetProfit, ' <sub>(VND)</sub>');
                                }
                            });
        </script>
        <style>
            .cash-flow-history {
                background-color: var(--bg-primary);
                border-radius: 12px;
                padding: 20px;
                margin: 20px 0;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }
            
            .cash-flow-table {
                margin-top: 20px;
                background-color: var(--bg-primary);
                border-radius: 8px;
                overflow: hidden;
            }
            
            .cash-flow-table table {
                width: 100%;
                border-collapse: collapse;
                table-layout: fixed;
            }
            
            .cash-flow-table th,
            .cash-flow-table td {
                padding: 12px;
                text-align: left;
                border-bottom: 1px solid var(--bg-ternary);
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            
            .cash-flow-table th {
                background-color: var(--bg-ternary);
                color: var(--heading-clr);
                font-weight: 600;
                font-size: 14px;
            }
            
            .cash-flow-table tr:hover {
                background-color: rgba(var(--bg-ternary-rgb), 0.1);
            }
            
            .money-in {
                background-color: rgba(95, 207, 101, 0.05);
            }
            
            .money-out {
                background-color: rgba(255, 107, 107, 0.05);
            }
            
            .money-in .amount {
                color: #5fcf65;
                font-weight: 500;
            }
            
            .money-out .amount {
                color: #ff6b6b;
                font-weight: 500;
            }
            
            .status {
                padding: 6px 12px;
                border-radius: 20px;
                font-size: 13px;
                font-weight: 500;
                display: inline-block;
                white-space: nowrap;
            }
            
            .status.customer-repay {
                background-color: rgba(95, 207, 101, 0.15);
                color: #5fcf65;
            }
            
            .status.customer-pay {
                background-color: rgba(52, 152, 219, 0.15);
                color: #3498db;
            }
            
            .status.owner-repay {
                background-color: rgba(155, 89, 182, 0.15);
                color: #9b59b6;
            }
            
            .status.owner-borrow {
                background-color: rgba(243, 156, 18, 0.15);
                color: #f39c12;
            }
            
            .status.unknown {
                background-color: rgba(158, 158, 158, 0.15);
                color: #9e9e9e;
            }
        </style>
    </body>
</html>

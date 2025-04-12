<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="./assets/fonts/themify-icons/themify-icons.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <title>Import Price History</title>
        <style>
            .table-container {
                overflow-x: auto;
                max-width: 100%;
            }
            .table {
                width: 100%;
                border-collapse: collapse;
            }
            .sortable {
                cursor: pointer;
            }
            .container.mt-4 {
                position: relative;
                padding-top: 15px;
            }
            .search-box {
                position: absolute;
                top: 20px;
                left: 50%;
                transform: translateX(-50%);
                width: 400px;
                height: 37px;
                background-color: var(--bg-ternary);
                border-radius: 50px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                z-index: 10;
            }
            .showing-info {
                margin-bottom: 10px;
                font-size: 14px;
            }
            .checkbox-container {
                margin-top: 10px;
                margin-bottom: -15px;
            }
            #contents {
                margin-left: 260px;
                padding: 20px;
                transition: margin-left 0.3s ease;
            }
            .panel-bar1 {
                background: #fff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                margin-right: 20px;
            }
            .search-container {
                background: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 20px;
            }
            @media (max-width: 768px) {
                #contents {
                    margin-left: 0;
                    padding: 10px;
                }
                .panel-bar1 {
                    margin-right: 10px;
                }
                .search-container form {
                    flex-direction: column;
                }
                .search-container form > * {
                    margin-bottom: 10px;
                }
            }
        </style>
    </head>
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
                    <div class="notifications" style="font-size: 16px; color: #333;">
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
                        <li class="active">
                            <a href="/ISP392_Project/HistoryExportPriceServlet">
                                <span class="nav-icon">
                                    <i class="fa-solid fa-file-invoice"></i>
                                </span>
                                <span class="nav-text">Price History</span>
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
                        </a>
                    </div>
                </div>
                <!--   === Side Bar Footer Ends ===   -->
            </aside>
            <!--   === Side Bar Ends ===   -->

            <div id="contents">
                <div class="panel-bar1">
                    <h2>Import Price History</h2>

                    <div class="search-container">
                        <form id="searchForm" action="HistoryImportPriceServlet" method="GET" style="display: flex; flex-wrap: wrap; gap: 10px; align-items: center;">
                            <input type="text" id="searchInput" name="keyword" placeholder="Search products..." value="${keyword}" 
                                   style="padding: 8px; border-radius: 4px; border: 1px solid #ddd; flex: 1;">

                            <select id="sortOrder" name="sortOrder" 
                                    style="padding: 8px; border-radius: 4px; border: 1px solid #ddd; min-width: 150px;">
                                <option value="desc" ${sortOrder == 'desc' ? 'selected' : ''}>Newest → Oldest</option>
                                <option value="asc" ${sortOrder == 'asc' ? 'selected' : ''}>Oldest → Newest</option>
                            </select>

                            <div style="display: flex; align-items: center; gap: 5px;">
                                <label for="startDate">From:</label>
                                <input type="date" id="startDate" name="startDate" value="${startDate}" 
                                       style="padding: 8px; border-radius: 4px; border: 1px solid #ddd;">
                            </div>

                            <div style="display: flex; align-items: center; gap: 5px;">
                                <label for="endDate">To:</label>
                                <input type="date" id="endDate" name="endDate" value="${endDate}" 
                                       style="padding: 8px; border-radius: 4px; border: 1px solid #ddd;">
                            </div>

                            <div style="display: flex; gap: 5px;">
                                <button type="submit" class="btn btn-success">Search</button>
                                <button type="button" class="btn btn-secondary" onclick="resetFilters()">Clear Filters</button>
                            </div>
                        </form>
                    </div>

                    <div style="text-align: right; margin-bottom: 20px;">
                        <button type="button" class="btn btn-info" onclick="redirectToExportPriceHistory()">
                            View Export Price History
                        </button>
                    </div>

                    <div class="table-container">
                        <table class="table table-striped table-hover">
                            <thead class="thead-light">
                                <tr>
                                    <th scope="col">Product Name</th>
                                    <th scope="col" style="text-align: right;">Import Price</th>
                                    <th scope="col">Supplier</th>
                                    <th scope="col">Change Date</th>
                                    <th scope="col">Changed By</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${empty HistoryList}">
                                    <tr>
                                        <td colspan="5" class="text-center">No records found</td>
                                    </tr>
                                </c:if>
                                <c:forEach var="history" items="${HistoryList}">
                                    <tr>
                                        <td style="text-align: left;">${history.productName}</td>
                                        <td style="text-align: right;">
                                            <fmt:formatNumber value="${history.price}" type="number" groupingUsed="true" /> đ
                                        </td>
                                        <td>${history.supplier}</td>
                                        <td>${history.changedAt}</td>
                                        <td>${history.changedBy}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="d-flex justify-content-center mt-3">
                        <ul class="pagination">
                            <c:if test="${currentPage > 1}">
                                <li class="page-item">
                                    <a class="page-link" href="HistoryImportPriceServlet?page=${currentPage - 1}&keyword=${keyword}&sortOrder=${sortOrder}&startDate=${startDate}&endDate=${endDate}">
                                        <i class="fa fa-angle-left"></i>
                                    </a>
                                </li>
                            </c:if>
                            
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="HistoryImportPriceServlet?page=${i}&keyword=${keyword}&sortOrder=${sortOrder}&startDate=${startDate}&endDate=${endDate}">${i}</a>
                                </li>
                            </c:forEach>
                            
                            <c:if test="${currentPage < totalPages}">
                                <li class="page-item">
                                    <a class="page-link" href="HistoryImportPriceServlet?page=${currentPage + 1}&keyword=${keyword}&sortOrder=${sortOrder}&startDate=${startDate}&endDate=${endDate}">
                                        <i class="fa fa-angle-right"></i>
                                    </a>
                                </li>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            function resetFilters() {
                document.getElementById('searchInput').value = '';
                document.getElementById('sortOrder').value = 'desc';
                document.getElementById('startDate').value = '';
                document.getElementById('endDate').value = '';
                document.getElementById('searchForm').submit();
            }

            function redirectToExportPriceHistory() {
                window.location.href = 'HistoryExportPriceServlet';
            }
        </script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
    </body>
</html> 
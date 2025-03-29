<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Stores List</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
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
            <!--   === Side Bar Starts ===   -->
            <aside class="side-bar">
                <!--   === Nav Bar Links Starts ===   -->
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
                    <li class="active">
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

            <div class="contents">
                <div class="panel-bar1">
                    <input type="hidden" name="service" value="storeList">

                    <h1>Stores List</h1>

                    <c:if test="${not empty sessionScope.Notification}">
                        <div class="alert alert-warning alert-dismissible">
                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                            <strong>${sessionScope.Notification}</strong>
                        </div>
                        <c:remove var="Notification" scope="session" />
                    </c:if>

                    <c:if test="${not empty sessionScope.successMessage}">
                        <div class="alert alert-success">
                            <strong>Success!</strong> ${sessionScope.successMessage}
                        </div>
                        <c:set var="successMessage" value="${sessionScope.successMessage}" />
                        <c:remove var="successMessage" />
                    </c:if>

                    <form action="${pageContext.request.contextPath}/Stores" method="get">
                        <div class="search-box">
                            <input type="hidden" name="service" value="searchStore" />
                            <input type="text" id="searchInput" class="input-box" name="searchStore"
                                   placeholder="Search for store name."
                                   value="${searchStore}" autocomplete="off" />
                            <button type="submit" class="search-btn">
                                <i class="fa-solid fa-search"></i>
                            </button>
                        </div>
                    </form>

                    <div class="action-bar d-flex align-items-center mb-4">
                        <a href="${pageContext.request.contextPath}/Stores?service=createStore" class="btn btn-outline-primary mr-lg-auto">
                            Add Store
                        </a>

                        <!-- Lọc theo trạng thái -->
                        <form action="${pageContext.request.contextPath}/Stores" method="get" id="filterByStatusForm" class="d-flex align-items-center">
                            <input type="hidden" name="service" value="filterStoreByStatus" />

                            <!-- Lọc theo trạng thái -->
                            <select class="form-control" name="status" id="statusFilter" onchange="this.form.submit()" style="white-space: nowrap; width: auto; max-width: 200px; height: 35px; font-size: 16px; padding: 5px 10px; margin-right: 10px;">
                                <option value="">All</option>
                                <option value="Active" ${param.status == 'Active' ? 'selected' : ''}>Active</option>
                                <option value="Inactive" ${param.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                            </select>
                        </form>
                        <!-- Form lọc cửa hàng theo ngày tạo -->
                        <form action="${pageContext.request.contextPath}/Stores" method="get" id="filterByDateForm" class="d-flex align-items-center">
                            <input type="hidden" name="service" value="filterStoreByDate" />

                            <!-- Lọc theo ngày -->
                            <input type="date" id="fromDate" class="form-control form-control-sm" name="fromDate" 
                                   value="${param.fromDate}" placeholder="From Date" 
                                   style="max-width: 150px; margin-right: 5px; height: 35px; font-size: 16px;" />

                            <input type="date" id="toDate" class="form-control form-control-sm" name="toDate" 
                                   value="${param.toDate}" placeholder="To Date" 
                                   style="max-width: 150px; margin-right: 10px; height: 35px; font-size: 16px;" />

                            <!-- Nút Filter -->
                            <button type="submit" class="btn btn-outline-primary btn-sm" style="white-space: nowrap; margin-right: 5px; height: 35px; font-size: 16px;">
                                Filter by Date
                            </button>

                            <!-- Nút x (clear) để xóa các ô ngày -->
                            <button type="button" class="btn btn-outline-danger btn-sm" id="clearDateBtn" 
                                    style="height: 35px; font-size: 16px;">
                                <i class="fa-solid fa-xmark"></i>
                            </button>  
                        </form>
                    </div>
                    <!-- Table Container -->
                    <div class="table-container mt-4">
                        <table class="table table-striped table-hover table-bordered" id="storeTable">
                            <thead>
                                <tr>
                                    <th class="sortable" data-sort="id">
                                        Store Name
                                    </th>
                                    <th class="resizable">Owner</th>
                                    <th class="resizable">Address</th>
                                    <th class="resizable">Phone</th>
                                    <th class="resizable">Email</th>
                                    <th class="resizable">Created At</th>
                                    <th class="resizable">Status</th>
                                    <th class="sticky-col">Actions</th>
                                </tr>
                            </thead>
                            <tbody id="storeTableBody">
                                <c:forEach var="store" items="${storeList}">
                                    <tr>
                                        <td>${store.name}</td>
                                        <td>
                                            <c:forEach var="owner" items="${ownerDetails}">
                                                <c:if test="${owner.storeId == store.id}">
                                                    ${owner.ownerName}
                                                </c:if>
                                            </c:forEach>
                                        </td>
                                        <td>${store.address}</td>
                                        <td>${store.phone}</td>
                                        <td>${store.email}</td>
                                        <td>${store.createdAt}</td>
                                        <td>${store.status}</td>
                                        <td class="sticky-col">
                                            <a href="${pageContext.request.contextPath}/Stores?service=editStore&store_id=${store.id}" class="btn btn-outline-primary">Edit</a>
                                            <a href="${pageContext.request.contextPath}/Stores?service=toggleBan&store_id=${store.id}"
                                               class="btn
                                               <c:if test='${store.status eq "Active"}'>btn-outline-danger</c:if>
                                               <c:if test='${store.status eq "Inactive"}'>btn-outline-success</c:if>">
                                                <c:choose>
                                                    <c:when test="${store.status eq 'Active'}">Ban</c:when>
                                                    <c:otherwise>Unban</c:otherwise>
                                                </c:choose>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>


                    <div class="container d-flex justify-content-center mt-4" id="pagination">
                        <ul class="pagination">
                            <c:if test="${index > 1}">
                                <li class="page-item">
                                    <a class="page-link" href="Stores?service=storeList&index=${index - 1}">
                                        <i class="fa fa-angle-left"></i>
                                    </a>
                                </li>
                            </c:if>

                            <li class="page-item ${index == 1 ? 'active' : ''}">
                                <a class="page-link" href="Stores?service=storeList&index=1">1</a>
                            </li>

                            <c:if test="${index > 3}">
                                <li class="page-item disabled">
                                    <span class="page-link">...</span>
                                </li>
                            </c:if>

                            <c:if test="${index < endPage - 2}">
                                <li class="page-item disabled">
                                    <span class="page-link">...</span>
                                </li>
                            </c:if>

                            <c:if test="${endPage > 1}">
                                <li class="page-item ${index == endPage ? 'active' : ''}">
                                    <a class="page-link" href="Users?service=users&searchUser=${searchUser}&index=${endPage}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                        ${endPage}
                                    </a>
                                </li>
                            </c:if>

                            <c:if test="${index < endPage}">
                                <li class="page-item">
                                    <a class="page-link" href=""Users?service=users&searchUser=${searchUser}&index=${index + 1}&sortBy=${sortBy}&sortOrder=${sortOrder}">
                                        <i class="fa fa-angle-right"></i>
                                    </a>
                                </li>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
        <script>
                                // Khi bấm icon x của form lọc theo ngày,
                                // xóa giá trị ở hai ô ngày và submit lại form để hiển thị toàn bộ danh sách.
                                document.getElementById('clearDateBtn').addEventListener('click', function () {
                                    document.getElementById('fromDate').value = '';
                                    document.getElementById('toDate').value = '';
                                    document.getElementById('filterByDateForm').submit();
                                });
        </script>
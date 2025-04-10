<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Invoice History</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
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
                        <li class="active">
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

            <!-- Main Content -->
            <div class="contents">
                <div class="panel-bar1">
                    <input type="hidden" name="service" value="InvoiceList">
                    <form action="${pageContext.request.contextPath}/InvoiceHistory" method="get">
                        <div class="search-box">
                            <input type="hidden" name="service" value="search" />
                            <input type="text" id="searchInput" class="input-box" name="searchInvoice"
                                   placeholder="Search for customer name."
                                   value="${searchInvoice}" autocomplete="off" />
                            <button type="submit" class="search-btn">
                                <i class="fa-solid fa-search"></i>
                            </button>
                        </div>
                    </form>
                    <h1>Invoice History</h1>



                    <div class="action-bar d-flex align-items-center mb-4">

                        <!-- Lọc theo trạng thái -->
                        <form action="${pageContext.request.contextPath}/InvoiceHistory" method="get" id="filterByStatusForm" class="d-flex align-items-center">
                            <input type="hidden" name="service" value="filterInvoiceByType" />

                            <!-- Lọc theo trạng thái -->
                            <select class="form-control" name="type" id="typeFilter" onchange="this.form.submit()" style="white-space: nowrap; width: auto; max-width: 200px; height: 35px; font-size: 16px; padding: 5px 10px; margin-right: 10px;">
                                <option value="">All</option>
                                <option value="Export" ${param.type == 'Export' ? 'selected' : ''}>Export</option>
                                <option value="Import" ${param.type == 'Import' ? 'selected' : ''}>Import</option>
                            </select>
                        </form>
                        <!-- Form lọc cửa hàng theo ngày tạo -->
                        <form action="${pageContext.request.contextPath}/InvoiceHistory" method="get" id="filterByDateForm" class="d-flex align-items-center">
                            <input type="hidden" name="service" value="filterInvoiceByDate" />

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

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show mt-3" role="alert">
                            ${errorMessage}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>

                    <div class="table-container mt-4">
                        <table class="table table-striped table-hover table-bordered" id="invoiceTable">
                            <thead>
                                <tr>
                                    <th class="resizable">Order ID</th>
                                    <th class="resizable">Customer Name</th>
                                    <th class="resizable">Type</th>
                                    <th class="resizable">Created At</th>
                                    <th class="sticky-col">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${param.t == 'body' || empty param.t}">
                                    <c:forEach var="order" items="${orders}">
                                        <tr>
                                            <td>${order.id}</td>
                                            <td>${order.customerID.name}</td>
                                            <td>${order.type}</td>
                                            <td><fmt:formatDate value="${order.createdAt}" pattern="yyyy-MM-dd"/></td>
                                            <td class="sticky-col">
                                                <button type="button"
                                                        class="btn btn-outline-primary btn-sm"
                                                        onclick="showOrderDetail(${order.id})">
                                                    Detail
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>                        
                                </c:if>
                            </tbody>
                            <span id="totalSizeHolder" style="display:none;">${totalSize}</span>

                        </table>
                    </div>
                    <div class="container d-flex justify-content-center mt-4">
                        <ul class="pagination">
                            <!-- Previous Page -->
                            <c:if test="${index > 1}">
                                <li class="page-item">
                                    <form action="InvoiceHistory" method="GET" style="display: inline;">
                                        <input type="hidden" name="service" value="ajaxPaging" />
                                        <input type="hidden" name="page" value="${index - 1}" />
                                        <input type="hidden" name="searchInvoice" value="${param.searchInvoice}" />
                                        <input type="hidden" name="type" value="${param.type}" />
                                        <input type="hidden" name="fromDate" value="${param.fromDate}" />
                                        <input type="hidden" name="toDate" value="${param.toDate}" />
                                        <button type="submit" class="page-link">&laquo;</button>
                                    </form>
                                </li>
                            </c:if>

                            <!-- Page Numbers -->
                            <c:forEach begin="1" end="${endPage}" var="pageNum">
                                <li class="page-item ${index == pageNum ? 'active' : ''}">
                                    <form action="InvoiceHistory" method="GET" style="display: inline;">
                                        <input type="hidden" name="service" value="ajaxPaging" />
                                        <input type="hidden" name="page" value="${pageNum}" />
                                        <input type="hidden" name="searchInvoice" value="${param.searchInvoice}" />
                                        <input type="hidden" name="type" value="${param.type}" />
                                        <input type="hidden" name="fromDate" value="${param.fromDate}" />
                                        <input type="hidden" name="toDate" value="${param.toDate}" />
                                        <button type="submit" class="page-link">${pageNum}</button>
                                    </form>
                                </li>
                            </c:forEach>

                            <!-- Next Page -->
                            <c:if test="${index < endPage}">
                                <li class="page-item">
                                    <form action="InvoiceHistory" method="GET" style="display: inline;">
                                        <input type="hidden" name="service" value="ajaxPaging" />
                                        <input type="hidden" name="page" value="${index + 1}" />
                                        <input type="hidden" name="searchInvoice" value="${param.searchInvoice}" />
                                        <input type="hidden" name="type" value="${param.type}" />
                                        <input type="hidden" name="fromDate" value="${param.fromDate}" />
                                        <input type="hidden" name="toDate" value="${param.toDate}" />
                                        <button type="submit" class="page-link">&raquo;</button>
                                    </form>
                                </li>
                            </c:if>
                        </ul>
                    </div>   
                </div>        
            </div>
        </div>
        <!-- Modal -->
        <div class="modal fade" id="orderDetailModal" tabindex="-1" role="dialog" aria-labelledby="orderDetailLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="orderDetailLabel">Invoice Detail</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" id="orderDetailBody">
                        <!-- Nội dung chi tiết sẽ được load bằng Ajax -->
                        Loading...
                    </div>
                </div>
            </div>
        </div>
    </body>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>
    <script>
                                                            // Khi bấm icon x của form lọc theo ngày,
                                                            // xóa giá trị ở hai ô ngày và submit lại form để hiển thị toàn bộ danh sách.
                                                            const clearBtn = document.getElementById('clearDateBtn');
                                                            if (clearBtn) {
                                                                clearBtn.addEventListener('click', function () {
                                                                    document.getElementById('fromDate').value = '';
                                                                    document.getElementById('toDate').value = '';
                                                                    document.getElementById('filterByDateForm').submit();
                                                                });
                                                            }

    </script>
    <script>
        function loadPage(pageNumber) {
            const search = $('#searchInput').val();
            const type = $('#typeFilter').val();
            const fromDate = $('#fromDate').val();
            const toDate = $('#toDate').val();

            $.ajax({
                url: '${pageContext.request.contextPath}/InvoiceHistory',
                method: 'POST',
                data: {
                    service: 'ajaxPaging',
                    page: pageNumber,
                    searchInvoice: search,
                    type: type,
                    fromDate: fromDate,
                    toDate: toDate
                },
                success: function (data) {
                    $('#invoiceTable tbody').html($(data).find('tbody').html());
                    $('#totalSizeHolder').text($(data).find('#totalSizeHolder').text());

                    const totalSize = parseInt($('#totalSizeHolder').text());
                    updatePagination(pageNumber, totalSize);
                }
            });
        }

        function updatePagination(currentPage, totalSize) {
            const totalPages = Math.ceil(totalSize / 5);
            let paginationHTML = '';

            if (totalPages <= 1) {
                $('#pagination ul').html('');
                return;
            }

            if (currentPage > 1) {
                paginationHTML += `
            <li class="page-item">
                <button class="page-link" onclick="loadPage(${currentPage - 1})">&laquo;</button>
            </li>`;
            }

            for (let i = 1; i <= totalPages; i++) {
                paginationHTML += `
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                  <button class="page-link" onclick="loadPage(${i})">${i}</button>
                </li>`;
            }

            if (currentPage < totalPages) {
                paginationHTML += `
            <li class="page-item">
                <button class="page-link" onclick="loadPage(${currentPage + 1})">&raquo;</button>
            </li>`;
            }

            $('#pagination ul').html(paginationHTML);
        }

        function showOrderDetail(orderId) {
            $.ajax({
                url: 'InvoiceHistory',
                method: 'GET',
                data: {
                    service: 'detail',
                    order_id: orderId
                },
                success: function (data) {
                    $('#orderDetailBody').html(data);
                    $('#orderDetailModal').modal('show');
                },
                error: function () {
                    $('#orderDetailBody').html('<p class="text-danger">Failed to load invoice detail.</p>');
                    $('#orderDetailModal').modal('show');
                }
            });
        }
    </script>
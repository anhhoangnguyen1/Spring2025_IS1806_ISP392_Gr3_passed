<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.Orders, entity.OrderDetails, entity.Customers" %>
<%@ page import="dal.OrderDetailsDAO" %>
<%@ page import="jakarta.servlet.http.HttpServletRequest" %>
<%@ page import="jakarta.servlet.http.HttpServletResponse" %>
<%@ page import="java.io.IOException" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Invoice History</title>

        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Font Awesome Icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" />

        <!-- Custom CSS -->
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/views/dashboard/style.css" />

        <!-- Chart.js -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.7.1/chart.min.js"></script>

        <!-- jQuery -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body>
        <!-- Page Wrapper -->
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
                <!-- End Side Bar Footer -->
            </aside>
            <!-- End Side Bar -->

            <!-- Main Content -->
            <div class="contents">
                <div class="container mt-4">
                    <div class="card shadow">
                        <div class="card-header bg-primary text-white">
                            <h2 class="mb-0"><i class="fas fa-history me-2"></i>Invoice History</h2>
                            <div class="filter-container mb-3">
                                <form action="orders" method="GET" class="d-flex">
                                    <label for="amountFilter" class="me-2">Type:</label>
                                    <select id="amountFilter" name="amountFilter" class="form-control form-control-sm">
                                        <option value="">All</option>
                                        <option value="positive">Export</option>
                                        <option value="negative">Import</option>
                                    </select>
                                    <button type="submit" class="btn btn-sm btn-outline-primary ms-2">Filter</button>
                                </form>
                            </div>
                        </div>
                        <div class="card-body">
                            <!-- Orders Table -->
                            <div class="table-responsive">
                                <table class="table table-hover table-striped">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Order ID</th>
                                            <th>Customer Name</th>
                                            <th>Total Amount</th>
                                            <th class="text-center">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                        List<Orders> orders = (List<Orders>) request.getAttribute("orders");
                                        List<Customers> customers = (List<Customers>) request.getAttribute("customersList");
                                        if (orders != null && !orders.isEmpty()) {
                                            for (Orders order : orders) {
                                                String customerName = "Khách lẻ";
                                                for (Customers c : customers) {
                                                    if (c.getId() != 0 && c.getId() == order.getCustomerID()) {
                                                        customerName = c.getName();
                                                        break;
                                                    }
                                                }
                                        %>
                                        <tr>
                                            <td><span class="badge bg-secondary">#<%= order.getOrderID() %></span></td>
                                            <td><%= customerName %></td>
                                            <td><fmt:formatNumber value="<%= order.getTotalAmount() %>" type="currency" currencySymbol="" /> VND</td>
                                            <td class="text-center">
                                                <button class="btn btn-sm btn-primary view-details" data-order-id="<%= order.getOrderID() %>">
                                                    <i class="fas fa-eye me-1"></i> View Details
                                                </button>
                                            </td>
                                        </tr>
                                        <% 
                                            }
                                        } else { 
                                        %>
                                        <tr>
                                            <td colspan="4" class="text-center">No orders found</td>
                                        </tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>

                            <!-- Pagination -->
                            <% 
                            Integer currentPage = (Integer) request.getAttribute("currentPage");
                            Integer totalPages = (Integer) request.getAttribute("totalPages");
                            if (currentPage != null && totalPages != null && totalPages > 1) { 
                            %>
                            <div class="row mt-4">
                                <div class="col-md-8 d-flex justify-content-center">
                                    <nav aria-label="Page navigation">
                                        <ul class="pagination">
                                            <!-- First Page -->
                                            <li class="page-item <%= (currentPage == 1) ? "disabled" : "" %>">
                                                <a class="page-link" href="?page=1" aria-label="First">
                                                    <span aria-hidden="true">&laquo;&laquo;</span>
                                                </a>
                                            </li>
                                            <!-- Previous Button -->
                                            <li class="page-item <%= (currentPage == 1) ? "disabled" : "" %>">
                                                <a class="page-link" href="?page=<%= currentPage - 1 %>" aria-label="Previous">
                                                    <span aria-hidden="true">&laquo;</span>
                                                </a>
                                            </li>

                                            <!-- Page Numbers -->
                                            <% 
                                            int startPage = Math.max(1, currentPage - 2);
                                            int endPage = Math.min(totalPages, startPage + 4);
                                            for (int i = startPage; i <= endPage; i++) { 
                                            %>
                                            <li class="page-item <%= (i == currentPage) ? "active" : "" %>">
                                                <a class="page-link" href="?page=<%= i %>"><%= i %></a>
                                            </li>
                                            <% } %>

                                            <!-- Next Button -->
                                            <li class="page-item <%= (currentPage == totalPages) ? "disabled" : "" %>">
                                                <a class="page-link" href="?page=<%= currentPage + 1 %>" aria-label="Next">
                                                    <span aria-hidden="true">&raquo;</span>
                                                </a>
                                            </li>
                                            <!-- Last Page -->
                                            <li class="page-item <%= (currentPage == totalPages) ? "disabled" : "" %>">
                                                <a class="page-link" href="?page=<%= totalPages %>" aria-label="Last">
                                                    <span aria-hidden="true">&raquo;&raquo;</span>
                                                </a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>

                                <!-- Go to Page -->
                                <div class="col-md-4 d-flex justify-content-end align-items-center">
                                    <form action="" method="GET" class="d-flex align-items-center">
                                        <label for="jumpPage" class="me-2">Go to page:</label>
                                        <input type="number" id="jumpPage" name="page" min="1" max="<%= totalPages %>" 
                                               class="form-control form-control-sm text-center" style="width: 70px;" value="<%= currentPage %>" required>
                                        <button type="submit" class="btn btn-sm btn-outline-primary ms-2">Go</button>
                                    </form>
                                </div>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
            <!-- End Main Content -->

            <!-- Order Details Modal -->
            <div class="modal fade" id="orderDetailsModal" tabindex="-1" aria-labelledby="orderDetailsModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header bg-primary text-white">
                            <h5 class="modal-title" id="orderDetailsModalLabel">Order Details</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Detail ID</th>
                                            <th>Product ID</th>
                                            <th>Quantity</th>
                                            <th>Price</th>
                                            <th>Subtotal</th>
                                        </tr>
                                    </thead>
                                    <tbody id="orderDetailsTable">
                                        <!-- Order details will be dynamically inserted here -->
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- End Page Wrapper -->

        <!-- Bootstrap JS Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <!-- Custom JS -->
        <script type="text/javascript" src="<%= request.getContextPath() %>/views/dashboard/script.js"></script>

        <script>
            $(document).ready(function () {
                $(".view-details").click(function () {
                    var orderId = $(this).data("order-id");

                    // Show loading spinner in modal
                    $("#orderDetailsTable").html('<tr><td colspan="5" class="text-center"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div></td></tr>');
                    $("#orderDetailsModal").modal("show");

                    // Fetch order details
                    $.ajax({
                        url: "orderDetailsController",
                        type: "GET",
                        data: {orderId: orderId},
                        success: function (response) {
                            $("#orderDetailsTable").html(response);
                        },
                        error: function () {
                            $("#orderDetailsTable").html('<tr><td colspan="5" class="text-center text-danger">Error loading details. Please try again.</td></tr>');
                        }
                    });
                });

                // Add event listener to the form submission to log the amountFilter value
                $("form").on("submit", function () {
                    var amountFilterValue = $("#amountFilter").val();
                    console.log("Amount Filter Value: ", amountFilterValue);  // Log the selected filter value
                });
            });
        </script>
    </body>
</html>
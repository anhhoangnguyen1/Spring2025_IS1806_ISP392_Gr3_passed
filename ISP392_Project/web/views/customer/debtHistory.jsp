<%-- 
    Document   : detaildebt
    Created on : Jan 27, 2025, 9:28:37 PM
    Author     : phamh
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <title>JSP Page</title>
    </head>
    <body>
        <style>.show-arrow i {
                opacity: 0; /* Ẩn mũi tên */
                transition: opacity 0.3s ease-in-out;
            }

            .show-arrow:hover i {
                opacity: 1; /* Hiện mũi tên khi trỏ vào */
            }
            .debt-filter-container {
                max-width: 800px;
                margin: 20px auto;
                padding: 20px;
                background: #f8f9fa;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .form-wrapper {
                display:5 flex;
                align-items: center;
                justify-content: space-between;
                gap: 15px;
            }

            .debt-filter-form {
                display: flex;
                align-items: flex-end;
                gap: 15px;
                flex: 1;
            }

            .form-group {
                flex: 1;
                min-width: 180px;
                margin-bottom: 0;
            }

            .form-label {
                display: block;
                margin-bottom: 5px;
                font-weight: 500;
                color: #333;
            }

            .form-control {
                width: 100%;
                padding: 8px 12px;
                border: 1px solid #ced4da;
                border-radius: 4px;
                font-size: 14px;
                height: 38px;
            }

            .button-group {
                display: flex;
                gap: 10px;
                align-items: center;
            }

            .btn {
                padding: 8px 20px;
                border-radius: 4px;
                font-weight: 500;
                height: 38px;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .btn-primary {
                background: #007bff;
                border: 1px solid #007bff;
                color: white;
            }

            .btn-primary:hover {
                background: #0056b3;
                border-color: #0056b3;
            }

            .btn-secondary {
                background: #6c757d;
                border: 1px solid #6c757d;
                color: white;
            }

            .btn-secondary:hover {
                background: #545b62;
                border-color: #545b62;
            }

            .btn-outline-success {
                border: 1px solid #28a745;
                color: #28a745;
                background: transparent;
            }

            .btn-outline-success:hover {
                background: #28a745;
                color: white;
            }

            .add-btn {
                margin-left: 15px;
                flex-shrink: 0;
            }

        </style>
        <div class="page-wrapper">
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
                    <li class="active">
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
            <div class="contents">
                <div class="panel-bar1">
                    <a href="/ISP392_Project/Customers" class="text-secondary show-arrow">
                        <i class="fa-solid fa-arrow-left"></i> Customers
                    </a>/ Debt list
                    <c:if test="${not empty sessionScope.Notification}">
                        <div class="alert alert-warning alert-dismissible">
                            <button type="button" class="close" data-dismiss="alert">
                                &times;
                            </button>
                            <strong>${sessionScope.Notification}</strong>
                        </div>
                        <c:remove var="Notification" scope="session" />
                    </c:if>
                    <div class="customer-info border p-3 rounded">
                        <h3 class="mb-3">Customer Information</h3>
                        <p><strong>Name:</strong> ${listName.name}</p>
                        <p><strong>Phone:</strong> ${listName.phone}</p>
                        <p><strong>Address:</strong> ${listName.address}</p>
                    </div>

                    <!-- Bộ lọc ngày -->
                    <div class="debt-filter-container">
                        <div class="form-wrapper">
                            <form action="Debts" method="POST" class="debt-filter-form">
                                <input type="hidden" name="service" value="debtInCustomers" />
                                <input type="hidden" name="customer_id" value="${listName.id}" />

                                <div class="form-group">
                                    <label for="startDate" class="form-label">From date:</label>
                                    <input type="date" id="startDate" name="startDate" 
                                           class="form-control" value="${startDate}">
                                </div>

                                <div class="form-group">
                                    <label for="endDate" class="form-label">To date:</label>
                                    <input type="date" id="endDate" name="endDate" 
                                           class="form-control" value="${endDate}">
                                </div>

                                <div class="form-group button-group">
                                    <button type="submit" class="btn btn-primary">Filter</button>
                                    <a href="Debts?service=debtInCustomers&customer_id=${listName.id}" 
                                       class="btn btn-secondary">Reset</a>
                                </div>
                                <div class="form-group">
                                    <button type="button" class="btn btn-outline-success add-btn" 
                                            data-toggle="modal" data-target="#DebtModal${listName.id}">
                                        <i class="fas fa-plus"></i> Add
                                    </button>
                                </div> 
                            </form>


                        </div>
                    </div>



                    <div class="table-container">
                        <table class="table-bordered" id="myTable1-${listName.id}" >

                            <thead>
                                <tr>
                                    <th class="resizable" onclick="sortTable1(${debt.id}, 0)">ID</th>
                                    <th class="resizable" onclick="sortTable1(${debt.id}, 1)">Amount</th>
                                    <th>Images</th>
                                    <th class="resizable">Description</th>
                                    <th class="resizable">Created At</th>
                                    <th class="resizable">Updated At</th>
                                    <th class="resizable">Created By</th>
                                    <th class="resizable">Status</th>
                                    <th class="sticky-col1">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty list}">
                                        <c:forEach var="debt" items="${list}">
                                            <tr>
                                                <td>${debt.id}</td>
                                                <td class="${debt.type == '-' ? 'text-danger' : ''}">
                                                    <fmt:formatNumber value="${debt.type == '-' ? -debt.amount : debt.amount}" pattern="###,##0"/>
                                                </td>
                                                <td>
                                                    <img src="images/${debt.image}" class="myImg" style="width: 100px; height: 100px; object-fit: cover; cursor: pointer;" alt="Debt evidence"/>
                                                </td>
                                                <td>${debt.description}</td>
                                                <td>${debt.createdAt}</td>
                                                <td>${debt.updatedAt}</td>
                                                <td>${debt.createdBy}</td>
                                                <td>${debt.status}</td>
                                                <td class="sticky-col1">
                                                    <button type="button" class="btn btn-outline-primary openDebtModal" 
                                                            data-id="${debt.id}" 
                                                            data-amount="${debt.amount}" 
                                                            data-createdat="${debt.createdAt}"
                                                            data-description="${debt.description}" 
                                                            data-status="${debt.status}"
                                                            data-image="images/${debt.image}">
                                                        <i class="fas fa-info-circle"></i>
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="9" class="text-center text-muted">
                                                No debts found
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>

                        </table>
                        <form action="Debts" method="POST" style="display: inline;">
                            <input type="hidden" name="service" value="debtInCustomers" />
                            <input type="hidden" name="index" value="${index}" />
                            <input type="hidden" name="customer_id" value="${listName.id}" />
                            <input type="hidden" name="startDate" value="${startDate}" />
                            <input type="hidden" name="endDate" value="${endDate}" />

                            <span class="mr-2">Showing:</span>
                            <select class="btn-outline-primary mr-lg-auto" id="pageSize" name="pageSize" onchange="this.form.submit()">
                                <option value="5" ${pageSize == 5 ? 'selected' : ''}>5</option>
                                <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                                <option value="15" ${pageSize == 15 ? 'selected' : ''}>15</option>
                                <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
                            </select>
                            <span>of ${totalDebts} debts</span>
                        </form>


                        <h4>Total Amount: <fmt:formatNumber value="${listName.balance}"/></h4>
                        <div class="container d-flex justify-content-center mt-4">
                            <ul class="pagination">
                                <!-- Previous Page -->
                                <c:if test="${index > 1}">
                                    <li class="page-item">
                                        <form action="Debts" method="POST" style="display: inline;">
                                            <input type="hidden" name="service" value="debtInCustomers" />
                                            <input type="hidden" name="customer_id" value="${listName.id}" />
                                            <input type="hidden" name="index" value="${index - 1}" />
                                            <input type="hidden" name="pageSize" value="${pageSize}" />
                                            <input type="hidden" name="startDate" value="${startDate}" />
                                            <input type="hidden" name="endDate" value="${endDate}" />
                                            <button type="submit" class="page-link"><<</button>
                                        </form>
                                    </li>
                                </c:if>

                                <!-- Page Numbers -->
                                <c:forEach begin="1" end="${endPage}" var="page">
                                    <li class="page-item ${index == page ? 'active' : ''}">
                                        <form action="Debts" method="POST" style="display: inline;">
                                            <input type="hidden" name="service" value="debtInCustomers" />
                                            <input type="hidden" name="customer_id" value="${listName.id}" />
                                            <input type="hidden" name="index" value="${page}" />
                                            <input type="hidden" name="pageSize" value="${pageSize}" />
                                            <input type="hidden" name="startDate" value="${startDate}" />
                                            <input type="hidden" name="endDate" value="${endDate}" />
                                            <button type="submit" class="page-link">${page}</button>
                                        </form>
                                    </li>
                                </c:forEach>

                                <!-- Next Page -->
                                <c:if test="${index < endPage}">
                                    <li class="page-item">
                                        <form action="Debts" method="POST" style="display: inline;">
                                            <input type="hidden" name="service" value="debtInCustomers" />
                                            <input type="hidden" name="customer_id" value="${listName.id}" />
                                            <input type="hidden" name="index" value="${index + 1}" />
                                            <input type="hidden" name="pageSize" value="${pageSize}" />
                                            <input type="hidden" name="startDate" value="${startDate}" />
                                            <input type="hidden" name="endDate" value="${endDate}" />
                                            <button type="submit" class="page-link">>></button>
                                        </form>
                                    </li>
                                </c:if>
                            </ul>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <c:import url="addDebt.jsp" />
        <div class="modal fade" id="debtDetailModal" tabindex="-1" role="dialog" aria-labelledby="debtDetailLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content shadow-lg">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title">
                            <i class="fas fa-file-invoice-dollar"></i> Debt Details - <strong id="modalDebtId"></strong>
                        </h5>
                        <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>

                    <div class="modal-body">
                        <div class="row">
                            <!-- Thông tin chính -->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="text-muted"><strong>Amount:</strong></label>
                                    <input id="modalDebtAmount" type="text" class="form-control bg-light border-0" readonly>
                                </div>
                                <div class="form-group">
                                    <label class="text-muted"><strong>Created At:</strong></label>
                                    <input id="modalDebtCreatedAt" type="text" class="form-control bg-light border-0" readonly>
                                </div>
                            </div>

                            <!-- Thông tin bổ sung -->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="text-muted"><strong>Description:</strong></label>
                                    <textarea id="modalDebtDescription" class="form-control bg-light border-0" rows="3" readonly></textarea>
                                </div>
                                <div class="form-group">
                                    <label class="text-muted"><strong>Status:</strong></label>
                                    <input id="modalDebtStatus" type="text" class="form-control bg-light border-0" readonly>
                                </div>
                            </div>
                        </div>

                        <!-- Ảnh chứng cứ -->
                        <div class="text-center mt-3">
                            <label class="text-muted"><strong>Evidence:</strong></label>
                            <div class="border p-2 rounded bg-light">
                                <img id="modalDebtImage" class="myImg img-fluid rounded shadow" style="max-height: 300px; object-fit: cover;" alt="Debt evidence">
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <i class="fas fa-times"></i> Close
                        </button>
                    </div>
                </div>
            </div>
        </div>



        <!-- Modal để hiển thị ảnh -->
        <div id="myModal" class="modalImage">
            <span class="close">&times;</span>
            <img class="modalImage-content" id="img01">
            <div id="caption"></div>
        </div>

        <script>


            $('.pagination').off('click', '.page-nav').on('click', '.page-nav', function (e) {
                e.preventDefault();
                const page = $(this).data('page');
                sessionStorage.setItem("customerPageIndex", page);  // Cập nhật trang mới
                sessionStorage.setItem("prevCustomerPageIndex", page); // Đảm bảo trang cũ luôn được cập nhật
                searchCustomers(keyword, page, currentSortBy, currentSortOrder);
            });

            function myFunction(customerId) {
                var input, filter, table, tr, td, i, txtValue;
                input = document.getElementById("myInput");
                filter = input.value.toUpperCase();
                // Tạo ID động dựa trên customerId
                table = document.getElementById(`myTable1-${listName.id}`);
                if (!table)
                    return; // Tránh lỗi nếu bảng không tồn tại

                tr = table.getElementsByTagName("tr");
                for (i = 0; i < tr.length; i++) {
                    td = tr[i].getElementsByTagName("td")[0];
                    if (td) {
                        txtValue = td.textContent || td.innerText;
                        if (txtValue.toUpperCase().indexOf(filter) > -1) {
                            tr[i].style.display = "";
                        } else {
                            tr[i].style.display = "none";
                        }
                    }
                }
            }


            // Chỉ cho phép modal mở một lần
            $("#myModal").one("show.bs.modal", function () {
                $(this).addClass("already-open");
            });
            // Modal hình ảnh
            $(".myImg").on("click", function () {
                var modal = $("#myModal");
                var modalImg = $("#img01");
                var captionText = $("#caption");
                modal.show();
                modalImg.attr("src", this.src);
                captionText.text(this.alt);
            });
            // Đóng modal khi bấm vào nền đen
            $("#myModal").on("click", function (e) {
                if (e.target === this) {
                    $(this).hide();
                }
            });
            // Gỡ sự kiện cũ trước khi gắn mới
            $(document).off("click", ".modal-button").on("click", ".modal-button", function () {
            console.log("Modal button clicked");
            });
            }
            );
        </script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>

    </body>
</html>

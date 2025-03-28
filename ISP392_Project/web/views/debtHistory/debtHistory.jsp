<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Debt List</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
    </head>
    <style>
        .debt-filter-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .debt-filter-form {
            display: flex;
            align-items: flex-end;
            gap: 15px;
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

        .form-control:focus {
            outline: none;
            border-color: #007bff;
            box-shadow: 0 0 5px rgba(0,123,255,0.3);
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
            text-decoration: none;
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
    </style>
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
                        <a href="sale">
                            <span class="nav-icon">
                                <i class="fa-solid fa-file-invoice"></i>
                            </span>
                            <span class="nav-text">Invoices</span>
                        </a>
                    </li>
                    <li class="active">
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

            <div class="contents">
                <c:if test="${not empty sessionScope.Notification}">
                    <div class="alert alert-warning alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert">
                            &times;
                        </button>
                        <strong>${sessionScope.Notification}</strong>
                    </div>
                    <c:remove var="Notification" scope="session" />
                </c:if>
                <div class="panel-bar1">
                    <div class="container">
                        <a href="/ISP392_Project/Debts" class="text-secondary show-arrow">
                            <i class="fa-solid fa-arrow-left"></i> Debts history
                        </a>/ Debt list
                        <h2 class="text-center">Debt List</h2>
                        <c:if test="${not empty sessionScope.Notification}">
                            <div class="alert alert-warning alert-dismissible">
                                <button type="button" class="close" data-dismiss="alert">
                                    &times;
                                </button>
                                <strong>${sessionScope.Notification}</strong>
                            </div>
                            <c:remove var="Notification" scope="session" />
                        </c:if>





                        <c:if test="${not empty list}">
                            <c:set var="customer" value="${list[0]}" />
                            <div class="customer-info border p-3 rounded">
                                <h3 class="mb-3">Customer Information</h3>
                                <p><strong>Name:</strong> ${customer.name}</p>
                                <p><strong>Phone:</strong> ${customer.phone}</p>
                                <p><strong>Address:</strong> ${customer.address}</p>
                            </div>
                        </c:if>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <!-- Bộ lọc ngày -->
                            <div class="debt-filter-container">
                                <form action="Debts" method="POST" class="debt-filter-form">
                                    <input type="hidden" name="service" value="debtHistory" />
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

                                    <div class="button-group">
                                        <button type="submit" class="btn btn-primary">Filter</button>
                                        <a href="Debts?service=debtHistory&customer_id=${listName.id}" 
                                           class="btn btn-secondary">Reset</a>
                                        <button type="button" class="btn btn-outline-success" 
                                                data-toggle="modal" data-target="#DebtModal${listName.id}">
                                            <i class="fas fa-plus"></i> Add
                                        </button>
                                    </div>
                                </form>


                            </div>

                            <!-- Nút "Add" -->

                        </div>

                        <div class="table-container">
                            <table class="table-bordered">
                                <thead>
                                    <tr>
                                        <th class="resizable">ID</th>
                                        <th class="resizable">Amount</th>
                                        <th>Images</th>
                                        <th class="resizable">Description</th>
                                        <th class="resizable">Created At</th>
                                        <th class="resizable">Created By</th>
                                        <th class="resizable">Status</th>
                                        <th class="sticky-col1">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="debt" items="${list}">
                                        <tr>
                                            <td>${debt.id}</td>
                                            <td class="amount ${debt.type == '-' ? 'text-danger' : ''}" 
                                                data-amount="${debt.amount}" 
                                                data-type="${debt.type}">
                                                <fmt:formatNumber value="${debt.type == '-' ? -debt.amount : debt.amount}" pattern="###,##0"/>
                                            </td>



                                            <td>
                                                <img src="images/${debt.image}" class="myImg" style="width: 100px; height: 100px; object-fit: cover; cursor: pointer;" alt="Debt evidence"/>
                                            </td>
                                            <td style="white-space: normal; word-wrap: break-word; max-width: 200px;">
                                                ${debt.description}
                                            </td>
                                            <td>${debt.createdAt}</td>
                                            <td>${debt.createdBy}</td>
                                            <td>${debt.status}</td>
                                            <td class="sticky-col1">
                                                <!-- Nút xem chi tiết nợ -->
                                                <button type="button" class="btn btn-outline-primary openDebtModal" 
                                                        data-id="${debt.id}" 
                                                        data-amount="${debt.amount}" 
                                                        data-type="${debt.type}"
                                                        data-createdat="${debt.createdAt}"
                                                        data-description="${debt.description}" 
                                                        data-status="${debt.status}"
                                                        data-image="images/${debt.image}">
                                                    <i class="fas fa-info-circle"></i>
                                                </button>


                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="7"></td>
                                        <td class="sticky-col1 text-right font-weight-bold" id="totalAmountCell">
                                            Total Amount: <span id="totalAmount">0</span>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>

            </div> 
            <div id="myModal" class="modalImage">
                <span class="close">&times;</span>
                <img class="modalImage-content" id="img01">
                <div id="caption"></div>
            </div>
            <div id="DebtModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
                <div class="modal-dialog modal-xl" role="document">
                    <div class="modal-content">
                        <form action="${pageContext.request.contextPath}/Debts" method="POST" enctype="multipart/form-data">
                            <input type="hidden" name="service" value="addDebtInDebtDetails" />
                            <input type="hidden" name="createdBy" value="${sessionScope.username}" />

                            <div class="modal-header">
                                <h4 class="modal-title">Add Debt</h4>
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            </div>
                            <div class="modal-body">
                                <!-- Debt Type Field -->
                                <div class="form-group">
                                    <label>Debt Type</label>
                                    <select class="form-control" name="status">
                                        <option value="Customer repays debt">Customer repays debt</option> 
                                        <option value="Customer borrows debt">Customer pays</option>
                                        <option value="Owner repays debt">Owner repays debt</option> 
                                        <option value="Owner borrows debt">Owner borrows debt</option>
                                    </select>
                                </div>

                                <!-- Amount Field -->
                                <div class="form-group">
                                    <label>Amount</label>
                                    <input type="text" oninput="formatNumber(event)" onblur="cleanInputBeforeSubmit(event)" class="form-control" name="amount" required />
                                </div>

                                <!-- Image Upload -->
                                <div class="form-group">
                                    <label>Image</label>
                                    <input type="file" name="image">
                                </div>

                                <!-- Date/Time Field -->
                                <div class="form-group">
                                    <label>Create at</label>
                                    <input type="datetime-local" name="created_at" class="form-control" value="" required />
                                </div>
                                <input type="hidden" name="customer_id" value="${sessionScope.customer_id}" />

                                <!-- Description Field -->
                                <div class="form-group">
                                    <label>Description</label>
                                    <textarea class="form-control" name="description" rows="3"></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
                                <input type="submit" class="btn btn-primary" value="Add Debt" />
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div> 
        <div class="modal fade" id="debtDetailModal" tabindex="-1" role="dialog" aria-labelledby="debtDetailLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Debt Details - <strong id="modalDebtId"></strong></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <!-- Cột trái -->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label><strong>Amount:</strong></label>
                                    <input id="modalDebtAmount" type="text" class="form-control" readonly>
                                </div>
                                <div class="form-group">
                                    <label><strong>Status:</strong></label>
                                    <input id="modalDebtStatus" type="text" class="form-control" readonly>
                                </div>
                            </div>

                            <!-- Cột phải -->
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label><strong>Created At:</strong></label>
                                    <input id="modalDebtCreatedAt" type="text" class="form-control" readonly>
                                </div>
                                <div class="form-group">
                                    <label><strong>Description:</strong></label>
                                    <textarea id="modalDebtDescription" class="form-control" rows="3" readonly></textarea>
                                </div>
                            </div>
                        </div>

                        <!-- Phần ảnh chứng cứ (Evidence) bên dưới) -->
                        <div class="form-group text-center mt-3">
                            <label><strong>Evidence:</strong></label>
                            <div>
                                <img id="modalDebtImage" class="myImg"
                                     style="max-width: 100%; max-height: 300px; object-fit: contain;" 
                                     alt="Debt evidence">
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

    </body>

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            // ✅ Tính tổng số tiền chính xác (bao gồm cả số âm)
            let total = 0;
            document.querySelectorAll(".amount").forEach(function (amountElement) {
                let text = amountElement.textContent.trim(); // Lấy nội dung số liệu
                let amount = parseFloat(text.replace(/,/g, "")) || 0; // Xóa dấu phẩy rồi chuyển thành số

                // Kiểm tra nếu có dấu '-' hoặc class 'text-danger' (dành cho nợ)
                if (text.includes("-") || amountElement.classList.contains("text-danger")) {
                    amount = -Math.abs(amount); // Đảm bảo luôn là số âm
                }

                total += amount;
            });

            // Hiển thị tổng số tiền với định dạng số có dấu phân cách hàng nghìn
            document.getElementById("totalAmount").textContent = total.toLocaleString();

            // ✅ Hiển thị modal ảnh
            var modal = document.getElementById("myModal");
            var modalImg = document.getElementById("img01");
            var captionText = document.getElementById("caption");

            document.querySelectorAll(".myImg").forEach(img => {
                img.addEventListener("click", function () {
                    modal.style.display = "block";
                    modalImg.src = this.src;
                    captionText.innerHTML = this.alt;
                });
            });

            document.querySelectorAll(".close").forEach(closeBtn => {
                closeBtn.addEventListener("click", function () {
                    modal.style.display = "none";
                });
            });

            // ✅ Hiển thị modal chi tiết nợ
            document.querySelectorAll(".openDebtModal").forEach(button => {
                button.addEventListener("click", function () {
                    // Lấy dữ liệu từ data-attribute của nút được click
                    const debtId = this.getAttribute("data-id");
                    const amount = this.getAttribute("data-amount");
                    const createdAt = this.getAttribute("data-createdat");
                    const description = this.getAttribute("data-description");
                    const status = this.getAttribute("data-status");
                    const image = this.getAttribute("data-image");

                    // Gán dữ liệu vào modal
                    document.getElementById("modalDebtId").textContent = debtId;
                    document.getElementById("modalDebtAmount").value = amount;
                    document.getElementById("modalDebtCreatedAt").value = createdAt;
                    document.getElementById("modalDebtDescription").value = description;
                    document.getElementById("modalDebtStatus").value = status;
                    document.getElementById("modalDebtImage").src = image;

                    // Hiển thị modal Bootstrap
                    $("#debtDetailModal").modal("show");
                });
            });
        });

    </script>

    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/css/script.js"></script>

</html>

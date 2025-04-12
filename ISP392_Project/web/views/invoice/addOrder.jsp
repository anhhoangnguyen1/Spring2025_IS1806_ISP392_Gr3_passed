<%-- 
    Document   : addOrder
    Created on : Apr 2, 2025, 10:44:27 AM
    Author     : THC
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">

        <title>Sales Order</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <style>
            :root {
                --primary-color: #4CAF50;
                --primary-hover: #45a049;
                --border-color: #ccc;
                --text-color: #333;
                --bg-color: #fff;
                --header-bg: #f5f5f5;
            }

            body {

                background-color: var(--bg-color);
                color: var(--text-color);
                padding: 20px;
            }

            h1, h2, h3 {
                color: var(--text-color);
            }

            .order-container {
                display: flex;
                flex-direction: column;
                gap: 20px;
            }


            .panel-box {
                background-color: #ffffff;
                border: 1px solid var(--border-color);
                border-radius: 8px;
                padding: 20px;
                margin-bottom: 20px;
            }

            .customer-section {

            }

            .product-section {

            }

            .payment-section {
                background-color: #f9f9f9;
            }

            .overlay {
                display: none; /* Ẩn mặc định */
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5); /* nền mờ */
                z-index: 9990; /* thấp hơn popup nhưng che toàn trang */
            }


            /* Form elements */
            input[type="text"],
            input[type="number"],
            input[type="date"],
            select,
            textarea {
                padding: 8px;
                border: 1px solid var(--border-color);
                border-radius: 4px;
                background-color: #fff;
                color: var(--text-color);
                font-size: 14px;
                width: 100%;
                box-sizing: border-box;
                margin: 5px 0;
            }

            button {
                padding: 8px 16px;
                border: none;
                border-radius: 4px;
                background-color: var(--primary-color);
                color: white;
                font-size: 14px;
                cursor: pointer;
                margin: 5px 0;
            }

            button:hover {
                background-color: var(--primary-hover);
            }



            /* Tables */
            table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 15px;

                border-radius: 5px;
                overflow: hidden;
            }

            th, td {
                border: 1px solid var(--border-color);
                padding: 8px;
                text-align: left;
            }

            th {
                background-color: var(--header-bg);
                color: #fff;
                position: sticky;
                top: 0;
            }


            /* Order details and items */
            .order-items-container {
                margin-top: 15px;
                color: black; /* Đổi màu chữ của toàn bộ khối */
            }

            #orderItems input {
                text-align: center;
                color: black; /* Chữ trong input */
                background-color: white; /* Đảm bảo nền sáng để dễ nhìn */
            }

            #orderItems th,
            #orderItems td {
                color: black !important; /* Màu chữ trong bảng */
                background-color: white; /* Nền ô trắng */
            }

            #orderItems th:nth-child(3),
            #orderItems td:nth-child(3) {
                width: 7px;
                min-width: 70px;
                text-align: center;
            }

            #orderItems th:nth-child(4),
            #orderItems td:nth-child(4) {
                width: 80px;
                min-width: 60px;
                text-align: center;
            }





            /* Order summary */
            .order-summary {
                background-color: #333;
                padding: 10px;
                border-radius: 5px;
                margin-top: 15px;
            }

            .summary-row {
                display: flex;
                justify-content: space-between;
                margin: 8px 0;
            }

            .summary-row strong {
                font-weight: bold;
            }

            .grand-total {
                font-size: 1.2em;
                font-weight: bold;
                color: #4CAF50;
            }

            /* Payment section */
            .payment-options {
                margin: 15px 0;
            }

            .radio-group {
                display: flex;
                gap: 10px;
                margin: 10px 0;
            }

            /* Notes area */
            .order-notes {
                margin-top: 15px;
            }

            /* Responsive styles */
            @media (max-width: 1024px) {
                .order-container {
                    flex-direction: column;
                }
            }

            .info-row {
                display: flex;
                justify-content: space-between;
                margin-bottom: 8px;
                align-items: center;
            }

            .info-label {
                font-weight: bold;
                min-width: 150px;
            }

            .info-value {
                flex: 1;
            }

            .success-text {
                color: #4CAF50;
            }

            .waiting-text {
                color: #FFC107;
            }

            .error-text {
                color: #F44336;
            }

            /* Buttons for submit/actions */
            .action-buttons {
                display: flex;
                gap: 10px;
                margin-top: 20px;
            }

            .primary-btn {
                flex: 1;
                padding: 12px;
                font-size: 16px;
                background-color: #2196F3;
            }

            .print-btn {
                background-color: #9C27B0;
            }

            /* Search Input */
            .search-boxx {
                position: relative;
            }

            .search-boxx input[type="text"] {
                width: 100%;
                padding: 12px 14px;
                font-size: 13px;
                border: 1px solid #ddd;
                border-radius: 8px;
                outline: none;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
                transition: all 0.3s ease;
            }

            .search-boxx input[type="text"]:focus {
                border-color: #4a90e2;
                box-shadow: 0 2px 8px rgba(74, 144, 226, 0.2);
            }

            .search-boxx input[type="text"]::placeholder {
                color: #aaa;
            }



            .main-content{
                font-size: 12px;
            }


            /* Mỗi sản phẩm gợi ý */
            .product-item {
                display: flex;
                align-items: center;

                padding: 6px 8px;
                cursor: pointer;
                transition: background-color 0.2s ease-in-out;
            }

            /* Khi hover */
            .product-item:hover {
                background-color: var(--primary-hover);
                transform: scale(1.02);
                transition: all 0.3s ease;
            }



            .product-item h3 {
                font-size: 15px;
                font-weight: bold;
                margin: 0;
                color: black;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
                flex: 1;
            }

            .product-item p {
                font-size: 14px;
                color: var(--text-color);
                margin: 0;
                font-weight: 500;
                white-space: nowrap;
            }


            /* Danh sách gợi ý */
            .search-suggestions {
                display: none;
                position: absolute;
                z-index: 1000;
                background-color: #fff;
                border: 1px solid #ccc;
                max-height: 300px;
                min-height: 50px;
                overflow-y: auto;
                overflow-x: hidden;
                padding: 8px;
                width: 100%;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
            }


            /* Mỗi khách hàng gợi ý */
            .customer-item {
                display: flex;
                align-items: center;

                padding: 6px 8px;
                cursor: pointer;
                transition: background-color 0.2s ease-in-out;
            }

            /* Khi hover */
            .customer-item:hover {
                background-color: var(--primary-hover);
                transform: scale(1.02);
                transition: all 0.3s ease;
            }

            /* Tên khách hàng */
            .customer-item h3 {
                font-size: 15px;
                font-weight: bold;
                margin: 0;
                color: black;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
                flex: 1;
            }

            /* Số điện thoại */
            .customer-item p {
                font-size: 14px;
                color: var(--text-color);
                margin: 0;
                font-weight: 500;
                white-space: nowrap;
            }

            .popup {
                display: none;
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                background-color: #fff;
                padding: 20px;
                width: 400px;
                border-radius: 8px;
                box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.2);
                z-index: 9999;
            }



            .popup h2 {
                font-size: 24px;
                text-align: center;
                margin-bottom: 20px;
            }

            .popup label {
                font-weight: bold;
                margin-bottom: 10px;
                display: block;
            }

            .popup input {
                width: 100%;
                padding: 8px;
                margin: 8px 0;
                border: 1px solid #ccc;
                border-radius: 4px;
            }

            .popup button {
                width: 100%;
                padding: 10px;
                background-color: #4CAF50;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }

            .popup button:hover {
                background-color: #45a049;
            }

            .close {
                position: absolute;
                top: 5px;
                right: 10px;
                cursor: pointer;
            }


            .back-button {
                position: fixed;
                top: 20px;
                right: 20px;
                background-color: #007bff;
                color: white;
                padding: 8px 14px;
                border-radius: 5px;
                text-decoration: none;
                font-size: 14px;
                box-shadow: 0 4px 6px rgba(0,0,0,0.1);
                z-index: 999;
                transition: background-color 0.3s ease;
            }

            .back-button:hover {
                background-color: #0056b3;
            }

        </style>

    </head>
    <body>
        <div class="main-content">
            <div class="top-actions d-flex justify-content-end" style="gap: 10px; margin-bottom: 20px;">
                <button onclick="openNewTab()" class="btn btn-primary">Add New Order</button>
                <button type="button"  onclick="window.location.href = '${pageContext.request.contextPath}/dashboard'">
                    Back to Dashboard</button>

            </div>

            <script>
                // Kiểm tra số hóa đơn hiện tại trong sessionStorage
                let invoiceCount = sessionStorage.getItem("invoiceCount");

                if (!invoiceCount) {
                    sessionStorage.setItem("invoiceCount", 1); // Nếu chưa có, đặt là 1
                }

                function openNewTab() {
                    let invoiceNumber = parseInt(sessionStorage.getItem("invoiceCount")) + 1;
                    sessionStorage.setItem("invoiceCount", invoiceNumber);

                    window.open('Orders?invoice=' + invoiceNumber, '_blank');
                }
            </script>

            <h1 style="text-align: center">
                Sales Invoice
            </h1>
            <form id="orderForm" action="Orders" method="post">
                <c:if test="${not empty message}">
                    <c:choose>
                        <c:when test="${message == 'success'}">
                            <div class="alert alert-success" role="alert">
                                Order created successfully!
                            </div>
                        </c:when>
                        <c:when test="${message == 'error'}">
                            <div class="alert alert-danger" role="alert">
                                Order creation failed. Please try again!
                            </div>
                        </c:when>
                        <c:when test="${message == 'Please choose product!'}">
                            <div class="alert alert-danger" role="alert">
                                Please choose product!
                            </div>
                        </c:when>
                        <c:when test="${message == 'Payment error!'}">
                            <div class="alert alert-danger" role="alert">
                                Payment error!
                            </div>
                        </c:when>
                        <c:when test="${message == 'Please choose customer!'}">
                            <div class="alert alert-danger" role="alert">
                                Please choose customer!
                            </div>
                        </c:when>
                    </c:choose>
                </c:if>
                <div class="order-container">


                    <div class="customer-section panel-box">
                        <div class="customer-search-box right-panel" style="margin-bottom: 20px;">
                            <h2>Customer Information</h2>

                            <div class="search-boxx">

                                <input type="text" id="searchCustomerInput"  placeholder="Search for customer." >


                                <div id="suggestionsCustomer" class="search-suggestions"></div>


                            </div>


                            <div style="margin-top: 10px;">
                                <button type="button" onclick="openAddCustomerPopup()" style="background-color: #28a745; color: white;">
                                    Add New Customer
                                </button>
                            </div>

                            <div id="customerInfo" class="customer-info" style="display: none;">
                                <h3 class="info-value success-text" id="customerName"></h3>
                                <p id="customerPhone"></p>
                                <p id="customerDebt"></p>
                                <input type="hidden" id="customerId" name="customerId">

                            </div>

                        </div>
                    </div>

                    <div class="product-section panel-box">
                        <h2>Order Details</h2>
                        <div class="search-boxx">

                            <input type="text" id="search" placeholder="Search for product." >
                            <div id="suggestions" class="search-suggestions"></div>
                        </div>




                        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
                        <script>
                                    $(document).ready(function () {
                                        let currentLoad;
                                        $("#search").on("input", function () {
                                            if (currentLoad) {
                                                console.log("Clear timeout");
                                                clearTimeout(currentLoad);
                                            }
                                            let that = this;
                                            currentLoad = setTimeout(function () {
                                                console.log("fetch product");
                                                let query = $(that).val();
                                                if (query.trim().length > 0) {
                                                    $.ajax({
                                                        url: "SearchServlet",
                                                        type: "GET",
                                                        data: {
                                                            searchProduct: query,
                                                            orderType: "Export"
                                                        },
                                                        success: function (data) {
                                                            console.log("Search response:", data);
                                                            if (data.trim() !== "") {
                                                                $("#suggestions").html(data).fadeIn();
                                                            } else {
                                                                $("#suggestions").fadeOut(100);
                                                            }
                                                        },
                                                        error: function (xhr, status, error) {
                                                            console.error("Search error:", error);
                                                            console.error("Status:", status);
                                                            console.error("Response:", xhr.responseText);
                                                        }
                                                    });
                                                } else {
                                                    $("#suggestions").hide();
                                                }
                                                currentLoad = null;
                                            }, 500);
                                        });

                                        // Kiểm tra khi click trên document
                                        $(document).on("click", function (event) {
                                            if (!$(event.target).closest("#search").length &&
                                                    !$(event.target).closest("#suggestions").length) {
                                                $("#suggestions").fadeOut(100);
                                            }
                                        });

                                        // Hiển thị lại khi focus
                                        $("#search").on("focus", function () {
                                            console.log("Search focused");
                                            if ($(this).val().length > 0) {
                                                $.ajax({
                                                    url: "SearchServlet",
                                                    type: "GET",
                                                    data: {
                                                        searchProduct: $(this).val(),
                                                        orderType: "Export"
                                                    },
                                                    success: function (data) {
                                                        console.log("Refreshed data on focus");
                                                        $("#suggestions").html(data);
                                                        $("#suggestions").show();
                                                    }
                                                });
                                            }
                                        });
                                    });
                        </script>

                        <input type="hidden" name="orderType" value="Export"><!-- xuất -->

                        <!-- Order items table -->
                        <div class="order-items-container">

                            <table id="orderItems">
                                <thead>
                                    <tr>
                                        <th>Product</th>
                                        <th>Quantity</th>
                                        <th>Unit</th>
                                        <th>Total Weight</th>
                                        <th>Unit Price</th>
                                        <th>Discount</th>
                                        <th>Amount</th>
                                        <th>Delete</th>

                                    </tr>
                                </thead>
                                <tbody>
                                    <!-- Order items will be added here -->
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="4" style="text-align: right;"><strong>Total Amount:</strong></td>

                                        <td colspan="4">
                                            <input type="hidden" id="totalOrderPriceHidden" name="totalOrderPriceHidden">
                                            <input type="text" id="totalOrderPrice" name="totalOrderPrice" readonly>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td colspan="4" style="text-align: right;"><strong>Total Discount:</strong></td>
                                        <td colspan="4">
                                            <input type="hidden" id="totalDiscount" name="totalDiscount">
                                            <input type="text" id="totalDiscountHidden" name="totalDiscountHidden"readonly >
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>

                        <!-- Order notes -->
                        <div class="order-notes">
                            <h3>Note</h3>
                            <textarea id="status" name="status" rows="4" placeholder="..."></textarea>
                        </div>




                        <script>
                            // Thêm biến role từ session
                            const userRole = "${sessionScope.role}";

                            // Hàm để ẩn số điện thoại cho staff
                            function maskPhoneNumber(phone) {
                                if (phone.length >= 10) {
                                    return phone.substring(0, 3) + "xxxxx" + phone.substring(8);
                                }
                                return phone;
                            }

                            // Cập nhật hàm selectCustomer
                            function selectCustomer(id, name, phone, balance) {
                                $("#customerId").val(id);
                                $("#customerName").text("Name: " + name);

                                // Xử lý hiển thị số điện thoại dựa trên role
                                let displayPhone = userRole === "staff" ? maskPhoneNumber(phone) : phone;
                                $("#customerPhone").text("Phone: " + displayPhone);

                                // Xác định cách hiển thị tổng nợ
                                if (balance < 0) {
                                    $("#customerDebt").text("Amount Due: " + formatNumberVND(-balance));
                                } else if (balance > 0) {
                                    $("#customerDebt").text("Amount Owed: " + formatNumberVND(balance));
                                } else {
                                    $("#customerDebt").text("Total Debt: " + formatNumberVND(balance));
                                }

                                $("#customerInfo").show();
                            }

                            $(document).ready(function () {
                                let currentLoad;
                                $("#searchCustomerInput").on("input", function () {
                                    if (currentLoad) {
                                        console.log("Clear timeout");
                                        clearTimeout(currentLoad);
                                    }
                                    let that = this;
                                    currentLoad = setTimeout(function () {
                                        console.log("fetch customer");
                                        let query = $(that).val();
                                        if (query.length > 0) {
                                            $.ajax({
                                                url: "SearchServlet",
                                                type: "POST",
                                                data: {keyword: query},
                                                success: function (data) {
                                                    console.log("Search response:", data);
                                                    if (data.trim() !== "") {
                                                        $("#suggestionsCustomer").html(data).fadeIn();
                                                    } else {
                                                        $("#suggestionsCustomer").fadeOut(100);
                                                    }
                                                },
                                                error: function (xhr, status, error) {
                                                    console.error("Search error:", error);
                                                    console.error("Status:", status);
                                                    console.error("Response:", xhr.responseText);
                                                }
                                            });
                                        } else {
                                            $("#suggestionsCustomer").hide();
                                        }
                                        currentLoad = null;
                                    }, 500);
                                });

                                // Hiển thị lại danh sách khi focus vào ô tìm kiếm (nếu có nội dung)
                                $("#searchCustomerInput").on("focus", function () {
                                    if ($(this).val().length > 0) {
                                        $("#suggestionsCustomer").show();
                                    }
                                });
                            });
                            // Hàm chọn khách hàng từ danh sách
                            function selectCustomer(id, name, phone, debt) {
                                $("#customerId").val(id);
                                $("#customerName").text("Name: " + name);

                                // Ẩn 3 số cuối số điện thoại
                                let maskedPhone = (userRole === 'owner') ? phone : phone.slice(0, 3) + "xxxxx" + phone.slice(8);
                                $("#customerPhone").text("Phone: " + maskedPhone);

                                // Xác định cách hiển thị tổng nợ
                                if (debt < 0) {
                                    $("#customerDebt").text("Amount Due: " + formatNumberVND(-debt));
                                } else if (debt > 0) {
                                    $("#customerDebt").text("Amount Owed:  " + formatNumberVND(debt));
                                } else {
                                    $("#customerDebt").text("Total Debt: " + formatNumberVND(debt));
                                }

                                $("#customerInfo").show(); // Hiển thị div chứa thông tin khách hàng
                                $("#searchCustomerInput").val("");
                                $("#suggestionsCustomer").hide();
                            }

                            function updateCustomerSuggestions() {
                                let query = $("#searchCustomerInput").val();  // Lấy lại giá trị từ ô tìm kiếm
                                if (query.trim().length > 0) {
                                    $.ajax({
                                        url: "SearchServlet",
                                        type: "POST",
                                        data: {keyword: query},
                                        success: function (data) {
                                            if (data.trim() !== "") {
                                                $("#suggestionsCustomer").html(data).fadeIn();
                                            } else {
                                                $("#suggestionsCustomer").fadeOut(100);
                                            }
                                        },
                                        error: function (xhr, status, error) {
                                            console.error("Search error:", error);
                                            console.error("Status:", status);
                                            console.error("Response:", xhr.responseText);
                                        }
                                    });
                                }
                            }


                        </script>




                        <script>
                            function calculateBalance() {
                                let totalOrderPrice = parseInt($("#totalOrderPriceHidden").val()) || 0;
                                let paidAmount = parseInt($("#paidAmount").val()) || 0;
                                if (isNaN(paidAmount) || paidAmount < 0) {
                                    paidAmount = 0;
                                    $("#paidAmount").val(0); // Cập nhật lại giá trị hiển thị
                                }
                                let balance = paidAmount - totalOrderPrice;

                                let $balanceOptions = $("#balanceOptions");
                                let $excessOption = $("#excessOption");
                                let $debtOption = $("#debtOption");




                                $("#balanceAmount").val(balance);
                                $("#balanceAmountHidden").val(formatNumberVND(balance));




                                if (balance > 0) {
                                    // Khách trả dư
                                    $excessOption.show();
                                    $debtOption.show();
                                    $balanceOptions.show();
                                    autoSelectOption("refund");
                                } else if (balance < 0) {
                                    // Khách trả thiếu
                                    $excessOption.hide();
                                    $debtOption.show();
                                    $balanceOptions.show();
                                    autoSelectOption("debt");
                                } else {
                                    // Thanh toán đúng số tiền
                                    $balanceOptions.hide();
                                }

                            }
                            function autoSelectOption(value) {
                                $("input[name='balanceAction'][value='" + value + "']").prop("checked", true);
                            }
                        </script>


                        <div class="payment-options">

                            <div class="info-row">
                                <span class="info-label">Payment</span>
                                <span class="info-value">
                                    <input type="number" id="paidAmount" name="paidAmount"oninput="calculateBalance()">
                                </span>
                            </div>

                            <div class="info-row">
                                <span class="info-label">Remaining amount</span>
                                <span class="info-value">
                                    <input type="hidden" id="balanceAmount" name="balanceAmount">
                                    <input type="text" id="balanceAmountHidden" name="balanceAmountHidden" readonly>
                                </span>
                            </div>

                            <div id="balanceOptions" style="display: none;">
                                <div id="excessOption" style="display: none;">
                                    <label><input type="radio" name="balanceAction" value="refund">Change Returned</label>
                                </div>
                                <div id="debtOption" style="display: none;">
                                    <label><input type="radio" name="balanceAction" value="debt">Add to Debt</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script>


                    $(document).ready(function () {
                        $("#submitOrder").on("click", function (event) {
                            if ($("#customerId").val() === "") {
                                event.preventDefault(); // Ngăn form hoặc hành động tiếp theo
                                alert("Please select a customer before creating the order!");
                            }
                            // Kiểm tra nếu bảng sản phẩm không có sản phẩm nào
                            if ($("#orderItems tbody tr").length === 0) {
                                event.preventDefault(); // Ngăn form hoặc hành động tiếp theo
                                alert("Please select at least one product to create the order.");
                                return; // Ngừng quá trình gửi form
                            }
                            let paidAmount = $("#paidAmount").val().trim();
                            // Kiểm tra đã nhập số tiền thanh toán chưa
                            if (!paidAmount || isNaN(parseFloat(paidAmount))) {
                                event.preventDefault();
                                alert("Please enter the amount paid by the customer!");
                                return;
                            }

                            let selectedOption = $("input[name='balanceAction']:checked").val();
                            // Kiểm tra nếu có tiền dư/thừa, phải chọn phương án xử lý
                            if ($("#balanceOptions").is(":visible") && !selectedOption) {
                                event.preventDefault();
                                alert("Please choose how to handle the remaining amount!");
                                return;
                            }

                        });
                    });

                </script>


                <!-- Action buttons -->
                <div class="top-actions d-flex justify-content-end" style="gap: 10px; margin-bottom: 20px;">
                    <button type="submit" id="submitOrder" style="background-color: #007bff">Submit Order</button>
                    <button type="button"  onclick="window.location.href = '${pageContext.request.contextPath}/InvoiceHistory'">
                        Go to Order List </button>
                </div>

                <p id="orderStatus"></p>
        </div>
        <script>
            function addProductToOrder(productID, productName, pricePerKg, availableQuantity, unitSizes) {
                // Kiểm tra nếu sản phẩm hết hàng
                if (availableQuantity <= 0) {
                    alert("The product is out of stock!");
                    return; // Dừng hàm nếu sản phẩm không còn hàng
                }

                var table = document.getElementById("orderItems").getElementsByTagName('tbody')[0];
                // Nếu sản phẩm chưa có, thêm dòng mới

                var newRow = table.insertRow(table.rows.length);
                var cell1 = newRow.insertCell(0);
                var cell2 = newRow.insertCell(1);
                var cell3 = newRow.insertCell(2);
                var cell4 = newRow.insertCell(3);
                var cell5 = newRow.insertCell(4);
                var cell6 = newRow.insertCell(5);
                var cell7 = newRow.insertCell(6);
                var cell8 = newRow.insertCell(7);
                // Hiển thị tên sản phẩm
                cell1.innerHTML =
                        '<input type="hidden" name="productID" value="' + productID + '">' +
                        '<input type="hidden" name="productName" value="' + productName + '">' +
                        productName;
                // Chọn đơn vị tính: kg hoặc bao
                // Tạo dropdown từ danh sách unitSizes
                var unitSelectHTML = '<select name="unitType" class="unitType" style="width: auto; min-width: 50px; white-space: normal;">';
                if (unitSizes && unitSizes.length > 0) {
                    unitSizes.forEach(size => {
                        unitSelectHTML += '<option value="' + size + '">' + (size === 1 ? "Kg" : "Bag (" + size + "kg)") + '</option>';
                    });
                } else {
                    unitSelectHTML += '<option value="1">Kg</option>'; // Mặc định nếu không có dữ liệu
                }
                unitSelectHTML += '</select>';
                cell3.innerHTML = unitSelectHTML;
                cell2.innerHTML = '<input type="number" name="quantity" class="quantity" required value="1" min="1" max="' + availableQuantity + '">';
                // Nếu xuất kho, giá cố định
                cell5.innerHTML = '<input type="hidden" name="unitPriceHidden" class="unitPriceHidden" value="' + pricePerKg + '">' +
                        '<input type="text" name="unitPrice" class="unitPrice" value="' + formatNumberVND(pricePerKg) + '" readonly>';
                // Giảm giá (VND/kg)
                cell6.innerHTML = '<input type="number" name="discount" class="discount" value="0" min="0">';
                cell7.innerHTML =
                        '<input type="hidden" name="totalPriceHidden" class="totalPriceHidden">' +
                        '<input type="text" name="totalPrice" class="totalPrice" readonly>';
                // Nút xóa
                cell8.innerHTML = '<button type="button" onclick="deleteRow(this)" style="background-color: #007bff">Delete</button>';
                // Thêm input ẩn để lưu totalWeight
                cell4.innerHTML = '<input type="number" name="totalWeight" class="totalWeight" readonly >';
                //không cho phép nhập âm số lượng và giảm giá

                var quantityInput = cell2.querySelector('.quantity');
                // Không cho nhập số âm
                quantityInput.addEventListener("input", function () {
                    if (this.value < 0) {
                        this.value = 1; // Đặt giá trị tối thiểu là 1
                    }
                    recalculateRow(newRow, pricePerKg, availableQuantity);
                });
                var unitTypeInput = cell3.querySelector('.unitType');
                var discountInput = cell6.querySelector('.discount');
                // Không cho nhập số âm
                discountInput.addEventListener("input", function () {
                    if (this.value < 0) {
                        this.value = 0; // Đặt giá trị tối thiểu là 1
                    }
                    if (parseFloat(this.value) > pricePerKg) {
                        alert("The discount must not exceed the price of 1 kg of rice!");
                        this.value = 0;
                    }
                    recalculateRow(newRow, pricePerKg, availableQuantity);
                });
                unitTypeInput.addEventListener('change', () => recalculateRow(newRow, pricePerKg, availableQuantity));
                discountInput.addEventListener('input', () => recalculateRow(newRow, pricePerKg, availableQuantity));
                recalculateRow(newRow, pricePerKg, availableQuantity);
                $("#suggestions").hide();
            }
            function recalculateRow(row, pricePerKg, availableQuantity) {

                var quantityInput = row.querySelector(".quantity");
                var unitTypeInput = row.querySelector(".unitType");
                var discountInput = row.querySelector(".discount");
                var totalPriceInput = row.querySelector(".totalPrice");
                var totalPriceHidden = row.querySelector(".totalPriceHidden");
                var totalWeightInput = row.querySelector(".totalWeight");
                var unitMultiplier = parseInt(unitTypeInput.value);
                var quantity = parseInt(quantityInput.value);
                var totalWeight = quantity * unitMultiplier;
                if (totalWeight > availableQuantity) {
                    alert(" The quantity exceeds the available stock! Please re-enter.");
                    quantityInput.value = Math.floor(availableQuantity / unitMultiplier); // Tự động điều chỉnh số lượng phù hợp
                    totalWeight = quantityInput.value * unitMultiplier; // Cập nhật lại tổng khối lượng
                }



                var discount = parseInt(discountInput.value) || 0;
                var totalDiscount = totalWeight * discount;
                var totalPrice = (totalWeight * pricePerKg) - totalDiscount;
                totalWeightInput.value = totalWeight;
                totalPriceHidden.value = totalPrice; // Lưu giá trị số
                totalPriceInput.value = formatNumberVND(totalPrice);
                updateTotalOrderPrice();
            }

            function deleteRow(button) {
                var row = button.parentNode.parentNode;
                row.parentNode.removeChild(row);
                updateTotalOrderPrice();
            }

            function updateTotalOrderPrice() {
                var total = 0;
                var totalDiscount = 0;
                var totalPriceInputs = document.querySelectorAll(".totalPriceHidden");
                var discountInputs = document.querySelectorAll(".discount");
                var quantityInputs = document.querySelectorAll(".quantity");
                var unitTypeInputs = document.querySelectorAll(".unitType");
                totalPriceInputs.forEach((input, index) => {
                    total += parseInt(input.value) || 0;
                    var discount = parseInt(discountInputs[index].value) || 0;
                    var quantity = parseInt(quantityInputs[index].value);
                    var unitMultiplier = parseInt(unitTypeInputs[index].value); // 1kg hoặc bao 10kg, 20kg, 50kg
                    var totalWeight = quantity * unitMultiplier; // Tổng số kg của sản phẩm này

                    totalDiscount += totalWeight * discount; // Tổng số tiền giảm giá của sản phẩm này
                });
                document.getElementById("totalOrderPriceHidden").value = total; // Lưu giá trị số thực
                document.getElementById("totalOrderPrice").value = formatNumberVND(total);
                document.getElementById("totalDiscount").value = totalDiscount;
                document.getElementById("totalDiscountHidden").value = formatNumberVND(totalDiscount);
            }






        </script>

        <script>


            function formatNumberVND(number) {
                return new Intl.NumberFormat('vi-VN').format(number);
            }

        </script>




    </form>
    <div id="overlay" class="overlay"></div>

    <div id="addCustomerPopup" class="popup" style="background-color: whitesmoke">

        <span class="close" onclick="closeAddCustomerPopup()">&times;</span>
        <h2>Add new customer</h2>

        <form id="addCustomerForm">
            <label for="newCustomerName">Name:</label>
            <input type="text" id="newCustomerName" name="name" required>

            <label for="newCustomerPhone">Phone:</label>
            <input type="number" id="newCustomerPhone" name="phone" required>


            <input type="hidden" name="address" value="">
            <input type="hidden" name="email" value="">
            <input type="hidden" name="total" value="0"> 

            <button type="button" onclick="saveNewCustomer()" style="background-color: #007bff">Save</button>
        </form>

    </div>



    <script>

        $("#paidAmount").on("input", function () {
            if ($(this).val() < 0) {
                $(this).val(0);
            }
        });


    </script>
    <script>
        function openAddCustomerPopup() {
            $("#overlay").fadeIn(200);
            $("#addCustomerPopup").fadeIn(200);
        }

        function closeAddCustomerPopup() {
            $("#addCustomerPopup").fadeOut(200);
            $("#overlay").fadeOut(200);
        }

        $("#overlay").on("click", function () {
            closeAddCustomerPopup();
        });



        function saveNewCustomer() {
            let name = $("#newCustomerName").val().trim();
            let phone = $("#newCustomerPhone").val().trim();

            if (name === "" || phone === "") {
                alert("Enter full information!");
                return;
            }

            // Tạo object chứa đầy đủ dữ liệu theo yêu cầu của Servlet
            let customerData = {
                name: name,
                phone: phone,
                address: "", // Giá trị mặc định
                email: "", // Giá trị mặc định
                total: "0" // Để Servlet chấp nhận
            };

            // Gửi dữ liệu lên server để lưu khách hàng mới
            $.ajax({
                url: "AddCustomer",
                type: "POST",
                data: customerData,
                success: function (response) {
                    console.log("Server Response:", response); // In ra phản hồi từ server

                    // Kiểm tra phản hồi JSON từ server
                    if (response.status === "success") {
                        alert("Add customer successful!");
                        closeAddCustomerPopup();
                    } else if (response.status === "error") {
                        alert(response.message); // Hiển thị thông báo lỗi từ server
                    } else {
                        alert("Unexpected response from server!");
                    }
                },
                error: function (xhr, status, error) {
                    console.error("AJAX error:", error); // In lỗi ra console
                    alert("Error to server!"); // Thông báo lỗi khi có sự cố
                }
            });
        }


    </script>

    <script>

        $(document).ready(function () {
            $("#orderForm").submit(function (event) {
                event.preventDefault(); // Ngăn chặn việc tải lại trang

                $("#orderStatus").text("The order is being processed..."); // Hiển thị trạng thái ngay lập tức
                $("#submitOrder").prop("disabled", true); // Disable nút submit

                $.ajax({
                    url: "Orders",
                    type: "POST",
                    data: $(this).serialize(), // Gửi dữ liệu form bằng AJAX
                    //Lấy toàn bộ dữ liệu từ form và serialize (chuyển đổi) chúng thành chuỗi dạng key-value, để có thể gửi đi qua AJAX.
                    dataType: "json",
                    success: function (response) {
                        if (response.status === "processing") {
                            checkOrderStatus(); // Kiểm tra trạng thái đơn hàng
                        } else if (response.status === "error") {
                            $("#orderStatus").html("<span style='color: red;'>❌ " + response.message + "</span>");
                        }
                    }
                });
            });
            function checkOrderStatus() {
                var userId = ${sessionScope.userID}; // Đảm bảo lấy đúng userID từ session

                $.ajax({
                    url: "CheckOrderStatusServlet",
                    type: "GET",
                    data: {userId: userId},
                    dataType: "json",
                    success: function (response) {
                        if (response.status === "done") {
                            $("#orderStatus").text("Order created successfully!");
                            setTimeout(function () {
                                window.location.href = "orders/list"; // <-- Đường dẫn tới trang danh sách hóa đơn
                            }, 1500); // đợi 1.5 giây rồi chuyển hướng
                        } else if (response.status === "error") {
                            $("#orderStatus").text("Error: Failed to create the order!");
                        } else {
                            setTimeout(checkOrderStatus, 1000); // Tiếp tục kiểm tra sau 1 giây
                        }
                    }
                });
            }


            // Lấy các phần tử cần ẩn/hiện
            const openAddNewDebt = document.querySelector('.js-hidden-menu'); // Nút toggle
            const newDebt = document.querySelector('.menu'); // Menu
            const newDebt1 = document.querySelector('.main-content'); // Nội dung chính
            const newDebt2 = document.querySelector('.sidebar'); // Sidebar

            // Kiểm tra trạng thái đã lưu trong localStorage khi trang load
            document.addEventListener("DOMContentLoaded", function () {
                if (localStorage.getItem("menuHidden") === "true") {
                    newDebt.classList.add('hiden');
                    newDebt1.classList.add('hiden');
                    newDebt2.classList.add('hiden');
                }
            });
            // Hàm toggle hiển thị
            function toggleAddNewDebt() {
                newDebt.classList.toggle('hiden');
                newDebt1.classList.toggle('hiden');
                newDebt2.classList.toggle('hiden');
                // Lưu trạng thái vào localStorage
                const isHidden = newDebt.classList.contains('hiden');
                localStorage.setItem("menuHidden", isHidden);
            }

            // Gán sự kiện click
            openAddNewDebt.addEventListener('click', toggleAddNewDebt);
        });
    </script>

</body>
</html>
